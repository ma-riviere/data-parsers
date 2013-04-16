package com.parser.write.aion.items;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.io.File;
import java.awt.Color;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import com.parser.input.aion.items.*;
import com.parser.input.aion.skill_learn.ClientSkillTree;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime;

import com.parser.commons.aion.AionDataCenter;
import com.parser.commons.aion.enums.*;
import com.parser.commons.aion.enums.items.*;
import com.parser.commons.aion.properties.AionProperties;

import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.items.AionItemsParser;
import com.parser.read.aion.skills.AionSkillTreeParser;
import com.parser.read.aion.world.AionCooltimesParser;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.items.*;

public class AionItemsWriter extends AbstractWriter {

	AionDataCenter data = new AionDataCenter().getInstance();

	ItemTemplates finalTemplates = new ItemTemplates();
	Collection<ItemTemplate> templateList = finalTemplates.getItemTemplate();
	List<ClientItem> clientItems;
	
	// Modifiers variables
	public List<Add> addList = new ArrayList<Add>();
	public List<Rate> rateList = new ArrayList<Rate>();
	
	// Actions
	public static List<ClientSkillTree> skillTreeList = new ArrayList<ClientSkillTree>();
	public static Map<Integer, String> cooltimeSyncMap = new HashMap<Integer, String>();
	private static Set<String> docFiles = new HashSet<String>();
	
	
	@Override
	public void parse() {
		clientItems = data.getClientItems();
		skillTreeList = new AionSkillTreeParser().parse();
	}

