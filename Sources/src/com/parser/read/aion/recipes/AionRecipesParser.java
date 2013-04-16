package com.parser.read.aion.recipes;

import java.util.List;

import com.parser.commons.aion.properties.RecipesProperties;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.recipes.ClientRecipes;

import com.parser.read.XMLParser;

public class AionRecipesParser extends XMLParser<ClientRecipes> {

	public AionRecipesParser() {super(RecipesProperties.INPUT_BINDINGS);}
	
	public List<ClientRecipe> parse() {
		ClientRecipes root = parseFile(RecipesProperties.INPUT);
		return root.getClientCombineRecipe();
	}
}
