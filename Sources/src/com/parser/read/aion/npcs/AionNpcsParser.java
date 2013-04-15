package com.parser.read.aion.npcs;

import java.util.List;

import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.npcs.ClientNpcs;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionNpcsParser extends XMLParser<ClientNpcs> {

	public AionNpcsParser() {super(AionReadingConfig.NPCS_BINDINGS);}

	public List<ClientNpc> parse() {
		ClientNpcs root = parseFile(AionReadingConfig.NPCS);
		return root.getNpcClient();
	}
}
