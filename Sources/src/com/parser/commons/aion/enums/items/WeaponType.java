package com.parser.commons.aion.enums.items;

import java.util.List;
import java.util.ArrayList;

public enum WeaponType {

	DAGGER_1H("1h_dagger"),
	MACE_1H("1h_mace"),
	SWORD_1H("1h_sword"),
	TOOLHOE_1H("1h_toolhoe"),
	BOOK_2H("2h_book"),
	ORB_2H("2h_orb"),
	POLEARM_2H("2h_polearm"),
	STAFF_2H("2h_staff"),
	SWORD_2H("2h_sword"),
	TOOLPICK_2H("2h_toolpick"),
	TOOLROD_2H("2h_toolrod"),
	BOW,
	GUN_1H("1h_gun"),
	CANON_2H("2h_cannon"),
	HARP_2H("2h_harp"),
	KEYBLADE_2H("2h_keyblade"),
	KEYHAMMER_2H("2h_keyhammer");

	private String clientString;
	
	private WeaponType(String clientString) {
		this.clientString = clientString;
	}
	
	private WeaponType() {
		this(null);
	}
	
	public String getClientString() {return clientString;}
	
	public static List<String> unkWeap = new ArrayList<String>();
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static WeaponType fromClient(String string) {
		for (WeaponType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		try {int value = Integer.parseInt(string);} 
		catch (Exception e) {
			if (!unkWeap.contains(string.toUpperCase())) {
				System.out.println("[WEAPON] No WeaponType matching : " + string.toUpperCase());
				unkWeap.add(string.toUpperCase());
			}
		}
		return null;
	}
	
	public static WeaponType fromValue(String name) {
		for (WeaponType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
