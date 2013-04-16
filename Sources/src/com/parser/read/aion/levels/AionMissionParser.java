package com.parser.read.aion.levels;

import java.util.List;
import java.util.Map;
import javolution.util.FastMap;

import com.parser.commons.aion.properties.LevelsProperties;

import com.parser.input.aion.mission.Mission;
import com.parser.input.aion.mission.ClientSpawns;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.mission.Entity;

import com.parser.read.XMLParser;

public class AionMissionParser extends XMLParser<Mission> {

	public AionMissionParser() {
		super(LevelsProperties.CLIENT_PATH + LevelsProperties.LEVELS, LevelsProperties.MISSION0_PREFIX, LevelsProperties.MISSION0_BINDINGS);
	}
	
	private static FastMap<String, Mission> rootData = null;
	
	private FastMap<String, Mission> getRootData() {
		if (rootData == null) {
			rootData = new FastMap<String, Mission>();
			for (Map.Entry<String[], Mission> entry : parseDir().entrySet())
				rootData.put(entry.getKey()[1], entry.getValue());
		}
		return rootData;
	}
	
	public FastMap<String, List<ClientSpawn>> parseSpawns() {
		FastMap<String, List<ClientSpawn>> spawns = new FastMap<String, List<ClientSpawn>>();
		for (Map.Entry<String, Mission> entry : getRootData().entrySet())
			spawns.put(entry.getKey(), entry.getValue().getObjects().getObject());
		return spawns;
	}
	
	public List<ClientSpawn> parseSpawns(String file) {
		return parseFile(file).getObjects().getObject();
	}
	
	public FastMap<String, List<Entity>> parseEntities() {
		FastMap<String, List<Entity>> entities = new FastMap<String, List<Entity>>();
		for (Map.Entry<String, Mission> entry : getRootData().entrySet())
			entities.put(entry.getKey(), entry.getValue().getObjects().getEntity());
		return entities;
	}

	public List<Entity> parseEntities(String file) {
		return parseFile(file).getObjects().getEntity();
	}
}