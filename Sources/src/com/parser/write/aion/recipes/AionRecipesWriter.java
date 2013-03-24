package com.parser.write.aion.recipes;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;

import com.parser.common.aion.AionDataCenter;
import com.parser.common.aion.AionProperties;
import com.parser.read.aion.recipes.AionRecipesParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.recipes.RecipeTemplates;
import com.parser.output.aion.recipes.RecipeTemplate;
import com.parser.output.aion.recipes.Component;
import com.parser.output.aion.recipes.Comboproduct;

public class AionRecipesWriter extends AbstractWriter {

	public static final String PREFIX = "";

	RecipeTemplates finalTemplates = new RecipeTemplates();
	Collection<RecipeTemplate> templateList = finalTemplates.getRecipeTemplate();
	List<ClientRecipe> clientRecipesData;
	
	@Override
	public void parse() {
		clientRecipesData = new AionRecipesParser().parse();
	}

	@Override
	public void transform() {
		for (ClientRecipe cr : clientRecipesData) {
			RecipeTemplate rt = new RecipeTemplate();
			
			rt.setId((Integer) getValueOfProperty(cr, "id"));
			rt.setNameid(new AionDataCenter().getInstance().getMatchingStringId(getValueOfProperty(cr, "desc").toString(), 2, 1));
			rt.setSkillid(getMatchingSkill(getValueOfProperty(cr, "combineskill").toString()));
			rt.setRace(adjustRace(getValueOfProperty(cr, "qualification_race").toString()));
			rt.setSkillpoint((Integer) getValueOfProperty(cr, "required_skillpoint"));
			rt.setDp((Integer) getValueOfProperty(cr, "require_dp"));
			rt.setAutolearn((Integer) getValueOfProperty(cr, "auto_learn"));
			rt.setProductid(new AionDataCenter().getInstance().getItemIdByName(getValueOfProperty(cr, "product").toString(), PREFIX));
			rt.setQuantity((Integer) getValueOfProperty(cr, "product_quantity"));
			rt.setMaxProductionCount((Integer) getValueOfProperty(cr, "max_production_count"));
			rt.setCraftDelayId((Integer) getValueOfProperty(cr, "craft_delay_id"));
			rt.setCraftDelayTime((Integer) getValueOfProperty(cr, "craft_delay_time"));
			for (int a = 1; a <= (Integer) getValueOfProperty(cr, "component_quantity"); a++) {
				Component compo = new Component();
				compo.setQuantity((Integer) getValueOfProperty(cr, "compo"+a+"_quantity"));
				compo.setItemid(new AionDataCenter().getInstance().getItemIdByName(getValueOfProperty(cr, "component"+a).toString(), PREFIX));
				rt.getComponent().add(compo);
			}
			for (int b = 1; b <= AionProperties.MAX_COMBO_FOR_RECIPES; b++) {
				Comboproduct cp = new Comboproduct();
				if (getValueOfProperty(cr, "combo"+b+"_product") != null) {
					cp.setItemid(new AionDataCenter().getInstance().getItemIdByName(getValueOfProperty(cr, "combo"+b+"_product").toString(), PREFIX));
					rt.getComboproduct().add(cp);
				}
			}
			templateList.add(rt);
		}
	}

	@Override
	public void marshall() {
		System.out.println("Recipes count: " + templateList.size());
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.RECIPES, AionWritingConfig.RECIPES_PACK);
	}
	
	private Object getValueOfProperty(ClientRecipe cr, String propertyName) {
		for (JAXBElement<? extends Serializable> c : cr.getIdOrNameOrDesc()) {
			if (c.getName().getLocalPart().equalsIgnoreCase(propertyName))
				return c.getValue();
		}
		return null;
	}
	
	private String adjustRace(String race) {
		if (race.equalsIgnoreCase("pc_dark")) {
			return "ASMODIANS";
		} else if (race.equalsIgnoreCase("pc_light")) {
			return "ELYOS";
		} else
			return "PC_ALL";
	}
	
	private int getMatchingSkill(String skillName) {
		return new AionDataCenter().getInstance().getSkillIdByName(skillName);
	}

}