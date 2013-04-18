package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class AionProperties {

	public AionProperties() {}

	public static String INPUT_PATH;
	public static String CLIENT_PATH;
	public static String SERVER_PATH;
	public static String CUSTOM_PATH;
	public static String OUTPUT_PATH;

	@Property(key = "general.version", defaultValue = "aion40")
	public static String VERSION;
	
	@Property(key = "general.input.dir", defaultValue = "../../Input/")
	public static String INPUT_DIR;
	@Property(key = "general.input.client.dir", defaultValue = "Client/")
	public static String CLIENT_DIR;
	@Property(key = "general.input.server.dir", defaultValue = "Server/")
	public static String SERVER_DIR;
	@Property(key = "general.input.custom.dir", defaultValue = "Custom/")
	public static String CUSTOM_DIR;
	@Property(key = "general.input.use.custom", defaultValue = "false")
	public static boolean USE_CUSTOM_INPUT;

	@Property(key = "general.output.dir", defaultValue = "../../Output/")
	public static String OUTPUT_DIR;
	@Property(key = "general.output.use.custom", defaultValue = "false")
	public static boolean USE_CUSTOM_OUTPUT;
	
	@Property(key = "general.use.geo", defaultValue = "false")
	public static boolean USE_GEO;
	
	@Property(key = "general.max.level", defaultValue = "60")
	public static int MAX_LEVEL;
		
	public void loadPaths() {
		INPUT_PATH = INPUT_DIR + VERSION + "/";
		CLIENT_PATH = INPUT_PATH + CLIENT_DIR;
		SERVER_PATH = INPUT_PATH + SERVER_DIR;
		CUSTOM_PATH = INPUT_PATH + CUSTOM_DIR;
		
		OUTPUT_PATH = OUTPUT_DIR + VERSION + "/";
	}
}