	@Override
	public void transform() {
		for (ClientItem ci : clientItems) {
			ItemTemplate it = new ItemTemplate();
			
			it.setId((int) ci.getId());
			if (!Strings.isNullOrEmpty(ci.getDesc()))
				it.setName(new AionDataCenter().getInstance().getMatchingStringText(ci.getDesc()));
			else
				it.setName(new AionDataCenter().getInstance().getMatchingStringText("STR_" + ci.getName()));
			it.setLevel((ci.getLevel() > AionProperties.MAX_LEVEL) ? AionProperties.MAX_LEVEL : (int) ci.getLevel()); // [UPDATE] Don't forget to keep MAX_LEVEL updated
			if (!Strings.isNullOrEmpty(ci.getDesc()))
				it.setDesc(new AionDataCenter().getInstance().getMatchingStringId(ci.getDesc().trim(), 2, 1));
			
			it.setSlot(EquipmentSlot.getEquipSlotByString(ci).getEquipmentSlot());
			it.setCategory(ItemCategory.getMatchingCategory(ci).toString());
			
			if (!Strings.isNullOrEmpty(ci.getItemType()))
				it.setItemType(ci.getItemType().toUpperCase());
			if (!Strings.isNullOrEmpty(ci.getWeaponType()) && !ci.getWeaponType().equalsIgnoreCase("no_weapon") && !ci.getWeaponType().equalsIgnoreCase("noweapon"))
				it.setWeaponType(WeaponType.fromClient(ci.getWeaponType()).toString());
			else {
				if (!Strings.isNullOrEmpty(ci.getArmorType()) && !ci.getArmorType().equalsIgnoreCase("no_armor") && ArmorType.fromClient(ci.getArmorType()) != ArmorType.NONE)
					it.setArmorType(ArmorType.fromClient(ci.getArmorType()).toString());
				else {
					if (ci.getEquipType() == 0 && (it.getCategory().equalsIgnoreCase("WINGS") || it.getCategory().equalsIgnoreCase("WINGS_BONE"))) {it.setArmorType("WINGS");}
					if (it.getCategory().equalsIgnoreCase("ARROW")) {it.setArmorType("ARROW");}
					if (it.getCategory().equalsIgnoreCase("SHIELD")) {it.setArmorType("SHIELD");}
				}
			}
			if (ItemCategory.getCategoryByName(it.getCategory()) != null) {
				it.setEquipmentType(ItemCategory.getCategoryByName(it.getCategory()).getEquipmentType());
				// Fixing Armors that don't have an equipmentType
				if (it.getEquipmentType() == null && it.getArmorType() != null)
					it.setEquipmentType("ARMOR");
			}
			// if (it.getSlot() != null && it.getSlot() == 24576) 
				// it.setCategory("SHARD");
							
			it.setQuality(ci.getQuality() != null ? ci.getQuality().toUpperCase() : "JUNK");
			it.setPrice(ci.getPrice());
			it.setRace(ci.getRacePermitted() != null ? Race.fromClient(ci.getRacePermitted()).toString() : "PC_ALL");
			if (!Strings.isNullOrEmpty(ci.getNoEnchant()) && !ci.getNoEnchant().equalsIgnoreCase("false"))
				it.setNoEnchant(ci.getNoEnchant().toLowerCase()); 																							// TODO : Remove no_enchant, use mask instead
			it.setRestrict(createRestrictString(ci));
			if (!createRestrictMaxString(ci).equals("0,0,0,0,0,0,0,0,0,0,0,0"))
				it.setRestrictMax(createRestrictMaxString(ci));
			
			it.setOptionSlotBonus((int) ci.getOptionSlotBonus()); 																								// TODO : remove ? (if not used) == mSlotsR
			
			it.setMaxStackCount(ci.getMaxStackCount() > 0 ? (int) ci.getMaxStackCount() : 1);
			// if (ci.getAttackGap() != 0f)
			it.setAttackGap(ci.getAttackGap());
			if (!Strings.isNullOrEmpty(ci.getAttackType()))
				it.setAttackType(ci.getAttackType().toUpperCase());
			
			// Manastone slots
			if (ci.getOptionSlotValue() != 0)
				it.setMSlots((int) ci.getOptionSlotValue());
			if (ci.getOptionSlotBonus() != 0)
				it.setMSlotsR((int) ci.getOptionSlotBonus());
			
			if (ci.getTemporaryExchangeTime() != 0)
				it.setTempExchangeTime((int) ci.getTemporaryExchangeTime());
			if (ci.getExpireTime() != 0)
				it.setExpireTime((int) ci.getExpireTime());
			// it.setRndBonus((int) 0); 																																		// TODO : client_item_random_bonuses.xml + ci.random_option_set
			if (ci.getCanDye() == 1)
				it.setDye("true"); 																																				// TODO : Remove, used in masks
			if (!Strings.isNullOrEmpty(ci.getActivateTarget()))
				it.setActivateTarget(ci.getActivateTarget().toUpperCase());
			if (ci.getActivationCount() != 0)
				it.setActivateCount((int) ci.getActivationCount());
			if (ci.getWeaponBoostValue() != 0)
				it.setWeaponBoost((int) ci.getWeaponBoostValue());
			it.setMask(getMask(ci));
			
			if (ci.getReturnWorldid() != 0)
				it.setReturnWorld(ci.getReturnWorldid());
			if (!Strings.isNullOrEmpty(ci.getReturnAlias()))
				it.setReturnAlias(ci.getReturnAlias().toUpperCase());
			
			// Modifiers -  [UPDATE] More attributes ? New Modifiers ?
			Modifiers mod = new Modifiers();
				rateList.clear(); addList.clear();
				// Standard Modifiers
				if (ci.getDodge() != 0) {addToModifierList(ci, (int) ci.getDodge(), "EVASION", "ADD", 0, false);}
				if (ci.getBlock() != 0) {addToModifierList(ci, (int) ci.getBlock(), "BLOCK", "ADD", 0, false);}
				if (ci.getMagicalResist() != 0) {addToModifierList(ci, (int) ci.getMagicalResist(), "MAGICAL_RESIST", "ADD", 0, false);}
				if (ci.getPhysicalDefend() != 0) {addToModifierList(ci, (int) ci.getPhysicalDefend(), "PHYSICAL_DEFENSE", "ADD", 0, false);}
				if (ci.getMagicalDefend() != 0) {addToModifierList(ci, (int) ci.getMagicalDefend(), "MAGICAL_DEFEND", "ADD", 0, false);}
				if (ci.getMagicalSkillBoostResist() != 0) {addToModifierList(ci, (int) ci.getMagicalSkillBoostResist(), "MAGIC_SKILL_BOOST_RESIST", "ADD", 0, false);}
				if (ci.getPhysicalCriticalReduceRate() != 0) {addToModifierList(ci, (int) ci.getPhysicalCriticalReduceRate(), "PHYSICAL_CRITICAL_RESIST", "ADD", 0, false);}
				if (ci.getMaxHp() != 0) {addToModifierList(ci, (int) ci.getMaxHp(), "MAXHP", "ADD", 0, false);}
				if (ci.getDamageReduce() != 0) {addToModifierList(ci, (int) ci.getDamageReduce(), "DAMAGE_REDUCE", "RATE", 0, false);}
				// Bonus Modifiers
				if (ci.getBonusAttr1() != null && ci.getBonusAttr1() != "") {computeAttributeBonus(ci, ci.getBonusAttr1(), 0);}
				if (ci.getBonusAttr2() != null && ci.getBonusAttr2() != "") {computeAttributeBonus(ci, ci.getBonusAttr2(), 0);}
				if (ci.getBonusAttr3() != null && ci.getBonusAttr3() != "") {computeAttributeBonus(ci, ci.getBonusAttr3(), 0);}
				if (ci.getBonusAttr4() != null && ci.getBonusAttr4() != "") {computeAttributeBonus(ci, ci.getBonusAttr4(), 0);}
				if (ci.getBonusAttr5() != null && ci.getBonusAttr5() != "") {computeAttributeBonus(ci, ci.getBonusAttr5(), 0);}
				if (ci.getBonusAttr6() != null && ci.getBonusAttr6() != "") {computeAttributeBonus(ci, ci.getBonusAttr6(), 0);}
				if (ci.getBonusAttr7() != null && ci.getBonusAttr7() != "") {computeAttributeBonus(ci, ci.getBonusAttr7(), 0);}
				if (ci.getBonusAttr8() != null && ci.getBonusAttr8() != "") {computeAttributeBonus(ci, ci.getBonusAttr8(), 0);}
				if (ci.getBonusAttr9() != null && ci.getBonusAttr9() != "") {computeAttributeBonus(ci, ci.getBonusAttr9(), 0);}
				if (ci.getBonusAttr10() != null && ci.getBonusAttr10() != "") {computeAttributeBonus(ci, ci.getBonusAttr10(), 0);}
				if (ci.getBonusAttr11() != null && ci.getBonusAttr11() != "") {computeAttributeBonus(ci, ci.getBonusAttr11(), 0);}
				if (ci.getBonusAttr12() != null && ci.getBonusAttr12() != "") {computeAttributeBonus(ci, ci.getBonusAttr12(), 0);}
				// Charge Modifiers
				if (ci.getBonusAttrA1() != null) { // [NOTICE] : ci.getChargeLevel is not always correct ...
					if (ci.getBonusAttrA1() != null && ci.getBonusAttrA1() != "") {computeAttributeBonus(ci, ci.getBonusAttrA1(), 1);}
					if (ci.getBonusAttrA2() != null && ci.getBonusAttrA2() != "") {computeAttributeBonus(ci, ci.getBonusAttrA2(), 1);}
					if (ci.getBonusAttrA3() != null && ci.getBonusAttrA3() != "") {computeAttributeBonus(ci, ci.getBonusAttrA3(), 1);}
					if (ci.getBonusAttrA4() != null && ci.getBonusAttrA4() != "") {computeAttributeBonus(ci, ci.getBonusAttrA4(), 1);}
				}
				if (ci.getBonusAttrB1() != null) { // [NOTICE] : ci.getChargeLevel is not always correct ...
					if (ci.getBonusAttrB1() != null && ci.getBonusAttrB1() != "") {computeAttributeBonus(ci, ci.getBonusAttrB1(), 2);}
					if (ci.getBonusAttrB2() != null && ci.getBonusAttrB2() != "") {computeAttributeBonus(ci, ci.getBonusAttrB2(), 2);}
					if (ci.getBonusAttrB3() != null && ci.getBonusAttrB3() != "") {computeAttributeBonus(ci, ci.getBonusAttrB3(), 2);}
					if (ci.getBonusAttrB4() != null && ci.getBonusAttrB4() != "") {computeAttributeBonus(ci, ci.getBonusAttrB4(), 2);}
				}
				// Manastones Modifiers
				if (ci.getStatEnchantType1() != null) {computeAttributeBonus(ci, ci.getStatEnchantType1() + " " + String.valueOf(ci.getStatEnchantValue1()), 0);}
				if (ci.getStatEnchantType2() != null) {computeAttributeBonus(ci, ci.getStatEnchantType2() + " " + String.valueOf(ci.getStatEnchantValue2()), 0);}
				
				for (Add add : addList)
					mod.getAdd().add(add);	
				for (Rate rate : rateList)
					mod.getRate().add(rate);	
			if (!mod.getAdd().isEmpty() || !mod.getRate().isEmpty()) {it.setModifiers(mod);}
			
			//WeaponList
			WeaponStats ws = new WeaponStats();
			boolean hasStats = false;
				if ((int) ci.getHitCount() != 0) {ws.setHitCount((int) ci.getHitCount()); hasStats = true;}
				if ((int) (ci.getAttackRange() * 1000) != 0) {ws.setAttackRange((int) (ci.getAttackRange() * 1000)); hasStats = true;}
				if ((int) ci.getParry() != 0) {ws.setParry((int) ci.getParry()); hasStats = true;}
				if ((int) ci.getCritical() != 0) {ws.setPhysicalCritical((int) ci.getCritical()); hasStats = true;}
				if ((int) ci.getAttackDelay() != 0) {ws.setAttackSpeed((int) ci.getAttackDelay()); hasStats = true;}
				if ((int) ci.getMaxDamage() != 0) {ws.setMaxDamage((int) ci.getMaxDamage()); hasStats = true;}
				if ((int) ci.getMinDamage() != 0) {ws.setMinDamage((int) ci.getMinDamage()); hasStats = true;}
				if ((int) ci.getMagicalHitAccuracy() != 0) {ws.setMagicalAccuracy((int) ci.getMagicalHitAccuracy()); hasStats = true;}
				if ((int) ci.getHitAccuracy() != 0) {ws.setPhysicalAccuracy((int) ci.getHitAccuracy()); hasStats = true;}
				if ((int) ci.getMagicalSkillBoost() != 0) {ws.setBoostMagicalSkill((int) ci.getMagicalSkillBoost()); hasStats = true;}
				if ((int) ci.getReduceMax() != 0) {ws.setReduceMax((int) ci.getReduceMax()); hasStats = true;}
			if (hasStats) {it.setWeaponStats(ws);}
			
			// TradeInList
			if (ci.getTradeInItemList() != null) {
				TradeInItemList clientTIL = ci.getTradeInItemList();
				TradeinList serverTIL = new TradeinList();
				boolean hasTradeInList = false;
				
				if (!clientTIL.getData().isEmpty()) {						
					for (Data data : clientTIL.getData()) {
						TradeinItem tii = new TradeinItem();
						if (data.getTradeInItem() != null) {
							int id = getItemId(data.getTradeInItem());
							if (id != 0) {
								tii.setId(id);
								hasTradeInList = true;
							}
							if (data.getTradeInItemCount() != null)
								tii.setPrice(data.getTradeInItemCount());
							else {
								tii.setPrice(1);
								System.out.println("[ITEMS][WARN] Value of 1 set by defaul for TradeinItem :" + id);
							}
							serverTIL.getTradeinItem().add(tii);
						}
					}
					if (hasTradeInList)
						it.setTradeinList(serverTIL);		
				}
			}
			
			// Acquisition
			if (ci.getAbyssItem() != null) {addAcquisition(it, "ABYSS", ci.getAbyssItemCount(), getItemId(ci.getAbyssItem()), ci.getAbyssPoint());}
			if (ci.getExtraCurrencyItem() != null) {addAcquisition(it, "REWARD", ci.getExtraCurrencyItemCount(), getItemId(ci.getExtraCurrencyItem()), ci.getAbyssPoint());}
			if (ci.getCouponItem() != null) {addAcquisition(it, "COUPON", ci.getCouponItemCount(), getItemId(ci.getCouponItem()), ci.getAbyssPoint());}
			if (ci.getAbyssPoint() > 0 && it.getAcquisition() == null) {addAcquisition(it, "AP", 0, 0, ci.getAbyssPoint());}
			
			// Improve (Conditionning)
			if (ci.getChargeWay() != 0) {
				Improve cond = new Improve();
				cond.setWay((int) ci.getChargeWay());
				if (ci.getChargeLevel() != 0)
					cond.setLevel((int) ci.getChargeLevel());
				if (ci.getChargePrice1() != 0.0f)
					cond.setPrice1(Math.round(ci.getChargePrice1() * 1000000));
				if (ci.getChargePrice2() != 0.0f)
					cond.setPrice2(Math.round(ci.getChargePrice1() * 1000000));
				if (ci.getBurnOnAttack() != 0)
					cond.setBurnAttack((int) ci.getBurnOnAttack());
				if (ci.getBurnOnDefend() != 0)
					cond.setBurnDefend((int) ci.getBurnOnDefend());
				it.setImprove(cond);
			}
			else if (ci.getChargeLevel() != 0)
				System.out.println("[ITEMS] Item : " + ci.getId() + " has 0 ChargeWay but has a positive ChargeLevel");
			
			// Use Limits
			boolean hasLimits = false;
			Uselimits limit = new Uselimits();
			if (ci.getAreaToUse() != null) {limit.setUsearea(ci.getAreaToUse().toUpperCase()); hasLimits = true;} // Use area
			if (ci.getOwnershipWorld() != null) {limit.setOwnershipWorld(getWorldId(ci.getOwnershipWorld())); hasLimits = true;} // Use world
			if ((int) ci.getUsableRankMin() > 0) {limit.setRankMin((int) ci.getUsableRankMin()); hasLimits = true;} // Use min rank
			if ((int) ci.getUsableRankMax() > 0) {limit.setRankMax((int) ci.getUsableRankMax()); hasLimits = true;} // Use max rank
			if (ci.getGenderPermitted() != null && !ci.getGenderPermitted().equalsIgnoreCase("all")) {limit.setGender(ci.getGenderPermitted().toUpperCase()); hasLimits = true;} // Use gendre
			if (ci.getUseDelay() != 0) {limit.setUsedelay((int) ci.getUseDelay()); hasLimits = true;} // Use delay
			if (ci.getUseDelayTypeId() != 0) {limit.setUsedelayid((int) ci.getUseDelayTypeId()); hasLimits = true;} // Use delay ID
			if (ci.getRideUsable() != 0) {limit.setUsableRide((int) ci.getRideUsable()); hasLimits = true;} // Use ride 																		TODO : Use

			if (hasLimits)
				it.setUselimits(limit);
			
			// Stigma
			if (ci.getGainSkill1() != null) {
				Stigma stigma = new Stigma();
				int skill1 = 0; int skill2 = 0;
				String skill = "";
				
				skill1 = getSkillId(ci.getGainSkill1());
				if (ci.getGainSkill2() != null)
					skill2 = getSkillId(ci.getGainSkill2());
				
				
				if (skill1 !=0)
					skill = ci.getGainLevel1() + ":" + skill1;
				if (skill2 != 0)
					skill = skill + " " + ci.getGainLevel2() + ":" + skill2;
				
				stigma.setSkill(skill);
				stigma.setShard((int) ci.getRequireShard());
				if (ci.getRequireSkill1() != null)
						addRequireSkillToStigma(stigma, ci.getRequireSkill1());
				if (ci.getRequireSkill2() != null)
						addRequireSkillToStigma(stigma, ci.getRequireSkill2());
					
				it.setStigma(stigma);
			}
			
			/* 																																																													TODO : Add & Implement
			if (ci.getDisposableTradeItem() != null) {
			Disposition disp = new Disposition();
			ci item = itemNameMap.get(ci.getDisposableTradeItem().toLowerCase());
			disp.setId(item.getId());
			if (ci.getDisposableTradeItemCount() == null) {
				disp.setCount(1);
				System.out.println("Missing count for disposition: " + ci.getId());
			}
			else
				disp.setCount(ci.getDisposableTradeItemCount()); 
			it.setDisposition(disp);
		}

		if (ci.getExtraInventory() != null) {
			ExtraInventory extra = new ExtraInventory();
			extra.setId(ci.getExtraInventory());
			it.setInventory(extra);
		}
		*/
			
			// Actions
			Actions actionList = new Actions();
			boolean hasActions = false;
				// Decompose
				if (ci.getDisassemblyItem() == 1) {
					actionList.setDecompose("");
					hasActions = true;
				}
				// Learn Recipe (CraftLearn)
				if (ci.getCraftRecipeInfo() != null) {
					Craftlearn recipeAction = new Craftlearn();
					
					int recipeId = new AionDataCenter().getInstance().getRecipeIdByName(ci.getCraftRecipeInfo());
					if (recipeId != 0) 
						recipeAction.setRecipeid(recipeId);
					
					actionList.setCraftlearn(recipeAction);
					hasActions = true;
				}
				// Use Skill
				if (ci.getActivationSkill() != null) {
					int skill = getSkillId(ci.getActivationSkill());
					if (skill != 0) {
						Skilluse sa = new Skilluse();
						
						sa.setSkillid(skill);
						sa.setLevel((int) ci.getActivationLevel());
						
						// Teleport Scrolls [UPDATE] New Active Target types ?
						if (ci.getActivateTarget() != null && ci.getActivateTarget().equalsIgnoreCase("mymento")) {
							String suffix = ci.getName().toLowerCase().replace("item_recallaid_", "");
							int undeline = suffix.indexOf('_');
							if (undeline != -1)
								suffix = suffix.substring(0, undeline);
							
							int worldId = getWorldId(suffix);
							if (worldId != 0)
								sa.setWorldId(worldId); 																																																	//TODO : Use !!!
						}
						actionList.setSkilluse(sa);
						hasActions = true;
					}
				}
				// Firework
				if (it.getCategory().equalsIgnoreCase("FIRECRACKER")) {
					actionList.setFireworkact("");
					hasActions = true;
				}
				// Extract
				if (it.getCategory().equalsIgnoreCase("EXTRACTOR")) {
					actionList.setExtract("");
					hasActions = true;
				}
				// Enchant
				Enchant e = new Enchant();
					boolean hasEnchant = false;
					if (ci.getTargetItemLevelMax() > 0) {e.setMaxLevel((int) ci.getTargetItemLevelMax()); hasEnchant = true;}
					if (ci.getTargetItemLevelMin() > 0) {e.setMinLevel((int) ci.getTargetItemLevelMin()); hasEnchant = true;}
					if (ci.getSubEnchantMaterialMany() > 0) {e.setCount((int) ci.getSubEnchantMaterialMany()); hasEnchant = true;}
					if (ci.getSubEnchantMaterialEffect() > 0) {e.setChance((int) ci.getSubEnchantMaterialEffect() / 10f); hasEnchant = true;}
					if (hasEnchant && ci.getSubEnchantMaterialEffectType() != null && ci.getSubEnchantMaterialEffectType().equalsIgnoreCase("cash_option_prob")) {e.setManastoneOnly("true");}
					if (hasEnchant) {
						actionList.setEnchant(e);
						hasActions = true;
					}
				// Charge
				if (ci.getChargeCapacity() > 0) {
					Charge charge = new Charge();
					charge.setCapacity((int) ci.getChargeCapacity());
					actionList.setCharge(charge);
					hasActions = true;
				}
				// Dye
				if (ci.getDyeingColor() != null || it.getCategory().equalsIgnoreCase("DYE_REMOVER") || it.getCategory().equalsIgnoreCase("PAINT_REMOVER")) {
					Dye dye = new Dye();
					if (it.getCategory().equalsIgnoreCase("DYE_REMOVER") || it.getCategory().equalsIgnoreCase("PAINT_REMOVER")) {
						dye.setColor("no");
					}
					else {
						String[] rgbParts = ci.getDyeingColor().split(",");
						Color c = new Color(Integer.parseInt(rgbParts[0]), Integer.parseInt(rgbParts[1]), Integer.parseInt(rgbParts[2]));
						String color = Integer.toHexString(c.getRGB());
						String rgb = color.substring(2, color.length());

						dye.setColor(rgb);
					}
					if ((int) ci.getCashAvailableMinute() > 0) 
						dye.setMinutes(ci.getCashAvailableMinute()); 																									//TODO : Use

					actionList.setDye(dye);
					hasActions = true;
				}
				// Learn Skill
				if (ci.getSkillToLearn() != null) {
					
					int skillId = getSkillId(ci.getSkillToLearn());	
					
					if (skillId != 0) {
						List<ClientSkillTree> skillsToLearn = getSkillTrees(ci.getSkillToLearn().toUpperCase());
																			
						if (skillsToLearn.isEmpty()) {
							if (skillId == 2670 || skillId == 2671) {
								Skilllearn sla = new Skilllearn();
								sla.setSkillid(skillId);
								sla.setLevel(21);
								sla.setClazz(PlayerClass.ALL.toString());
								actionList.getSkilllearn().add(sla);
								hasActions = true;
							}
							else
								System.out.println("[ITEMS] No SkillTree equivalent found for : " + ci.getSkillToLearn().toUpperCase());
						}
						else {
							for (ClientSkillTree csl : skillsToLearn) {
								Skilllearn sla = new Skilllearn();
								sla.setSkillid(skillId);
								sla.setLevel((int) csl.getPcLevel());
								sla.setClazz(PlayerClass.fromClient(csl.getClazz()).toString());
								actionList.getSkilllearn().add(sla);
								hasActions = true;
							}
						}
					}
				}
				// Learn Emotion
				if (ci.getCashSocial() != 0) {
					Learnemotion lea = new Learnemotion();
					lea.setEmotionid((int) ci.getCashSocial());
					if (ci.getCashAvailableMinute() > 0)
						lea.setMinutes((int) ci.getCashAvailableMinute());
					actionList.setLearnemotion(lea);
					hasActions = true;
				}
				// Add Title (Title Cards)
				if (ci.getCashTitle() != 0) {
					Titleadd ta = new Titleadd();
					ta.setTitleid((int) ci.getCashTitle());
					if (ci.getCashAvailableMinute() > 0)
						ta.setMinutes((int) ci.getCashAvailableMinute());
					actionList.setTitleadd(ta);
					hasActions = true;
				}
				// Expand Inventory
				if (ci.getInvenWarehouseMaxExtendlevel() != 0) {
					Expandinventory ea = new Expandinventory();
					ea.setLevel((int) ci.getInvenWarehouseMaxExtendlevel());
					if (ci.getName().toUpperCase().contains("CUBE"))
						ea.setStorage("CUBE");
					else if (ci.getName().toUpperCase().contains("WAREHOUSE"))
						ea.setStorage("WAREHOUSE");
					else
						System.out.println("[ITEMS] Unknown Expand Storage item : " + ci.getName());
					actionList.setExpandinventory(ea);
					hasActions = true;
				}
				// Cosmetic
				if (ci.getCosmeticName() != null) {
					Cosmetic ca = new Cosmetic();
					ca.setName(ci.getCosmeticName().toLowerCase());
					actionList.setCosmetic(ca);
					hasActions = true;
				}
				// Spawn House Object
				if (ci.getSummonHousingObject() != null) {
					Houseobject ho = new Houseobject();
					ho.setId(new AionDataCenter().getInstance().getHousingObjectIdByName(ci.getSummonHousingObject()));
					actionList.setHouseobject(ho);
					hasActions = true;
				}
				// Spawn House Decoration
				if (ci.getCustomPartName() != null) {
					Housedeco hd = new Housedeco();
					hd.setId(new AionDataCenter().getInstance().getHousingPartIdByName(ci.getCustomPartName()));
					actionList.setHousedeco(hd);
					hasActions = true;
				}
				// Quest Start 
				if (it.getCategory().equalsIgnoreCase("QUEST_START_ITEM")) {
					Queststart qs = new Queststart();
					
					// Extracting the quest ID
					String name = ci.getName().toUpperCase().trim();
					name = name.replace("DOC_QUEST_", "");
					name = name.replace("QUEST_", "");
					name = name.subSequence(0, name.length() - 1).toString();
					try {
						qs.setQuestid(Integer.parseInt(name));
					} catch (Exception ex) {
						System.out.println(ex.getMessage() + " " + it.getId());
					}
					
					if (qs.getQuestid() != null && qs.getQuestid() != 0) {
						actionList.setQueststart(qs);
						hasActions = true;
					}
					/*
					if (ci.getDescLong() != null) {
						String desc = new AionDataCenter().getInstance().getMatchingStringText(ci.getDescLong()).toUpperCase();
						
						if (desc.contains("Double-click to begin a quest".toUpperCase()) || desc.contains("더블 클릭하여 퀘스트를 받을 수 있습니다".toUpperCase()))
							System.out.println("[ITEMS] : Item " + it.getId() + " starts a quest and has <quest> value of : " + ci.getQuest());
					}*/
				}
				// Read - [NOTICE] Using "Double-click to read" from strings get the same results
				if (ci.getDocBg() != null) {
					if (docFiles.isEmpty()) {loadDocFiles();}
					if (docFiles.contains(ci.getName().toUpperCase())) {
						actionList.setRead("");
						hasActions = true;
					}
					/*
					if (ci.getDescLong() != null) {
						String desc = new AionDataCenter().getInstance().getMatchingStringText(ci.getDescLong()).toUpperCase();
						
						if (it.getActions() != null && it.getActions().getRead() == null && (desc.contains("Double-click to read".toUpperCase()) || desc.contains("더블 클릭하면 읽을 수 있습니다".toUpperCase())))
							System.out.println("[ITEMS] : Item " + it.getId() + " should be readable !");
						if (it.getActions() != null && it.getActions().getRead() != null && (!desc.contains("Double-click to read".toUpperCase()) || !desc.contains("더블 클릭하면 읽을 수 있습니다".toUpperCase())))
							System.out.println("[ITEMS] : Item " + it.getId() + " should not be readable !");
					}*/
				}
				// ToyPet spawn
				if (ci.getToyPetName() != null) {
					int npcId = getNpcId(ci.getToyPetName());
					if (npcId != 0) {
						Toypetspawn toy = new Toypetspawn();
						toy.setNpcid(npcId);
						actionList.setToypetspawn(toy);
						hasActions = true;							
					}
				}
				// FuncPet																																																			TODO : Use
				if (ci.getFuncPetName() != null) {
					int toyId = new AionDataCenter().getInstance().getToyPetIdByName(ci.getFuncPetName());
					if (toyId != 0) {
						Toypetadopt adopt = new Toypetadopt();
						adopt.setPetid(toyId);
						if (ci.getFuncPetDurMinute() != 0)
							adopt.setMinutes((int) ci.getFuncPetDurMinute());
						// if (ci.getFuncPetName().indexOf("entertainer") != -1)
							// adopt.setSidekick(true);
						actionList.setToypetadopt(adopt);
						hasActions = true;
					}
				}
				// Assemble
				if (ci.getAssemblyItem() != null) {
					Assemble avengers = new Assemble();
					avengers.setItem(getItemId(ci.getAssemblyItem()));
					actionList.setAssemble(avengers);
					hasActions = true;	
				}
				// Learn Animation
				if (ci.getCustomIdleAnim() != null || ci.getCustomJumpAnim() != null || ci.getCustomRestAnim() != null || ci.getCustomRunAnim() != null) {
					Animation anim = new Animation();
					if (ci.getCustomIdleAnim() != null) {anim.setIdle(getAnimationId(ci.getCustomIdleAnim()));}
					if (ci.getCustomJumpAnim() != null) {anim.setJump(getAnimationId(ci.getCustomJumpAnim()));}
					if (ci.getCustomRestAnim() != null) {anim.setRest(getAnimationId(ci.getCustomRestAnim()));}
					if (ci.getCustomRunAnim() != null) {anim.setRun(getAnimationId(ci.getCustomRunAnim()));}
					if (ci.getAnimExpireTime() > 0) {anim.setMinutes((int) ci.getAnimExpireTime());}
					actionList.setAnimation(anim);
					hasActions = true;	
				}
				// Instance Time clear
				if (ci.getResetInstanceCooltSyncId() != null && ci.getResetInstanceCooltSyncId() != "") {
					if (cooltimeSyncMap.isEmpty()) {loadCooltimeSyncMap();}
					Instancetimeclear itc = new Instancetimeclear();
					List<Integer> mapIdList = new ArrayList<Integer>();
					String[] coolt_sync_ids = ci.getResetInstanceCooltSyncId().trim().split(",");
					for (int i = 0; i < coolt_sync_ids.length; i++)
						mapIdList.add(getWorldId(cooltimeSyncMap.get(Integer.parseInt(coolt_sync_ids[i].trim()))));																											// TODO : Use
					if (!mapIdList.isEmpty() && mapIdList != null)
						itc.getMapid().addAll(mapIdList);
					actionList.setInstancetimeclear(itc);
					hasActions = true;	
				}
				// Ride
				if (ci.getRideDataName() != null) {
					Ride rideAction = new Ride();
					rideAction.setNpcId(new AionDataCenter().getInstance().getRideIdByName(ci.getRideDataName()));
					actionList.setRide(rideAction);
					hasActions = true;
				}
			
			if (hasActions)
				it.setActions(actionList);
				
			// Godstone
			if (ci.getProcEnchantSkill() != null) {
				Godstone gd = new Godstone();

				int skillId = getSkillId(ci.getProcEnchantSkill());
				if (skillId != 0) {
					gd.setSkillid(skillId);
					gd.setSkilllvl((int) ci.getProcEnchantSkillLevel());
					gd.setProbability((int) ci.getProcEnchantEffectOccurProb());
					gd.setProbabilityleft((int) ci.getProcEnchantEffectOccurLeftProb());
					it.setGodstone(gd);
				} else
					System.out.println("[ITEMS][WARN] No skill for godstone : " + skillId);
			}
			
			// END
			templateList.add(it);
		}
	}

