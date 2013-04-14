package com.parser.commons.aion.properties;

import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.parser.commons.utils.properties.*;

public class AionPropertiesLoader extends PropertiesLoader {

	private static final String GAME = "aion";

	@Override
	public void loadProperty(String file) {
		try {
			String path = PROPERTIES_DIR + GAME + "\\" + file;

			Properties prop = PropertiesUtils.load(path + ".properties");

			ConfigurableProcessor.process(Class.forName("com.parser.commons." + GAME + ".properties." + file + "Properties"), prop);
			log.info("Loading properties from : " + file);

		}
		catch (Exception e) {
			log.error("Can't load " + GAME + "'s " + file, e);
			throw new Error("Can't load " + GAME + "'s " + file, e);
		}
	}
}
