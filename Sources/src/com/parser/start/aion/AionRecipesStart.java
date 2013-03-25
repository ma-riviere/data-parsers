package com.parser.start.aion;

import com.parser.write.aion.recipes.AionRecipesWriter;

public class AionRecipesStart {

	public static void main(String[] args) {
		AionRecipesWriter writer = new AionRecipesWriter(Boolean.parseBoolean(args[0]));
		writer.build();
	}
}