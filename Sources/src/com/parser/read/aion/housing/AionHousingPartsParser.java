package com.parser.read.aion.housing;

import java.util.List;

import com.parser.input.aion.housing.ClientHousingCustomPart;
import com.parser.input.aion.housing.ClientHousingCustomParts;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;


public class AionHousingPartsParser extends AbstractFileParser<ClientHousingCustomPart> {

	public AionHousingPartsParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.HOUSING_BINDINGS, AionReadingConfig.HOUSING_PARTS);
	}

	@Override
	protected List<ClientHousingCustomPart> castFrom(Object topNode) {
		return ((ClientHousingCustomParts) topNode).getClientHousingCustomPart();
	}

}