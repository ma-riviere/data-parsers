package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class WorldProperties {

	@Property(key = "world.input", defaultValue = "Data/World/")
	public static String INPUT;
	@Property(key = "world.clientWorld.prefix", defaultValue = "client_world_")
	public static String CLIENT_WORLD_PREFIX;
	
	@Property(key = "world.source_sphere.input", defaultValue = "source_sphere.csv")
	public static String SPHERE_INPUT;
	@Property(key = "world.source_sphere.output", defaultValue = "Tests/source_sphere.xml")
	public static String SPHERE_OUTPUT;
	@Property(key = "world.source_sphere.output.bindings", defaultValue = "")
	public static String SPHERE_OUTPUT_BINDINGS;
	
	@Property(key = "world.waypoint.input", defaultValue = "waypoint.csv")
	public static String WAYPOINT_INPUT;
	@Property(key = "world.walkers.output.bindings", defaultValue = "")
	public static String WALKERS_BINDINGS;
	@Property(key = "world.walkers.output", defaultValue = "npc_walkers/npc_walkers.xml")
	public static String WALKERS;
	@Property(key = "world.walkers.fromSpawns.output", defaultValue = "npc_walkers/npc_walkers_spawns.xml")
	public static String WALKERS_FROM_SPAWNS;
	
	static {
		INPUT = AionProperties.CLIENT_PATH + INPUT;
		
		SPHERE_INPUT = INPUT + SPHERE_INPUT;
		SPHERE_OUTPUT = AionProperties.OUTPUT_PATH + SPHERE_OUTPUT;
		
		WAYPOINT_INPUT = INPUT + WAYPOINT_INPUT;
		
		WALKERS = AionProperties.OUTPUT_PATH + WALKERS;
		WALKERS_FROM_SPAWNS = AionProperties.OUTPUT_PATH + WALKERS_FROM_SPAWNS;
	}
}
