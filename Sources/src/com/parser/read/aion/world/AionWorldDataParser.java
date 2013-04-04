package com.parser.read.aion.world;

import java.util.List;

import com.parser.input.world_data.Clientzones;
import com.parser.input.world_data.NpcInfos;

import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldDataParser extends AbstractDirectoryParser<NpcInfos> {

	public AionWorldDataParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.WORLD_DATA_BINDINGS, AionReadingConfig.WORLD_DATA, AionReadingConfig.WORLD_DATA_PREFIX);
	}

	@Override
	protected List<NpcInfos> castFrom(Object topNode) {
		return ((Clientzones) topNode).getNpcInfos();
	}
	
	@Override
	protected String mapFileName(String fileName) {
		return fileName.replaceAll("client_world_", "").replaceAll(".xml", "");
	}
}
