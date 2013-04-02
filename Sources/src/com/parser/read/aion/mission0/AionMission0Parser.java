package com.parser.read.aion.mission0;

import java.util.List;

import com.parser.input.aion.missionO.Mission;
import com.parser.input.aion.missionO.Objects;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionMission0Parser extends AbstractDirectoryParser<ClientItem> {

	public AionMission0Parser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.MISSION0_BINDINGS, AionReadingConfig.MISSION0, AionReadingConfig.MISSION0_PREFIX);
	}
	
	public AionMission0Parser(String version, String pack, String folder, String prefix) {
		super(version, pack, folder, prefix);
	}

	@Override
	protected List<ClientItem> castFrom(Object topNode) {
		return ((Mission) topNode).getObjects();
	}
	
	@Override
	protected String mapFileName(String fileName) {
		return fileName.replaceAll("mission_", "").replaceAll(".xml", "");
	}
}
