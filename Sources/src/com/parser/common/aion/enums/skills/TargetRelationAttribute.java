package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum TargetRelationAttribute {

	NONE,
    ENEMY,
    MYPARTY,
    ALL,
    FRIEND;

	private String clientString;
	
	private TargetRelationAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private TargetRelationAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static TargetRelationAttribute fromClient(String string) {
		for (TargetRelationAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No TargetRelationAttribute matching :" + string);
		return null;
	}
	
	public static TargetRelationAttribute fromValue(String name) {
		for (TargetRelationAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
