package com.parser.common.aion.enums.skills;

/**
 * @author Imaginary
 */
public enum TargetSlot {

	NONE,
    BUFF,
    DEBUFF,
    SPEC("special"),
    SPEC2("special2"),
    BOOST,
    NOSHOW,
    CHANT;

	private String clientString;
	
	private TargetSlot(String clientString) {
		this.clientString = clientString;
	}
	
	private TargetSlot() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static TargetSlot fromClient(String string) {
		for (TargetSlot v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No TargetSlot matching :" + string);
		return null;
	}
	
	public static TargetSlot fromValue(String name) {
		for (TargetSlot v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