	@Override
	public void marshall() {
		addAionOrder(AionWritingConfig.ITEMS, AionWritingConfig.ITEMS_BINDINGS, finalTemplates);
		FileMarhshaller.marshallFile(orders);
		System.out.println("[ITEMS] Items count: " + templateList.size());
	}
	
	/****** EXTRA *******/
	
	private int getItemId(String s) {return (s != null) ? new AionDataCenter().getInstance().getItemIdByName(s) : 0;}
	private int getSkillId(String s) {return (s != null) ? new AionDataCenter().getInstance().getSkillIdByName(s) : 0;}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	private int getWorldId(String s) {return (new AionDataCenter().getInstance().getWorld(s) != null) ? new AionDataCenter().getInstance().getWorld(s).getId() : 0;}
	private int getAnimationId(String s) {return (s != null) ? new AionDataCenter().getInstance().getAnimationIdByName(s) : 0;}
	
	// TODO : getGenderPermitted getCanPolish getCannotChangeskin (to property)
	private int getMask(ClientItem ci) {
		int mask = 0;
		if (ci.getLore() != null && ci.getLore().equalsIgnoreCase("true"))
			mask |= 1;
		if (ci.getCanExchange() != null && ci.getCanExchange().equalsIgnoreCase("true"))
			mask |= 2;
		if (ci.getCanSellToNpc() != null && ci.getCanSellToNpc().equalsIgnoreCase("true"))
			mask |= 4;
		if (ci.getCanDepositToCharacterWarehouse() != null && ci.getCanDepositToCharacterWarehouse().equalsIgnoreCase("true"))
			mask |= 8;
		if (ci.getCanDepositToAccountWarehouse() != null && ci.getCanDepositToAccountWarehouse().equalsIgnoreCase("true"))
			mask |= 16;
		if (ci.getCanDepositToGuildWarehouse() != null && ci.getCanDepositToGuildWarehouse().equalsIgnoreCase("true"))
			mask |= 32;
		if (ci.getBreakable() != null && ci.getBreakable().equalsIgnoreCase("true"))
			mask |= 64;
		if (ci.getSoulBind() != null && ci.getSoulBind().equalsIgnoreCase("true"))
			mask |= 128;
		if (ci.getRemoveWhenLogout() != null && ci.getRemoveWhenLogout().equalsIgnoreCase("true"))
			mask |= 256;
		if (ci.getNoEnchant() != null && ci.getNoEnchant().equalsIgnoreCase("true"))
			mask |= 512;
		if (ci.getCanProcEnchant() != null && ci.getCanProcEnchant().equalsIgnoreCase("true"))
			mask |= 1024;
		if (ci.getCanCompositeWeapon() != null && ci.getCanCompositeWeapon().equalsIgnoreCase("true"))
			mask |= 2048;
		if (ci.getCannotChangeskin() == 0)
			mask |= 4096;
		if (ci.getCanSplit() != null && ci.getCanSplit().equalsIgnoreCase("true"))
			mask |= 8192;
		if (ci.getItemDropPermitted() != null && ci.getItemDropPermitted().equalsIgnoreCase("true"))
			mask |= 16384;
		if (ci.getCanDye()  > 0)
			mask |= 32768;
		if (ci.getCanApExtraction() != null && ci.getCanApExtraction().equalsIgnoreCase("true")) 																																			// TODO : Use
			mask |= 65536;
		return mask;
	}
	
