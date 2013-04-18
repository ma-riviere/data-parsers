package com.parser.start;

import org.apache.commons.lang.StringUtils;

import com.parser.commons.utils.properties.PropertiesLoader;
import static com.parser.commons.utils.Util.printSection;

import com.parser.write.aion.items.AionItemsWriter;
import com.parser.write.aion.items.AionItemsInternalWriter;
import com.parser.write.aion.items.AionClientItemsWriter;
import com.parser.write.aion.levels.AionSpawnsWriter;
import com.parser.write.aion.levels.AionHeightMapWriter;
import com.parser.write.aion.recipes.AionRecipesWriter;
import com.parser.write.aion.rides.AionRidesWriter;
import com.parser.write.aion.skills.AionSkillsWriter;
import com.parser.write.aion.world.AionWalkersWriter;
import com.parser.write.aion.world.AionSourceSphereWriter;
import com.parser.write.aion.world.AionCooltimesWriter;

public abstract class AionStart {

	private static final PropertiesLoader loader = new PropertiesLoader("aion");
	
	public static void main(String[] args) {
		
		printSection("Loading required properties");
		loader.loadProperties("Aion");
		
		if (StringUtils.startsWithIgnoreCase("SPAWNS", args[0])) {
			AionSpawnsWriter writer = new AionSpawnsWriter();
			loader.loadProperties("Levels");
			loader.loadProperties("World");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("HEIGHT", args[0])) {
			AionHeightMapWriter writer = new AionHeightMapWriter();
			loader.loadProperties("Levels");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("ITEMS", args[0])) {
			AionItemsWriter writer = new AionItemsWriter();
			// loader.loadProperties("Items");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("ITEMS_ID", args[0])) {
			AionItemsInternalWriter writer = new AionItemsInternalWriter();
			// loader.loadProperties("Items");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("ITEMS_CLIENT", args[0])) {
			AionClientItemsWriter writer = new AionClientItemsWriter();
			// loader.loadProperties("Items");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("SKILLS", args[0])) {
			AionSkillsWriter writer = new AionSkillsWriter();
			// loader.loadProperties("Skills");
			// loader.loadProperties("Items");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("RIDES", args[0])) {
			AionRidesWriter writer = new AionRidesWriter();
			// loader.loadProperties("Rides");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("RECIPES", args[0])) {
			AionRecipesWriter writer = new AionRecipesWriter();
			loader.loadProperties("Recipes"); //TODO: Move to Data
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("COOLTIMES", args[0])) {
			AionCooltimesWriter writer = new AionCooltimesWriter();
			loader.loadProperties("World");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("WALKERS", args[0])) {
			AionWalkersWriter writer = new AionWalkersWriter();
			loader.loadProperties("World");
			writer.start();
		}
		else if (StringUtils.startsWithIgnoreCase("SPHERE", args[0])) {
			AionSourceSphereWriter writer = new AionSourceSphereWriter();
			loader.loadProperties("World");
			writer.start();
		}
		else {
			System.out.println("[ERROR] The input you specified does not execute any Writer !");
		}
	}
	
}