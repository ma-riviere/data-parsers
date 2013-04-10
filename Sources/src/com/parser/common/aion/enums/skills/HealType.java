package com.parser.common.aion.enums.skills;

public enum HealType {

	NONE,
	HP,
    MP,
    DP,
    FP;

	private String clientString;
	
	private HealType(String clientString) {
		this.clientString = clientString;
	}
	
	private HealType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static HealType fromClient(String string) {
		for (HealType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No HealType matching :" + string);
		return HealType.NONE;
	}
	
	public static HealType fromValue(String name) {
		for (HealType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
