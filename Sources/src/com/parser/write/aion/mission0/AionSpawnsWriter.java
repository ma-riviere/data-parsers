package com.parser.write.aion.mission0;

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
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.common.*;
import com.parser.common.aion.*;
import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.mission0.AionSpawnsParser;
import com.parser.write.*;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.mission.*;

public class AionSpawnsWriter extends AbstractWriter {
	
	Map<String, List<ClientSpawns>> clientSpawnData;
	
	SpawnMap npcSpawnMap = new SpawnMap();
	SpawnMap instanceSpawnMap = new SpawnMap();
	SpawnMap gatherSpawnMap = new SpawnMap();
	SpawnMap siegesSpawnMap = new SpawnMap();
	SpawnMap riftsSpawnMap = new SpawnMap();
	SpawnMap staticSpawnMap = new SpawnMap();
	
	int mapId = 0;
	String mapName = "";
	int count = 0; 
	List<NpcInfo> currentNpcInfos = new ArrayList<NpcInfo>();
	
	int RANDOM_WALK_CAP = 15;
	
	@Override
	public void parse() {
		clientSpawnData = new AionSpawnsParser().parse();
		new AionDataCenter().getInstance().loadDescWorldIdMap();
		new AionDataCenter().getInstance().loadNpcNameIdMap();
	}

