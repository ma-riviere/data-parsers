package com.parser.write.aion.items;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.input.aion.mission0.Objects;
import com.parser.input.aion.mission0.Object;

import com.parser.common.aion.*;
import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.items.AionMission0Parser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.mission0.Spawns; //TODO: Rename spawns (Jar Jar)
import com.parser.output.aion.mission0.SpawnMap;
import com.parser.output.aion.mission0.Spawn;

public class AionSpawnsWriter extends AbstractWriter {

	Spawns finalTemplates = new Spawns();
	Collection<SpawnMap> templateList = finalTemplates.getSpawnMap();
	Map<String, List<SpawnMap>> clientSpawnData;
	
	
	@Override
	public void parse() {
		clientSpawnData = new AionMission0Parser().parse();
	}

	@Override
	public void transform() {
		for (List<Object> objects : clientSpawnData.values()) {
			for (Object object : objects) {
				SpawnMap spawns = new SpawnMap();
				// START
				
				// END
				templateList.add(spawns);
			}
		}
	}

	// TODO: marshall for each mission0 file
	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.SPAWNS, AionWritingConfig.SPAWNS_BINDINGS);
		System.out.println("[SPAWNS] Spawns count: " + templateList.size());
	}
}