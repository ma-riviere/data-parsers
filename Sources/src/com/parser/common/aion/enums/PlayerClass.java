package com.parser.common.aion.enums;

/**
 * @author Imaginary
 */
public enum PlayerClass {

	WARRIOR("WARRIOR"),
	GLADIATOR("FIGHTER"),
	TEMPLAR("KNIGHT"),
	SCOUT("SCOUT"),
	RANGER("RANGER"),
	ASSASSIN("ASSASSIN"),
	CLERIC("CLERIC"),
	PRIEST("PRIEST"),
	CHANTER("CHANTER"),
	MAGE("MAGE"),
	SORCERER("WIZARD"),
	SPIRIT_MASTER("ELEMENTALLIST"),
	ALL("ALL"),
	ENGINEER("ENGINEER"),
	ARTIST("ARTIST"),
	GUNNER("GUNNER"),
	RIDER("RIDER"),
	BARD("BARD");

	private String clientString;
	
	private PlayerClass(String clientString) {
		this.clientString = clientString;
	}
	
	public String getClientString() {return clientString;}
	
	public static PlayerClass getClassByString(String string) {
		for (PlayerClass pc : values()) {
			if (pc.getClientString().equalsIgnoreCase(string))
				return pc;
		}
		System.out.println("[CLASS] No PlayerClass matching :" + string);
		return null;
	}
	
	public static PlayerClass getClassByName(String name) {
		for (PlayerClass pc : values()) {
			if (pc.toString().equals(name))
				return pc;
		}
		System.out.println("[CLASS] No PlayerClass named :" + name);
		return null;
	}
}
