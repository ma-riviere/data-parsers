package com.parser.common.aion.enums.skills;

/**
 * @author Viria
 */
public enum DispelCategoryType {

	NONE,
    ALL,
    BUFF,
	NPC_BUFF,
	DEBUFF,
    DEBUFF_MENTAL("DebuffMen"),
    DEBUFF_PHYSICAL("DebuffPhy"),
	NPC_DEBUFF_PHYSICAL("Npc_DebuffPhy"),
    EXTRA,
    NEVER,
    STUN;

	private String clientString;
	
	private DispelCategoryType(String clientString) {
		this.clientString = clientString;
	}
	
	private DispelCategoryType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static DispelCategoryType fromClient(String string) {
		for (DispelCategoryType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No DispelCategoryType matching :" + string);
		return null;
	}
	
	public static DispelCategoryType fromValue(String name) {
		for (DispelCategoryType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
