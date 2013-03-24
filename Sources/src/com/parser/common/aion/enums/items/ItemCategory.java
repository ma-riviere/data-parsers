package com.parser.common.aion.enums.items;

import java.util.Collections;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

import com.parser.input.aion.items.ClientItem;
import com.parser.common.aion.AionDataCenter;

/**
 * @author Imaginary
 */
public enum ItemCategory {

	//TODO: PET (food, egg, present ...) CASH
	NONE(),
	ARROW("arrow", "_Arrow"),
	WINGS("wing_", "_Feather"),
	WINGS_BONE("_wing_", "_wing_bone"),
	// Improvements
	MANASTONE("matter_option_", "_magicstone_"),
	MANASTONE_SACK("matter_option_", "_Sack"),
	MANASTONE_ENCHANTMENT_BOX("strengthbox_", "_SpecialBox"),
	GODSTONE("matter_proc", "_Holystone_"),
	GODSTONE_SACK("matter_proc", "_Sack"), 
	ENCHANTMENT("matter_enchant_", "_EnchantStone"),
	ENCHANTMENT_POUCH("_enchant_", "_Pouch"),
	// Crafting items
	FLUX("material_", "_CrystalBall"),
	FLUX_POUCH("material_", "_Pouch"),
	FLUX_BOX("materialbox", "_box_"),
	BALIC_EMOTION("ab_material_", "_ElementalStone"),
	BALIC_EMOTION_SACK("ab_material_box_", "_Sack"),
	BALIC_MATERIAL("dr_material_"),
	BALIC_MATERIAL_POUCH("dragon_material_", "_Pouch"),
	BALIC_MATERIAL_SACK("dr_material_", "_Sack"),
	BALIC_HEART("master_recipe_quest_", "_Heart"),
	RAWHIDE("rawhide_", "_Rawhide"),
	SOULSTONE("soulstone_", "_ElementalStone"),
	SOULSTONE_POUCH("soulstone_", "_Pouch"),
	RECIPE("rec_d_", "_Scroll"),
	GATHERABLE(),
	CRAFT_BOOST("skillboost_"),
	// Weapons
	SWORD("WEAPON", "sword_", "_Sword_"),
	DAGGER("WEAPON", "dagger_", "_Dagger_"),
	MACE("WEAPON", "mace_", "_Mace_"),
	ORB("WEAPON", "orb_", "_Orb_"),
	SPELLBOOK("WEAPON", "book_", "_Book_"),
	GREATSWORD("WEAPON", "2hsword_", "_2HSword_"),
	POLEARM("WEAPON", "polearm_", "_Polearm_"),
	STAFF("WEAPON", "staff_", "_Staff_"),
	BOW("WEAPON", "bow_", "_Bow_"),
	GUN("WEAPON", "gun_", "_Gun_"), 
	CANON("WEAPON", "cannon_", "_Cannon_"),
	HARP("WEAPON", "harp_", "_Harp_"),
	KEYBLADE("WEAPON", "keyblade_", "_2Hsword_"),
	KEYHAMMER("WEAPON", "keyhammer_", "_2HSword_"),
	TOOLHOE("WEAPON", "tool_hoe_", "_hoe"),
	TOOLPICK("WEAPON", "tool_pick_", "_pick"),
	TOOLROD("WEAPON", "tool_fishingrod_", "_rod"),
	// Armors
	SHIELD("ARMOR", "shield_", "_Shield_"),
	JACKET("ARMOR", "torso_", "_Torso_"),
	PANTS("ARMOR", "pants_", "_Pants_"),
	SHOES("ARMOR", "shoes_", "_Shoes_"),
	GLOVES("ARMOR", "gloves_", "_Gloves_"),
	SHOULDERS("ARMOR", "shoulder_", "_Shoulder_"),
	// Accesories
	NECKLACE("ARMOR", "necklace_", "_Necklace_"),
	EARRINGS("ARMOR", "earring_", "_Earring_"),
	RINGS("ARMOR", "ring_", "_Ring_"),
	HELMET("ARMOR", "head_", "_head_"),
	BELT("ARMOR", "belt_", "_Belt_"),
	// Skills
	SKILLBOOK("skillbook_", "_skillbook_"),
	STIGMA("stigma_", "_Stigma"),
	STIGMA_ADVANCED(),
	// Shards
	STIGMA_SHARDS("stigma_shard", "_Crystal"),
	POWER_SHARDS("battery_", "_Battery"), 
	POWER_SHARDS_SACK("battery_", "_Sack"),
	// Misc
	COIN("coin_", "_Coin"), // Also includes tokens 
	COIN_BOX("coin_", "_Box"),
	MEDAL("medal_", "_badge"),
	MEDAL_BOX("medal_", "_Box"),
	QUEST_ITEM(),
	QUEST_START_ITEM(),
	QUEST_USE_ITEM(),
	FIRECRACKER("firecracker", "_firecracker"),
	EXTRACTOR("matter_extraction_", "_Pincer"),
	DYE("dye_", "_dye_"),
	DYE_REMOVER("dye_remover", "_Adhesive"),
	PAINT("paint_", "_dye_"),
	PAINT_REMOVER("paint_remover", "_Adhesive"),
	KEY("key_", "_Key");