	//TODO : Add new classes when they are implemented
	private String createRestrictString(ClientItem ci) {
		String[] restricts = new String[12];
		restricts[0] = Byte.toString(ci.getWarrior());
		restricts[1] = Byte.toString(ci.getFighter());
		restricts[2] = Byte.toString(ci.getKnight());
		restricts[3] = Byte.toString(ci.getScout());
		restricts[4] = Byte.toString(ci.getAssassin());
		restricts[5] = Byte.toString(ci.getRanger());
		restricts[6] = Byte.toString(ci.getMage());
		restricts[7] = Byte.toString(ci.getWizard());
		restricts[8] = Byte.toString(ci.getElementalist());
		restricts[9] = Byte.toString(ci.getCleric());
		restricts[10] = Byte.toString(ci.getPriest());
		restricts[11] = Byte.toString(ci.getChanter());
		for (int i = 0; i <= 11; i++) 
			if (Integer.parseInt(restricts[i]) > AionProperties.MAX_LEVEL)
				restricts[i] = String.valueOf(AionProperties.MAX_LEVEL);
		return Joiner.on(",").join(restricts);
	}
	
	private String createRestrictMaxString(ClientItem ci) {
		String[] restricts_max = new String[12];
		restricts_max[0] = Byte.toString(ci.getWarriorMax());
		restricts_max[1] = Byte.toString(ci.getFighterMax());
		restricts_max[2] = Byte.toString(ci.getKnightMax());
		restricts_max[3] = Byte.toString(ci.getScoutMax());
		restricts_max[4] = Byte.toString(ci.getAssassinMax());
		restricts_max[5] = Byte.toString(ci.getRangerMax());
		restricts_max[6] = Byte.toString(ci.getMageMax());
		restricts_max[7] = Byte.toString(ci.getWizardMax());
		restricts_max[8] = Byte.toString(ci.getElementalistMax());
		restricts_max[9] = Byte.toString(ci.getClericMax());
		restricts_max[10] = Byte.toString(ci.getPriestMax());
		restricts_max[11] = Byte.toString(ci.getChanterMax());
		return Joiner.on(",").join(restricts_max);
	}
	
