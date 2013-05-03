package com.parser.commons.aion.enums.skills;

public enum UIType {
	
	CRAFT,
	NONE;

	private String clientString;
	
	private UIType(String clientString) {
		this.clientString = clientString;
	}
	
	private UIType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static UIType fromClient(String string) {
		for (UIType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[NPCS] No UIType matching :" + string);
		return UIType.NONE;
	}
	
	public static UIType fromValue(String name) {
		for (UIType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
