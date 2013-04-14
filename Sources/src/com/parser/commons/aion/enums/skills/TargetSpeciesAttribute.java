package com.parser.commons.aion.enums.skills;

public enum TargetSpeciesAttribute {

	NONE,
    ALL,
    PC,
    NPC;

	private String clientString;
	
	private TargetSpeciesAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private TargetSpeciesAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static TargetSpeciesAttribute fromClient(String string) {
		for (TargetSpeciesAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No TargetSpeciesAttribute matching :" + string);
		return null;
	}
	
	public static TargetSpeciesAttribute fromValue(String name) {
		for (TargetSpeciesAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
