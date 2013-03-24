package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum ActivationAttribute {

	NONE,
    ACTIVE,
    PROVOKED,
    MAINTAIN,
    TOGGLE,
    PASSIVE,
	CHARGE;

	private String clientString;
	
	private ActivationAttribute(String clientString) {
		this.clientString = clientString;
	}
	
	private ActivationAttribute() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static ActivationAttribute fromClient(String string) {
		for (ActivationAttribute v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No ActivationAttribute matching :" + string);
		return null;
	}
	
	public static ActivationAttribute fromValue(String name) {
		for (ActivationAttribute v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
