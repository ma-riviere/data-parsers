package com.parser.write.aion.mission0;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.parser.input.aion.mission.Objects;
import com.parser.input.aion.mission.Object;

import com.parser.common.aion.*;
import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.mission0.AionMission0Parser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.mission.*;

public class AionSpawnsWriter extends AbstractWriter {
	
	Map<String, List<Objects>> clientSpawnData;
	
	Map<Integer, Spawns> toMarshall = new HashMap<Integer, Spawns>();
		
	@Override
	public void parse() {
		clientSpawnData = new AionMission0Parser().parse();
		new AionDataCenter().getInstance().loadDescWorldIdMap();
		new AionDataCenter().getInstance().loadNpcNameIdMap();
	}

	@Override
	public void transform() {
		for (String mappedName : clientSpawnData.keySet()) {
		
			int mapId = getCurrentMapId(mappedName);
			
			SpawnMap spawnMap = new SpawnMap();
			Spawns spawns = new Spawns();
			
			if (mapId != 0) {
				spawnMap.setMapId(mapId);
				/// START ///
				for (Objects objects : clientSpawnData.get(mappedName)) {
					List<Spawn> newSpawns = new ArrayList<Spawn>();
					for (Object cSpawn : objects.getObject()) {
						if (!Strings.isNullOrEmpty(cSpawn.getNpc()) && getNpcId(cSpawn.getNpc()) != 0) {
							Spawn newSpawn = new Spawn();
							
							newSpawn.setNpcId(getNpcId(cSpawn.getNpc()));
							
							if (!Strings.isNullOrEmpty(cSpawn.getPos()))
								setSpawnXYZ(newSpawn, cSpawn.getPos());
							
							addOrMerge(newSpawns, newSpawn);
						}
					}
					// Sorting Spawn by NpcId
					Collections.sort(newSpawns, new Comparator<Spawn>() {	
						public int compare(Spawn o1, Spawn o2) {return o1.getNpcId().compareTo(o2.getNpcId());}
					});
					
					spawnMap.getSpawn().addAll(newSpawns);
					newSpawns.clear();
				}
				
				/// END ///
				spawns.getSpawnMap().add(spawnMap);
				toMarshall.put(mapId, spawns);
				spawns = null;
			}
		}
	}

	@Override
	public void marshall() {
		for (Integer mapId : toMarshall.keySet()) {
			String fileName = "../../" + mapId + ".xml";
			System.out.println("[SPAWNS][" + mapId + "] Spawns count: " + getSize(toMarshall.get(mapId)));
			FileMarhshaller.marshallFile(toMarshall.get(mapId), "../../" + mapId + ".xml", AionWritingConfig.SPAWNS_BINDINGS);
		}
	}
	
	private int getWorld(String s) {return (s != null) ? new AionDataCenter().getInstance().getWorldIdByName(s) : 0;}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	
	private int getSize(Spawns spawns) {
		int count = 0;
		for (SpawnMap sm : spawns.getSpawnMap())
			count += sm.getSpawn().size();
		return count;
	}
	
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
	
	private void setSpawnXYZ(Spawn newSpawn, String cPos) {
		Spot spot = new Spot();
		String[] xyz = cPos.split(",");
		if (xyz.length != 3)
			System.out.println("[SPAWNS][ERROR] Error while splitting position ...");
		else {
			spot.setX(Float.parseFloat(xyz[0]));
			spot.setY(Float.parseFloat(xyz[1]));
			spot.setZ(Float.parseFloat(xyz[2]));
			// if (Float.valueOf(Math.abs(spot.getX())).compareTo(0.01f) <= 0 && Float.valueOf(Math.abs(spot.getY())).compareTo(0.01f) <= 0)
				newSpawn.getSpot().add(spot);
			// else
				// System.out.println("[SPAWNS] [WARN] " + newSpawn.getNpcId() + " has a {O,O} spawn !");
		}
	}
	
	private void addOrMerge(List<Spawn> newSpawns, Spawn newSpawn) {
		if (!newSpawns.isEmpty()) {
			boolean exists = false;
			int index = -1;
			List<Spot> newSpots = newSpawn.getSpot();
			
			for (Spawn spawn : newSpawns) {
				if (spawn.getNpcId().equals(newSpawn.getNpcId())) {
					exists = true;
					index = newSpawns.indexOf(spawn);
				}
			}
			if (exists) {
				newSpawns.get(index).getSpot().addAll(newSpots);
			}
			else
				newSpawns.add(newSpawn);
		}
		else
			newSpawns.add(newSpawn);
	}
}