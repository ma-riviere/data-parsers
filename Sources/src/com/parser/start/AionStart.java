package com.parser.start;

import com.parser.commons.aion.properties.AionPropertiesLoader;
import com.parser.commons.utils.Util;

import com.parser.write.aion.cooltimes.AionCooltimesWriter;
import com.parser.write.aion.items.AionItemsWriter;
import com.parser.write.aion.items.AionItemsInternalWriter;
import com.parser.write.aion.items.AionClientItemsWriter;
import com.parser.write.aion.levels.AionSpawnsWriter;
import com.parser.write.aion.npcs.AionWalkersWriter;
import com.parser.write.aion.npcs.AionSourceSphereWriter;
import com.parser.write.aion.recipes.AionRecipesWriter;
import com.parser.write.aion.rides.AionRidesWriter;
import com.parser.write.aion.skills.AionSkillsWriter;

public abstract class AionStart {

	private static final AionPropertiesLoader loader = new AionPropertiesLoader();
	
	public static void main(String[] args) {
		
		Util.printSection("Loading required properties");
		loader.loadProperty("General");
		loader.loadProperty("Read");
		loader.loadProperty("Write");
		
		if ("SPAWNS".startsWith(args[0].toUpperCase())) {
			AionSpawnsWriter writer = new AionSpawnsWriter();
			loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("ITEMS".startsWith(args[0].toUpperCase())) {
			AionItemsWriter writer = new AionItemsWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("ITEMS_INTERNAL".startsWith(args[0].toUpperCase())) {
			AionItemsInternalWriter writer = new AionItemsInternalWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("ITEMS_CLIENT".startsWith(args[0].toUpperCase())) {
			AionClientItemsWriter writer = new AionClientItemsWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("SKILLS".startsWith(args[0].toUpperCase())) {
			AionSkillsWriter writer = new AionSkillsWriter(Boolean.parseBoolean(args[1]));
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("WALKERS".startsWith(args[0].toUpperCase())) {
			AionWalkersWriter writer = new AionWalkersWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("SPHERE".startsWith(args[0].toUpperCase())) {
			AionSourceSphereWriter writer = new AionSourceSphereWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("RIDES".startsWith(args[0].toUpperCase())) {
			AionRidesWriter writer = new AionRidesWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
		else if ("RECIPES".startsWith(args[0].toUpperCase())) {
			AionRecipesWriter writer = new AionRecipesWriter(Boolean.parseBoolean(args[1]));
			loader.loadProperty("Recipes");
			writer.start();
		}
		else if ("COOLTIMES".startsWith(args[0].toUpperCase())) {
			AionCooltimesWriter writer = new AionCooltimesWriter();
			// loader.loadProperty("Spawn");
			writer.start();
		}
	}
	
}