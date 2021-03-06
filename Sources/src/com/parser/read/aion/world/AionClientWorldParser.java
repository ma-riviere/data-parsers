package com.parser.read.aion.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javolution.util.FastMap;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.input.aion.world_data.Clientzones;
import com.parser.input.aion.world_data.NpcInfos;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.input.aion.world_data.Subzones; //TODO
import com.parser.input.aion.world_data.Subzone; //TODO

import com.parser.input.aion.world_data.Attributes; //TODO
import com.parser.input.aion.world_data.ItemUseArea; //TODO

import com.parser.input.aion.world_data.HousingAreas; //TODO

import com.parser.read.XMLParser;

public class AionClientWorldParser extends XMLParser<Clientzones> {

	public AionClientWorldParser() {
		super(WorldProperties.CLIENT_WORLD, WorldProperties.CLIENT_WORLD_PREFIX, WorldProperties.CLIENT_WORLD_BINDINGS);
	}
	
	private static FastMap<String, Clientzones> rootData = null;
	
	private FastMap<String, Clientzones> getRootData() {
		if (rootData == null) {
			rootData = new FastMap<String, Clientzones>();
			for (Map.Entry<String[], Clientzones> entry : parseDir().entrySet())
				rootData.put(entry.getKey()[0].replaceAll(WorldProperties.CLIENT_WORLD_PREFIX, ""), entry.getValue());
		}
		return rootData;
	}

	public FastMap<String, List<NpcInfo>> parseNpcInfos() {
		FastMap<String, List<NpcInfo>> npcInfos = new FastMap<String, List<NpcInfo>>();
		for (Map.Entry<String, Clientzones> entry : getRootData().entrySet())
			npcInfos.put(entry.getKey(), entry.getValue().getNpcInfos().getNpcInfo());
		return npcInfos;
	}
	
	public List<NpcInfo> parseNpcInfos(String file) {
		return parseFile(file).getNpcInfos().getNpcInfo();
	}
}
