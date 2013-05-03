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

import com.parser.write.DataProcessor;
import com.parser.write.aion.world.AionWalkersWriter;

import com.parser.output.aion.mission.*;

public class AionSpawnsWriter extends DataProcessor {
	
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
			if (mapId == 0) continue;
			initAllSpawns(mapId);
			Util.printSubSection(mapId + " : " + aion.getStringText("STR_ZONE_NAME_" + mapName));
			
			// Initializing client spawn data
			List<NpcInfo> spawnsFromWorld = aion.getDataSpawns(mapName);
			
			List<ClientSpawn> allCSpawns =  levelSpawns.get(mapName);
			List<ClientSpawn> currentCSpawns = new LinkedList<ClientSpawn>();
			for (ClientSpawn cs : allCSpawns) {
				if (ClientSpawnType.fromClient(cs.getType()) != null && ClientSpawnType.fromClient(cs.getType()).shouldBeSpawned())
					currentCSpawns.add(cs);
			}
			int startLevelSize = currentCSpawns.size();
			List<ClientSpawn> usedCS = new LinkedList<ClientSpawn>();
			//TODO: Store used SS with WP !
			
			// Spawning WORLD
			for (NpcInfo info : spawnsFromWorld) {
				SpawnData sd = new SpawnData(info.getNameid(), info, mapName);
				ClientSpawn match = sd.findMatchingCS(info, currentCSpawns);
				if (match != null) { 
					sd = new SpawnData(info.getNameid(), match, mapName);
					currentCSpawns.remove(match);
				}
				addSpawn(sd);
			}
			System.out.println("[SPAWNS] Added " + spawnsFromWorld.size() + " spots from World spawns !");
			
			// Spawning LEVELS (only spawns that can be identified)
			for (ClientSpawn cSpawn : currentCSpawns) {
				if (!Strings.isNullOrEmpty(cSpawn.getNpc()) && aion.getNpcs().get(cSpawn.getNpc().toUpperCase()) != null) {
					SpawnData sd = new SpawnData(aion.getNpcs().get(cSpawn.getNpc().toUpperCase()).getId(), cSpawn, mapName);
					addSpawn(sd);
					usedCS.add(cSpawn);
				}
			}
			for (ClientSpawn cs : usedCS) {currentCSpawns.remove(cs);}
			usedCS.clear();
			
			// Spawning unidentified LEVELS spawns using SPHERES
			for (ClientSpawn uCSpawn : currentCSpawns) {
				SpawnData sd = new SpawnData(uCSpawn, mapName);
				if (sd.getNpcId() >= 200000) {
					addSpawn(sd);
					usedCS.add(uCSpawn);
				}
			}
			for (ClientSpawn cs : usedCS) {currentCSpawns.remove(cs);}
			usedCS.clear();
			
			System.out.println("[SPAWNS] Added " + (startLevelSize - currentCSpawns.size()) + " / " + startLevelSize + " spots from Levels spawns !");

			// FINALLY //
			addAllSpawns(mapId, mapName);
			log.reset();
		}
	}
	
	@Override
	public void create() {
		// Creating walkers needed for the maps
		AionWalkersWriter writer = new AionWalkersWriter();
		writer.writeFromSpawns(toWrite);
	}
	
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

		if (sd.getWalkerId() != null) {
			spot.setWalkerId(sd.getWalkerId());
			if (!toWrite.contains(sd.getSS()))
				toWrite.add(sd.getSS());
		}
		else if (sd.getRandomWalk() > 0)
			spot.setRandomWalk(sd.getRandomWalk());
		
		//TODO: Walker state && walker ID ??
		
		if (sd.getStaticId() > 0) 
			spot.setStaticId(sd.getStaticId());
		
		if (sd.getH() != 0) {spot.setH(sd.getH());}
		
		// To match WalkerFormator logic
		// if (spot.getWalkerId() != null) {
			// spot.setX(sd.getSS().getX());
			// spot.setY(sd.getSS().getY());
			// spot.setZ(sd.getSS().getZ());
		// }
		
		// Fixing Z with Geo && useful level data (TODO)
		// if (spot.getStaticId() == null)
			// spot.setZ(ZUtils.getBestZ(sd));
		
		s.getSpot().add(spot);
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
				List<Spot> merged = new ArrayList<Spot>();
				merged.addAll(sm.getSpawn().get(index).getSpot());
				merged.addAll(newSpots);
				merged = noDuplicates(merged);
				sm.getSpawn().get(index).getSpot().clear();
				sm.getSpawn().get(index).getSpot().addAll(merged);
			} 
			else {
				sm.getSpawn().add(s);
				addComment(s, aion.getStringText(aion.getNpc(s.getNpcId()).getDesc()));
			}
		}
		else {
			sm.getSpawn().add(s);
			// addLine();
			addComment(s, aion.getStringText(aion.getNpc(s.getNpcId()).getDesc()));
		}
	}
	
	private List<Spot> noDuplicates(List<Spot> spots) {
		List<Spot> results = new LinkedList<Spot>(spots);
		for (int i = 0; i < spots.size(); i++) {
			Spot s1 = spots.get(i);
			for (int j = i+1; j < spots.size(); j++) {
				Spot s2 = spots.get(j);
				if (MathUtil.isCloseEnough(s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), 2, 8))
					results.remove(s2); // TODO: Remove s1 (first added) or s2 (last added) ?
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
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(npcSpawnMap) + " Npc Spots");
			addOrder(LevelsProperties.SPAWNS + "Npcs/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Instances
		if (!instanceSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(instanceSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(instanceSpawnMap) + " Instance Spots");
			addOrder(LevelsProperties.SPAWNS + "Instances/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Gather
		if (!gatherSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(gatherSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(gatherSpawnMap) + " Gather Spots");
			addOrder(LevelsProperties.SPAWNS + "Gather/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Siege
		if (!siegesSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(siegesSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(siegesSpawnMap) + " Siege Spots");
			addOrder(LevelsProperties.SPAWNS + "Sieges/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Rifts
		if (!riftsSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(riftsSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(riftsSpawnMap) + " Rifts Spots");
			addOrder(LevelsProperties.SPAWNS + "Rifts/" + getXml(mapId, mapName), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Statics
		if (!staticSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(staticSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSpotSize(staticSpawnMap) + " Static Spots");
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
		return mapId + "_" + aion.getStringText("STR_ZONE_NAME_" + mapName);
	}
}