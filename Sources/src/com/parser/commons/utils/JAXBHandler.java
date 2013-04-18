package com.parser.commons.utils;

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

	public static Object getValue(ClientRecipe cr, String propertyName) {
	
		for (JAXBElement<? extends Serializable> jaxb : cr.getIdOrNameOrDesc()) {
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
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
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName)) {
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
}