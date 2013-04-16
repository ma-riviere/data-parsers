package com.parser.read.aion.levels;

import java.util.ArrayList;
import java.util.List;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;

import com.parser.read.XMLParser;
import com.parser.commons.aion.properties.LevelsProperties;

public class AionLevelDataParser extends XMLParser<LevelData> {

	public AionLevelDataParser() {
		super(LevelsProperties.CLIENT_PATH + LevelsProperties.LEVELS, LevelsProperties.LEVELDATA_PREFIX, LevelsProperties.LEVELDATA_BINDINGS);
	}
	
	public List<LevelInfo> parse() {
		List<LevelInfo> elements = new ArrayList<LevelInfo>();
		for (LevelData roots : parseDir().values())
			elements.add(roots.getLevelInfo());
		return elements;
	}
}
