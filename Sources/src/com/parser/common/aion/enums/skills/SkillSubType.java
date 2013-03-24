package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum SkillSubType {

	NONE,
    ATTACK,
	BUFF,
	DEBUFF,
    HEAL,
    SUMMONTRAP,
	SUMMONHOMING,
    SUMMON,
    CHANT;

	private String clientString;
	
	private SkillSubType(String clientString) {
		this.clientString = clientString;
	}
	
	private SkillSubType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static SkillSubType fromClient(String string) {
		for (SkillSubType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No SkillSubType matching :" + string);
		return null;
	}
	
	public static SkillSubType fromValue(String name) {
		for (SkillSubType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
