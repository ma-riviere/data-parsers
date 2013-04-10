package com.parser.common.aion.enums.skills;

public enum TargetRangeAttribute {

	NONE,
    ONLYONE,
    PARTY,
    AREA,
    PARTY_WITHPET,
    POINT;

	private String clientString;
	
	private TargetRangeAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private TargetRangeAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static TargetRangeAttribute fromClient(String string) {
		for (TargetRangeAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No TargetRangeAttribute matching :" + string);
		return null;
	}
	
	public static TargetRangeAttribute fromValue(String name) {
		for (TargetRangeAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
