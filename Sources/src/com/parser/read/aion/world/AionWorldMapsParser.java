package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.world_maps.WorldMaps;
import com.parser.input.aion.world_maps.WorldMap;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldMapsParser extends XMLParser<WorldMaps> {

	public AionWorldMapsParser() {super(AionReadingConfig.WORLD_MAPS_BINDINGS);}
	
	public List<WorldMap> parse() {
		WorldMaps root = parseFile(AionReadingConfig.WORLD_MAPS);
		return root.getData();
	}
}
