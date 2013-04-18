package com.parser.commons.aion.properties;

import com.parser.commons.utils.properties.Property;

public class RecipesProperties {

	public RecipesProperties() {}

	@Property(key = "recipes.input", defaultValue = "")
	public static String INPUT;
	@Property(key = "recipes.input.bindings", defaultValue = "")
	public static String INPUT_BINDINGS;
	@Property(key = "recipes.output", defaultValue = "Recipes/")
	public static String OUTPUT;
	@Property(key = "recipes.output.bindings", defaultValue = "")
	public static String OUTPUT_BINDINGS;

	@Property(key = "recipes.max.combo", defaultValue = "10")
	public static int MAX_COMBO;
	
	public void loadPaths() {
		INPUT = AionProperties.CLIENT_PATH + INPUT;
		OUTPUT = AionProperties.OUTPUT_PATH + OUTPUT;
	}
}
