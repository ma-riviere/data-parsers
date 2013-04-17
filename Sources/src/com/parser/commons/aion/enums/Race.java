package com.parser.commons.aion.enums;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Strings;

public enum Race {

	NONE("none"),
	PC_ALL("both"),
	ELYOS("pc_light"),
	ASMODIANS("pc_dark"),
	LYCAN,
	CONSTRUCT,
	CARRIER,
	DRAKAN,
	LIZARDMAN,
	TELEPORTER,
	NAGA,
	BROWNIE,
	KRALL,
	SHULACK,
	BARRIER,
	PC_LIGHT_CASTLE_DOOR,
	PC_DARK_CASTLE_DOOR,
	DRAGON_CASTLE_DOOR,
	GCHIEF_LIGHT,
	GCHIEF_DARK,
	DRAGON,
	OUTSIDER,
	RATMAN,
	DEMIHUMANOID,
	UNDEAD,
	BEAST,
	MAGICALMONSTER,
	ELEMENTAL,
	LIVINGWATER,
	DEFORM,
	NEUT,
	GHENCHMAN_LIGHT,
	GHENCHMAN_DARK,
	EVENT_TOWER_DARK,
	EVENT_TOWER_LIGHT,
	GOBLIN,
	TRICODARK,
	SIEGEDRAKAN,
	NPC;

	private String clientString;
	
	private Race(String clientString) {
		this.clientString = clientString;
	}
	
	private Race() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	public static List<String> unkRace = new ArrayList<String>();
	
	public static Race fromClient(String string) {
	
		if (string.equalsIgnoreCase("ALL")) {string = "BOTH";}
	
		if (Strings.isNullOrEmpty(string))
			return Race.NONE;
	
		String[] races = string.split(" ");
		if (races.length > 1)
			return PC_ALL;
		else {
			for (Race v : values()) {
				if (v.getClientString() != null) {
					if (v.getClientString().equalsIgnoreCase(string))
						return v;
				} else {
					if (fromValue(string) != null)
						return fromValue(string);
				}
			}
		}
		try {int value = Integer.parseInt(string);} 
		catch (Exception e) {
			if (!unkRace.contains(string.toUpperCase())) {
				System.out.println("[RACE] No Race matching : " + string.toUpperCase());
				unkRace.add(string.toUpperCase());
			}
		}
		return Race.NONE;
	}
	
	public static Race fromValue(String name) {
		for (Race r : values()) {
			if (r.toString().equalsIgnoreCase(name))
				return r;
		}
		return null;
	}
}
