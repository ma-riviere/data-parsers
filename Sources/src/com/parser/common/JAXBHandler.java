package com.parser.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.skills.ClientSkill;

public class JAXBHandler {

	public static Map<String, List<String>> unused = new HashMap<String, List<String>>();
	public static Map<String, List<String>> all = new HashMap<String, List<String>>();

	public static Object getRecipeValue(ClientRecipe cr, String propertyName) {
		
		
		for (JAXBElement<? extends Serializable> jaxb : cr.getIdOrNameOrDesc()) {
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				return jaxb.getValue();
			}
		}
		return null;
	}
	
	public static Object getSkillValue(ClientSkill cs, String propertyName) {

		for (JAXBElement<? extends Serializable> jaxb : cs.getIdOrNameOrDesc()) {
			/*if (all.get("skills") == null || all.get("skills").isEmpty()) {
				List<String> niu = new ArrayList<String>();
				niu.add(jaxb.getName().getLocalPart());
				all.put("skills", niu);
			}
			else if (!all.get("skills").contains(jaxb.getName().getLocalPart())) {
				all.get("skills").add(jaxb.getName().getLocalPart());
			}*/

			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
				/*if (unused.get("skills") == null || unused.get("skills").isEmpty()) {
					List<String> niu2 = new ArrayList<String>();
					niu2.add(jaxb.getName().getLocalPart());
					unused.put("skills", niu2);
				}
				else if (!unused.get("skills").contains(jaxb.getName().getLocalPart())) {
					unused.get("skills").add(jaxb.getName().getLocalPart());
				}*/
				
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