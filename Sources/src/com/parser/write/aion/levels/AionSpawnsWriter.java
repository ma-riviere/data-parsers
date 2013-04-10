package com.parser.write.aion.levels;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.geo.aion.GeoService;

import com.parser.input.aion.mission.ClientSpawns;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.npcs.ClientNpc;

import com.parser.common.aion.AionDataCenter;
import com.parser.common.aion.bindings.SourceSphere;
import com.parser.common.aion.models.SpawnData;
import com.parser.common.math.MathUtil;
import com.parser.common.utils.Util;

import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.levels.AionMissionParser;

import com.parser.write.*;
import com.parser.write.aion.AionWritingConfig;
import com.parser.write.aion.npcs.AionWalkersWriter;

import com.parser.output.aion.mission.*;

public class AionSpawnsWriter extends AbstractWriter {
	
	Map<String, List<ClientSpawns>> clientSpawnData;
	
	SpawnMap npcSpawnMap = new SpawnMap();
	SpawnMap instanceSpawnMap = new SpawnMap();
	SpawnMap gatherSpawnMap = new SpawnMap();
	SpawnMap siegesSpawnMap = new SpawnMap();
	SpawnMap riftsSpawnMap = new SpawnMap();
	SpawnMap staticSpawnMap = new SpawnMap();
	
	String mapName = null;
	int mapId = 0;
	SpawnData sd = null;
	
	int RANDOM_WALK_CAP = 10;
	double PRECISION = 2.0;
	double PRECISION_Z = 7.0;
	boolean USE_GEO = true; //TODO: Move to properties
	
	// Walkers info
	List<SourceSphere> toWrite = new ArrayList<SourceSphere>();
	
	@Override
	public void parse() {
		clientSpawnData = new AionMissionParser().parse(); //TODO: Move to ADC
		
		new AionDataCenter().getInstance().getWorldMaps();
		new AionDataCenter().getInstance().loadNpcNameIdMap();
		new AionDataCenter().getInstance().loadDataStrings();
		new AionDataCenter().getInstance().loadL10NStrings();
		
		if (USE_GEO) {GeoService.getInstance().initializeGeo();}
	}

