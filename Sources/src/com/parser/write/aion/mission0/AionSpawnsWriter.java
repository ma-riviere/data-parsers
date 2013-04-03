package com.parser.write.aion.mission0;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.parser.input.aion.mission.CLientSpawns; //TODO rename in external Bindings
import com.parser.input.aion.mission.CLientSpawn; //TODO rename in external Bindings

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
								setSpawnXYZH(newSpawn, cSpawn, mapId);
							
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
	
	//TODO: Marshall to different output folders (Make a method for that, to redirect output (folder Output & Input))
	@Override
	public void marshall() {
		for (Integer mapId : toMarshall.keySet()) {
			String filePath = "../../" + mapId + ".xml";
			System.out.println("[SPAWNS][" + mapId + "] Spawns count: " + getSize(toMarshall.get(mapId)));
			FileMarhshaller.marshallFile(toMarshall.get(mapId), filePath, AionWritingConfig.SPAWNS_BINDINGS);
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
	
	private void setSpawnXYZH(Spawn newSpawn, CLientSpawn cSpawn, int mapId) {
		Spot spot = new Spot();
		
		String[] xyz = cSpawn.getPos().split(",");
		
		if (xyz.length != 3)
			System.out.println("[SPAWNS][ERROR] Error while splitting position ...");
		else {
			spot.setX(Float.parseFloat(xyz[0]));
			spot.setY(Float.parseFloat(xyz[1]));
			//TODO: spot.setZ(Geodata.getZ(mapId, spot.getX(), spot.getY()))
			spot.setZ(Float.parseFloat(xyz[2]));
			spot.setH(MathUtil.convertAngleToHeading(cSpawn.getDir()));
			setWalkingStatus(spot, cSpawn); // Walker or RandomWalker
			newSpawn.getSpot().add(spot);
		}
	}
	
	private void setWalkingStatus(Spot spot, ClientSpawn cSpawn) {
		if (cSpawn.getIidleRange() > 0)
			spot.setRandomWalk(cSpawn.getIidleRange());
		// else if (cSpawn.getIidleRange() == 0) //TODO
			// spot.setWalkerId();
		if (cSpawn.getIidleRange() < -1)
			System.out.println("[SPAWNS] [INFO] Idle Range inferior to -1 for Object : " + cSpawn.getNpc());
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