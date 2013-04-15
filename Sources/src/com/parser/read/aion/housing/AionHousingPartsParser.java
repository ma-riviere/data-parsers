package com.parser.read.aion.housing;

import java.util.List;

import com.parser.input.aion.housing.ClientHousingCustomPart;
import com.parser.input.aion.housing.ClientHousingCustomParts;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionHousingPartsParser extends XMLParser<ClientHousingCustomParts> {

	public AionHousingPartsParser() {super(AionReadingConfig.HOUSING_BINDINGS);} 

	public List<ClientHousingCustomPart> parse() {
		ClientHousingCustomParts root = parseFile(AionReadingConfig.HOUSING_PARTS);
		return root.getClientHousingCustomPart();
	}
}