package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class GeneralProperties {

	@Property(key = "general.version", defaultValue = "")
	public static String VERSION;
	@Property(key = "general.max_level", defaultValue = "60")
	public static int MAX_LEVEL;
}
