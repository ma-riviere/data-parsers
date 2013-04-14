package com.parser.commons.aion.enums.spawns;

public enum ClientEntityType {

	DEFERREDLIGHT,
	BASICENTITY(true, -3f), // Dangerous ...
	SOUNDSPOT(true, -0.5f),
	PARTICLEEFFECT, // Fire
	BILLBOARD_OBJECT,
	BUGS,
	MILESTONE(true, -1.47f),
	BIRDS,
	PLACEABLEOBJECT(true, -0.1f),
	RANDOMAMBIENTSOUND,
	CHAIR(true, -0.2f),
	DYNAMICLIGHT,
	WEATHERAMBIENTSOUND,
	DIRECTPORTALEFF;

	private String clientString;
	private boolean hasCorrectZ = false;
	private float zCorrection = 0.0f;
	
	private ClientEntityType(String clientString, boolean hasCorrectZ, float zCorrection) {
		this.clientString = clientString;
		this.hasCorrectZ = hasCorrectZ;
		this.zCorrection = zCorrection;
	}
	
	private ClientEntityType(boolean hasCorrectZ, float zCorrection) {
		this(null, hasCorrectZ, zCorrection);
	}
	
	private ClientEntityType() {
		this(null, false, 0.0f);
	}
	
	public String getClientString() {return clientString;}
	public boolean hasCorrectZ() {return hasCorrectZ;}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static ClientEntityType fromClient(String string) {
		for (ClientEntityType v : values()) {
			if (v.getClientString() != null) {
				if (v.getClientString().equalsIgnoreCase(string))
					return v;
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		System.out.println("[SPAWNS] No ClientEntityType matching : " + string);
		return null;
	}
	
	public static ClientEntityType fromValue(String name) {
		for (ClientEntityType v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
