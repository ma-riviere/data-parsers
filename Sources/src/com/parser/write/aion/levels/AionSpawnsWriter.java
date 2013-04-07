package com.parser.write.aion.levels;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.parser.input.aion.mission.ClientSpawns;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.common.aion.bindings.SourceSphere;
import com.parser.common.math.MathUtil;
import com.parser.common.utils.Util;
import com.parser.common.aion.AionDataCenter;

import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.levels.AionSpawnsParser;
import com.parser.read.aion.world.AionSourceSphereParser;

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
	
	// Current map info
	String mapName = "";
	int mapId = 0;
	
	// Current Npc info
	int npcId = 0;
	List<NpcInfo> mapOverrideData = new ArrayList<NpcInfo>(); // Store WorldData override data for the current map
	NpcInfo npcOverrideData = null; // Store WorldData override data for the current Npc
	int npcOverrideDataCount = 0;
	int npcOverrideEIDCount = 0;
	
	int RANDOM_WALK_CAP = 10;
	int BASE_RESPAWN_TIME = 295;
	double PRECISION = 1.5;
	double PRECISION_Z = 7.0;
	
	// Walkers info
	List<SourceSphere> ssList = new ArrayList<SourceSphere>();
	List<SourceSphere> toWrite = new ArrayList<SourceSphere>();
	
	@Override
	public void parse() {
		clientSpawnData = new AionSpawnsParser().parse();
		new AionDataCenter().getInstance().getWorldMaps();
		new AionDataCenter().getInstance().loadNpcNameIdMap();
		ssList = new AionSourceSphereParser().parse();
	}

	@Override
	public void transform() {
		
		for (String mappedName : clientSpawnData.keySet()) {
					
			mapName = getMapName(mappedName);
			if (mapName != null)
				mapId = getWorldId(mapName);
			
			Util.printSubSection("" + mapId);
			
			if (mapId != 0) {
				/// START OF CURRENT MAP ///
				initAllSpawns();
				mapOverrideData = new AionDataCenter().getInstance().getNpcInfoByMap(mapName);
				
				for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
					for (ClientSpawn cSpawn : clientSpawns.getObject()) {
						/** Loading current Npc data **/
						npcOverrideData = getNpcOverrideData(cSpawn);
						npcId = getSpawnNpcId(cSpawn);
						/** Starting spawn analysis **/
						switch (cSpawn.getType()) {
							case "SP":
								addNpcSpawn(cSpawn);
								break;
							// case "HSP":
								// if (!Strings.isNullOrEmpty(cSpawn.getNpc()))
									// addGatherableSpawn(cSpawn);
									// break;
						}
						npcOverrideData = null;
					}
				}
				orderAllSpawns();
				addAllSpawns();
				log.reset();
				mapOverrideData = null;
				System.out.println("[SPAWNS] " + npcOverrideDataCount + " Override Data were matched");
				npcOverrideDataCount = 0;
				System.out.println("[SPAWNS] " + npcOverrideEIDCount + " Override Entity ID were matched");
				npcOverrideEIDCount = 0;
				/// END OF CURRENT MAP ///
			}
		}
	}
	
	@Override
	public void finalise() {
		//TODO: Move all spawns with a static ID to staticSpawnMap
		AionWalkersWriter writer = new AionWalkersWriter();
		writer.writeFromSpawns(ssList, toWrite);
	}
	
	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(orders);
		// System.out.println(); //TODO: Log of written spawns
	}
	
	private int getWorldId(String s) {return new AionDataCenter().getInstance().getWorldId(s);}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	private String getName(String s) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringText(s) : "";}
	private ClientNpc getNpc(int id) {return (id != 0) ? new AionDataCenter().getInstance().getClientNpcs().get(id) : null;}
	
	private NpcInfo getNpcOverrideData(ClientSpawn cSpawn) {
		for (NpcInfo npc : mapOverrideData) {
			float x = npc.getPos().getX();
			float y = npc.getPos().getY();
			float z = npc.getPos().getZ();
			if (cSpawn.getPos() != null) {
				if (MathUtil.isCloseEnough(cSpawn.getPos(), x, y, z, PRECISION, PRECISION_Z)) {
					npcOverrideDataCount++;
					return npc;
				}
			}
		}
		return null;
	}
	
	private int getSpawnNpcId(ClientSpawn cSpawn) {
		int npcId = 0;
		if (!Strings.isNullOrEmpty(cSpawn.getNpc()))
			npcId = getNpcId(cSpawn.getNpc());
		
		if (npcId == 0 && npcOverrideData != null)
			npcId = npcOverrideData.getNameid();
		
		return npcId;
	}
	
	private String getMapName(String mappedName) {
	
		String map = Util.getDirName(mappedName);
		if (!map.equalsIgnoreCase("Levels"))
			return map.toUpperCase();
		else
			System.out.println("[SPAWNS][ERROR] A mission0 file was inside of Levels directory !");
		
		return null;
	}
	
	private void addNpcSpawn(ClientSpawn cSpawn) {
		Spawn s = computeSpawn(cSpawn);
		if (isValidSpawn(s))
			addOrMerge(npcSpawnMap, s);
	}
	
	private void addGatherableSpawn(ClientSpawn cSpawn) {
		Spawn s = computeSpawn(cSpawn);
		if (isValidSpawn(s))
			addOrMerge(gatherSpawnMap, s);
	}
	
	private Spawn computeSpawn(ClientSpawn cSpawn) {
		Spawn s = new Spawn();
		
		s.setNpcId(npcId);
		s.setRespawnTime(getRespawnTime(npcId));
		
		if (npcOverrideData != null) {
			String newPos = cSpawn.getPos();
			String[] xyz = new String[3];
			xyz[0] = String.valueOf(npcOverrideData.getPos().getX());
			xyz[1] = String.valueOf(npcOverrideData.getPos().getY());
			xyz[2] = String.valueOf(npcOverrideData.getPos().getZ());
			newPos = Joiner.on(",").join(xyz);
			cSpawn.setPos(newPos);
		}
		if (!Strings.isNullOrEmpty(cSpawn.getPos()))
			setSpot(s, cSpawn, mapId);
		
		return s;
	}
	
	//TODO: Move to ClientNpc.java
	private int getRespawnTime(int npcId) {
		int respawnTime = BASE_RESPAWN_TIME;
		//TODO: fonction de hpgauge, isMonster ...
		return respawnTime;
	}
	
	private void setSpot(Spawn s, ClientSpawn cSpawn, int mapId) {
		Spot spot = new Spot();
		
		String[] xyz = cSpawn.getPos().split(",");
		
		if (xyz.length != 3)
			System.out.println("[SPAWNS][ERROR] Error while splitting position ...");
		else {
			spot.setX(MathUtil.toFloat3(xyz[0]));
			spot.setY(MathUtil.toFloat3(xyz[1]));

				spot.setZ(MathUtil.toFloat3(xyz[2]));
				// spot.setZ(Geodata.getZ(mapId, spot.getX(), spot.getY()));
				int h = MathUtil.degreeToHeading(cSpawn.getDir());
				if (h != 0) {spot.setH(h);}
				if (cSpawn.getType().equalsIgnoreCase("SP") && !canMove(npcId)) //TODO: other cSpawn type can be overriden ?
					setStaticId(spot);
				if (spot.getStaticId() == null)
					setWalkingInfo(spot, cSpawn);
				
				s.getSpot().add(spot);
			
		}
	}
	
	//TODO: Moce to ClientNpc.java
	private boolean canMove(int npcId) {
		boolean canMove = true;
		ClientNpc npc = getNpc(npcId);
		if (npc != null && ((int) npc.getMoveSpeedNormalWalk()) == 0)
			canMove = false;
		//TODO: Add checks on NpcType (Portal, ...)
		return canMove;
	}
	
	private void setStaticId(Spot spot) {
		for (String mappedName : clientSpawnData.keySet()) {
			for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
				for (Entity ent : clientSpawns.getEntity()) {
					if (MathUtil.isCloseEnough(ent.getPos(), spot.getX(), spot.getY(), spot.getZ(), PRECISION, PRECISION_Z)) {
						spot.setStaticId((int) ent.getEntityId());
						npcOverrideEIDCount++;
					}
				}
			}
		}
	}
	
	private void setWalkingInfo(Spot spot, ClientSpawn cSpawn) {
		//TODO: if npcOverrideData != null check movetype !!!
		if (cSpawn.getIidleRange() > 0) {
			if (cSpawn.getIidleRange() > RANDOM_WALK_CAP)
				if (npcId != 0) {
					System.out.println("[SPAWNS] Npc " + npcId + " has " + cSpawn.getIidleRange() + " iidle range !");
					spot.setRandomWalk(RANDOM_WALK_CAP);
				}
			else
				spot.setRandomWalk(cSpawn.getIidleRange());
		}
		else if (cSpawn.getIidleRange() == 0) { //TODO: Can have a walk route even if not 0 ?
			List<SourceSphere> temp = new ArrayList<SourceSphere>();
			for (SourceSphere ss : ssList) {
				if (getNpc(npcId) != null && getNpc(npcId).getName().equalsIgnoreCase(ss.getName()))
					if (!Strings.isNullOrEmpty(ss.getWpName()) && mapId == getWorldId(ss.getMap()))
						temp.add(ss);
			}
			
			for (SourceSphere ss2 : temp) {
				if (MathUtil.isCloseEnough(ss2.getX(), ss2.getY(), ss2.getZ(), spot.getX(), spot.getY(), spot.getZ(), ss2.getRadius() + PRECISION, ss2.getRadius() + PRECISION_Z)) {
					toWrite.add(ss2);
					spot.setWalkerId(ss2.getWpName().toUpperCase());
				}
			}
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
		return mapId + getName("STR_ZONE_NAME_" + mapName) + ".xml";
	}
}