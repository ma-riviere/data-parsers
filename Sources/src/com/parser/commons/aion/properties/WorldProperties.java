package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class WorldProperties {

	@Property(key = "world.client_world.input", defaultValue = "Data/World/")
	public static String CLIENT_WORLD;
	
	@Property(key = "world.client_world.input.prefix", defaultValue = "client_world_")
	public static String CLIENT_WORLD_PREFIX;
	@Property(key = "world.client_world.input.bindings", defaultValue = "")
	public static String CLIENT_WORLD_BINDINGS;
	
	@Property(key = "world.source_sphere.input", defaultValue = "source_sphere.csv")
	public static String INPUT_SPHERE;
	@Property(key = "world.source_sphere.output", defaultValue = "Tests/source_sphere.xml")
	public static String OUTPUT_SPHERE;
	@Property(key = "world.source_sphere.output.bindings", defaultValue = "")
	public static String SPHERE_OUTPUT_BINDINGS;
	
	@Property(key = "world.waypoint.input", defaultValue = "waypoint.csv")
	public static String WAYPOINT;
	@Property(key = "world.walkers.output.bindings", defaultValue = "")
	public static String WALKERS_BINDINGS;
	@Property(key = "world.walkers.output", defaultValue = "npc_walkers/npc_walkers.xml")
	public static String WALKERS;
	@Property(key = "world.walkers.fromSpawns.output", defaultValue = "npc_walkers/npc_walkers_spawns.xml")
	public static String WALKERS_FROM_SPAWNS;
	
	@Property(key = "world.cooltimes.input", defaultValue = "client_instance_cooltime.xml")
	public static String INPUT_COOLTIMES;
	@Property(key = "world.cooltimes2.input", defaultValue = "client_instance_cooltime2.xml")
	public static String INPUT_COOLTIMES2;
	@Property(key = "world.cooltimes.output.bindings", defaultValue = "")
	public static String INPUT_COOLTIMES_BINDINGS;
	@Property(key = "world.cooltimes.output", defaultValue = "instance_cooltimes/instance_cooltimes.xml")
	public static String OUTPUT_COOLTIMES;
	@Property(key = "world.cooltimes.output.bindings", defaultValue = "")
	public static String OUTPUT_COOLTIMES_BINDINGS;
	
	@Property(key = "world.zone_maps.input", defaultValue = "zonemap.xml")
	public static String ZONE_MAPS;
	@Property(key = "world.zone_maps.input.bindings", defaultValue = "")
	public static String ZONE_MAPS_BINDINGS;
	
	@Property(key = "world.world_maps.input", defaultValue = "WorldId.xml")
	public static String INPUT_WORLD_MAPS;
	@Property(key = "world.world_maps.input.bindings", defaultValue = "")
	public static String INPUT_WORLD_MAPS_BINDINGS;
	@Property(key = "world.world_maps.output", defaultValue = "world_maps.xml")
	public static String OUTPUT_WORLD_MAPS;
	@Property(key = "world.world_maps.output.bindings", defaultValue = "")
	public static String OUTPUT_WORLD_MAPS_BINDINGS;
	
	public void loadPaths() {
		CLIENT_WORLD = AionProperties.CLIENT_PATH + CLIENT_WORLD;
		
		INPUT_SPHERE = CLIENT_WORLD + INPUT_SPHERE;
		OUTPUT_SPHERE = AionProperties.OUTPUT_PATH + OUTPUT_SPHERE;
		
		WAYPOINT = CLIENT_WORLD + WAYPOINT;
		WALKERS = AionProperties.OUTPUT_PATH + WALKERS;
		WALKERS_FROM_SPAWNS = AionProperties.OUTPUT_PATH + WALKERS_FROM_SPAWNS;
		
		INPUT_COOLTIMES = CLIENT_WORLD + INPUT_COOLTIMES;
		INPUT_COOLTIMES2 = CLIENT_WORLD + INPUT_COOLTIMES2;
		OUTPUT_COOLTIMES = AionProperties.OUTPUT_PATH + OUTPUT_COOLTIMES;
		
		ZONE_MAPS = CLIENT_WORLD + ZONE_MAPS;
		INPUT_WORLD_MAPS = CLIENT_WORLD + INPUT_WORLD_MAPS;
		OUTPUT_WORLD_MAPS = AionProperties.OUTPUT_PATH + OUTPUT_WORLD_MAPS;
	}
}
