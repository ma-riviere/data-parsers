package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.world.Data;
import com.parser.input.aion.world.WorldId;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldDataParser extends AbstractFileParser<Data> {

	public AionWorldDataParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.WORLD_BINDINGS, AionReadingConfig.WORLD_ID);
	}

	@Override
	protected List<Data> castFrom(Object topNode) {
		return ((WorldId) topNode).getData();
	}

}
