package com.parser.read.aion.recipes;

import java.util.List;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.recipes.ClientRecipes;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionRecipesParser extends AbstractFileParser<ClientRecipe> {

	public AionRecipesParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.RECIPES_PACK, AionReadingConfig.RECIPES);
	}

	@Override
	protected List<ClientRecipe> castFrom(Object topNode) {
		return ((ClientRecipes) topNode).getClientCombineRecipe();
	}

}
