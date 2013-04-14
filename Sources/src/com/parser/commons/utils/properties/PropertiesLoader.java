package com.parser.commons.utils.properties;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public abstract class PropertiesLoader {

	protected static final Logger log = LoggerFactory.getLogger("Configs");
	protected static final String PROPERTIES_DIR = "../../Properties/";

	protected abstract void loadProperty(String file);
}
