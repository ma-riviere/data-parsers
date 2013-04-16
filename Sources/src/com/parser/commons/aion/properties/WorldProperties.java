package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class WorldProperties {

	@Property(key = "world.input.dir", defaultValue = "Data/World/")
	public static String INPUT;
	@Property(key = "world.input.prefix", defaultValue = "client_world_")
	public static String INPUT_PREFIX;
	
	@Property(key = "world.source_sphere.input", defaultValue = "source_sphere.csv")
	public static String SPHERE_INPUT;
	
	@Property(key = "world.source_sphere.output", defaultValue = "Tests/source_sphere.xml")
	public static String SPHERE_OUTPUT;
	@Property(key = "world.source_sphere.output.bindings", defaultValue = "")
	public static String SPHERE_OUTPUT_BINDINGS;
	
	static {
		INPUT = AionProperties.CLIENT_PATH + INPUT;
		SPHERE_INPUT = INPUT + SPHERE_INPUT;
		SPHERE_OUTPUT = AionProperties.OUTPUT_PATH + SPHERE_OUTPUT;
	}
}
