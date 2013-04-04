package com.parser.read.aion.npcs;

import java.util.List;

import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.npcs.ClientNpcs;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionNpcsParser extends AbstractFileParser<ClientNpc> {

	public AionNpcsParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.NPCS_BINDINGS, AionReadingConfig.NPCS);
	}

	@Override
	protected List<ClientNpc> castFrom(Object topNode) {
		return ((ClientNpcs) topNode).getNpcClient();
	}
}
