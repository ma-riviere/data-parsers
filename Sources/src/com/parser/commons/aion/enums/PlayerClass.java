package com.parser.commons.aion.enums;

import java.util.ArrayList;
import java.util.List;

public enum PlayerClass {

	WARRIOR,
	GLADIATOR("FIGHTER"),
	TEMPLAR("KNIGHT"),
	SCOUT,
	RANGER,
	ASSASSIN,
	CLERIC,
	PRIEST,
	CHANTER,
	MAGE,
	SORCERER("WIZARD"),
	SPIRIT_MASTER("ELEMENTALLIST"),
	ALL,
	ENGINEER,
	ARTIST,
	GUNNER,
	RIDER,
	BARD;

	private String clientString;
	
	private PlayerClass(String clientString) {
		this.clientString = clientString;
	}
	
	private PlayerClass() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	public static List<String> unknownClass = new ArrayList<String>();
	
	public static PlayerClass fromClient(String string) {
	
		for (PlayerClass v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}/*
		try {int value = Integer.parseInt(string);} 
		catch (Exception e) {
			if (!unknownClass.contains(string.toUpperCase())) {
				System.out.println("[PLAYER CLASS] No PlayerClass matching : " + string.toUpperCase());
				unknownClass.add(string.toUpperCase());
			}
		}*/
		return null;
	}
	
	public static PlayerClass fromValue(String name) {
		for (PlayerClass v : values()) {
			if (v.toString().equals(name))
				return v;
		}
		return null;
	}
}
