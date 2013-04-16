package com.parser.write.aion.recipes;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;

import com.parser.commons.utils.JAXBHandler;
import com.parser.commons.aion.AionDataCenter;
import com.parser.commons.aion.properties.RecipesProperties;

import com.parser.read.aion.recipes.AionRecipesParser;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.recipes.RecipeTemplates;
import com.parser.output.aion.recipes.RecipeTemplate;
import com.parser.output.aion.recipes.Component;
import com.parser.output.aion.recipes.Comboproduct;

public class AionRecipesWriter extends AbstractWriter {

	public static boolean ANALYSE = false;
	
	RecipeTemplates finalTemplates = new RecipeTemplates();
	Collection<RecipeTemplate> templateList = finalTemplates.getRecipeTemplate();
	List<ClientRecipe> clientRecipesData;
	
	public AionRecipesWriter(boolean analyse) {
		this.ANALYSE = analyse;
	}
	
	@Override
	public void parse() {
		clientRecipesData = new AionRecipesParser().parse();
	}

	@Override
	public void transform() {
		for (ClientRecipe cr : clientRecipesData) {
			RecipeTemplate rt = new RecipeTemplate();
			
			rt.setId((Integer) JAXBHandler.getValue(cr, "id"));
			rt.setNameid(new AionDataCenter().getInstance().getMatchingStringId(JAXBHandler.getValue(cr, "desc").toString(), 2, 1));
			rt.setSkillid(getSkill(JAXBHandler.getValue(cr, "combineskill").toString()));
			rt.setRace(adjustRace(JAXBHandler.getValue(cr, "qualification_race").toString()));
			rt.setSkillpoint((Integer) JAXBHandler.getValue(cr, "required_skillpoint"));
			rt.setDp((Integer) JAXBHandler.getValue(cr, "require_dp"));
			rt.setAutolearn((Integer) JAXBHandler.getValue(cr, "auto_learn"));
			rt.setProductid(new AionDataCenter().getInstance().getItemIdByName(JAXBHandler.getValue(cr, "product").toString()));
			rt.setQuantity((Integer) JAXBHandler.getValue(cr, "product_quantity"));
			rt.setMaxProductionCount((Integer) JAXBHandler.getValue(cr, "max_production_count"));
			rt.setCraftDelayId((Integer) JAXBHandler.getValue(cr, "craft_delay_id"));
			rt.setCraftDelayTime((Integer) JAXBHandler.getValue(cr, "craft_delay_time"));
			for (int a = 1; a <= (Integer) JAXBHandler.getValue(cr, "component_quantity"); a++) {
				Component compo = new Component();
				compo.setQuantity((Integer) JAXBHandler.getValue(cr, "compo"+a+"_quantity"));
				compo.setItemid(new AionDataCenter().getInstance().getItemIdByName(JAXBHandler.getValue(cr, "component"+a).toString()));
				rt.getComponent().add(compo);
			}
			for (int b = 1; b <= RecipesProperties.MAX_COMBO_FOR_RECIPES; b++) {
				Comboproduct cp = new Comboproduct();
				if (JAXBHandler.getValue(cr, "combo"+b+"_product") != null) {
					cp.setItemid(new AionDataCenter().getInstance().getItemIdByName(JAXBHandler.getValue(cr, "combo"+b+"_product").toString()));
					rt.getComboproduct().add(cp);
				}
			}
			templateList.add(rt);
		}
	}
	
	@Override
	public void finalise() {
		if (ANALYSE)
			JAXBHandler.printUnused("recipes");
	}

	@Override
	public void marshall() {
		addAionOrder(AionWritingConfig.RECIPES_BINDINGS, AionWritingConfig.RECIPES, finalTemplates);
		FileMarhshaller.marshallFile(orders);
		System.out.println("[RECIPES] Recipes count: " + templateList.size());
	}
	
	private int getSkill(String s) {return new AionDataCenter().getInstance().getSkillIdByName(s);}
	
	private String adjustRace(String race) {
		if (race.equalsIgnoreCase("pc_dark")) {
			return "ASMODIANS";
		} else if (race.equalsIgnoreCase("pc_light")) {
			return "ELYOS";
		} else
			return "PC_ALL";
	}

}