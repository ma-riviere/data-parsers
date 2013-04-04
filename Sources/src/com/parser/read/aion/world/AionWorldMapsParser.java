package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.world_maps.Data;
import com.parser.input.aion.world_maps.WorldId;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldMapsParser extends AbstractFileParser<Data> {

	public AionWorldMapsParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.WORLD_BINDINGS, AionReadingConfig.WORLD_ID);
	}

	@Override
	protected List<Data> castFrom(Object topNode) {
		return ((WorldId) topNode).getData();
	}

}
