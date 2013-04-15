package com.parser.read.aion.housing;

import java.util.List;

import com.parser.input.aion.housing.ClientHousingObject;
import com.parser.input.aion.housing.ClientHousingObjects;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionHousingObjectsParser extends XMLParser<ClientHousingObjects> {

	public AionHousingObjectsParser() {super(AionReadingConfig.HOUSING_BINDINGS);} 

	public List<ClientHousingObject> parse() {
		ClientHousingObjects root = parseFile(AionReadingConfig.HOUSING_OBJECTS);
		return root.getClientHousingObject();
	}
}
