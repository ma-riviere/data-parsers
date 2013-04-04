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
			Util.printSubSection("" + mapId);
			
			if (mapId != 0) {
				initAllSpawns();
				/// START OF CURRENT MAP ///
				for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
					
					for (ClientSpawn cSpawn : clientSpawns.getObject()) {
					
						switch (cSpawn.getType()) {
							case "SP":
								if (Strings.isNullOrEmpty(cSpawn.getNpc()))
										addClientWorldSpawn(cSpawn);
								else if (getNpcId(cSpawn.getNpc()) != 0)
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
	
	private int getCurrentMapId(String mappedName) {
		int mapId = 0;
		String[] names = mappedName.split("@");
		if (names.length != 2)
			System.out.println("[SPAWNS] Error while splitting name ...");
		else if (!names[0].equalsIgnoreCase("Levels"))
			mapId = getWorld(names[0]);
		else
			System.out.println("[SPAWNS][ERROR] A mission0 file was inside of Levels directory !");
		return mapId;
	}
	
	private void addNpcSpawn(ClientSpawn cSpawn) {
		Spawn s = computeSpawn(cSpawn);
		addOrMerge(npcSpawnMap, s);
	}
	
	private void addClientWorldSpawn(ClientSpawn cSpawn) {
		// 1 : Check if already exists (same npcId & very close coordinates)
		Spawn s = computeSpawn(cSpawn);
		// TODO: Get the npc_id from client_world_....xml, <npc_info>
		// TODO: Check if null coordinates
		// npcSpawnMap.getSpawn().add(s);
	}
	
	private void addGatherableSpawn(ClientSpawn cSpawn) {
		Spawn s = computeSpawn(cSpawn);
		addOrMerge(gatherSpawnMap, s);
	}
	
	private Spawn computeSpawn(ClientSpawn cSpawn) {
		Spawn s = new Spawn();
		
		s.setNpcId(getNpcId(cSpawn.getNpc()));
		
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
			spot.setH(MathUtil.degreeToHeading(cSpawn.getDir()));
			if (cSpawn.getType().equalsIgnoreCase("SP")&& Strings.isNullOrEmpty(cSpawn.getNpc()))
				setStaticId(spot);
			setWalkingInfo(spot, cSpawn); // Walker or RandomWalker
			s.getSpot().add(spot);
		}
	}
	
	private float toFloat3(String s) {
		return Math.round(Float.parseFloat(s) * (1000.00f)) / (1000.00f);
	}
	
	private void setStaticId(Spot spot) {
		for (String mappedName : clientSpawnData.keySet()) {
			for (ClientSpawns clientSpawns : clientSpawnData.get(mappedName)) {
				for (Entity ent : clientSpawns.getEntity()) {
					String[] xyz = ent.getPos().split(",");
					if (MathUtil.getDistance(spot.getX(), spot.getY(), spot.getZ(), Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2])) < 0.05){
						//TODO: Check if z var not too big
						spot.setStaticId((int) ent.getEntityId());
					}
				}
			}
		}
	}
	
	private void setWalkingInfo(Spot spot, ClientSpawn cSpawn) {
		if (cSpawn.getIidleRange() > 0)
			spot.setRandomWalk(cSpawn.getIidleRange());
		// else if (cSpawn.getIidleRange() == 0) //TODO
			// spot.setWalkerId();
		if (cSpawn.getIidleRange() < -1)
			System.out.println("[SPAWNS] [INFO] Idle Range inferior to -1 for Object : " + cSpawn.getNpc());
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
				sm.getSpawn().get(index).getSpot().addAll(newSpots);
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