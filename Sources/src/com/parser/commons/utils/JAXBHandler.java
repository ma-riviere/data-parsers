package com.parser.commons.utils;

import java.io.Serializable;
import javax.xml.bind.JAXBElement;

import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.skills.ClientSkill;

public class JAXBHandler {

	public static Object getValue(ClientRecipe cr, String propertyName) {
		for (JAXBElement<? extends Serializable> jaxb : cr.getIdOrNameOrDesc())
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName))
				return (jaxb != null) ? jaxb.getDeclaredType().cast(jaxb.getValue()) : null;
		return null;
	}
	
	public static Object getValue(ClientSkill cs, String propertyName) {
		for (JAXBElement<? extends Serializable> jaxb : cs.getIdOrNameOrDesc())
			if (jaxb.getName().getLocalPart().equalsIgnoreCase(propertyName))
				return (jaxb != null) ? jaxb.getDeclaredType().cast(jaxb.getValue()) : null;
		return null;
	}
}