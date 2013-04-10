package com.parser.common.aion.enums.skills;

public enum StigmaType {

	NONE,
    BASIC,
    ADVANCED;

	private String clientString;
	
	private StigmaType(String clientString) {
		this.clientString = clientString;
	}
	
	private StigmaType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static StigmaType fromClient(String string) {
		for (StigmaType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No StigmaType matching :" + string);
		return null;
	}
	
	public static StigmaType fromValue(String name) {
		for (StigmaType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
