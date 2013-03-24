package com.parser.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.skills.ClientSkill;

public class JAXBHandler {

	public static List<JAXBElement<? extends Serializable>> unusedRecipesProperties = null;
	public static List<JAXBElement<? extends Serializable>> unusedSkillsProperties = null;
	public static List<JAXBElement<? extends Serializable>> notToAdd = new ArrayList<JAXBElement<? extends Serializable>>();

	public static Object getRecipeValue(ClientRecipe cr, String propertyName) {
		unusedRecipesProperties = cr.getIdOrNameOrDesc();
		
		for (JAXBElement<? extends Serializable> jaxb : cr.getIdOrNameOrDesc()) {
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				if (unusedRecipesProperties.contains(jaxb))
					unusedRecipesProperties.remove(jaxb);
				return jaxb.getValue();
			}
		}
		return null;
	}
	
	public static Object getSkillValue(ClientSkill cs, String propertyName) {
		if (unusedSkillsProperties == null)
			unusedSkillsProperties = cs.getIdOrNameOrDesc();

		for (JAXBElement<? extends Serializable> jaxb : cs.getIdOrNameOrDesc()) {
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				if (unusedSkillsProperties != null && unusedSkillsProperties.contains(jaxb.getValue())) {
					unusedSkillsProperties.remove(jaxb.getValue());
					notToAdd.add(jaxb);
				}
				if (jaxb.getValue() instanceof Integer)
					return ((Number) jaxb.getValue()).intValue();
				else if (jaxb.getValue() instanceof String)
					return ((Object) jaxb.getValue()).toString();
				else
					return jaxb.getValue();
			}
		}
		return null;
	}
	
	public static void printUnused(String which) {
		List<JAXBElement<? extends Serializable>> toPrint = new ArrayList<JAXBElement<? extends Serializable>>();
		switch(which) {
			case "skills":
				toPrint = unusedSkillsProperties; break;
			case "recipes":
				toPrint = unusedRecipesProperties; break;
			default:
				System.out.println("[JAXB] Trying to print non-available info");
		}
		System.out.println("\n[JAXB] Unused properties of Client "+ which + " :");
		for (JAXBElement<? extends Serializable> jaxb : toPrint) {
			System.out.println(jaxb.getName().getLocalPart());
		}
	}
}