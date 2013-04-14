package com.parser.commons.aion.enums.skills;

public enum Element {
	
	NONE,
	FIRE,
	WIND("Air"),
	WATER,
	EARTH,
	DARK,
	LIGHT;

	private String clientString;
	
	private Element(String clientString) {
		this.clientString = clientString;
	}
	
	private Element() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static Element fromClient(String string) {
		for (Element v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		try {int value = Integer.parseInt(string);} catch (Exception e) {System.out.println("[SKILLS] No Element matching : " + string);}
		return Element.NONE;
	}
	
	public static Element fromValue(String name) {
		for (Element v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
