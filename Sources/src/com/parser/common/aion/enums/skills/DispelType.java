package com.parser.common.aion.enums.skills;

public enum DispelType {
	
	NONE,
	EFFECTID("Effect_Id"),
    EFFECTIDRANGE("Effect_ID_Range"),
    EFFECTTYPE("Effect_Type"),
    SLOTTYPE("Slot_Type");

	private String clientString;
	
	private DispelType(String clientString) {
		this.clientString = clientString;
	}
	
	private DispelType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static DispelType fromClient(String string) {
		for (DispelType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No DispelType matching :" + string);
		return DispelType.NONE;
	}
	
	public static DispelType fromValue(String name) {
		for (DispelType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