	private String equipmentType;
	private String clientNamePart;
	private String clientIconNamePart;
	
	private ItemCategory(String equipmentType, String clientNamePart, String clientIconNamePart){
		this.equipmentType = equipmentType;
		this.clientNamePart = clientNamePart;
		this.clientIconNamePart = clientIconNamePart;
	}
	
	private ItemCategory(String clientNamePart, String clientIconNamePart) {
		this(null, clientNamePart, clientIconNamePart);
	}
	
	private ItemCategory(String clientNamePart) {
		this(null, clientNamePart, null);
	}
	
	private ItemCategory() {
		this(null, null, null);
	}
	
	public String getEquipmentType() {return equipmentType;}
	public String getClientNamePart() {return clientNamePart;}
	public String getClientIconNamePart() {return clientIconNamePart;}
	
	public static ItemCategory getMatchingCategory(ClientItem ci) {
		ItemCategory category = ItemCategory.NONE;
		
		for (ItemCategory ic : values()) {
			boolean name = false;
			if (ic.getClientNamePart() == null)  // If not defined, true
				name = true;
			boolean iconName = false;
			if (ic.getClientIconNamePart() == null) // If not defined, true
				iconName = true;
			
			// If both checks are already true, it means the category has no definition, thus it should no be modified and stay NONE
			if (!iconName || !name) {
				
				// Check match with Item Name
				if (ic.getClientNamePart() != null && ci.getName() != null)
					if(ci.getName().toUpperCase().contains(ic.getClientNamePart().toUpperCase())) {
						if (name == false)
							name = true;
						else
							System.out.println("[CATEGORY][WARN] Multiple category matches for item : " + ci.getName());
					}
				// Check match with Icon Name	
				if (ic.getClientIconNamePart() != null && ci.getIconName() != null)
					if(ci.getIconName().toUpperCase().contains(ic.getClientIconNamePart().toUpperCase())) {
						if (iconName == false)
							iconName = true;
						else
							System.out.println("[CATEGORY][WARN] Multiple category matches for item : " + ci.getName());
					}
				
				if(name && iconName)
					category = ic;
			}
		}
				
		// Non-string based category calculation / Rectification
		if (category == ItemCategory.NONE) {
			if (ci.getCategory() != null && ci.getCategory().equalsIgnoreCase("harvest"))
				category = ItemCategory.GATHERABLE;
			if ((int) ci.getQuestLabel() == 1)
				category = ItemCategory.QUEST_ITEM;
			if ((int) ci.getQuestLabel() == 2) {
				if (ci.getActivationSkill() != null)
					category = ItemCategory.QUEST_USE_ITEM;
				else if (ci.getAreaToUse() != null)
					category = ItemCategory.QUEST_USE_ITEM;
				else
					category = ItemCategory.QUEST_ITEM;
			}
			if (ci.getDescLong() != null) {
				String desc = new AionDataCenter().getInstance().getMatchingStringText(ci.getDescLong()).toUpperCase();
				if (desc.contains("Double-click to begin a quest".toUpperCase()) || desc.contains("더블 클릭하여 퀘스트를 받을 수 있습니다".toUpperCase())) // Check 182213370
					category = ItemCategory.QUEST_START_ITEM;
			}
		}
		
		if (category == ItemCategory.STIGMA) {
			if (ci.getStigmaType() != null && StringUtils.containsIgnoreCase(ci.getStigmaType(), "Enhanced")) {
				category = ItemCategory.STIGMA_ADVANCED;
			}
			if (ci.getGainSkill1() == null && ci.getGainSkill2() == null) {
				category = ItemCategory.NONE;
				System.out.println("[CATEGORY] STIGMA category was removed for item : " + ci.getId() + " as it does not add any skill !");
			}
		}
		
		return category;
	}
	
	public static ItemCategory getCategoryByName(String name) {
		for (ItemCategory ic : values()) {
			if (ic.toString().equals(name))
				return ic;
		}
		System.out.println("[CATEGORY] No ItemCategory named :" + name);
		return null;
	}
}