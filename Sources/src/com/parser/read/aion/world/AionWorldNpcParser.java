package com.parser.read.aion.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javolution.util.FastMap;

import com.parser.input.aion.world_data.Clientzones;
import com.parser.input.aion.world_data.NpcInfos;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.input.aion.world_data.Subzones; //TODO
import com.parser.input.aion.world_data.Subzone; //TODO

import com.parser.input.aion.world_data.Attributes; //TODO
import com.parser.input.aion.world_data.ItemUseArea; //TODO

import com.parser.input.aion.world_data.HousingAreas; //TODO

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldNpcParser extends XMLParser<Clientzones> {

	public AionWorldNpcParser() {
		super(AionReadingConfig.WORLD_DATA, AionReadingConfig.WORLD_DATA_PREFIX, AionReadingConfig.WORLD_DATA_BINDINGS);
	}
	
	private static FastMap<String, Clientzones> rootData = null;
	
	private FastMap<String, Clientzones> getRootData() {
		FastMap<String[], Clientzones> parsed = parseDir();
		if (rootData == null) {
			rootData = new FastMap<String, Clientzones>();
			for (Map.Entry<String[], Clientzones> entry : parsed.entrySet())
				rootData.put(entry.getKey()[0].replaceAll(AionReadingConfig.WORLD_DATA_PREFIX, ""), entry.getValue());
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
