package com.parser.start.aion;

import com.parser.write.aion.spawns.AionSpawnsWriter;

public class AionSpawnsStart {

	public static void main(String[] args) {
		AionSpawnsWriter writer = new AionSpawnsWriter(Boolean.parseBoolean(args[0]));
		writer.start();
	}
}