	private void computeAttributeBonus(ClientItem ci, String clientBonus, int chargeLevel) {
		String type = ""; String attribute = "";
		int value = 0;
		
		// Finding out type
		if (clientBonus.endsWith("%"))
			type = "RATE";
		else
			type = "ADD";
		
		// Splitting
		clientBonus = clientBonus.trim().replaceAll("  ", " ").replaceAll("- ", "-").replace("`", "").replace("%", "").trim();
		String[] bonus = clientBonus.trim().split(" ");
		if (bonus[0].contains(" ")) {
			String[] bonus2 = bonus[0].trim().split(" ");
			bonus[0] = bonus2[0].trim();
			bonus[1] = bonus2[1].trim();
		}
		
		// Finding out attribute
		if (!Strings.isNullOrEmpty(bonus[0])) {
			if (StatModifiers.fromClient(bonus[0]) != StatModifiers.NONE)
				attribute = StatModifiers.fromClient(bonus[0]).toString();
			if (attribute == "") 
				System.out.println("[ITEMS] No Attribute for :" + bonus[0].toUpperCase());
		}
		
		// Finding out value
		if (bonus.length >= 2)
			value = Integer.parseInt(bonus[1]);
		
		if (value > 0 && attribute.equalsIgnoreCase("ATTACK_SPEED"))
			value = -1 * value;
		
		if (attribute != "" && type != "")
			addToModifierList(ci, value, attribute, type, chargeLevel, true);
	}
	
