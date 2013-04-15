package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2S;
import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;


public class AionCooltimes2Parser extends XMLParser<ClientInstanceCooltime2S> {

	public AionCooltimes2Parser() {super(AionReadingConfig.COOLTIMES_BINDINGS);}

	public List<ClientInstanceCooltime2> parse() {
		ClientInstanceCooltime2S root = parseFile(AionReadingConfig.COOLTIMES2);
		return root.getClientInstanceCooltime2();
	}
}
