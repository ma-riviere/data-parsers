package com.parser.write.aion.levels;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import javolution.util.FastMap;

import com.geo.aion.GeoService;

import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.commons.aion.AionDataHub;
import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.enums.spawns.ClientSpawnType;
import com.parser.commons.aion.models.SpawnData;
import com.parser.commons.aion.properties.AionProperties;
import com.parser.commons.aion.properties.LevelsProperties;
import com.parser.commons.aion.utils.ZUtils;
import com.parser.commons.utils.maths.MathUtil;
import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;
import com.parser.write.aion.world.AionWalkersWriter;

import com.parser.output.aion.mission.*;

public class AionSpawnsWriter extends AbstractWriter {
	
	SpawnMap npcSpawnMap = new SpawnMap();
	SpawnMap instanceSpawnMap = new SpawnMap();
	SpawnMap gatherSpawnMap = new SpawnMap();
	SpawnMap siegesSpawnMap = new SpawnMap();
	SpawnMap riftsSpawnMap = new SpawnMap();
	SpawnMap staticSpawnMap = new SpawnMap();
	
	FastMap<String, List<ClientSpawn>> levelSpawns;
	List<SourceSphere> toWrite = new ArrayList<SourceSphere>();
	
	@Override
	public void collect() {
		levelSpawns = new FastMap<String, List<ClientSpawn>>(aion.getLevelSpawns());
		aion.getLevelEntities();
		aion.getSpheres();
		aion.getWorldMaps();
		aion.getNpcs();
		aion.getStrings();
		
		if (AionProperties.USE_GEO) {GeoService.getInstance().initializeGeo();}
	}
	
	@Override
	public void transform() {
		
		for (String mapName : levelSpawns.keySet()) {
			int mapId = aion.getWorldId(mapName);
			initAllSpawns(mapId);
			Util.printSubSection(mapId + " : " + getName("STR_ZONE_NAME_" + mapName));
			
			List<ClientSpawn> currentCSpawns = levelSpawns.get(mapName);
			int startLevelSize = currentCSpawns.size();
			List<NpcInfo> currentWDSpawns = aion.getDataSpawns().get(mapName);
			int startWorldSize = currentWDSpawns.size();
			
			List<ClientSpawn> usedCSpawns = new LinkedList<ClientSpawn>();
			List<NpcInfo> usedWDSpawns = new LinkedList<NpcInfo>(); 
			
			// Loading spawns from Mission_Mission0.xml
			for (ClientSpawn cSpawn : currentCSpawns) {
				if (ClientSpawnType.fromClient(cSpawn.getType()) != null && ClientSpawnType.fromClient(cSpawn.getType()).shouldBeSpawned()) {
					SpawnData sd = new SpawnData(cSpawn, mapName);
					if (sd.getNpcId() > 0) {
						addSpawn(sd);
						if (!usedCSpawns.contains(cSpawn))
							usedCSpawns.add(cSpawn);
						if (sd.getNpcInfo() != null && !usedWDSpawns.contains(sd.getNpcInfo()))
							usedWDSpawns.add(sd.getNpcInfo());
					}
				}
			}
			for (ClientSpawn cs : usedCSpawns)
				currentCSpawns.remove(cs);
				
			System.out.println("[SPAWNS] " + currentCSpawns.size() + " / " + startLevelSize + " spots from LEVELS were not used");
			
			// Loading spawns from client_world_*.xml
			for (NpcInfo info : currentWDSpawns) {
				if (!usedWDSpawns.contains(info)) {
					SpawnData sd = new SpawnData(info, mapName);
					if (sd.getNpcId() > 0) {
						addSpawn(sd);
						usedWDSpawns.add(info);
					}
				}
			}
			for (NpcInfo inf : usedWDSpawns)
				currentWDSpawns.remove(inf);
			
			System.out.println("[SPAWNS] " + currentWDSpawns.size() + " / " + startWorldSize + " spots from WORLD were not used");

			// FINALLY //
			addAllSpawns(mapId, mapName);
			log.reset();
		}
	}
	
	@Override
	public void finalise() {
		AionWalkersWriter writer = new AionWalkersWriter();
		writer.writeFromSpawns(toWrite);
	}
	
	@Override
	public void create() {
		FileMarshaller.marshallFile(orders);
	}
	
	private String getName(String s) {return (s != null) ? aion.getStrings().get(s).getBody() : "";}
	
	private void addSpawn(SpawnData sd) {
		Spawn s = computeSpawn(sd);
		if (isValidSpawn(s)) {
			// if (isFullStatic(s)) // TODO: Should be checked after the merge
				// addOrMerge(staticSpawnMap, s);
			// else
				addOrMerge(npcSpawnMap, s);
		}
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
		spot.setZ(sd.getZ());
		
		if (sd.canBeStatic() && sd.getStaticId() != -1) 
			spot.setStaticId(sd.getStaticId());
		if (spot.getStaticId() == null && sd.canMove())
			setWalkingInfo(spot, sd);
		
		if (sd.getCSpawn() != null && sd.getCSpawn().getDir() != null && spot.getRandomWalk() == null && spot.getWalkerId() == null && spot.getStaticId() == null) {
			int h = MathUtil.degreeToHeading(sd.getCSpawn().getDir());
			if (h != 0) {spot.setH(h);}
		}
		
		// To match WalkerFormator logic
		if (spot.getWalkerId() != null) {
			spot.setX(sd.getSS().getX());
			spot.setY(sd.getSS().getY());
			spot.setZ(sd.getSS().getZ());
		}
		
		// if (spot.getStaticId() == null)
			// spot.setZ(ZUtils.getBestZ(sd));
		
		s.getSpot().add(spot);
	}
	
