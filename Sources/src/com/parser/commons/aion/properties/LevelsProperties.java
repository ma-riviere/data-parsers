package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class LevelsProperties {

	@Property(key = "levels.input", defaultValue = "")
	public static String LEVELS;
	
	@Property(key = "levels.mission0.input.bindings", defaultValue = "")
	public static String MISSION0_BINDINGS;
	@Property(key = "levels.mission0.prefix", defaultValue = "mission_")
	public static String MISSION0_PREFIX;
	
	@Property(key = "levels.spawns.output", defaultValue = "Spawns/")
	public static String SPAWNS;
	@Property(key = "levels.spawns.output.bindings", defaultValue = "")
	public static String SPAWNS_BINDINGS;
	
	@Property(key = "levels.spawns.random.output", defaultValue = "random_spawns/random_spawns.xml")
	public static String RANDOM_SPAWNS;

	@Property(key = "levels.random.walk.cap", defaultValue = "7")
	public static int RANDOM_WALK_CAP;
	
	@Property(key = "levels.leveldata.input.bindings", defaultValue = "")
	public static String LEVELDATA_BINDINGS;
	@Property(key = "levels.leveldata.prefix", defaultValue = "leveldata")
	public static String LEVELDATA_PREFIX;	
	
	@Property(key = "levels.heightmap.output", defaultValue = "Tests/height_map.xml")
	public static String HEIGHTMAP;
	@Property(key = "levels.heightmap.output.bindings", defaultValue = "")
	public static String HEIGHTMAP_BINDINGS;
	
	public void loadPaths() {
		// LEVELS = AionProperties.CLIENT_PATH + LEVELS;
		SPAWNS = AionProperties.OUTPUT_PATH + SPAWNS;
		RANDOM_SPAWNS = AionProperties.OUTPUT_PATH + RANDOM_SPAWNS;
		HEIGHTMAP = AionProperties.OUTPUT_PATH + HEIGHTMAP;
	}
}
