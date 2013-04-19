package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.world_maps.WorldMaps;
import com.parser.input.aion.world_maps.WorldMap;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.read.XMLParser;

public class AionWorldMapsParser extends XMLParser<WorldMaps> {

	public AionWorldMapsParser() {super(WorldProperties.INPUT_WORLD_MAPS_BINDINGS);}
	
	public List<WorldMap> parse() {
		WorldMaps root = parseFile(WorldProperties.INPUT_WORLD_MAPS);
		return root.getData();
	}
}
