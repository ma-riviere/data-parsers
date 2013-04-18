package com.parser.commons.utils.properties;

import java.lang.reflect.Method;
import java.util.Properties;

import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

public class PropertiesLoader {

	public final Logger log = new Logger().getInstance();
	protected static final String PROPERTIES_DIR = "../../Properties/";
	
	private String game = null;
	
	public PropertiesLoader(String game) {
		this.game = game;
	}

	public void loadProperties(String file) {
		
		try {
			Class clazz = Class.forName("com.parser.commons." + game + ".properties." + file + "Properties");
			
			// Loading fields
			String path = PROPERTIES_DIR + game + "\\" + file;
			Properties prop = PropertiesUtils.load(path + ".properties");
			ConfigurableProcessor.process(clazz, prop);			
			log.info("Loading properties from file : " + file);
			
			// Loading methods
			Object properties = clazz.newInstance();
			try {
				Method loadPaths = clazz.getMethod("loadPaths");
				loadPaths.invoke(properties, new Object[0]);
			} catch (NoSuchMethodException nsme) {
				log.error("Couldn't find method loadPaths() for " + game + "'s file {" + file + "} with error : " + nsme);
			}

		}
		catch (Exception e) {
			log.error("Can't load " + game + "'s file {" + file + "} with error : " + e);
		}
	}
}
