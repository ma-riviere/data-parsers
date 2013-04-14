package com.parser.commons.aion.enums.items;

import com.parser.input.aion.items.ClientItem;

public enum EquipmentSlot {

	NONE(0),
	MAIN(1),
	SUB(2),
	MAIN_OR_SUB(3),
	HEAD(4),
	TORSO(8),
	GLOVE(16),
	FOOT(32),
	RIGHT_OR_LEFT_EAR(192),
	RIGHT_OR_LEFT_FINGER(768),
	NECK(1024),
	SHOULDER(2048),
	LEG(4096),
	TORSO_GLOVE_FOOT_SHOULDER_LEG(6200),
	RIGHT_OR_LEFT_BATTERY(24576),
	WING(32768),
	WAIST(65536);

	private int value;
	
	private EquipmentSlot(int value){
		this.value = value;
	}
	
	public int getEquipmentSlot() {
		return value;
	}
	
	public static EquipmentSlot getEquipSlotByString(ClientItem ci) {
		EquipmentSlot slot = EquipmentSlot.NONE;
		for (EquipmentSlot es : values()) {
			if (ci.getEquipmentSlots() != null && es.toString().equals(ci.getEquipmentSlots().toUpperCase()))
				slot = es;
		}
		if (slot == EquipmentSlot.NONE && ci.getEquipmentSlots() != null && ci.getEquipmentSlots().equalsIgnoreCase("torso glove foot shoulder leg"))
			slot = EquipmentSlot.TORSO_GLOVE_FOOT_SHOULDER_LEG;
		
		return slot;
	}
}