	@Override
	public void transform() {
		
		for (String mappedName : clientSpawnData.keySet()) {
					
			mapId = getCurrentMapId(mappedName);
			mapName = mappedName;
			
			Util.printSubSection("" + mapId);
			
			if (mapId != 0) {
				initAllSpawns();
				/// START OF CURRENT MAP ///
				for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
					
					for (ClientSpawn cSpawn : clientSpawns.getObject()) {
					
						switch (cSpawn.getType()) {
							case "SP":
								addNpcSpawn(cSpawn);
								break;
							// case "HSP":
								// if (!Strings.isNullOrEmpty(cSpawn.getNpc()))
									// addGatherableSpawn(cSpawn);
									// break;
						}
						
					}
				}
				orderAllSpawns();
				addAllSpawns();
			}
			log.reset();
			System.out.println("[SPAWNS] " + count + " Npc IDs were matched fron WorldData");
			count = 0;
			/// END OF CURRENT MAP ///
		}
		
	}
	
	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(orders);
		// System.out.println(); //TODO: Log of written spawns
	}
	
	private int getWorld(String s) {return (s != null) ? new AionDataCenter().getInstance().getWorldIdByName(s) : 0;}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	private int getSpawnNpcId(ClientSpawn cSpawn) {
		int npcId = 0;
		if (!Strings.isNullOrEmpty(cSpawn.getNpc()))
			npcId = getNpcId(cSpawn.getNpc());
		
		currentNpcInfos = new AionDataCenter().getInstance().getNpcInfoByMap(mapName);
		if (npcId == 0 && !currentNpcInfos.isEmpty())
			npcId = getNpcIdByWorldData(cSpawn);
		
		return npcId;
	}
	
	private int getNpcIdByWorldData(ClientSpawn cSpawn) {
		int npcId = 0;
		for (NpcInfo npc : currentNpcInfos) {
			float x = npc.getPos().getX();
			float y = npc.getPos().getY();
			float z = npc.getPos().getZ();
			if (cSpawn.getPos() != null) {
				String[] xyz = cSpawn.getPos().split(",");
				if (MathUtil.getDistance(x, y, z, toFloat3(xyz[0]), toFloat3(xyz[1]), toFloat3(xyz[2])) < 0.7) { //TODO: Change precision and see count
					npcId = npc.getNameid();
					count++;
				}
			}
		}
		return npcId;
	}
	
	private int getCurrentMapId(String mappedName) {
		int map = 0;
		String mapName = Util.getDirName(mappedName);
		if (!mapName.equalsIgnoreCase("Levels"))
			map = getWorld(mapName);
		else
			System.out.println("[SPAWNS][ERROR] A mission0 file was inside of Levels directory !");
		return map;
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
		
		s.setNpcId(getSpawnNpcId(cSpawn));
		
		if (!Strings.isNullOrEmpty(cSpawn.getPos()))
			setSpot(s, cSpawn, mapId);
		
		return s;
	}
	
	private void setSpot(Spawn s, ClientSpawn cSpawn, int mapId) {
		Spot spot = new Spot();
		
		String[] xyz = cSpawn.getPos().split(",");
		
		if (xyz.length != 3)
			System.out.println("[SPAWNS][ERROR] Error while splitting position ...");
		else {
			spot.setX(toFloat3(xyz[0]));
			spot.setY(toFloat3(xyz[1]));
			spot.setZ(toFloat3(xyz[2]));
			// spot.setZ(Geodata.getZ(mapId, spot.getX(), spot.getY())); // TODO
			int h = MathUtil.degreeToHeading(cSpawn.getDir());
			if (h != 0)
				spot.setH(h);
			if (cSpawn.getType().equalsIgnoreCase("SP") && canMove(getSpawnNpcId(cSpawn))) //TODO: Move to Npc.canMove()
				setStaticId(spot);
			if (spot.getStaticId() == null)
				setWalkingInfo(spot, cSpawn); // Walker or RandomWalker
			
			s.getSpot().add(spot);
		}
	}
	
	private float toFloat3(String s) {
		return Math.round(Float.parseFloat(s) * (1000.00f)) / (1000.00f);
	}
	
	private boolean canMove(int npcId) {
		boolean canMove = true; //false
		
		return canMove;
	}
	
	private void setStaticId(Spot spot) {
		for (String mappedName : clientSpawnData.keySet()) {
			for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
				for (Entity ent : clientSpawns.getEntity()) {
					String[] xyz = ent.getPos().split(",");
					if (MathUtil.getDistance(spot.getX(), spot.getY(), spot.getZ(), toFloat3(xyz[0]), toFloat3(xyz[1]), toFloat3(xyz[2])) < 0.7){
						//TODO: Check if z var not too big (function that check z and x,y seperately)
						spot.setStaticId((int) ent.getEntityId());
					}
				}
			}
		}
	}
	
	private void setWalkingInfo(Spot spot, ClientSpawn cSpawn) {
		if (cSpawn.getIidleRange() > 0) {
			if (cSpawn.getIidleRange() > RANDOM_WALK_CAP)
				System.out.println("[SPAWNS] Npc " + getSpawnNpcId(cSpawn) + " has " + cSpawn.getIidleRange() + " random_walk range !");
			else //TODO: if speed = 0 or type portal/noaction/... do not set
				spot.setRandomWalk(cSpawn.getIidleRange());
		}
		// else if (cSpawn.getIidleRange() == 0) //TODO
			// spot.setWalkerId();
	}
	
	public boolean isValidSpawn(Spawn s) {
		if (s.getNpcId() >= 200000 && s.getNpcId() <= 900000) {
			// TODO: Check coords
			return true;
		}
		return false;
	}
	
	// TODO: Check if null coordinates
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
			
			if (exists)
				sm.getSpawn().get(index).getSpot().addAll(newSpots); //TODO: If a spot very close exists, do not add
			else
				sm.getSpawn().add(s);
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
	}
	
	private void addAllSpawns() {
		Spawns spawns = new Spawns();
		// Npcs
		if (!npcSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(npcSpawnMap);
			System.out.println("[SPAWNS] Adding " + npcSpawnMap.getSpawn().size() + " Npc Spawns");
			addOrder(AionWritingConfig.SPAWNS_BINDINGS, AionWritingConfig.SPAWNS + "Npcs/" + getXml(mapId) , (Object) spawns);
			spawns = new Spawns();
		}
		// Instances
		if (!instanceSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(instanceSpawnMap);
			System.out.println("[SPAWNS] Adding " + instanceSpawnMap.getSpawn().size() + " Instance Spawns");
			spawns = new Spawns();
		}
		// Gather
		if (!gatherSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(gatherSpawnMap);
			System.out.println("[SPAWNS] Adding " + gatherSpawnMap.getSpawn().size() + " Gather Spawns");
			spawns = new Spawns();
		}
		// Siege
		if (!siegesSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(siegesSpawnMap);
			System.out.println("[SPAWNS] Adding " + siegesSpawnMap.getSpawn().size() + " Siege Spawns");
			spawns = new Spawns();
		}
		// Rifts
		if (!riftsSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(riftsSpawnMap);
			System.out.println("[SPAWNS] Adding " + riftsSpawnMap.getSpawn().size() + " Rifts Spawns");
			spawns = new Spawns();
		}
		// Statics
		if (!staticSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(staticSpawnMap);
			System.out.println("[SPAWNS] Adding " + staticSpawnMap.getSpawn().size() + " Static Spawns");
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
	
	private String getXml(int mapId) {
		return mapId + ".xml"; //TODO: Add name after mapId
	}
}