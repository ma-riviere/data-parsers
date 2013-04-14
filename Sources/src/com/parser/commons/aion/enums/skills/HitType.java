package com.parser.commons.aion.enums.skills;

public enum HitType {

	NONE,
	EVERYHIT,
	NMLATK,
	MAHIT,
	PHHIT;
	//FEAR; ???

	private String clientString;
	
	private HitType(String clientString) {
		this.clientString = clientString;
	}
	
	private HitType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static HitType fromClient(String string) {
		for (HitType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		try {int value = Integer.parseInt(string);} catch (Exception e) {System.out.println("[SKILLS] No HitType matching : " + string);}
		return HitType.NONE;
	}
	
	public static HitType fromValue(String name) {
		for (HitType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
