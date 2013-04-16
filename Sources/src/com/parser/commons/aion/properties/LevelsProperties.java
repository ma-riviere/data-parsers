package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class LevelsProperties {

	@Property(key = "levels.input.dir", defaultValue = "")
	public static String LEVELS;
	
	@Property(key = "levels.mission0.input.bindings", defaultValue = "")
	public static String MISSION0_BINDINGS;
	@Property(key = "levels.mission0.prefix", defaultValue = "mission_")
	public static String MISSION0_PREFIX;
	
	@Property(key = "levels.mission0.spawns.output.dir", defaultValue = "Spawns/")
	public static String SPAWNS;
	@Property(key = "levels.mission0.spawns.output.bindings", defaultValue = "")
	public static String SPAWNS_BINDINGS;
	
	@Property(key = "levels.mission0.random.spawns.output.dir", defaultValue = "random_spawns/")
	public static String RANDOM_SPAWNS;

	@Property(key = "levels.random.walk.cap", defaultValue = "7")
	public static int RANDOM_WALK_CAP;
	
	@Property(key = "levels.leveldata.input.bindings", defaultValue = "")
	public static String LEVELDATA_BINDINGS;
	@Property(key = "levels.leveldata.prefix", defaultValue = "leveldata")
	public static String LEVELDATA_PREFIX;
	
	static {
		LEVELS = AionProperties.CLIENT_PATH + LEVELS;
		SPAWNS = AionProperties.OUTPUT_PATH + SPAWNS;
		RANDOM_SPAWNS = AionProperties.OUTPUT_PATH + RANDOM_SPAWNS;
	}
}
