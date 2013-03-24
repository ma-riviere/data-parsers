package com.parser.read.aion.housing;

import java.util.List;

import com.parser.input.aion.housing.ClientHousingObject;
import com.parser.input.aion.housing.ClientHousingObjects;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;


public class AionHousingObjectsParser extends AbstractFileParser<ClientHousingObject> {

	public AionHousingObjectsParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.HOUSING_PACK, AionReadingConfig.HOUSING_OBJECTS);
	}

	@Override
	protected List<ClientHousingObject> castFrom(Object topNode) {
		return ((ClientHousingObjects) topNode).getClientHousingObject();
	}

}
