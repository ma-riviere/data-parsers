package com.parser.read.aion.levels;

import java.util.ArrayList;
import java.util.List;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionLevelDataParser extends XMLParser<LevelData> {

	public AionLevelDataParser() {
		super(AionReadingConfig.MISSION0, AionReadingConfig.LEVEL_DATA_PREFIX, AionReadingConfig.LEVEL_DATA_BINDINGS);
	}
	
	public List<LevelInfo> parse() {
		List<LevelInfo> elements = new ArrayList<LevelInfo>();
		for (LevelData roots : parseDir().values())
			elements.addAll(roots.getLevelInfo());
		return elements;
	}
}
