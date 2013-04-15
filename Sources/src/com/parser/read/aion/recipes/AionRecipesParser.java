package com.parser.read.aion.recipes;

import java.util.List;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.recipes.ClientRecipes;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionRecipesParser extends XMLParser<ClientRecipes> {

	public AionRecipesParser() {super(AionReadingConfig.RECIPES_BINDINGS);}
	
	public List<ClientRecipe> parse() {
		ClientRecipes root = parseFile(AionReadingConfig.RECIPES);
		return root.getClientCombineRecipe();
	}
}
