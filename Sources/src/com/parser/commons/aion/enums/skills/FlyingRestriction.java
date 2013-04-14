package com.parser.commons.aion.enums.skills;

public enum FlyingRestriction {

	ALL,
    FLY,
    GROUND;

	private String clientString;
	
	private FlyingRestriction(String clientString) {
		this.clientString = clientString;
	}
	
	private FlyingRestriction() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static FlyingRestriction fromClient(String string) {
		for (FlyingRestriction v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No FlyingRestriction matching :" + string);
		return null;
	}
	
	public static FlyingRestriction fromValue(String name) {
		for (FlyingRestriction v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
