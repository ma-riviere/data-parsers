package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.world_maps.WorldMaps;
import com.parser.input.aion.world_maps.WorldMap;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldMapsParser extends AbstractFileParser<WorldMap> {

	public AionWorldMapsParser() {
		super(AionReadingConfig.WORLD_MAPS_BINDINGS, AionReadingConfig.WORLD_MAPS);
	}

	@Override
	protected List<WorldMap> castFrom(Object topNode) {
		return ((WorldMaps) topNode).getData();
	}

}
