package com.parser.start;

import com.parser.commons.aion.properties.AionPropertiesLoader;
import static com.parser.commons.utils.Util.printSection;

import com.parser.write.aion.items.AionItemsWriter;
import com.parser.write.aion.items.AionItemsInternalWriter;
import com.parser.write.aion.items.AionClientItemsWriter;
import com.parser.write.aion.levels.AionSpawnsWriter;
import com.parser.write.aion.levels.AionHeightMapWriter;
import com.parser.write.aion.levels.AionWalkersWriter;
import com.parser.write.aion.recipes.AionRecipesWriter;
import com.parser.write.aion.rides.AionRidesWriter;
import com.parser.write.aion.skills.AionSkillsWriter;
import com.parser.write.aion.world.AionSourceSphereWriter;
import com.parser.write.aion.world.AionCooltimesWriter;

public abstract class AionStart {

	private static final AionPropertiesLoader loader = new AionPropertiesLoader();
	
	public static void main(String[] args) {
		
		printSection("Loading required properties");
		loader.loadProperty("General");
		
		if ("SPAWNS".startsWith(args[0].toUpperCase())) {
			AionSpawnsWriter writer = new AionSpawnsWriter();
			loader.loadProperty("Levels");
			writer.start();
		}
		else if ("ITEMS".startsWith(args[0].toUpperCase())) {
			AionItemsWriter writer = new AionItemsWriter();
			// loader.loadProperty("Items");
			writer.start();
		}
		else if ("ITEMS_INTERNAL".startsWith(args[0].toUpperCase())) {
			AionItemsInternalWriter writer = new AionItemsInternalWriter();
			// loader.loadProperty("Items");
			writer.start();
		}
		else if ("ITEMS_CLIENT".startsWith(args[0].toUpperCase())) {
			AionClientItemsWriter writer = new AionClientItemsWriter();
			// loader.loadProperty("Items");
			writer.start();
		}
		else if ("SKILLS".startsWith(args[0].toUpperCase())) {
			AionSkillsWriter writer = new AionSkillsWriter(Boolean.parseBoolean(args[1]));
			// loader.loadProperty("Skills");
			writer.start();
		}
		else if ("WALKERS".startsWith(args[0].toUpperCase())) {
			AionWalkersWriter writer = new AionWalkersWriter();
			loader.loadProperty("Levels");
			writer.start();
		}
		else if ("SPHERE".startsWith(args[0].toUpperCase())) {
			AionSourceSphereWriter writer = new AionSourceSphereWriter();
			loader.loadProperty("World");
			writer.start();
		}
		else if ("RIDES".startsWith(args[0].toUpperCase())) {
			AionRidesWriter writer = new AionRidesWriter();
			// loader.loadProperty("Rides");
			writer.start();
		}
		else if ("RECIPES".startsWith(args[0].toUpperCase())) {
			AionRecipesWriter writer = new AionRecipesWriter(Boolean.parseBoolean(args[1]));
			loader.loadProperty("Recipes");
			writer.start();
		}
		else if ("COOLTIMES".startsWith(args[0].toUpperCase())) {
			AionCooltimesWriter writer = new AionCooltimesWriter();
			loader.loadProperty("World");
			writer.start();
		}
		else if ("HEIGHT".startsWith(args[0].toUpperCase())) {
			AionHeightMapWriter writer = new AionHeightMapWriter();
			loader.loadProperty("Levels");
			writer.start();
		}
		else {
			System.out.println("[ERROR] The input you specified does not execute any Writer !");
		}
	}
	
}