	@Override
	public void transform() {
		
		for (String mappedName : clientSpawnData.keySet()) {
					
			mapName = getMapName(mappedName);
			if (mapName != null) {mapId = getWorldId(mapName);}
			
			Util.printSubSection(mapId + " " + getName("STR_ZONE_NAME_" + mapName));
			
			if (mapId != 0) {
				/// START OF CURRENT MAP ///
				initAllSpawns();				
				for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
					for (ClientSpawn cSpawn : clientSpawns.getObject()) {
						/** Loading current Spawn data **/
						sd = new SpawnData(cSpawn, mapId);
						if (sd.getNpcId() <= 0)
							continue;

						//TODO: Analyse afterwards, isNpc, isSiege ... based on more solid criteria
						switch (sd.getCSpawn().getType()) {
							case "SP":
								addNpcSpawn(sd);
								break;
							// case "HSP":
								// if (!Strings.isNullOrEmpty(sd.getCSpawn().getNpc()))
									// addGatherableSpawn(sd.getCSpawn());
									// break;
						}
					}
				}
				orderAllSpawns();
				addAllSpawns();
				log.reset();
				sd = null;
				/// END OF CURRENT MAP ///
			}
		}
	}
	
	@Override
	public void finalise() {
		//TODO: Move all spawns with a static ID to staticSpawnMap
		AionWalkersWriter writer = new AionWalkersWriter();
		writer.writeFromSpawns(toWrite);
	}
	
	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(orders);
		// printMarshallStatus(orders); //TODO
	}
	
	private int getWorldId(String s) {return new AionDataCenter().getInstance().getWorldId(s);}
	private String getName(String s) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringText(s) : "";}
	private ClientNpc getNpc(int id) {return (id != 0) ? new AionDataCenter().getInstance().getClientNpcs().get(id) : null;}
	
	private String getMapName(String mappedName) {
	
		String map = Util.getDirName(mappedName);
		if (!map.equalsIgnoreCase("Levels"))
			return map.toUpperCase();
		else
			System.out.println("[SPAWNS][ERROR] A mission0 file was inside of Levels directory !");
		
		return null;
	}
	
	private void addNpcSpawn(SpawnData sd) {
		Spawn s = computeSpawn(sd);
		if (isValidSpawn(s))
			addOrMerge(npcSpawnMap, s);
	}
	
	private void addGatherableSpawn(SpawnData sd) {
		Spawn s = computeSpawn(sd);
		if (isValidSpawn(s))
			addOrMerge(gatherSpawnMap, s);
	}
	
	private Spawn computeSpawn(SpawnData sd) {
		Spawn s = new Spawn();
		
		s.setNpcId(sd.getNpcId());
		s.setRespawnTime(sd.getRespawnTime());
		setSpot(s, sd);
		
		return s;
	}
	
	private void setSpot(Spawn s, SpawnData sd) {
		Spot spot = new Spot();
		
		spot.setX(sd.getX());
		spot.setY(sd.getY());
		if (USE_GEO)
			spot.setZ(GeoService.getInstance().getZ(mapId, spot.getX(), spot.getY()));
		else
			spot.setZ(sd.getZ());

		int h = MathUtil.degreeToHeading(sd.getCSpawn().getDir());
		if (h != 0) {spot.setH(h);} //TODO: h needed if static ?
		
		if (!canMove(sd.getNpcId()) && sd.getStaticId() > 0) //TODO: which types can be overriden (SP...)
			spot.setStaticId(sd.getStaticId());
		if (spot.getStaticId() == null)
			setWalkingInfo(spot, sd);
		
		s.getSpot().add(spot);
	}
	
	// Used to find if we should look out for a static id
	private boolean canMove(int npcId) {
		boolean canMove = true;
		ClientNpc npc = getNpc(sd.getNpcId());
		if (npc != null && ((int) npc.getMoveSpeedNormalWalk()) == 0)
			canMove = false;
		if (sd.getNpcInfo() != null && sd.getNpcInfo().getMovetype() != null && sd.getNpcInfo().getMovetype().equalsIgnoreCase("false"))
			canMove = false;
		//TODO: Add checks on NpcType (Portal, ...)
		return canMove;
	}
	
	private void setWalkingInfo(Spot spot, SpawnData sd) {
		
		if (sd.getCSpawn().getIidleRange() > 0) {
			if (sd.getCSpawn().getIidleRange() > RANDOM_WALK_CAP) {
				System.out.println("[SPAWNS] Npc " + sd.getNpcId() + " has " + sd.getCSpawn().getIidleRange() + " iidle range !");
				spot.setRandomWalk(RANDOM_WALK_CAP);
			}
			else
				spot.setRandomWalk(sd.getCSpawn().getIidleRange());
		}
		else if (!Strings.isNullOrEmpty(sd.getSS().getWpName())) {
			spot.setWalkerId(sd.getSS().getWpName().toUpperCase());
			toWrite.add(sd.getSS());
		}
	}
	
	public boolean isValidSpawn(Spawn s) {
		if (s.getNpcId() >= 200000 && s.getNpcId() <= 900000) {
			if (!s.getSpot().isEmpty())
				return true;
		}
		return false;
	}
	
	private void addOrMerge(SpawnMap sm, Spawn s) {

		if (!sm.getSpawn().isEmpty()) {
			boolean exists = false;
			int index = -1;
			List<Spot> newSpots = s.getSpot();
			
			for (Spawn spawn : sm.getSpawn()) {
				if (spawn.getNpcId().equals(s.getNpcId())) {
					exists = true;
					index = sm.getSpawn().indexOf(spawn);
				}
			}
			
			if (exists) {
				//TODO: If a spot very close exists, do not add
				sm.getSpawn().get(index).getSpot().addAll(newSpots);
			} else {
				sm.getSpawn().add(s);
			}
		}
		else
			sm.getSpawn().add(s);
	}
	
	private void initAllSpawns() {
		// Npc
		npcSpawnMap = new SpawnMap();
		npcSpawnMap.setMapId(mapId);
		// Instances
		instanceSpawnMap = new SpawnMap();
		instanceSpawnMap.setMapId(mapId);
		// Gather
		gatherSpawnMap = new SpawnMap();
		gatherSpawnMap.setMapId(mapId);
		// Sieges
		siegesSpawnMap = new SpawnMap();
		siegesSpawnMap.setMapId(mapId);
		// Rifts
		riftsSpawnMap = new SpawnMap();
		riftsSpawnMap.setMapId(mapId);
		// Static
		staticSpawnMap = new SpawnMap();
		staticSpawnMap.setMapId(mapId);
		//TODO: random_spawns
	}
	
	private void addAllSpawns() {
		Spawns spawns = new Spawns();
		// Npcs
		if (!npcSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(npcSpawnMap);
			System.out.println("[SPAWNS] Adding " + npcSpawnMap.getSpawn().size() + " Npc Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Npcs/" + getXml() , (Object) spawns);
			spawns = new Spawns();
		}
		// Instances
		if (!instanceSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(instanceSpawnMap);
			System.out.println("[SPAWNS] Adding " + instanceSpawnMap.getSpawn().size() + " Instance Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Instances/" + getXml() , (Object) spawns);
			spawns = new Spawns();
		}
		// Gather
		if (!gatherSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(gatherSpawnMap);
			System.out.println("[SPAWNS] Adding " + gatherSpawnMap.getSpawn().size() + " Gather Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Gather/" + getXml() , (Object) spawns);
			spawns = new Spawns();
		}
		// Siege
		if (!siegesSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(siegesSpawnMap);
			System.out.println("[SPAWNS] Adding " + siegesSpawnMap.getSpawn().size() + " Siege Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Sieges/" + getXml() , (Object) spawns);
			spawns = new Spawns();
		}
		// Rifts
		if (!riftsSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(riftsSpawnMap);
			System.out.println("[SPAWNS] Adding " + riftsSpawnMap.getSpawn().size() + " Rifts Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Rifts/" + getXml() , (Object) spawns);
			spawns = new Spawns();
		}
		// Statics
		if (!staticSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(staticSpawnMap);
			System.out.println("[SPAWNS] Adding " + staticSpawnMap.getSpawn().size() + " Static Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Statics/" + getXml() , (Object) spawns);
		}
		// END
		spawns = new Spawns();
	}
	
	private void orderAllSpawns() {
		order(npcSpawnMap);
		order(instanceSpawnMap);
		order(gatherSpawnMap);
		order(siegesSpawnMap);
		order(riftsSpawnMap);
		order(staticSpawnMap);
	}
	
	private void order(SpawnMap sm) {
		Collections.sort(sm.getSpawn(), new Comparator<Spawn>() {	
			public int compare(Spawn o1, Spawn o2) {return o1.getNpcId().compareTo(o2.getNpcId());}
		});
	}
	
	private String getXml() {
		return mapId + "_" + getName("STR_ZONE_NAME_" + mapName) + ".xml";
	}
}