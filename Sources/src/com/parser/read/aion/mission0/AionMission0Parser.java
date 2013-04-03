package com.parser.read.aion.mission0;

import java.util.List;
import java.util.ArrayList;

import com.parser.input.aion.mission.Mission;
import com.parser.input.aion.mission.Objects;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionMission0Parser extends AbstractDirectoryParser<Objects> {

	public AionMission0Parser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.MISSION0_BINDINGS, AionReadingConfig.MISSION0, AionReadingConfig.MISSION0_PREFIX);
	}
	
	public AionMission0Parser(String version, String pack, String folder, String prefix) {
		super(version, pack, folder, prefix);
	}

	@Override
	protected List<Objects> castFrom(Object topNode) {
		List<Objects> objects = new ArrayList<Objects>();
		objects.add(((Mission) topNode).getObjects());
		return objects;
	}
	
	@Override
	protected String mapFileName(String fileName) {
		return fileName.replaceAll("mission_", "").replaceAll(".xml", "");
	}
}
