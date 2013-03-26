package com.parser.common.aion.enums.skills;

/**
 * @author Viria
 */
public enum TargetAttribute {

	NPC,
    PC,
    ALL,
    NONE;

	private String clientString;
	
	private TargetAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private TargetAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static TargetAttribute fromClient(String string) {
		for (TargetAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No TargetAttribute matching :" + string);
		return null;
	}
	
	public static TargetAttribute fromValue(String name) {
		for (TargetAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
