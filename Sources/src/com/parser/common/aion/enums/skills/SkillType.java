package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum SkillType {

	NONE(),
    PHYSICAL(),
    MAGICAL();

	private String clientString;
	
	private SkillType(String clientString) {
		this.clientString = clientString;
	}
	
	private SkillType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static SkillType fromClient(String string) {
		for (SkillType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No SkillType matching :" + string);
		return null;
	}
	
	public static SkillType fromValue(String name) {
		for (SkillType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
