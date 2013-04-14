package com.parser.commons.aion.enums.spawns;

public enum ClientSpawnType {

	SP(true), // Spawn Point
	HSP, // Harvest Spawn Point, move to true later
	PORTAL, // ?
	WATERVOLUME, // Position of water
	MP, // Map Point
	SHAPE,
	DP, // Direct Portal
	KP, // Key Point
	OCCLUDERAREA, // ?
	SEEDPOINT, // ?
	LOCATION, // Arrival positions for TP, Scrolls, Main ...
	VISAREA, // ?
	GROUP, // ?
	AREABOX, // ?
	STARTPOINT; // Start Point

	private String clientString;
	private boolean shouldBeSpawned = false;
	
	private ClientSpawnType(String clientString, boolean shouldBeSpawned) {
		this.clientString = clientString;
		this.shouldBeSpawned = shouldBeSpawned;
	}
	
	private ClientSpawnType(boolean shouldBeSpawned) {
		this(null, shouldBeSpawned);
	}
	
	private ClientSpawnType() {
		this(null, false);
	}
	
	public String getClientString() {return clientString;}
	public boolean shouldBeSpawned() {return shouldBeSpawned;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static ClientSpawnType fromClient(String string) {
		for (ClientSpawnType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SPAWNS] No ClientSpawnType matching : " + string);
		return null;
	}
	
	public static ClientSpawnType fromValue(String name) {
		for (ClientSpawnType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
