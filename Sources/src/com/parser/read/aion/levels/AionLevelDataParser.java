package com.parser.read.aion.levels;

import javolution.util.FastMap;
import java.util.Map;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;

import com.parser.read.XMLParser;
import com.parser.commons.aion.properties.LevelsProperties;

public class AionLevelDataParser extends XMLParser<LevelData> {

	public AionLevelDataParser() {
		super(LevelsProperties.LEVELS, LevelsProperties.LEVELDATA_PREFIX, LevelsProperties.LEVELDATA_BINDINGS);
	}
	
	public FastMap<String, LevelData> parse() {
		FastMap<String, LevelData> levelData = new FastMap<String, LevelData>();
		for (Map.Entry<String[], LevelData> entry : parseDir().entrySet())
			levelData.put(entry.getKey()[1].toUpperCase(), entry.getValue());
		return levelData;
	}
}