	private void addToModifierList(ClientItem ci, int value, String attribute, String type, int chargeLevel, boolean isBonus) {
	
		Conditions cond = new Conditions();
		if (chargeLevel > 0) {
			Charge charge = new Charge();
				
			charge.setLevel(chargeLevel);
				
			cond.setCharge(charge);
		}
		
		if (type == "ADD") {
			Add add = new Add();
			
			add.setName(attribute);
			add.setAddValue(value);
			if (isBonus) {add.setBonus("true");}
			if (chargeLevel > 0) {add.setConditions(cond);}
			
			addList.add(add);
		}
		else if (type == "RATE") {
			Rate rate = new Rate();
			
			rate.setName(attribute);
			rate.setRateValue(value);
			if (isBonus) {rate.setBonus("true");}
			if (chargeLevel > 0) {rate.setConditions(cond);}
			
			rateList.add(rate);
		}
		else
			System.out.println("[ITEMS] Error, modifier type : " + type  + " is incorrect !");
	}
	
	private void addAcquisition(ItemTemplate it, String type, int count, int itemId, int ap) {
		Acquisition acq = new Acquisition();
		
		acq.setType(type);
		if (count > 0) {
			acq.setCount(count);
			acq.setItem(itemId);
		}
		if (ap > 0)
			acq.setAp(ap);
		
		it.setAcquisition(acq);
	}
	
