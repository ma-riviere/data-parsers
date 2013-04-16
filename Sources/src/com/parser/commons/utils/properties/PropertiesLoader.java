package com.parser.commons.utils.properties;

import java.util.logging.Logger;

import com.parser.commons.utils.Util;

public abstract class PropertiesLoader {

	public final Logger log = Logger.getLogger("CONFIGS");
	protected static final String PROPERTIES_DIR = "../../Properties/";
	
	protected abstract void loadProperty(String file);
}
