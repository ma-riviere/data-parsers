package com.parser.common.aion.enums;

/**
 * @author Imaginary
 */
public enum Race {

	NONE("none"),
	PC_ALL("all"),
	ELYOS("pc_light"),
	ASMODIANS("pc_dark"),
	LYCAN("lycan"),
	CONSTRUCT("construct"),
	CARRIER("Carrier"),
	DRAKAN("drakan"),
	LIZARDMAN("lizardman"),
	TELEPORTER("Teleporter"),
	NAGA("naga"),
	BROWNIE("brownie"),
	KRALL("krall"),
	SHULACK("Shulack"),
	BARRIER("Barrier"),
	PC_LIGHT_CASTLE_DOOR("pc_light_Castle_Door"),
	PC_DARK_CASTLE_DOOR("pc_dark_Castle_Door"),
	DRAGON_CASTLE_DOOR("dragon_Castle_Door"),
	GCHIEF_LIGHT("Gchief_Light"),
	GCHIEF_DARK("Gchief_Dark"),
	DRAGON("dragon"),
	OUTSIDER("outsider"),
	RATMAN("ratman"),
	DEMIHUMANOID("demihumanoid"),
	UNDEAD("undead"),
	BEAST("Beast"),
	MAGICALMONSTER("MagicalMonster"),
	ELEMENTAL("Elemental"),
	LIVINGWATER("Livingwater"),
	DEFORM(""),
	NEUT("neut"),
	GHENCHMAN_LIGHT("GHenchman_Light"),
	GHENCHMAN_DARK("GHenchman_Dark"),
	EVENT_TOWER_DARK("Event_Tower_Dark"),
	EVENT_TOWER_LIGHT("Event_Tower_Light"),
	GOBLIN("Goblin"),
	TRICODARK("TricoDark"),
	NPC("");

	private String clientString;
	
	private Race(String clientString) {
		this.clientString = clientString;
	}
	
	public String getClientString() {return clientString;}
	
	public static Race getRaceByString(String string) {
		String[] races = string.split(" ");
		if (races.length > 1)
			return PC_ALL;
		else {
			for (Race r : values()) {
				if (r.getClientString().equalsIgnoreCase(string))
					return r;
			}
		}
		System.out.println("[RACE] No Race matching :" + string);
		return null;
	}
	
	public static Race getRaceByName(String name) {
		for (Race r : values()) {
			if (r.toString().equalsIgnoreCase(name))
				return r;
		}
		System.out.println("[RACE] No Race named :" + name);
		return null;
	}
}
