package com.parser.read.aion.toypets;

import java.util.List;

import com.parser.input.aion.toypets.ClientToypet;
import com.parser.input.aion.toypets.ClientToypets;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionToyPetsParser extends AbstractFileParser<ClientToypet> {

	public AionToyPetsParser() {
			super(AionReadingConfig.TOYPETS_BINDINGS, AionReadingConfig.TOYPETS);
	}

	@Override
	protected List<ClientToypet> castFrom(Object topNode) {
		return ((ClientToypets) topNode).getClientToypet();
	}
}
