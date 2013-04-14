package com.parser.start.aion;

import com.parser.write.aion.levels.AionSpawnsWriter;
import com.parser.commons.aion.properties.AionPropertiesLoader;

public class AionSpawnsStart {

	public static void main(String[] args) {
		AionPropertiesLoader loader = new AionPropertiesLoader();
		loader.loadProperty("Spawn");
		AionSpawnsWriter writer = new AionSpawnsWriter();
		writer.start();
	}
}