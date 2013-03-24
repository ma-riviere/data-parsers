package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum FirstTargetAttribute {

	NONE,
    TARGETORME,
    ME,
    MYPET,
    MYMASTER,
    TARGET,
    PASSIVE,
    TARGET_MYPARTY_NONVISIBLE,
    POINT;

	private String clientString;
	
	private FirstTargetAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private FirstTargetAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static FirstTargetAttribute fromClient(String string) {
		for (FirstTargetAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No FirstTargetAttribute matching :" + string);
		return null;
	}
	
	public static FirstTargetAttribute fromValue(String name) {
		for (FirstTargetAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
