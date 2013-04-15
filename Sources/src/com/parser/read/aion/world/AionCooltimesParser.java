package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltimes;
import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;


public class AionCooltimesParser extends XMLParser<ClientInstanceCooltimes> {

	public AionCooltimesParser() {super(AionReadingConfig.COOLTIMES_BINDINGS);}

	public List<ClientInstanceCooltime> parse() {
		ClientInstanceCooltimes root = parseFile(AionReadingConfig.COOLTIMES);
		return root.getClientInstanceCooltime();
	}
}
