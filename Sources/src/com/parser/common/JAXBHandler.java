package com.parser.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.skills.ClientSkill;

import com.parser.write.aion.recipes.AionRecipesWriter;
import com.parser.write.aion.skills.AionSkillsWriter;

public class JAXBHandler {

	public static Map<String, List<String>> unused = new HashMap<String, List<String>>();
	public static Map<String, List<String>> all = new HashMap<String, List<String>>();

	public static Object getValue(ClientRecipe cr, String propertyName) {
	
		for (JAXBElement<? extends Serializable> jaxb : cr.getIdOrNameOrDesc()) {
			if (AionRecipesWriter.ANALYSE)
				fill(all, "recipes", jaxb);
				
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				if (AionRecipesWriter.ANALYSE)
					fill(unused, "recipes", jaxb);
				
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
	
	public static Object getValue(ClientSkill cs, String propertyName) {

		for (JAXBElement<? extends Serializable> jaxb : cs.getIdOrNameOrDesc()) {
			if (AionSkillsWriter.ANALYSE)
				fill(all, "skills", jaxb);
			
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				if (AionSkillsWriter.ANALYSE)
					fill(unused, "skills", jaxb);
				
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
	
	private static void fill(Map<String, List<String>> list, String which, JAXBElement<? extends Serializable> jaxb) {
		if (list.get(which) == null || list.get(which).isEmpty()) {
			List<String> niu = new ArrayList<String>();
			niu.add(jaxb.getName().getLocalPart());
			list.put(which, niu);
		}
		else if (!list.get(which).contains(jaxb.getName().getLocalPart())) {
			list.get(which).add(jaxb.getName().getLocalPart());
		}
	}
	
	public static void printUnused(String which) {	
		if (unused.get(which) != null && !unused.get(which).isEmpty()) {
			for (String s : unused.get(which)) {
				if (all.get(which).contains(s))
					all.get(which).remove(s);
				else
					System.out.println("[JAXB] Problem : All list does not contain value : " + s);
			}
		
			System.out.println("\n[JAXB] Unused properties of Client "+ which + " :");
			for (String s : all.get(which)) {
				System.out.println("<"+s+">");
			}
		}
		else
			System.out.println("[JAXB] Trying to print non-available info");
	}
}