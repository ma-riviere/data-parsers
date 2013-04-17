package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltimes;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.read.XMLParser;

public class AionCooltimesParser extends XMLParser<ClientInstanceCooltimes> {

	public AionCooltimesParser() {super(WorldProperties.INPUT_COOLTIMES_BINDINGS);}

	public List<ClientInstanceCooltime> parse() {
		ClientInstanceCooltimes root = parseFile(WorldProperties.INPUT_COOLTIMES);
		return root.getClientInstanceCooltime();
	}
}
