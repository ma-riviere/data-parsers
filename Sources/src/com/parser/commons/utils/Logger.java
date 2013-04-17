package com.parser.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	
	// TODO: Use regex matching (%d, %d) et value multiples
	//TODO: [INFO] + message for log.info, etc

	public List<String> used = new ArrayList<String>();
	
	public Logger() {}
	public static final Logger getInstance() {return SingletonHolder.instance;}

	public void info(String message, Object value) {
		System.out.println(message + value.toString().toUpperCase());
	}
	
	public void info(String message) {
		info(message, "");
	}
	
	public void unique(String message, Object value, boolean ignoreNum) {
		if (value.toString() != "") {
			if (ignoreNum) {
				try {Integer.parseInt(value.toString());} catch (Exception e) {
					logUnique(message, value);
				}
			}
			else
				logUnique(message, value);
		}
	}
	
	private void logUnique(String message, Object value) {
		if (!used.contains(value.toString().toUpperCase())) {
			info(message, value);
			used.add(value.toString().toUpperCase());
		}
	}
	
	public void reset() {used = new ArrayList<String>();}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder 	{
		protected static final Logger instance = new Logger();
	}
}