	private void setWalkingInfo(Spot spot, SpawnData sd) {
		
		if (sd.getCSpawn() != null) {
			if (sd.getCSpawn().getIidleRange() != null && sd.getCSpawn().getIidleRange() >= 0) {
				if (sd.getWalkerId() != null) {
					spot.setWalkerId(sd.getSS().getWpName().toUpperCase());
					//TODO: Set Walker Id & State
					if (!toWrite.contains(sd.getSS()))
						toWrite.add(sd.getSS());
				}
				else {
					if (sd.getCSpawn().getIidleRange() > LevelsProperties.RANDOM_WALK_CAP || sd.getCSpawn().getIidleRange() <= 1)
						spot.setRandomWalk(LevelsProperties.RANDOM_WALK_CAP);
					else
						spot.setRandomWalk(sd.getCSpawn().getIidleRange());
				}
			}
		} else if (sd.getNpcInfo() != null) {
			if (sd.getNpcInfo().getMovetype().equalsIgnoreCase("true")) {
				if (sd.getWalkerId() != null) {
					spot.setWalkerId(sd.getSS().getWpName().toUpperCase());
					//TODO: Set Walker Id & State
					if (!toWrite.contains(sd.getSS()))
						toWrite.add(sd.getSS());
				}
				else
					spot.setRandomWalk(LevelsProperties.RANDOM_WALK_CAP);
			}
		}
	}
	
	private boolean isValidSpawn(Spawn s) {
		if (s.getNpcId() >= 200000 && s.getNpcId() <= 900000) {
			if (!s.getSpot().isEmpty())
				return true;
		}
		return false;
	}
	
	private boolean isFullStatic(Spawn s) {
		for (Spot spot : s.getSpot())
			if (spot.getStaticId() == null || spot.getStaticId() <= 0)
				return false;
		return true;
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
				List<Spot> merged = sm.getSpawn().get(index).getSpot();
				merged.addAll(newSpots);
				merged = noDuplicates(merged);
				sm.getSpawn().get(index).getSpot().clear();
				sm.getSpawn().get(index).getSpot().addAll(merged);
			} 
			else
				sm.getSpawn().add(s);
		}
		else
			sm.getSpawn().add(s);
	}
	
	private List<Spot> noDuplicates(List<Spot> spots) {
		List<Spot> results = new LinkedList<Spot>(spots);
		for (int i = 0; i < spots.size(); i++) {
			Spot s1 = spots.get(i);
			for (int j = i+1; j < spots.size(); j++) {
				Spot s2 = spots.get(j);
				if (MathUtil.isCloseEnough(s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), 2, 8))
					results.remove(s2); // TODO: Remove s1 or s2 ?
			}
		}
		return results;
	}
	
	private void initAllSpawns(int mapId) {
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
	
	private void addAllSpawns(int mapId, String mapName) {
		orderAllSpawns();
		
		Spawns spawns = new Spawns();
		// Npcs
		if (!npcSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(npcSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(npcSpawnMap) + " Npc Spots");
			addOrder(LevelsProperties.SPAWNS + "Npcs/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Instances
		if (!instanceSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(instanceSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(instanceSpawnMap) + " Instance Spots");
			addOrder(LevelsProperties.SPAWNS + "Instances/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Gather
		if (!gatherSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(gatherSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(gatherSpawnMap) + " Gather Spots");
			addOrder(LevelsProperties.SPAWNS + "Gather/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Siege
		if (!siegesSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(siegesSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(siegesSpawnMap) + " Siege Spots");
			addOrder(LevelsProperties.SPAWNS + "Sieges/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Rifts
		if (!riftsSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(riftsSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(riftsSpawnMap) + " Rifts Spots");
			addOrder(LevelsProperties.SPAWNS + "Rifts/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Statics
		if (!staticSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(staticSpawnMap);
			System.out.println("[SPAWNS] Adding " + getSpotSize(staticSpawnMap) + " Static Spots");
			addOrder(LevelsProperties.SPAWNS + "Statics/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
		}
		// END
		spawns = new Spawns();
	}
	
	private int getSpotSize(SpawnMap sm) {
		int c = 0;
		for (Spawn s : sm.getSpawn())
			c += s.getSpot().size();
		return c;
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
	
	private String getXml(int mapId, String mapName) {
		return mapId + "_" + getName("STR_ZONE_NAME_" + mapName) + ".xml";
	}
}