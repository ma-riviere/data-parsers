package com.parser.read.aion.levels;

import java.util.List;
import java.util.ArrayList;

import com.parser.input.aion.mission.Mission;
import com.parser.input.aion.mission.ClientSpawns;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionMissionParser extends AbstractDirectoryParser<Mission, ClientSpawns> {

	public AionMissionParser() {
		super(AionReadingConfig.MISSION0_BINDINGS, AionReadingConfig.MISSION0, AionReadingConfig.MISSION0_PREFIX);
	}

	@Override
	protected List<ClientSpawns> cast(Object topNode) {
		List<ClientSpawns> clientSpawns = new ArrayList<ClientSpawns>();
		clientSpawns.add(((Mission) topNode).getObjects());
		return clientSpawns;
	}
}