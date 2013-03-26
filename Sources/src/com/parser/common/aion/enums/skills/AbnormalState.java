package com.parser.common.aion.enums.skills;

/**
 * @author Viria
 */
public enum AbnormalState {

	BIND,
    BLEED,
    BLIND,
    CURSE,
    DEFORM,
    FEAR,
    OPENAERIAL,
    PARALYZE,
    POISON,
    SNARE,
    SPIN,
    STAGGER,
    STUMBLE,
    STUN;

	private String clientString;
	
	private AbnormalState(String clientString) {
		this.clientString = clientString;
	}
	
	private AbnormalState() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static AbnormalState fromClient(String string) {
		for (AbnormalState v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SKILLS] No AbnormalState matching :" + string);
		return null;
	}
	
	public static AbnormalState fromValue(String name) {
		for (AbnormalState v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
