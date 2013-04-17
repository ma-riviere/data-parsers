package com.parser.write.aion.recipes;

import java.util.Collection;

import com.parser.input.aion.recipes.ClientRecipe;

import com.parser.commons.utils.JAXBHandler;
import com.parser.commons.aion.enums.Race;
import com.parser.commons.aion.properties.RecipesProperties;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;

import com.parser.output.aion.recipes.*;

public class AionRecipesWriter extends AbstractWriter {

	public static boolean ANALYSE = false;
	
	RecipeTemplates recipes = new RecipeTemplates();
	Collection<RecipeTemplate> recipeList = recipes.getRecipeTemplate();
	Collection<ClientRecipe> clientRecipes;
	
	public AionRecipesWriter(boolean analyse) {this.ANALYSE = analyse;}
	
	@Override
	public void parse() {
		clientRecipes = aion.getRecipes().values();
		aion.getStrings();
		aion.getSkills();
		aion.getItems();
	}

	@Override
	public void transform() {
		for (ClientRecipe cr : clientRecipes) {
			RecipeTemplate rt = new RecipeTemplate();
			
			rt.setId((Integer) JAXBHandler.getValue(cr, "id"));
			rt.setNameid(aion.getStrings().get(JAXBHandler.getValue(cr, "desc").toString()).getId() * 2 + 1);
			rt.setSkillid(aion.getSkillId(JAXBHandler.getValue(cr, "combineskill").toString()));
			rt.setRace(Race.fromClient(JAXBHandler.getValue(cr, "qualification_race").toString()).toString());
			rt.setSkillpoint((Integer) JAXBHandler.getValue(cr, "required_skillpoint"));
			rt.setDp((Integer) JAXBHandler.getValue(cr, "require_dp"));
			rt.setAutolearn((Integer) JAXBHandler.getValue(cr, "auto_learn"));
			rt.setProductid(aion.getItems().get(JAXBHandler.getValue(cr, "product").toString()).getId());
			rt.setQuantity((Integer) JAXBHandler.getValue(cr, "product_quantity"));
			rt.setMaxProductionCount((Integer) JAXBHandler.getValue(cr, "max_production_count"));
			rt.setCraftDelayId((Integer) JAXBHandler.getValue(cr, "craft_delay_id"));
			rt.setCraftDelayTime((Integer) JAXBHandler.getValue(cr, "craft_delay_time"));
			for (int a = 1; a <= (Integer) JAXBHandler.getValue(cr, "component_quantity"); a++) {
				Component compo = new Component();
				compo.setQuantity((Integer) JAXBHandler.getValue(cr, "compo"+a+"_quantity"));
				compo.setItemid(aion.getItems().get(JAXBHandler.getValue(cr, "component"+a).toString()).getId());
				rt.getComponent().add(compo);
			}
			for (int b = 1; b <= RecipesProperties.MAX_COMBO; b++) {
				Comboproduct cp = new Comboproduct();
				if (JAXBHandler.getValue(cr, "combo"+b+"_product") != null) {
					cp.setItemid(aion.getItems().get(JAXBHandler.getValue(cr, "combo"+b+"_product").toString()).getId());
					rt.getComboproduct().add(cp);
				}
			}
			recipeList.add(rt);
		}
	}
	
	@Override
	public void finalise() {
		if (ANALYSE) JAXBHandler.printUnused("recipes");
	}

	@Override
	public void marshall() {
		addOrder(RecipesProperties.OUTPUT_BINDINGS, RecipesProperties.OUTPUT, recipes);
		FileMarshaller.marshallFile(orders);
		System.out.println("\n[RECIPES] Recipes count: " + recipeList.size());
	}
}