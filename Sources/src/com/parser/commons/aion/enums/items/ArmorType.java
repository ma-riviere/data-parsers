package com.parser.commons.aion.enums.items;

public enum ArmorType {

	NONE,
	CHAIN,
    CLOTHES,
    LEATHER,
    PLATE,
    ROBE,
    SHARD, // Custom
    ARROW, // Custom
    SHIELD,
	DUAL; // New (4.0)

	private String clientString;
	
	private ArmorType(String clientString) {
		this.clientString = clientString;
	}
	
	private ArmorType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static ArmorType fromClient(String string) {
		for (ArmorType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No ArmorType matching :" + string);
		return ArmorType.NONE;
	}
	
	public static ArmorType fromValue(String name) {
		for (ArmorType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