	// Finds all skills containing that name
	private static void addRequireSkillToStigma(Stigma stigma, String skillString) {
		RequireSkill requireSkill = new RequireSkill();
		List<Integer> skills = new ArrayList<Integer>();
		
		skills = new AionDataCenter().getInstance().getAllSkillsContaining(skillString);
		requireSkill.getSkillId().addAll(skills);
		
		stigma.getRequireSkill().add(requireSkill);
	}
	
	private static void loadCooltimeSyncMap() {
		List<ClientInstanceCooltime> cics = new ArrayList<ClientInstanceCooltime>();
		cics = new AionCooltimesParser().parse();
		for (ClientInstanceCooltime cic : cics) {
			if (cic != null)
				cooltimeSyncMap.put((int) cic.getCooltSyncId(), cic.getName());
		}
	}
	
	private static void loadDocFiles() {
		File docFilesFolder = new File(AionReadingConfig.DOC_FILES);
		File[] children = docFilesFolder.listFiles();
		for (int i = 0; i < children.length; i++) {
			if (children[i].isDirectory())
				continue;
			docFiles.add(children[i].getName().substring(0, children[i].getName().lastIndexOf(".")).toUpperCase());
		}
	}
	
	private List<ClientSkillTree> getSkillTrees(String skill) {
		List<ClientSkillTree> temp = new ArrayList<ClientSkillTree>();
		List<ClientSkillTree> results = new ArrayList<ClientSkillTree>();
		ClientSkillTree skillTree = null;
		List<String> usedClass = new ArrayList<String>();
		
		for (ClientSkillTree cst : skillTreeList) {
			if (cst.getSkill().equalsIgnoreCase(skill))
				temp.add(cst);
		}
		
		for (ClientSkillTree cst : temp) {
			if (usedClass.contains(cst.getClazz())) {
				if (results.contains(cst)) {
					skillTree = results.get(results.indexOf(cst));
					if (cst.getPcLevel() < skillTree.getPcLevel()) {
						results.remove(skillTree);
						results.add(cst);
					}
				}
			}
			else {
				usedClass.add(cst.getClazz());
				results.add(cst);
			}			
		}
		
		return results;
	}
}