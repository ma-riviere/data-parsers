package com.parser.read.aion.levels;

import javolution.util.FastMap;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;

import com.parser.read.XMLParser;
import com.parser.commons.aion.properties.LevelsProperties;

public class AionLevelDataParser extends XMLParser<LevelData> {

	public AionLevelDataParser() {
		super(LevelsProperties.LEVELS, LevelsProperties.LEVELDATA_PREFIX, LevelsProperties.LEVELDATA_BINDINGS);
	}
	
	public FastMap<String, LevelInfo> parse() {
		FastMap<String, LevelInfo> levelInfos = new FastMap<String, LevelInfo>();
		for (Map.Entry<String[], LevelData> entry : parseDir())
			levelInfos.put(entry.getKey()[1], entry.getValue().getLevelInfo());
		return levelInfos;
	}
}
