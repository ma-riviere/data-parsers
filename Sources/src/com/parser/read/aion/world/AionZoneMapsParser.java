package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.zone_maps.Zonemaps;
import com.parser.input.aion.zone_maps.Zonemap;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.read.XMLParser;

public class AionZoneMapsParser extends XMLParser<Zonemaps> {

	public AionZoneMapsParser() {super(WorldProperties.ZONE_MAPS_BINDINGS);}
	
	public List<Zonemap> parse() {
		Zonemaps root = parseFile(WorldProperties.ZONE_MAPS);
		return root.getZonemap();
	}
}
