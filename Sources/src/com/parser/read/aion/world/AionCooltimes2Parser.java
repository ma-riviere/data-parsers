package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2S;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.read.XMLParser;

public class AionCooltimes2Parser extends XMLParser<ClientInstanceCooltime2S> {

	public AionCooltimes2Parser() {super(WorldProperties.INPUT_COOLTIMES_BINDINGS);}

	public List<ClientInstanceCooltime2> parse() {
		ClientInstanceCooltime2S root = parseFile(WorldProperties.INPUT_COOLTIMES2);
		return root.getClientInstanceCooltime2();
	}
}
