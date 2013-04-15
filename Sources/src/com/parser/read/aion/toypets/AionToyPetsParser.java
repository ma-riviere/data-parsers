package com.parser.read.aion.toypets;

import java.util.List;

import com.parser.input.aion.toypets.ClientToypet;
import com.parser.input.aion.toypets.ClientToypets;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionToyPetsParser extends XMLParser<ClientToypets> {

	public AionToyPetsParser() {super(AionReadingConfig.TOYPETS_BINDINGS);}
	
	public List<ClientToypet> parse() {
		ClientToypets root = parseFile(AionReadingConfig.TOYPETS);
		return root.getClientToypet();
	}
}
