package com.parser.write.aion.skills;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import javolution.util.FastMap;
import org.apache.commons.lang.StringUtils;

import com.parser.input.aion.skills.ClientSkill;
import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.skill_learn.ClientSkillTree;

import com.parser.common.JAXBHandler;
import com.parser.common.aion.*;
import com.parser.common.aion.enums.*;
import com.parser.common.aion.enums.items.*;
import com.parser.common.aion.enums.skills.*;
import com.parser.read.aion.skills.AionSkillsParser;
import com.parser.read.aion.skills.AionSkillTreeParser;
import com.parser.read.aion.items.AionItemsParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.skills.*;

/**
 *@author Viria
 */
public class AionSkillsWriter extends AbstractWriter {

	public static boolean ANALYSE = false;

	SkillData finalTemplates = new SkillData();
	Collection<SkillTemplate> templateList = finalTemplates.getSkillTemplate();
	
	List<ClientSkill> skillBaseList = null;
	List<ClientSkillTree> skillTreeList = null;
	Map<String, ClientItem> stigmaItemMap = new HashMap<String, ClientItem>();
	
	public AionSkillsWriter(boolean analyse) {
		this.ANALYSE = analyse;
	}
	
	@Override
	public void parse() {
		skillBaseList = new AionDataCenter().getInstance().getClientSkills();
		skillTreeList = new AionDataCenter().getInstance().getClientSkillTree();
		for (ClientItem ci : new AionDataCenter().getInstance().getClientItems()) {
			if (!Strings.isNullOrEmpty(ci.getGainSkill1()))
				stigmaItemMap.put(ci.getGainSkill1().toUpperCase(), ci);
		}
		System.out.println("[SKILLS] Loaded " + stigmaItemMap.size() + " Stigma Skill-Item pairs");
	}

	@Override
	public void transform() {
		for (ClientSkill cs : skillBaseList) {
			SkillTemplate st = new SkillTemplate();
			
			/***************** START **********************/
			
			/* Attributes */
			if (JAXBHandler.getValue(cs, "counter_skill") != null)
				st.setCounterSkill(JAXBHandler.getValue(cs, "counter_skill").toString());
			if (JAXBHandler.getValue(cs, "penalty_skill_succ") != null)
				st.setPenaltySkillId((Integer) findSkill("id", "name", JAXBHandler.getValue(cs, "penalty_skill_succ")));
			
			st.setSkillId((Integer) JAXBHandler.getValue(cs, "id"));
			if (JAXBHandler.getValue(cs, "desc") != null) {
				String desc = JAXBHandler.getValue(cs, "desc").toString().trim();
				st.setName(getName(desc));
				st.setNameId(getNameId(desc, 2, 1));
				st.setLvl(getSkillLevel(cs));
				st.setStack(getStack(cs));
			}
			
			if (JAXBHandler.getValue(cs, "delay_id") != 0)
				st.setCooldownId((Integer) JAXBHandler.getValue(cs, "delay_id"));
			st.setSkilltype(JAXBHandler.getValue(cs, "type") == null ? "NONE" : SkillType.fromClient(JAXBHandler.getValue(cs, "type").toString()).toString());
			st.setSkillsubtype(JAXBHandler.getValue(cs, "sub_type") == null ? "NONE" : SkillSubType.fromClient(JAXBHandler.getValue(cs, "sub_type").toString()).toString());
			if (JAXBHandler.getValue(cs, "conflict_id") != 0)
				st.setConflictId((Integer) JAXBHandler.getValue(cs, "conflict_id"));
			if (JAXBHandler.getValue(cs, "target_slot") != null) {
				TargetSlot ts = TargetSlot.fromClient(JAXBHandler.getValue(cs, "target_slot").toString());
				if (ts != TargetSlot.NONE)
					st.setTslot(ts.toString());
			}
			if (JAXBHandler.getValue(cs, "target_slot_level") != 0)
				st.setTslotLevel((Integer) JAXBHandler.getValue(cs, "target_slot_level"));
			if (JAXBHandler.getValue(cs, "dispel_category") != null && DispelCategoryType.fromClient(JAXBHandler.getValue(cs, "dispel_category").toString()) != null)
				st.setDispelCategory(DispelCategoryType.fromClient(JAXBHandler.getValue(cs, "dispel_category").toString()).toString());
			if (JAXBHandler.getValue(cs, "required_dispel_level") != 0)
				st.setReqDispelLevel((Integer) JAXBHandler.getValue(cs, "required_dispel_level"));
			if (JAXBHandler.getValue(cs, "activation_attribute") != null)
				st.setActivation(ActivationAttribute.fromClient(JAXBHandler.getValue(cs, "activation_attribute").toString()).toString());
			
			if (getStigmaType(cs) != null)
				st.setStigma(getStigmaType(cs).toString());
			if (JAXBHandler.getValue(cs, "ammo_speed") != 0)
				st.setAmmospeed((Integer) JAXBHandler.getValue(cs, "ammo_speed"));
			if (JAXBHandler.getValue(cs, "casting_delay") != null)
				st.setDuration((Integer) JAXBHandler.getValue(cs, "casting_delay"));
			st.setCooldown(JAXBHandler.getValue(cs, "delay_time") != null ? (int)((Integer) JAXBHandler.getValue(cs, "delay_time") / 100f) : 0);
			if (JAXBHandler.getValue(cs, "pvp_damage_ratio") != 0)
				st.setPvpDamage((Integer) JAXBHandler.getValue(cs, "pvp_damage_ratio"));
			if (JAXBHandler.getValue(cs, "pvp_remain_time_ratio") != 0)
				st.setPvpDuration((Integer) JAXBHandler.getValue(cs, "pvp_remain_time_ratio"));
			if (JAXBHandler.getValue(cs, "chain_skill_prob2") != 0)
				st.setChainSkillProb((Integer) JAXBHandler.getValue(cs, "chain_skill_prob2"));
			if (JAXBHandler.getValue(cs, "cancel_rate") != 0)
				st.setCancelRate((Integer) JAXBHandler.getValue(cs, "cancel_rate"));
			if (JAXBHandler.getValue(cs, "change_stance") != null && !JAXBHandler.getValue(cs, "change_stance").toString().equalsIgnoreCase("NONE"))
				st.setStance(true);
			// if (cs.get() != null)
				// st.setSkillsetException(cs.get());
			// if (cs.get() != null)
				// st.setSkillsetMaxoccur(cs.get());
			if (JAXBHandler.getValue(cs, "desc") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "desc").toString(), "_avatar_") 
				&& JAXBHandler.getValue(cs, "sub_type") != null && JAXBHandler.getValue(cs, "sub_type").toString().equalsIgnoreCase("buff"))
				st.setAvatar(true);
			if (JAXBHandler.getValue(cs, "target_flying_restriction") != null && JAXBHandler.getValue(cs, "target_flying_restriction").toString().equalsIgnoreCase("ground"))
				st.setGround(true);
			if (isUnpottable(cs))
				st.setUnpottable(true);
			if (JAXBHandler.getValue(cs, "remove_at_fly_end") == 1)
				st.setRemoveFlyend(true);
			if (JAXBHandler.getValue(cs, "remove_at_die") == 1)
				st.setNoremoveatdie(true);
			
			/* Elements */
			
			// Properties
			Properties prop = new Properties();
				boolean hasProperties = false;
				// Add the weapon range to the distances calculation
				if (JAXBHandler.getValue(cs, "add_wpn_range") != 0 && JAXBHandler.getValue(cs, "activation_attribute") != null && !JAXBHandler.getValue(cs, "activation_attribute").toString().equalsIgnoreCase("Passive")) {
					prop.setAwr(true);
					hasProperties = true;
				}
				// Max target that can be hit (for AoE)
				if (JAXBHandler.getValue(cs, "target_maxcount") != 0) {
					prop.setTargetMaxcount((Integer) JAXBHandler.getValue(cs, "target_maxcount")); 
					hasProperties = true;
				}
				// Type of targets affected by the skill (for AoE) : party, area ...
				if (JAXBHandler.getValue(cs, "target_range") != null) {
					prop.setTargetType(TargetRangeAttribute.fromClient(JAXBHandler.getValue(cs, "target_range").toString()).toString());
					hasProperties = true;
				}
				// Relation between the caster and the first target : ENEMY, FRIEND ...
				if (JAXBHandler.getValue(cs, "target_relation_restriction") != null) {
					prop.setTargetRelation(TargetRelationAttribute.fromClient(JAXBHandler.getValue(cs, "target_relation_restriction").toString()).toString());
					hasProperties = true;
				}
				// Max distance to first target for the skill to start
				if (JAXBHandler.getValue(cs, "first_target_valid_distance") != 0) {
					prop.setFirstTargetRange((Integer) JAXBHandler.getValue(cs, "first_target_valid_distance"));
					hasProperties = true;
				}
				// Type of the first target affected by the skill : ME, MASTER, POINT ...
				if (JAXBHandler.getValue(cs, "first_target") != null) {
					prop.setFirstTarget(FirstTargetAttribute.fromClient(JAXBHandler.getValue(cs, "first_target").toString()).toString());
					hasProperties = true;
				}
				// State requirements for the target for the skill to be useable : STUMBLE, OPENAERIAL
				if (!getRequiredTargetStatus(cs).isEmpty()) {
					prop.getTargetStatus().addAll(getRequiredTargetStatus(cs));
					hasProperties = true;
				}
				// ??? Most of the times = 12
				if (JAXBHandler.getValue(cs, "revision_distance") != 0) {
					prop.setRevisionDistance((Integer) JAXBHandler.getValue(cs, "revision_distance"));
					hasProperties = true;
				}
				// Target Range Optional Values
				if (JAXBHandler.getValue(cs, "target_range_area_type") != null) {
					hasProperties = setRangeOptionalProperties(cs, prop);
				}
				// Check if the skill aim's the target's butt
				if (JAXBHandler.getValue(cs, "target_range_opt4") != null && JAXBHandler.getValue(cs, "target_range_opt4").toString().equalsIgnoreCase("back")) {
					prop.setDirection(1);
					hasProperties = true;
				}
				// Races the skill will affect
				if (JAXBHandler.getValue(cs, "target_species_restriction") != null) {
					String species = TargetSpeciesAttribute.fromClient(JAXBHandler.getValue(cs, "target_species_restriction").toString()).toString();
					if (!Strings.isNullOrEmpty(species) && !species.equalsIgnoreCase("all")) {
						prop.setTargetSpecies(species);
						hasProperties = true;
					}
				}
			if (hasProperties)
				st.setProperties(prop);
			
			// Start Conditions			
			Conditions start = new Conditions();
			boolean hasStartConditions = false;
				
				if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "HP") && !isPeriodic(cs) && getCost(cs) != 0) {
					HpCondition hp = new HpCondition();
					hasStartConditions |= setConditions(cs, start, hp);
				}
				
				if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "MP") && !isPeriodic(cs) && getCost(cs) != 0) {
					MpCondition mp = new MpCondition();
					hasStartConditions |= setConditions(cs, start, mp);
				}
				
				if (JAXBHandler.getValue(cs, "cost_dp") != null && JAXBHandler.getValue(cs, "cost_dp") != 0) {
					DpCondition dp = new DpCondition();
					hasStartConditions |= setConditions(cs, start, dp);
				}
				
				WeaponCondition weap = new WeaponCondition();
				hasStartConditions |= setConditions(cs, start, weap);
				
				if (JAXBHandler.getValue(cs, "required_leftweapon") != null) {
					ArmorCondition armor = new ArmorCondition();
						hasStartConditions |= setConditions(cs, start, armor);
				}
				
				if (JAXBHandler.getValue(cs, "use_arrow") != null && (Integer) JAXBHandler.getValue(cs, "use_arrow") != 0) {
					hasStartConditions |= setConditions(cs, start, new ArrowCheckCondition());
				}
				
				if (JAXBHandler.getValue(cs, "self_flying_restriction") != null) {
					SelfFlyingCondition selfFlying = new SelfFlyingCondition();
					hasStartConditions |= setConditions(cs, start, selfFlying);
				}
				
				if (JAXBHandler.getValue(cs, "target_species_restriction") != null
					&& (JAXBHandler.getValue(cs, "target_species_restriction").toString().equalsIgnoreCase("PC") || JAXBHandler.getValue(cs, "target_species_restriction").toString().equalsIgnoreCase("NPC"))) {
					TargetCondition target = new TargetCondition();
					hasStartConditions |= setConditions(cs, start, target);
				}
				
				if (JAXBHandler.getValue(cs, "target_flying_restriction") != null) {
					TargetFlyingCondition targetFlying = new TargetFlyingCondition();
					hasStartConditions |= setConditions(cs, start, targetFlying);
				}
				
				if (JAXBHandler.getValue(cs, "nouse_combat_state") != null && (Integer) JAXBHandler.getValue(cs, "nouse_combat_state") != 0) {
					hasStartConditions |= setConditions(cs, start, new CombatCheckCondition());
				}
				
				ChainCondition chain = new ChainCondition();
				hasStartConditions |= setConditions(cs, start, chain);
				
				if (JAXBHandler.getValue(cs, "allow_use_form_category") != null) {
					FormCondition form = new FormCondition();
					hasStartConditions |= setConditions(cs, start, form);
				}
				
			if (hasStartConditions)			
				st.setStartconditions(start);
			
			// Use Conditions
			Conditions use = new Conditions();
				boolean hasUseConditions = false;
			
				if (JAXBHandler.getValue(cs, "target_stop") != null && (Integer) JAXBHandler.getValue(cs, "target_stop") == 0) {
					PlayerMovedCondition move = new PlayerMovedCondition();
						hasUseConditions |= setConditions(cs, use, move);
				}
				
			if (hasUseConditions)			
				st.setUseconditions(use);
			
			// Use Equipments Conditions
			Conditions equip = new Conditions();
			boolean hasEquipConditions = false;
				
				if (JAXBHandler.getValue(cs, "required_leftweapon") != null
					&& JAXBHandler.getValue(cs, "sub_type") != null && JAXBHandler.getValue(cs, "sub_type").toString().equalsIgnoreCase("buff")) {
					ArmorCondition armor = new ArmorCondition();
					hasEquipConditions |= setConditions(cs, equip, armor);
				}
				
			if (hasEquipConditions)			
				st.setUseequipmentconditions(equip);
			
			// Effects
			if (hasEffects(cs)) {
				Effects effects = new Effects();
				boolean hasEffects = false;
				
				// [UPDATE] Update max value if skill get more effects
				for (int a = 1; a <= 4; a++) {
					if (JAXBHandler.getValue(cs, "effect"+a+"_type") != null && EffectType.fromClient(JAXBHandler.getValue(cs, "effect"+a+"_type").toString()) != null) {
						hasEffects |= computeEffects(cs, effects, EffectType.fromClient(JAXBHandler.getValue(cs, "effect"+a+"_type").toString()), a);
					}
				}

				if (hasEffects)
					st.setEffects(effects);
			}
			// Actions
			Actions actions = new Actions();
			boolean hasActions = false;
			
			if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "HP") && !isPeriodic(cs) && getCost(cs) != 0) {
				HpUseAction hp = new HpUseAction();
				hp.setValue(getCost(cs));
				hp.setDelta(getDelta(cs));
				if (JAXBHandler.getValue(cs, "cost_parameter").toString().equalsIgnoreCase("HP_RATIO")) {hp.setRatio(true);}
				actions.getItemuseAndMpuseAndHpuse().add(hp);
				hasActions = true;
			}
			
			if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "MP") && !isPeriodic(cs) && getCost(cs) != 0) {
				MpUseAction mp = new MpUseAction();
				mp.setValue(getCost(cs));
				mp.setDelta(getDelta(cs));
				if (JAXBHandler.getValue(cs, "cost_parameter").toString().equalsIgnoreCase("MP_RATIO")) {mp.setRatio(true);}
				actions.getItemuseAndMpuseAndHpuse().add(mp);
				hasActions = true;
			}
			
			if (JAXBHandler.getValue(cs, "cost_dp") != null && (Integer) JAXBHandler.getValue(cs, "cost_dp") != 0) {
				DpUseAction dp = new DpUseAction();
				dp.setValue((Integer) JAXBHandler.getValue(cs, "cost_dp"));
				actions.getItemuseAndMpuseAndHpuse().add(dp);
				hasActions = true;
			}
			
			if (JAXBHandler.getValue(cs, "component") != null && (Integer) JAXBHandler.getValue(cs, "component_count") != 0) {
				ItemUseAction item = new ItemUseAction();
				item.setItemid(getItemId(JAXBHandler.getValue(cs, "component").toString()));
				item.setCount((Integer) JAXBHandler.getValue(cs, "component_count"));
				actions.getItemuseAndMpuseAndHpuse().add(item);
				hasActions = true;
			}
			
			if (hasActions)
				st.setActions(actions);
			
			// Periodic Actions
			if (isPeriodic(cs) && getChecktimeCost(cs) != 0) {
				PeriodicActions pa = new PeriodicActions();
				pa.setChecktime((Integer) JAXBHandler.getValue(cs, "effect1_checktime"));

				if (JAXBHandler.getValue(cs, "cost_checktime_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_checktime_parameter").toString(), "HP")) {
					HpUsePeriodicAction hp = new HpUsePeriodicAction();
					hp.setValue(getChecktimeCost(cs));
					if (getChecktimeDelta(cs) != 0) {hp.setDelta(getChecktimeDelta(cs));}
					pa.getHpuseAndMpuse().add(hp);
				}
				if (JAXBHandler.getValue(cs, "cost_checktime_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_checktime_parameter").toString(), "MP")) {
					MpUsePeriodicAction mp = new MpUsePeriodicAction();
					mp.setValue(getChecktimeCost(cs));
					// if (getChecktimeDelta(cs) != 0) {mp.setDelta(getChecktimeDelta(cs));} // TODO : Add if needed
					pa.getHpuseAndMpuse().add(mp);
				}
				st.setPeriodicactions(pa);
			}
			// Motion
			Motion motion = new Motion();
			boolean hasMotion = false;
				if (JAXBHandler.getValue(cs, "motion_name") != null) {
					motion.setName(JAXBHandler.getValue(cs, "motion_name").toString().toLowerCase());
					hasMotion = true;
				}
				if (JAXBHandler.getValue(cs, "motion_play_speed") != null && JAXBHandler.getValue(cs, "motion_play_speed") != 0) {
					motion.setSpeed((Integer) JAXBHandler.getValue(cs, "motion_play_speed"));
					hasMotion = true;
				}
				if (JAXBHandler.getValue(cs, "instant_skill") != null && JAXBHandler.getValue(cs, "instant_skill") == 1) {
					motion.setInstantSkill(true);
					hasMotion = true;
				}
			if (hasMotion)
				st.setMotion(motion);
			
			/************************** END **************************/
			templateList.add(st);
		}
	}
	
	@Override
	public void analyze() {
		if (ANALYSE)
			JAXBHandler.printUnused("skills");
	}

	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.SKILLS, AionWritingConfig.SKILLS_PACK);
		System.out.println("\n[SKILLS] Skills written : " + templateList.size());
	}
	
	/******* EXTRA ********/
	
	private String getName(String s) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringText(s) : "";}
	private int getNameId(String s, int mult, int plus) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringId(s, mult, plus) : 0;}
	private int getItemId(String s) {return (s != null) ? new AionDataCenter().getInstance().getItemIdByName(s) : 0;}
	private int getSkillId(String s) {return (s != null) ? new AionDataCenter().getInstance().getSkillIdByName(s) : 0;}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	private int getWorld(String s) {return (s != null) ? new AionDataCenter().getInstance().getWorldIdByName(s) : 0;}
	
	private Object findSkill(String needed, String prop, Object value) {return new AionDataCenter().getInstance().skillFinder(needed, prop, value);} //TODO: Use instead of usual getters
	
	private int getSkillLevel(ClientSkill cs) {
		int level1 = 1;
		// int level2 = 1;
		
		// First calculation
		String desc = JAXBHandler.getValue(cs, "desc").toString();
		if (StringUtils.containsIgnoreCase(desc, "_G")) {
			String[] parts = desc.toUpperCase().trim().split("_G");
			if (parts.length >= 1) {
				String s = parts[parts.length - 1];
				String[] sl = s.split("_");
				try {
					level1 = Integer.parseInt(sl[0]);
				} catch (Exception ex) {
					// System.out.println(ex.getMessage() + " " + desc.toUpperCase());
				}
			}
			else
				System.out.println("[SKILLS] Error splitting Skill name for skill : " + desc);
		}
		
		// Second calculation (less precise, sometimes more, sometimes less)
		// if (JAXBHandler.getValue(cs, "chain_category_level") != 0)
			// level2 = (Integer) JAXBHandler.getValue(cs, "chain_category_level");
		
		// if (level1 != level2)
			// System.out.println("[SKILLS] Level methods returned 2 different results : " + level1 + " -- " + level2 + " for : " + desc.toUppercase());

		return level1;
	}
	
	private String getStack(ClientSkill cs) {
		String stack = null;
		String desc = JAXBHandler.getValue(cs, "desc").toString();
		String[] parts = desc.toUpperCase().trim().replaceFirst("STR_", "").split("_G");
		if (parts.length >= 1)
			stack = parts[0].toUpperCase();
		
		return stack;
	}
	
	private StigmaType getStigmaType(ClientSkill cs) {
		String name = JAXBHandler.getValue(cs, "name").toString();
		
		// First Calculation
		List<ClientSkillTree> temp = new ArrayList<ClientSkillTree>();
		StigmaType rm1 = null;
		StigmaType rm2 = null;
		
		for (ClientSkillTree cst : skillTreeList) {
			if (cst.getSkill().equalsIgnoreCase(name))
				temp.add(cst);
		}
		
		if (!temp.isEmpty()) {
			ClientSkillTree cst1 = temp.get(0);
			if (cst1.getStigmaDisplay() != null && cst1.getStigmaDisplay() == 1)
				rm1 = StigmaType.BASIC;
			if (cst1.getStigmaDisplay() != null && cst1.getStigmaDisplay() == 2)
				rm1 = StigmaType.ADVANCED;
		}
		
		//Second Claclulation
		ClientItem stigma = stigmaItemMap.get(name.toUpperCase());
		if (stigma != null) {
			ItemCategory stigmaCategory = ItemCategory.getMatchingCategory(stigma);
			if (stigmaCategory == ItemCategory.STIGMA)
				rm2 = StigmaType.BASIC;
			if (stigmaCategory == ItemCategory.STIGMA_ADVANCED)
				rm2 = StigmaType.ADVANCED;
		}
		
		if (rm1 != null && rm2 == null) {
			// System.out.println("[SKILLS] Stigma method 1 returned a result for : " + name.toUpperCase() + " but not 2");
			return rm1;
		}
		if (rm1 == null && rm2 != null) {
			// System.out.println("[SKILLS] Stigma method 2 returned a result for : " + name.toUpperCase() + " but not 1");
			return rm2;
		}
		
		return null;
	}
	
	private boolean isUnpottable(ClientSkill cs) {
		switch (getIntValue(cs, "", "delay_id")) {
			case 632:
			case 1040:
			case 1560:
			case 1660: 
			case 1663: 
			case 1666:
			case 1774:
			case 1789:
				return true;
		}
		return false;
	}
	
	// [UPDATE] There may be more then 5 statuses max in the future
	private List<String> getRequiredTargetStatus(ClientSkill cs) {
		List<String> results = new ArrayList<String>();
		for (int a = 1; a <= 5; a++) {
		if (JAXBHandler.getValue(cs, "target_valid_status"+a) != null)
			results.add(JAXBHandler.getValue(cs, "target_valid_status"+a).toString().toUpperCase());
		}
		return results;
	}
	
	private boolean setRangeOptionalProperties(ClientSkill cs, Properties prop) {
		boolean hasProperties = false;
		if (JAXBHandler.getValue(cs, "target_range_area_type").toString().equalsIgnoreCase("FIREBALL")) {
			if (JAXBHandler.getValue(cs, "target_range_opt1") != null && JAXBHandler.getValue(cs, "target_range_opt1") != 0) {
				prop.setTargetDistance((Integer) JAXBHandler.getValue(cs, "target_range_opt1"));
				hasProperties = true;
			}
		}
		else if (JAXBHandler.getValue(cs, "target_range_area_type").toString().equalsIgnoreCase("FIRESTORM")) {
			if (JAXBHandler.getValue(cs, "target_range_opt1") != null && JAXBHandler.getValue(cs, "target_range_opt1") != 0) {
				prop.setTargetDistance((Integer) JAXBHandler.getValue(cs, "target_range_opt1"));
				hasProperties = true;
			}
			if (JAXBHandler.getValue(cs, "target_range_opt2") != null && JAXBHandler.getValue(cs, "target_range_opt2") != 0) {
				prop.setEffectiveAngle((Integer) JAXBHandler.getValue(cs, "target_range_opt2"));
				hasProperties = true;
			}
		}
		else { // LIGHTNINGBOLT
			if (JAXBHandler.getValue(cs, "target_range_opt1") != null && JAXBHandler.getValue(cs, "target_range_opt1") != 0) {
				prop.setEffectiveWidth((Integer) JAXBHandler.getValue(cs, "target_range_opt1"));
				hasProperties = true;
			}
			if (JAXBHandler.getValue(cs, "target_range_opt2") != null && JAXBHandler.getValue(cs, "target_range_opt2") != 0) {
				prop.setTargetDistance((Integer) JAXBHandler.getValue(cs, "target_range_opt2"));
				hasProperties = true;
			}
		}
		return hasProperties;
	}
	
	private boolean setConditions(ClientSkill cs, Conditions conditions, Condition c) {
		boolean hasConditions = false;
		
		if (c instanceof HpCondition) {
			HpCondition hp = (HpCondition) c;
			hp.setValue(getCost(cs));
			hp.setDelta(getDelta(cs));
			if (JAXBHandler.getValue(cs, "cost_parameter").toString().equalsIgnoreCase("HP_RATIO")) {hp.setRatio(true);}
			if (hp.getValue() != 0) {
				conditions.getMpAndHpAndDp().add(hp);
				hasConditions = true;
			}
		}
		else if (c instanceof MpCondition) {
			MpCondition mp = (MpCondition) c;
			mp.setValue(getCost(cs));
			mp.setDelta(getDelta(cs));
			if (JAXBHandler.getValue(cs, "cost_parameter").toString().equalsIgnoreCase("MP_RATIO")) {mp.setRatio(true);}
			if (mp.getValue() != 0) {
				conditions.getMpAndHpAndDp().add(mp);
				hasConditions = true;
			}
		}
		else if (c instanceof DpCondition) {
			DpCondition dp = (DpCondition) c;
			dp.setValue((Integer) JAXBHandler.getValue(cs, "cost_dp"));
			if (dp.getValue() != 0) {
				conditions.getMpAndHpAndDp().add(dp);
				hasConditions = true;
			}
		}
		else if (c instanceof WeaponCondition) {
			WeaponCondition weap = (WeaponCondition) c;
			if (JAXBHandler.getValue(cs, "required_sword") != null && JAXBHandler.getValue(cs, "required_sword") == 1) {weap.getWeapon().add(WeaponType.SWORD_1H.toString());}	
			if (JAXBHandler.getValue(cs, "required_mace") != null && JAXBHandler.getValue(cs, "required_mace") == 1) {weap.getWeapon().add(WeaponType.MACE_1H.toString());}	
			if (JAXBHandler.getValue(cs, "required_dagger") != null && JAXBHandler.getValue(cs, "required_dagger") == 1) {weap.getWeapon().add(WeaponType.DAGGER_1H.toString());}	
			if (JAXBHandler.getValue(cs, "required_2hsword") != null && JAXBHandler.getValue(cs, "required_2hsword") == 1) {weap.getWeapon().add(WeaponType.SWORD_2H.toString());}	
			if (JAXBHandler.getValue(cs, "required_polearm") != null && JAXBHandler.getValue(cs, "required_polearm") == 1) {weap.getWeapon().add(WeaponType.POLEARM_2H.toString());}	
			if (JAXBHandler.getValue(cs, "required_staff") != null && JAXBHandler.getValue(cs, "required_staff") == 1) {weap.getWeapon().add(WeaponType.STAFF_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_bow") != null && JAXBHandler.getValue(cs, "required_bow") == 1) {weap.getWeapon().add(WeaponType.BOW.toString());}
			if (JAXBHandler.getValue(cs, "required_orb") != null && JAXBHandler.getValue(cs, "required_orb") == 1) {weap.getWeapon().add(WeaponType.ORB_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_book") != null && JAXBHandler.getValue(cs, "required_book") == 1) {weap.getWeapon().add(WeaponType.BOOK_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_gun") != null && JAXBHandler.getValue(cs, "required_gun") == 1) {weap.getWeapon().add(WeaponType.GUN_1H.toString());}
			if (JAXBHandler.getValue(cs, "required_cannon") != null && JAXBHandler.getValue(cs, "required_cannon") == 1) {weap.getWeapon().add(WeaponType.CANON_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_harp") != null && JAXBHandler.getValue(cs, "required_harp") == 1) {weap.getWeapon().add(WeaponType.HARP_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_keyblade") != null && JAXBHandler.getValue(cs, "required_keyblade") == 1) {weap.getWeapon().add(WeaponType.KEYBLADE_2H.toString());}
			if (JAXBHandler.getValue(cs, "required_keyhammer") != null && JAXBHandler.getValue(cs, "required_keyhammer") == 1) {weap.getWeapon().add(WeaponType.KEYHAMMER_2H.toString());}
			// if (JAXBHandler.getValue(cs, "required_ride_robot") == 1) {weap.getWeapon().add(WeaponType.RIDE_ROBOT.toString());}
			if (!weap.getWeapon().isEmpty()) {
				conditions.getMpAndHpAndDp().add(weap);
				hasConditions = true;
			}
		}
		else if (c instanceof ArmorCondition) {
			ArmorCondition armor = (ArmorCondition) c;
			armor.setArmor(ArmorType.fromClient(JAXBHandler.getValue(cs, "required_leftweapon").toString()).toString());
			if (armor.getArmor() != null) {
				conditions.getMpAndHpAndDp().add(armor);
				hasConditions = true;
			}
		}
		else if (c instanceof ArrowCheckCondition) {
			ArrowCheckCondition arrow = (ArrowCheckCondition) c;
			conditions.getMpAndHpAndDp().add(arrow);
			hasConditions = true;
		}
		else if (c instanceof PlayerMovedCondition) {
			PlayerMovedCondition move = (PlayerMovedCondition) c;
			move.setAllow(false);
			if (!move.isAllow()) {
				conditions.getMpAndHpAndDp().add(move);
				hasConditions = true;
			}
		}
		else if (c instanceof TargetCondition) {
			TargetCondition target = (TargetCondition) c;
			target.setValue(TargetAttribute.fromClient(JAXBHandler.getValue(cs, "target_species_restriction").toString()).toString());
			if (target.getValue() != null) {
				conditions.getMpAndHpAndDp().add(target);
				hasConditions = true;
			}
		}
		else if (c instanceof TargetFlyingCondition) {
			TargetFlyingCondition targetFlying = (TargetFlyingCondition) c;
			targetFlying.setRestriction(FlyingRestriction.fromClient(JAXBHandler.getValue(cs, "target_flying_restriction").toString()).toString());
			if (targetFlying.getRestriction() != null) {
				conditions.getMpAndHpAndDp().add(targetFlying);
				hasConditions = true;
			}
		}
		else if (c instanceof SelfFlyingCondition) {
			SelfFlyingCondition selfFlying = (SelfFlyingCondition) c;
			selfFlying.setRestriction(FlyingRestriction.fromClient(JAXBHandler.getValue(cs, "self_flying_restriction").toString()).toString());
			if (selfFlying.getRestriction() != null) {
				conditions.getMpAndHpAndDp().add(selfFlying);
				hasConditions = true;
			}
		}
		else if (c instanceof ChainCondition) {
			ChainCondition chain = (ChainCondition) c;
			if (JAXBHandler.getValue(cs, "self_chain_count") != null && JAXBHandler.getValue(cs, "self_chain_count") != 0) {chain.setSelfcount((Integer) JAXBHandler.getValue(cs, "self_chain_count"));}
			if (JAXBHandler.getValue(cs, "prechain_count") != null && JAXBHandler.getValue(cs, "prechain_count") != 0) {chain.setPrecount((Integer) JAXBHandler.getValue(cs, "prechain_count"));}
			if (JAXBHandler.getValue(cs, "chain_time") != null && JAXBHandler.getValue(cs, "chain_time") != 0) {chain.setTime((Integer) JAXBHandler.getValue(cs, "chain_time"));}
			if (JAXBHandler.getValue(cs, "prechain_category_name") != null) {chain.setPrecategory(JAXBHandler.getValue(cs, "prechain_category_name").toString().toUpperCase());}
			if (JAXBHandler.getValue(cs, "chain_category_name") != null) {chain.setCategory(JAXBHandler.getValue(cs, "chain_category_name").toString().toUpperCase());}
			if (chain.getSelfcount() != null || chain.getPrecount() != null || chain.getTime() != null || chain.getPrecategory() != null || chain.getCategory() != null) {
				conditions.getMpAndHpAndDp().add(chain);
				hasConditions = true;
			}
		}
		else if (c instanceof FormCondition) {
			FormCondition form = (FormCondition) c;
			form.setValue(TransformType.fromClient(JAXBHandler.getValue(cs, "allow_use_form_category").toString()).toString());
			if (form.getValue() != null) {
				conditions.getMpAndHpAndDp().add(form);
				hasConditions = true;
			}
		}
		else if (c instanceof CombatCheckCondition) {
			CombatCheckCondition combat = (CombatCheckCondition) c;
			conditions.getMpAndHpAndDp().add(combat);
			hasConditions = true;
		}
		else
			System.out.println("[SKILLS][ERROR] Trying to set a non existing condition");
		
		return hasConditions;
	}
	
	private boolean isPeriodic(ClientSkill cs) {
		if (JAXBHandler.getValue(cs, "cost_checktime_parameter") != null && (Integer) JAXBHandler.getValue(cs, "cost_checktime") != 0) // USUAL
			return true;
		else if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "ratio") 
			&& JAXBHandler.getValue(cs, "cost_time") != null && JAXBHandler.getValue(cs, "cost_time") != 0 && getCost(cs) != 0) // EXCEPTIONS:  Skill 912 i.e. (and new 4.0 skills)
			return true;
		else
			return false;
	}
	
	private int getCost(ClientSkill cs) {
		int cost = 0;
		if (JAXBHandler.getValue(cs, "cost_end") != null && JAXBHandler.getValue(cs, "cost_end") != 0)
			cost = (Integer) JAXBHandler.getValue(cs, "cost_end");
		else if (JAXBHandler.getValue(cs, "activation_attribute").toString().equalsIgnoreCase("Toggle") && JAXBHandler.getValue(cs, "cost_toggle") != null && JAXBHandler.getValue(cs, "cost_toggle") != 0)
			cost = (Integer) JAXBHandler.getValue(cs, "cost_toggle");
		else if (JAXBHandler.getValue(cs, "cost_start") != null && JAXBHandler.getValue(cs, "cost_start") != 0)
			cost = (Integer) JAXBHandler.getValue(cs, "cost_start");
		return cost;
	}
	
	private int getDelta(ClientSkill cs) {
		int delta = 0;
		if (JAXBHandler.getValue(cs, "cost_end_lv") != null && JAXBHandler.getValue(cs, "cost_end_lv") != 0)
			delta = (Integer) JAXBHandler.getValue(cs, "cost_end_lv");
		else if (JAXBHandler.getValue(cs, "cost_start_lv") != null && JAXBHandler.getValue(cs, "cost_start_lv") != 0)
			delta = (Integer) JAXBHandler.getValue(cs, "cost_start_lv");
		return delta;
	}
	
	private int getChecktimeCost(ClientSkill cs) {
		int pCost = 0;
		Object costParam = JAXBHandler.getValue(cs, "cost_parameter");
		if (costParam != null && StringUtils.containsIgnoreCase(costParam.toString(), "ratio") && JAXBHandler.getValue(cs, "cost_time") != null && JAXBHandler.getValue(cs, "cost_time") != 0) // EXCEPTIONS:  Skill 912 i.e. (and new 4.0 skills)
			pCost = getCost(cs);
		else if (JAXBHandler.getValue(cs, "cost_checktime") != null && JAXBHandler.getValue(cs, "cost_checktime") != 0) // USUAL
			pCost = (Integer) JAXBHandler.getValue(cs, "cost_checktime");
		return pCost;
	}
	
	private int getChecktimeDelta(ClientSkill cs) {
		int pDelta = 0;
		Object costParam = JAXBHandler.getValue(cs, "cost_parameter");
		if (costParam != null && StringUtils.containsIgnoreCase(costParam.toString(), "ratio") && JAXBHandler.getValue(cs, "cost_time") != null && JAXBHandler.getValue(cs, "cost_time") != 0) // EXCEPTIONS:  Skill 912 i.e. (and new 4.0 skills)
			pDelta = getDelta(cs);
		else if (JAXBHandler.getValue(cs, "cost_checktime_lv") != null && JAXBHandler.getValue(cs, "cost_checktime_lv") != 0) // USUAL
			pDelta = (Integer) JAXBHandler.getValue(cs, "cost_checktime_lv");
		return pDelta;
	}
	
	// [UPDATE] Add check if skills get more than 4 effects with future updates
	private boolean hasEffects(ClientSkill cs) {
		for (int a = 1; a <= 4; a++) {
			if (JAXBHandler.getValue(cs, "effect"+a+"_type") != null && JAXBHandler.getValue(cs, "effect"+a+"_type").toString() != "")
				return true;
		}
		return false;
	}
	
	private boolean computeEffects(ClientSkill cs, Effects effects, EffectType et, int a) {
		boolean compute = false;
		String current = "effect" + a + "_";
		
		if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractOverTimeEffect")) {
			// HealOverTimeEffect
			if (et == EffectType.HEAL) {
				HealEffect heal = new HealEffect();
				if (setGeneral(cs, heal, a) | setAbstractOverTimeEffect(cs, heal, current)) {
					effects.getRootAndStunAndSleep().add(heal);
					compute = true;
				}
			}
			else if (et == EffectType.MPHEAL) {
				MPHealEffect mpHeal = new MPHealEffect();
				if (setGeneral(cs, mpHeal, a) | setAbstractOverTimeEffect(cs, mpHeal, current)) {
					effects.getRootAndStunAndSleep().add(mpHeal);
					compute = true;
				}
			}
			else if (et == EffectType.FPHEAL) {
				FPHealEffect fpHeal = new FPHealEffect();
				if (setGeneral(cs, fpHeal, a) | setAbstractOverTimeEffect(cs, fpHeal, current)) {
					effects.getRootAndStunAndSleep().add(fpHeal);
					compute = true;
				}
			}
			else if (et == EffectType.DPHEAL) {
				DPHealEffect dpHeal = new DPHealEffect();
				if (setGeneral(cs, dpHeal, a) | setAbstractOverTimeEffect(cs, dpHeal, current)) {
					effects.getRootAndStunAndSleep().add(dpHeal);
					compute = true;
				}
			}
			// SpellAtackEffect
			else if (et == EffectType.SPELLATK) {
				SpellAttackEffect spell = new SpellAttackEffect();
				if (setGeneral(cs, spell, a) | setAbstractOverTimeEffect(cs, spell, current)) {
					effects.getRootAndStunAndSleep().add(spell);
					compute = true;
				}
			}
			// SpellAtkDrainEffect
			else if (et == EffectType.SPELLATKDRAIN) {
				SpellAtkDrainEffect spellDrain = new SpellAtkDrainEffect();
				boolean check = false;
				if (JAXBHandler.getValue(cs, current + "reserved15") != null) {
					int value = getIntValue(cs, current, "reserved15");
					if (value != 0) {
						spellDrain.setHpPercent(value);
						check = true;
					}
				}
				if (JAXBHandler.getValue(cs, current + "reserved17") != null) {
					int value = getIntValue(cs, current, "reserved17");
					if (value != 0) {
						spellDrain.setMpPercent(value);
						check = true;
					}
				}
				if (setGeneral(cs, spellDrain, a) | setAbstractOverTimeEffect(cs, spellDrain, current) | check) {
					effects.getRootAndStunAndSleep().add(spellDrain);
					compute = true;
				}
			}
			// MpAttackEffect
			else if (et == EffectType.MPATTACK) {
				MpAttackEffect mpAttack = new MpAttackEffect();
				if (setGeneral(cs, mpAttack, a) | setAbstractOverTimeEffect(cs, mpAttack, current)) {
					effects.getRootAndStunAndSleep().add(mpAttack);
					compute = true;
				}
			}
			// FpAttackEffect
			else if (et == EffectType.FPATK) {
				MpAttackEffect fpAttack = new MpAttackEffect();
				if (setGeneral(cs, fpAttack, a) | setAbstractOverTimeEffect(cs, fpAttack, current)) {
					effects.getRootAndStunAndSleep().add(fpAttack);
					compute = true;
				}
			}
			// BleedEffect
			else if (et == EffectType.BLEED) {
				BleedEffect bleed = new BleedEffect();
				if (setGeneral(cs, bleed, a) | setAbstractOverTimeEffect(cs, bleed, current)) {
					effects.getRootAndStunAndSleep().add(bleed);
					compute = true;
				}
			}
			// PoisonEffect
			else if (et == EffectType.POISON) {
				PoisonEffect poison = new PoisonEffect();
				if (setGeneral(cs, poison, a) | setAbstractOverTimeEffect(cs, poison, current)) {
					effects.getRootAndStunAndSleep().add(poison);
					compute = true;
				}
			}
		}
		else if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractHealEffect")) {
			// HealInstantEffect
			if (et == EffectType.HEAL_INSTANT) {
				HealInstantEffect e = new HealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// MPHealInstantEffect
			else if (et == EffectType.MPHEAL_INSTANT) {
				MPHealInstantEffect e = new MPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DPHealInstantEffect
			else if (et == EffectType.DPHEAL_INSTANT) {
				DPHealInstantEffect e = new DPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// FPHealInstantEffect
			else if (et == EffectType.FPHEAL_INSTANT) {
				FPHealInstantEffect e = new FPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcHealInstantEffect
			else if (et == EffectType.PROCHEAL_INSTANT) {
				ProcHealInstantEffect e = new ProcHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcMPHealInstantEffect
			else if (et == EffectType.PROCMPHEAL_INSTANT) {
				ProcMPHealInstantEffect e = new ProcMPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcDPHealInstantEffect
			else if (et == EffectType.PROCDPHEAL_INSTANT) {
				ProcDPHealInstantEffect e = new ProcDPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcFPHealInstantEffect
			else if (et == EffectType.PROCFPHEAL_INSTANT) {
				ProcFPHealInstantEffect e = new ProcFPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcVPHealInstantEffect
			else if (et == EffectType.PROCVPHEAL_INSTANT) {
				ProcVPHealInstantEffect e = new ProcVPHealInstantEffect();
				if (JAXBHandler.getValue(cs, current + "reserved3") != null) {
					int value = getIntValue(cs, current, "reserved3");
					if (value != 0) {
						e.setValue2(value);
					}
				}
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current) | e.getValue2() != null) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// CaseHealEffect
			else if (et == EffectType.CASEHEAL) {
				CaseHealEffect e = new CaseHealEffect();
				if (JAXBHandler.getValue(cs, current + "reserved10") != null) {
					int value = getIntValue(cs, current, "reserved10");
					if (value != 0) {
						e.setCondValue(value);
					}
				}
				if (JAXBHandler.getValue(cs, current + "reserved13") != null) {
					e.setType(HealType.fromClient(JAXBHandler.getValue(cs, current + "reserved13").toString()).toString());
				}
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current) | e.getType() != null | e.getCondValue() != null) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// HealCastorOnAttackedEffect
			else if (et == EffectType.HEALCASTORONATTACKED) {
				HealCastorOnAttackedEffect e = new HealCastorOnAttackedEffect();
				boolean check = false;
				if (JAXBHandler.getValue(cs, current + "reserved4") != null) {
					float value = getFloatValue(cs, current, "reserved4");
					if (value != 0.0f) {
						e.setRange(value);
						check = true;
					}
				}
				e.setType("HP");
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// HealCastorOnTargetDeadEffect
			else if (et == EffectType.HEALCASTORONTARGETDEAD) {
				HealCastorOnTargetDeadEffect e = new HealCastorOnTargetDeadEffect();
				boolean check = false;
				if (JAXBHandler.getValue(cs, current + "reserved4") != null) {
					float value = getFloatValue(cs, current, "reserved4");
					if (value != 0.0f) {
						e.setRange(value);
						check = true;
					}
				}
				e.setType("HP");
				if (JAXBHandler.getValue(cs, current + "reserved14") != null && JAXBHandler.getValue(cs, current + "reserved14").toString().equalsIgnoreCase("Castor_Party")) {
					e.setHealparty(true);
					check = true;
				}
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
		}
		else if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractDispelEffect")) {
			// DispelDebuffEffect
			if (et == EffectType.DISPELDEBUFF) {
				DispelDebuffEffect e = new DispelDebuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelNpcDebuffEffect
			else if (et == EffectType.DISPELNPCDEBUFF) {
				DispelNpcDebuffEffect e = new DispelNpcDebuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelDebuffPhysicalEffect
			else if (et == EffectType.DISPELDEBUFFPHYSICAL) {
				DispelDebuffPhysicalEffect e = new DispelDebuffPhysicalEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelDebuffMentalEffect
			else if (et == EffectType.DISPELDEBUFFMENTAL) {
				DispelDebuffMentalEffect e = new DispelDebuffMentalEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelBuffEffect
			else if (et == EffectType.DISPELBUFF) {
				DispelBuffEffect e = new DispelBuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelNpcBuffEffect
			else if (et == EffectType.DISPELNPCBUFF) {
				DispelNpcBuffEffect e = new DispelNpcBuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
		}
		// DispelEffect
		else if (et == EffectType.DISPEL) {
			DispelEffect e = new DispelEffect();
			if (JAXBHandler.getValue(cs, current + "reserved1") != null) {
				DispelType dtype = DispelType.fromClient(JAXBHandler.getValue(cs, current + "reserved1").toString());
				if (dtype != DispelType.NONE) {
					e.setDispeltype(dtype.toString());
					if (dtype == DispelType.EFFECTID || dtype == DispelType.EFFECTIDRANGE && !getEffectIds(cs, current).isEmpty())
						e.getEffectids().addAll(getEffectIds(cs, current));
					else if (dtype == DispelType.EFFECTTYPE && !getEffectOrSlotType(cs, current).isEmpty())
						e.getEffecttype().addAll(getEffectOrSlotType(cs, current));
					else if (dtype == DispelType.SLOTTYPE && !getEffectOrSlotType(cs, current).isEmpty())
						e.getSlottype().addAll(getEffectOrSlotType(cs, current));
				}
			}
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		else if (et.getAbstractCatetgory().equalsIgnoreCase("BufEffect")) {
			// OneTimeBoostHealEffect
			if (et == EffectType.ONETIMEBOOSTHEALEFFECT) {
				OneTimeBoostHealEffect e = new OneTimeBoostHealEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BoostHealEffect
			else if (et == EffectType.BOOSTHEALEFFECT) {
				BoostHealEffect e = new BoostHealEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BoostHateEffect
			else if (et == EffectType.BOOSTHATE) {
				BoostHateEffect e = new BoostHateEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SubTypeExtendDurationEffect
			else if (et == EffectType.SUBTYPEEXTENDDURATION) {
				SubTypeExtendDurationEffect e = new SubTypeExtendDurationEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SubTypeBoostResistEffect
			else if (et == EffectType.SUBTYPEBOOSTRESIST) {
				SubTypeBoostResistEffect e = new SubTypeBoostResistEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ExtendAuraRangeEffect
			else if (et == EffectType.EXTENDAURARANGE) {
				ExtendAuraRangeEffect e = new ExtendAuraRangeEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// NoDeathPenaltyEffect
			else if (et == EffectType.NODEATHPENALTY) {
				NoDeathPenaltyEffect e = new NoDeathPenaltyEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SwitchHostileEffect
			else if (et == EffectType.SWITCHHOSTILE) {
				SwitchHostileEffect e = new SwitchHostileEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// HiPassEffect
			else if (et == EffectType.HIPASS) {
				HiPassEffect e = new HiPassEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// OneTimeBoostSkillCriticalEffect
			else if (et == EffectType.ONETIMEBOOSTSKILLCRITICAL) {
				OneTimeBoostSkillCriticalEffect e = new OneTimeBoostSkillCriticalEffect();
				boolean check = false;
				if (getIntValue(cs, current, "reserved7") != 0) {
					e.setCount(getIntValue(cs, current, "reserved7"));
					check = true;
				}
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current) | check) {
					compute |= setValue(cs, e, current, new Integer[] {4, 2});
					if (e.getValue() < 100) {e.setPercent(true);}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BoostSpellAttackEffect
			else if (et == EffectType.BOOSTSPELLATTACKEFFECT) {
				BoostSpellAttackEffect e = new BoostSpellAttackEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// CurseEffect
			else if (et == EffectType.CURSE) {
				CurseEffect e = new CurseEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DeboostHealEffect
			else if (et == EffectType.DEBOOSTHEALAMOUNT) {
				DeboostHealEffect e = new DeboostHealEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// XPBoostEffect
			else if (et == EffectType.XPBOOST) {
				XPBoostEffect e = new XPBoostEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// NoResurrectPenaltyEffect
			else if (et == EffectType.NORESURRECTPENALTY) {
				NoResurrectPenaltyEffect e = new NoResurrectPenaltyEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BoostSkillCastingTimeEffect
			else if (et == EffectType.BOOSTSKILLCASTINGTIME) {
				BoostSkillCastingTimeEffect e = new BoostSkillCastingTimeEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BoostDropRateEffect
			else if (et == EffectType.BOOSTDROPRATE) {
				BoostDropRateEffect e = new BoostDropRateEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// StatupEffect
			else if (et == EffectType.STATUP) {
				StatupEffect e = new StatupEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// StatboostEffect
			else if (et == EffectType.STATBOOST) {
				StatboostEffect e = new StatboostEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// StatdownEffect
			else if (et == EffectType.STATDOWN) {
				StatdownEffect e = new StatdownEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// WeaponStatupEffect
			else if (et == EffectType.WEAPONSTATUP) {
				WeaponStatupEffect e = new WeaponStatupEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// WeaponStatboostEffect
			else if (et == EffectType.WEAPONSTATBOOST) {
				WeaponStatboostEffect e = new WeaponStatboostEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// WeaponMasteryEffect
			else if (et == EffectType.WPN_MASTERY) {
				WeaponMasteryEffect e = new WeaponMasteryEffect();
				if (JAXBHandler.getValue(cs, current + "reserved5") != null) {
					WeaponType weapon = WeaponType.fromClient(JAXBHandler.getValue(cs, current + "reserved5").toString());
					e.setWeapon(weapon.toString());
				}
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current) | e.getWeapon() != null) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ArmorMasteryEffect
			else if (et == EffectType.AMR_MASTERY) {
				ArmorMasteryEffect e = new ArmorMasteryEffect();
				if (JAXBHandler.getValue(cs, current + "reserved5") != null) {
					ArmorType armor = ArmorType.fromClient(JAXBHandler.getValue(cs, current + "reserved5").toString());
					e.setArmor(armor.toString());
				}
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current) | e.getArmor() != null) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ShieldMasteryEffect
			else if (et == EffectType.SHIELD_MASTERY) {
				ShieldMasteryEffect e = new ShieldMasteryEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// WeaponDualEffect
			else if (et == EffectType.WPN_DUAL) {
				WeaponDualEffect e = new WeaponDualEffect();
				if (setGeneral(cs, e, a) | setBufEffect(cs, e, current)) {
					compute |= setValue(cs, e, current, new Integer[] {6});
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
		}
		else if (et.getAbstractCatetgory().equalsIgnoreCase("DamageEffect")) {
			
			// MoveBehindEffect
			if (et == EffectType.MOVEBEHINDATK) {
				MoveBehindEffect e = new MoveBehindEffect();
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DeathBlowEffect
			else if (et == EffectType.DEATHBLOW) {
				DeathBlowEffect e = new DeathBlowEffect();
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelBuffCounterAtkEffect
			else if (et == EffectType.DISPELBUFFCOUNTERATK) {
				DispelBuffCounterAtkEffect e = new DispelBuffCounterAtkEffect();
				boolean check = false;
				if (getIntValue(cs, current, "reserved8") != 0) {e.setHitdelta(getIntValue(cs, current, "reserved8")); check = true;}
				if (getIntValue(cs, current, "reserved9") != 0) {e.setHitvalue(getIntValue(cs, current, "reserved9")); check = true;}
				if (getIntValue(cs, current, "reserved16") != 0) {e.setDispelLevel(getIntValue(cs, current, "reserved16")); check = true;}
				if (getIntValue(cs, current, "reserved17") != 0) {e.setDpower(getIntValue(cs, current, "reserved17")); check = true;}
				if (getIntValue(cs, current, "reserved18") != 0) {e.setPower(getIntValue(cs, current, "reserved18")); check = true;}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current) | check) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DelayedSpellAttackInstantEffect
			else if (et == EffectType.DELAYEDSPELLATK_INSTANT) {
				DelayedSpellAttackInstantEffect e = new DelayedSpellAttackInstantEffect();
				if (getIntValue(cs, current, "reserved9") > 0) {e.setDelay(getIntValue(cs, current, "reserved9"));}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DashEffect
			else if (et == EffectType.DASHATK) {
				DashEffect e = new DashEffect();
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// MpAttackInstantEffect
			else if (et == EffectType.MPATTACK_INSTANT) {
				MpAttackInstantEffect e = new MpAttackInstantEffect();
				if (getIntValue(cs, current, "reserved6") == 1) {e.setPercent(true);}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// FpAttackInstantEffect
			else if (et == EffectType.FPATK_INSTANT) {
				FpAttackInstantEffect e = new FpAttackInstantEffect();
				if (getIntValue(cs, current, "reserved6") == 1) {e.setPercent(true);}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcAtkInstantEffect
			else if (et == EffectType.PROCATK_INSTANT) {
				ProcAtkInstantEffect e = new ProcAtkInstantEffect();
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// NoReduceSpellATKInstantEffect
			else if (et == EffectType.NOREDUCESPELLATK_INSTANT) {
				NoReduceSpellATKInstantEffect e = new NoReduceSpellATKInstantEffect();
				if (getIntValue(cs, current, "reserved6") == 1) {e.setPercent(true);}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// CarveSignetEffect
			else if (et == EffectType.CARVESIGNET) {
				CarveSignetEffect e = new CarveSignetEffect();
				boolean check = false;
				if (getIntValue(cs, current, "reserved16") != 0 && getIntValue(cs, current, "reserved16") < 100) {e.setProb(getIntValue(cs, current, "reserved16")); check = true;}
				if (getIntValue(cs, current, "reserved10") > 1) {e.setSignetlvlstart(getIntValue(cs, current, "reserved10")); check = true;}
				if (getIntValue(cs, current, "reserved14") != 0) {e.setSignetlvl(getIntValue(cs, current, "reserved14")); check = true;}
				if (getIntValue(cs, current, "reserved13") != 0) {
					String signet = "SYSTEM_SKILL_SIGNET" + getIntValue(cs, current, "reserved13");
					e.setSignet(signet);
					if (findSkill("id", "desc", "STR_" + signet.toUpperCase() + "_1") != null)
						e.setSignetid((Integer) findSkill("id", "desc", "STR_" + signet.toUpperCase() + "_1"));
					check = true;
				}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SignetBurstEffect
			else if (et == EffectType.SIGNETBURST) {
				SignetBurstEffect e = new SignetBurstEffect();
				boolean check = false;
				// if () {e.setAbsorb();} //TODO: Unused ?
				if (getIntValue(cs, current, "reserved14") != 0) {e.setSignetlvl(getIntValue(cs, current, "reserved14")); check = true;}
				if (getIntValue(cs, current, "reserved13") != 0) {
					String signet = "SYSTEM_SKILL_SIGNET" + getIntValue(cs, current, "reserved13");
					e.setSignet(signet);
					check = true;
				}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// BackDashEffect
			else if (et == EffectType.BACKDASHATK) {
				BackDashEffect e = new BackDashEffect();
				if (getIntValue(cs, current, "reserved12") > 0) {e.setDistance((float) getIntValue(cs, current, "reserved12"));}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SkillAttackInstantEffect
			else if (et == EffectType.SKILLATK_INSTANT) {
				SkillAttackInstantEffect e = new SkillAttackInstantEffect();
				if (getIntValue(cs, current, "reserved15") == 1) {e.setCannotmiss(true);}
				if (getIntValue(cs, current, "reserved8") > 0) {e.setRnddmg(getIntValue(cs, current, "reserved8"));}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SkillAtkDrainInstantEffect
			else if (et == EffectType.SKILLATKDRAIN_INSTANT) {
				SkillAtkDrainInstantEffect e = new SkillAtkDrainInstantEffect();
				boolean check = false;
				if (getIntValue(cs, current, "reserved15") != 0) {e.setHpPercent(getIntValue(cs, current, "reserved15"));	check = true;}
				if (getIntValue(cs, current, "reserved17") != 0) {e.setMpPercent(getIntValue(cs, current, "reserved17"));	check = true;}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SpellAttackInstantEffect
			else if (et == EffectType.SPELLATK_INSTANT) {
				SpellAttackInstantEffect e = new SpellAttackInstantEffect();
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current)) {
					if (getIntValue(cs, current, "reserved6") == 1) {e.setMode("PERCENT");}
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// SpellAtkDrainInstantEffect
			else if (et == EffectType.SPELLATKDRAIN_INSTANT) {
				SpellAtkDrainInstantEffect e = new SpellAtkDrainInstantEffect();
				boolean check = false;
				if (getIntValue(cs, current, "reserved15") != 0) {e.setHpPercent(getIntValue(cs, current, "reserved15"));	check = true;}
				if (getIntValue(cs, current, "reserved17") != 0) {e.setMpPercent(getIntValue(cs, current, "reserved17"));	check = true;}
				if (setGeneral(cs, e, a) | setDamageEffect(cs, e, current) | check) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
		}
		// SignetEffect
		else if (et == EffectType.SIGNET) {
			SignetEffect e = new SignetEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// **** Alterations ****/
		// StunEffect
		else if (et == EffectType.STUN) {
			StunEffect e = new StunEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BuffStunEffect
		else if (et == EffectType.BUFFSTUN) {
			BuffStunEffect e = new BuffStunEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SleepEffect
		else if (et == EffectType.SLEEP) {
			SleepEffect e = new SleepEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BuffSleepEffect
		else if (et == EffectType.BUFFSLEEP) {
			BuffSleepEffect e = new BuffSleepEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// FearEffect
		else if (et == EffectType.FEAR) {
			FearEffect e = new FearEffect();
			if (getIntValue(cs, current, "reserved2") < 100) {e.setResistchance(getIntValue(cs, current, "reserved2"));}
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SimpleRootEffect
		else if (et == EffectType.SIMPLE_ROOT) {
			SimpleRootEffect e = new SimpleRootEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// RootEffect
		else if (et == EffectType.ROOT) {
			RootEffect e = new RootEffect();
			if (getIntValue(cs, current, "reserved2") < 100) {e.setResistchance(getIntValue(cs, current, "reserved2"));}
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SnareEffect 
		else if (et == EffectType.SNARE) {
			SnareEffect e = new SnareEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SilenceEffect 
		else if (et == EffectType.SILENCE) {
			SilenceEffect e = new SilenceEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BuffSilenceEffect 
		else if (et == EffectType.BUFFSILENCE) {
			BuffSilenceEffect e = new BuffSilenceEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// ParalyzeEffect 
		else if (et == EffectType.PARALYZE) {
			ParalyzeEffect e = new ParalyzeEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// DiseaseEffect 
		else if (et == EffectType.DISEASE) {
			DiseaseEffect e = new DiseaseEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// StumbleEffect 
		else if (et == EffectType.STUMBLE) {
			StumbleEffect e = new StumbleEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SlowEffect
		else if (et == EffectType.SLOW) {
			SlowEffect e = new SlowEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BlindEffect
		else if (et == EffectType.BLIND) {
			BlindEffect e = new BlindEffect();
			if (setGeneral(cs, e, a)) {
				compute |= setValue(cs, e, current, new Integer[] {2});
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// ConfuseEffect 
		else if (et == EffectType.CONFUSE) {
			ConfuseEffect e = new ConfuseEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BindEffect 
		else if (et == EffectType.BIND) {
			BindEffect e = new BindEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// BuffBindEffect 
		else if (et == EffectType.BUFFBIND) {
			BuffBindEffect e = new BuffBindEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SpinEffect 
		else if (et == EffectType.SPIN) {
			SpinEffect e = new SpinEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// StaggerEffect 
		else if (et == EffectType.STAGGER) {
			StaggerEffect e = new StaggerEffect();
			if (setGeneral(cs, e, a)) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// AlwaysBlockEffect 
		else if (et == EffectType.ALWAYSBLOCK) {
			AlwaysBlockEffect e = new AlwaysBlockEffect();
			if (setGeneral(cs, e, a)) {
				compute |= setValue(cs, e, current, new Integer[] {9});
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// AlwaysDodgeEffect 
		else if (et == EffectType.ALWAYSDODGE) {
			AlwaysDodgeEffect e = new AlwaysDodgeEffect();
			if (setGeneral(cs, e, a)) {
				compute |= setValue(cs, e, current, new Integer[] {9});
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// AlwaysParryEffect 
		else if (et == EffectType.ALWAYSPARRY) {
			AlwaysParryEffect e = new AlwaysParryEffect();
			if (setGeneral(cs, e, a)) {
				compute |= setValue(cs, e, current, new Integer[] {9});
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// AlwaysResistEffect 
		else if (et == EffectType.ALWAYSRESIST) {
			AlwaysResistEffect e = new AlwaysResistEffect();
			if (setGeneral(cs, e, a)) {
				compute |= setValue(cs, e, current, new Integer[] {9});
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// SearchEffect
		else if (et == EffectType.SEARCH) {
			SearchEffect e = new SearchEffect();
			if (getIntValue(cs, current, "reserved7") > 0) {e.setState("SEARCH" + getIntValue(cs, current, "reserved7"));}
			if (setGeneral(cs, e, a) && e.getState() != null) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}
		// HideEffect
		else if (et == EffectType.HIDE) {
			HideEffect e = new HideEffect();
			//TODO : applyChange(StatModifiers.SPEED, "PERCENT", (100 - getIntValue(cs, current, "reserved2") * -1));
			// overload the method and add a msk to the main one
			// if stat not given, mask |= 1
			// if percent not given mask |= 2
			// if value not given, mask |= 4
			// if mask == 7, compute change from client data
			if (getIntValue(cs, current, "reserved3") != 0) {e.setBufcount(getIntValue(cs, current, "reserved3"));}
			if (getIntValue(cs, current, "reserved4") != 0) {e.setType(getIntValue(cs, current, "reserved4"));}
			if (getIntValue(cs, current, "reserved7") > 0) {e.setState("HIDE" + getIntValue(cs, current, "reserved7"));}
			if (setGeneral(cs, e, a) && e.getState() != null) {
				effects.getRootAndStunAndSleep().add(e);
				compute = true;
			}
		}

		return compute;
	}
	
	private boolean setAbstractOverTimeEffect(ClientSkill cs, AbstractOverTimeEffect effect, String current) {
		boolean hasAbstractEffects = false;
		if (getIntValue(cs, current, "checktime") != 0) {
			effect.setChecktime(getIntValue(cs, current, "checktime"));
			hasAbstractEffects = true;
		}
		if (getIntValue(cs, current, "reserved6") == 1) {
			effect.setPercent(true);
			hasAbstractEffects = true;
		}
		hasAbstractEffects |= setDelta(cs, effect, current, new Integer[] {8});
		hasAbstractEffects |= setValue(cs, effect, current, new Integer[] {9});
		return hasAbstractEffects;
	}
	
	private boolean setAbstractHealEffect(ClientSkill cs, AbstractHealEffect effect, String current) {
		boolean hasAbstractEffects = false;
		if (getIntValue(cs, current, "reserved6") == 1) {
			effect.setPercent(true);
			hasAbstractEffects = true;
		}
		hasAbstractEffects |= setDelta(cs, effect, current, new Integer[] {1});
		hasAbstractEffects |= setValue(cs, effect, current, new Integer[] {2});
		return hasAbstractEffects;
	}
	
	private boolean setAbstractDispelEffect(ClientSkill cs, AbstractDispelEffect effect, String current) {
		boolean hasAbstractEffects = false;
	
		if (getIntValue(cs, current, "reserved16") != 0) {
			effect.setDispelLevel(getIntValue(cs, current, "reserved16"));
			hasAbstractEffects = true;
		}
		if (getIntValue(cs, current, "reserved17") != 0) {
			effect.setDpower(getIntValue(cs, current, "reserved17"));
			hasAbstractEffects = true;
		}
		if (getIntValue(cs, current, "reserved18") != 0) {
			effect.setPower(getIntValue(cs, current, "reserved18"));
			hasAbstractEffects = true;
		}
		hasAbstractEffects |= setDelta(cs, effect, current, new Integer[] {1});
		hasAbstractEffects |= setValue(cs, effect, current, new Integer[] {2});
		return hasAbstractEffects;
	}
	
	private List<Integer> getEffectIds(ClientSkill cs, String current) {
		List<Integer> results = new ArrayList<Integer>();
		for (int a = 2; a <= 9; a++) {
			if (JAXBHandler.getValue(cs, current + "reserved"+a) != null) {
				int value = getIntValue(cs, current, "reserved"+a);
				if (value != 0)
					results.add(value);
			}
		}
		return results;
	}
	
	private List<String> getEffectOrSlotType(ClientSkill cs, String current) {
		List<String> results = new ArrayList<String>();
		for (int a = 2; a <= 9; a++) {
			if (JAXBHandler.getValue(cs, current + "reserved"+a) != null && JAXBHandler.getValue(cs, current + "reserved"+a).toString() != null) {
				EffectType et = EffectType.fromClient(JAXBHandler.getValue(cs, current + "reserved"+a).toString());
				if (et != null)
					results.add(et.toString());
				else {
					TargetSlot ts = TargetSlot.fromClient(JAXBHandler.getValue(cs, current + "reserved"+a).toString());
					if (ts != TargetSlot.NONE)
						results.add(ts.toString());
				}
			}
		}
		return results;
	}
	
	private boolean setBufEffect(ClientSkill cs, BufEffect effect, String current) {
		boolean hasAbstractEffects = false;
		int e = effect.getE();
		int f = 1;
		
		if (e > 1) {
			while (e - f > 0) {
				EffectType et = EffectType.fromClient(JAXBHandler.getValue(cs, "effect"+ (int)(e - f) +"_type").toString());
				if (et != null && et == EffectType.SHAPECHANGE) {
					for (Change change : effect.getChange()) {
						if (StatModifiers.fromValue(change.getStat()) == StatModifiers.MAXHP || StatModifiers.fromValue(change.getStat()) == StatModifiers.MAXMP) {
							effect.setMaxstat(true);
							hasAbstractEffects = true;
							break;
						}
					}
				}
				f++;
			}
		}
		return hasAbstractEffects;
	}
	
	private boolean setDamageEffect(ClientSkill cs, DamageEffect effect, String current) {
		boolean hasAbstractEffects = false;
		hasAbstractEffects |= setDelta(cs, effect, current, new Integer[] {1, 3, 7});
		hasAbstractEffects |= setValue(cs, effect, current, new Integer[] {2, 4});
		return hasAbstractEffects;
	}
	
	private boolean setValue(ClientSkill cs, Effect e, String current, Integer[] target) {
		List<Integer> targets = new ArrayList<Integer>();
		Collections.addAll(targets, target);
		
		for (Integer i : targets) {
			if (getIntValue(cs, current, "reserved" + i) != 0) {
				e.setValue(getIntValue(cs, current, "reserved" + i));
				return true;
			}
		}
		return false;
	}
	
	private boolean setDelta(ClientSkill cs, Effect e, String current, Integer[] target) {
		List<Integer> targets = new ArrayList<Integer>();
		Collections.addAll(targets, target);
		
		for (Integer i : targets) {
			if (getIntValue(cs, current, "reserved" + i) != 0) {
				e.setDelta(getIntValue(cs, current, "reserved" + i));
				return true;
			}
		}
		return false;
	}
	
	private boolean setGeneral(ClientSkill cs, Effect effect, int a) {
		boolean hasGeneralEffects = false;
		String current = "effect" + a + "_";

		hasGeneralEffects |= setChange(cs, effect, current);
		if (!effect.getChange().isEmpty()) {
			for (Change change : effect.getChange())
				if (change.getConditions() == null) {
					Conditions cond = new Conditions();
					cond = getEffectConditions(cs, cond, current);
					if (cond != null) {
						effect.setConditions(cond);
						hasGeneralEffects = true;
					}
				}
		}
		hasGeneralEffects |= setSubEffect(cs, effect, current);
		hasGeneralEffects |= setModifiersOrSubConditions(cs, effect, current);
		
		
		if (JAXBHandler.getValue(cs, current + "hop_a") != null && JAXBHandler.getValue(cs, current + "hop_a") != 0) {
			effect.setHopa((Integer) JAXBHandler.getValue(cs, current + "hop_a"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "hop_b") != null && JAXBHandler.getValue(cs, current + "hop_b") != 0) {
			effect.setHopb((Integer) JAXBHandler.getValue(cs, current + "hop_b"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "hop_type") != null) {
			effect.setHoptype(HopType.fromClient(JAXBHandler.getValue(cs, current + "hop_type").toString()).toString());
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "critical_add_dmg_mod1") != null && JAXBHandler.getValue(cs, current + "critical_add_dmg_mod1") != 0) {
			effect.setCritadddmg1((Integer) JAXBHandler.getValue(cs, current + "critical_add_dmg_mod1"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "critical_add_dmg_mod2") != null && JAXBHandler.getValue(cs, current + "critical_add_dmg_mod2") != 0) {
			effect.setCritadddmg2((Integer) JAXBHandler.getValue(cs, current + "critical_add_dmg_mod2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "critical_prob_mod2") != null && (Integer) JAXBHandler.getValue(cs, current + "critical_prob_mod2") != 100) {
			effect.setCritprobmod2((Integer) JAXBHandler.getValue(cs, current + "critical_prob_mod2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != null
			&& (Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != 0 && (Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != 100) {
			effect.setPreeffectProb((Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "cond_preeffect") != null) {
			effect.setPreeffect(JAXBHandler.getValue(cs, current + "cond_preeffect").toString().replaceFirst("e", ""));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "reserved10") != null) {
			Element element = Element.fromClient(JAXBHandler.getValue(cs, current + "reserved10").toString());
			if (element != Element.NONE) {
				effect.setElement(element.toString());
				hasGeneralEffects = true;
			}
		}
		if (a > 1 && JAXBHandler.getValue(cs, current + "reserved_cond"+(a-1)+"_prob2") != null
			&& (Integer) JAXBHandler.getValue(cs, current + "reserved_cond"+(a-1)+"_prob2") != 0 && (Integer) JAXBHandler.getValue(cs, current + "reserved_cond"+(a-1)+"_prob2") != 100) {
			effect.setHittypeprob2((Integer) JAXBHandler.getValue(cs, current + "reserved_cond"+(a-1)+"_prob2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "reserved_cond1") != null) {
			HitType hitType = HitType.fromClient(JAXBHandler.getValue(cs, current + "reserved_cond1").toString());
			if (hitType != HitType.NONE) {
				effect.setHittype(hitType.toString());
				hasGeneralEffects = true;
			}
		}
		// effect.setMrresist(); //TODO: Remove, unused
		if (JAXBHandler.getValue(cs, current + "acc_mod1") != null && (Integer) JAXBHandler.getValue(cs, current + "acc_mod1") != 0) {
			effect.setAccmod1((Integer) JAXBHandler.getValue(cs, current + "acc_mod1"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "acc_mod2") != null && (Integer) JAXBHandler.getValue(cs, current + "acc_mod2") != 0) {
			effect.setAccmod2((Integer) JAXBHandler.getValue(cs, current + "acc_mod2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "noresist") != null && (Integer) JAXBHandler.getValue(cs, current + "noresist") == 1) {
			effect.setNoresist(true);
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "basiclv") != null && (Integer) JAXBHandler.getValue(cs, current + "basiclv") != 0) {
			effect.setBasiclvl((Integer) JAXBHandler.getValue(cs, current + "basiclv"));
			hasGeneralEffects = true;
		}
		effect.setE(a);
		if (JAXBHandler.getValue(cs, current + "effectid") != null && (Integer) JAXBHandler.getValue(cs, current + "effectid") != 0) {
			effect.setEffectid((Integer) JAXBHandler.getValue(cs, current + "effectid"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "randomtime") != null && (Integer) JAXBHandler.getValue(cs, current + "randomtime") != 0) {
			effect.setRandomtime((Integer) JAXBHandler.getValue(cs, current + "randomtime"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "remain1") != null && (Integer) JAXBHandler.getValue(cs, current + "remain1") != 0) {
			effect.setDuration1((Integer) JAXBHandler.getValue(cs, current + "remain1"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "remain2") != null && (Integer) JAXBHandler.getValue(cs, current + "remain2") != 0) {
			effect.setDuration2((Integer) JAXBHandler.getValue(cs, current + "remain2"));
			hasGeneralEffects = true;
		}
		// effect.setOnfly(); //TODO Remove, unused
		
		// [NOTICE] Delta & Value are set seperatly in each AbstractEffect
		
		return hasGeneralEffects;
	}
	
	private boolean setChange(ClientSkill cs, Effect e, String current) {
		Change change = new Change();
		List<Change> changes = new ArrayList<Change>();
		Map<StatModifiers, Integer> modifiers = new HashMap<StatModifiers, Integer>(); 
		int value = 0;
		
		if (e instanceof ArmorMasteryEffect || e instanceof ShieldMasteryEffect) {
			change.setStat(StatModifiers.PHYSICAL_DEFENSE.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof WeaponMasteryEffect) {
			change.setStat(StatModifiers.PHYSICAL_ATTACK.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof BoostDropRateEffect) {
			change.setStat(StatModifiers.BOOST_DROP_RATE.toString());
			change.setFunc("ADD");
		}
		else if (e instanceof OneTimeBoostHealEffect || e instanceof BoostHealEffect) {
			change.setStat(StatModifiers.HEAL_BOOST.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof BoostHateEffect) {
			change.setStat(StatModifiers.BOOST_HATE.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof SubTypeExtendDurationEffect) {
			change.setStat(StatModifiers.BOOST_DURATION_BUFF.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof SubTypeBoostResistEffect) {
			change.setStat(StatModifiers.BOOST_RESIST_DEBUFF.toString());
			change.setFunc("ADD");
		}
		else if (e instanceof ExtendAuraRangeEffect) {
			change.setStat(StatModifiers.BOOST_MANTRA_RANGE.toString());
			change.setFunc("ADD");
		}
		else if (e instanceof BoostSpellAttackEffect) {
			change.setStat(StatModifiers.BOOST_SPELL_ATTACK.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof DeboostHealEffect) {
			change.setStat(StatModifiers.HEAL_SKILL_BOOST.toString());
			if (getIntValue(cs, current, "reserved1") != 0) {change.setValue(getIntValue(cs, current, "reserved1"));}
			change.setFunc("PERCENT");
		}
		else if (e instanceof BoostSkillCastingTimeEffect) {
			change.setStat(StatModifiers.BOOST_CASTING_TIME_ATTACK.toString());
			change.setFunc("PERCENT");
		}
		else if (e instanceof HideEffect) {
			change.setStat(StatModifiers.SPEED.toString());
			if (getIntValue(cs, current, "reserved2") > 0 && getIntValue(cs, current, "reserved2") < 100) {change.setValue(100 -  getIntValue(cs, current, "reserved2"));}
			change.setFunc("PERCENT");
		}
		
		if (change.getValue() == 0)
			change.setValue(getIntValue(cs, current, "reserved2"));
		
		if (change.getStat() != null)
			changes.add(change);
		else
			modifiers = getClientModAndValue(cs, current);
		
		if (e instanceof CurseEffect) {
			modifiers.put(StatModifiers.MAXHP, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.MAXMP, getIntValue(cs, current, "reserved2"));
		}
		else if (e instanceof XPBoostEffect) {
			modifiers.put(StatModifiers.BOOST_CRAFTING_XP_RATE, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.BOOST_GATHERING_XP_RATE, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.BOOST_GROUP_HUNTING_XP_RATE, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.BOOST_HUNTING_XP_RATE, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.BOOST_QUEST_XP_RATE, getIntValue(cs, current, "reserved2"));
		}
		else if (e instanceof SnareEffect) {
			modifiers.put(StatModifiers.SPEED, getIntValue(cs, current, "reserved2"));
			modifiers.put(StatModifiers.FLY_SPEED, getIntValue(cs, current, "reserved2"));
		}
		else if (e instanceof SlowEffect)
			modifiers.put(StatModifiers.ATTACK_SPEED, getIntValue(cs, current, "reserved2"));
		
		if (!modifiers.isEmpty()) {
			Conditions cond = new Conditions();
			for (StatModifiers mod : modifiers.keySet()) {
				change = new Change();
				
				// Conditions
				cond = getEffectConditions(cs, cond, current);
				if (cond != null)
					change.setConditions(cond);
				
				// Stat
				change.setStat(mod.toString());
				// Func
				change.setFunc("ADD");
				if (getIntValue(cs, current, "reserved6") == 1)
					change.setFunc("PERCENT");
				if (getIntValue(cs, current, "reserved4") != 0 && getIntValue(cs, current, "reserved2") == 0)
					change.setFunc("PERCENT");
				// Value
				change.setValue(modifiers.get(mod));
				// Delta
				if (getIntValue(cs, current, "reserved1") != 0 && (getIntValue(cs, current, "reserved2") != 0 || getIntValue(cs, current, "reserved4") != 0))
					change.setDelta(getIntValue(cs, current, "reserved1"));
				
				// change.setUnchecked(); //TODO: Remove, unused
				changes.add(change);
			}
		}
		// Correcting some specificities
		for (Change c : changes) {
			if (c.getStat().equalsIgnoreCase("ATTACK_SPEED"))
				c.setValue(c.getValue() * -1);
			if (e instanceof StatdownEffect || e instanceof SlowEffect || e instanceof SnareEffect || e instanceof CurseEffect || e instanceof DeboostHealEffect || e instanceof HideEffect) {
				if (c.getValue() > 0)
					c.setValue(c.getValue() * -1);
			}
			if (c.getStat().equalsIgnoreCase("ATTACK_RANGE") && c.getFunc().equalsIgnoreCase("ADD"))
				c.setValue(c.getValue() * 1000);
			if (c.getStat().equalsIgnoreCase("FLY_SPEED") && c.getFunc().equalsIgnoreCase("ADD"))
				c.setValue(c.getValue() * 100);
			if (c.getStat().equalsIgnoreCase("SPEED") && c.getFunc().equalsIgnoreCase("ADD"))
				c.setValue(c.getValue() * 100);
		}
		if (!changes.isEmpty()) {
			e.getChange().addAll(changes);
			return true;
		} else {
			return false;
		}
	}
	
	private Map<StatModifiers, Integer> getClientModAndValue(ClientSkill cs, String current) {
	
		Map<StatModifiers, Integer> modifiers = new HashMap<StatModifiers, Integer>();
		
		if (JAXBHandler.getValue(cs, current + "reserved13") != null && StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved13").toString()) != StatModifiers.NONE) {
			int value = 0;
			StatModifiers stat = StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved13").toString());
			
			if (getIntValue(cs, current, "reserved2") != 0)
				value = getIntValue(cs, current, "reserved2");
			else if (getIntValue(cs, current, "reserved4") != 0)
				value = getIntValue(cs, current, "reserved4");
			else if (getIntValue(cs, current, "reserved1") != 0)
				value = getIntValue(cs, current, "reserved1");
			
			if (stat.getLinked().isEmpty())
				modifiers.put(stat, value);
			else {
				for (String ficelle : stat.getLinked())
					modifiers.put(StatModifiers.fromValue(ficelle), value);
			}
		}
		if (JAXBHandler.getValue(cs, current + "reserved14") != null && StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved14").toString()) != StatModifiers.NONE) {
			int value = 0;
			StatModifiers stat = StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved14").toString());
			
			if (getIntValue(cs, current, "reserved4") != 0)
				value = getIntValue(cs, current, "reserved4");
			else if (getIntValue(cs, current, "reserved2") != 0)
				value = getIntValue(cs, current, "reserved2");
			else if (getIntValue(cs, current, "reserved1") != 0)
				value = getIntValue(cs, current, "reserved1");
			
			if (stat.getLinked().isEmpty())
				modifiers.put(stat, value);
			else {
				for (String ficelle : stat.getLinked())
					modifiers.put(StatModifiers.fromValue(ficelle), value);
			}
		}
		if (JAXBHandler.getValue(cs, current + "reserved18") != null && StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved18").toString()) != StatModifiers.NONE) {
			int value = 0;
			StatModifiers stat = StatModifiers.fromClient(JAXBHandler.getValue(cs, current + "reserved18").toString());
			
			if (getIntValue(cs, current, "reserved16") != 0)
				value = getIntValue(cs, current, "reserved16");
			
			if (stat.getLinked().isEmpty())
				modifiers.put(stat, value);
			else {
				for (String ficelle : stat.getLinked())
					modifiers.put(StatModifiers.fromValue(ficelle), value);
			}
		}
		return modifiers;
	}
	
	private Conditions getEffectConditions(ClientSkill cs, Conditions conditions, String current) {
		boolean hasConditions = false;
		
		if (JAXBHandler.getValue(cs, current + "cond_status") != null) {
		
			String cond = JAXBHandler.getValue(cs, current + "cond_status").toString().toUpperCase().trim();
			
			if (cond.equalsIgnoreCase("_NFLYING")) {
				NoFlyingCondition noFly = new NoFlyingCondition();
				conditions.getMpAndHpAndDp().add(noFly);
				hasConditions = true;
			}
			else if (cond.equalsIgnoreCase("_FLYING")) {
				TargetFlyingCondition fly = new TargetFlyingCondition();
				conditions.getMpAndHpAndDp().add(fly);
				hasConditions = true;
			}
			else if (EffectType.fromClient(cond) != null) {
				AbnormalStateCondition abnormalCond = new AbnormalStateCondition();
				abnormalCond.setValue(EffectType.fromClient(cond).toString());
				conditions.getMpAndHpAndDp().add(abnormalCond);
				hasConditions = true;
			}
			else
				try {int poubelle = Integer.parseInt(cond);} catch (Exception ex) {System.out.println("[CONDITIONS] Undeclared Condition : " + cond);}
		}
		else if (JAXBHandler.getValue(cs, current + "cond_attack_dir") != null && JAXBHandler.getValue(cs, current + "cond_attack_dir") != 0) {
			BackCondition back = new BackCondition();
			conditions.getMpAndHpAndDp().add(back);
			hasConditions = true;
		}
		else if (getIntValue(cs, current, "reserved9") == 1) {
			OnFlyCondition onFly = new OnFlyCondition();
			conditions.getMpAndHpAndDp().add(onFly);
			hasConditions = true;
		}
		else if (JAXBHandler.getValue(cs, current + "reserved5") != null && JAXBHandler.getValue(cs, current + "reserved13") != null) {
			WeaponCondition weapon = new WeaponCondition();
			
			String weaponS = JAXBHandler.getValue(cs, current + "reserved5").toString().toUpperCase().trim();
			
			List<String> weapons = new ArrayList<String>();
			Collections.addAll(weapons, weaponS.split(","));
			
			for (String weap : weapons) {
				weap = weap.trim();
				if (WeaponType.fromClient(weap) != null)
					weapon.getWeapon().add(WeaponType.fromClient(weap).toString());
			}
			conditions.getMpAndHpAndDp().add(weapon);
			hasConditions = true;
		}
		//TODO: Implement ArmorCondition ?
		
		if (hasConditions)
			return conditions;
		else
			return null;
	}
	
	private boolean setSubEffect(ClientSkill cs, Effect effect, String current) {
		boolean hasSubEffect = false;
		String s = null;
		int skillId = 0;
		
		if (getIntValue(cs, current, "reserved14") == 0 && JAXBHandler.getValue(cs, current + "reserved14") != null)
			s = JAXBHandler.getValue(cs, current + "reserved14").toString();
		else if (getIntValue(cs, current, "reserved15") == 0 && JAXBHandler.getValue(cs, current + "reserved15") != null)
			s = JAXBHandler.getValue(cs, current + "reserved15").toString();
		else if (getIntValue(cs, current, "reserved7") == 0 && JAXBHandler.getValue(cs, current + "reserved7") != null)
			s = JAXBHandler.getValue(cs, current + "reserved7").toString();
		
		// Computing skillId
		if (!Strings.isNullOrEmpty(s)) {
			if (getSkillId(s) != 0) {skillId = getSkillId(s);}
			else if (getSkillId("NormalAttack_" + s) != 0) {skillId = getSkillId("NormalAttack_" + s);}
			else if (getSkillId(s.substring(1)) != 0) {skillId = getSkillId(s.substring(1));}
			else if (s.matches(".*\\d.*")) {
				Scanner in = new Scanner("s").useDelimiter("[^0-9]+");
				if (in.hasNext()) {
					int i = in.nextInt();
					if (i != 0) {
						s = s.replaceAll(".*\\d.*", "") + "_" + i;
						if (getSkillId(s) != 0) {skillId = getSkillId(s);}
						else if (getSkillId("NormalAttack_" + s) != 0) {skillId = getSkillId("NormalAttack_" + s);}
					}
				}
			}
		}
		
		if (skillId != 0) {
			SubEffect sub = new SubEffect();
			sub.setSkillId(skillId);
			if (getIntValue(cs, current, "reserved18") > 0 && getIntValue(cs, current, "reserved18") < 100)
				sub.setChance(getIntValue(cs, current, "reserved18"));
			if (effect instanceof SignetBurstEffect)
				sub.setAddeffect(true);
			effect.setSubeffect(sub);
			hasSubEffect = true;
		}
	
		return hasSubEffect;
	}
	
	private boolean setModifiersOrSubConditions(ClientSkill cs, Effect effect, String current) {
		boolean hasModifiers = false; boolean hasConditions = false;
		
		ActionModifiers modifiers = new ActionModifiers();
		Conditions conditions = new Conditions();
		
		if (JAXBHandler.getValue(cs, current + "reserved16") != null) {
		
			String modString = JAXBHandler.getValue(cs, current + "reserved16").toString().toUpperCase().trim();
			
			List<String> modStrings = new ArrayList<String>();
			Collections.addAll(modStrings, modString.split(","));
			
			for (String s : modStrings) {
				s= s.trim();
				boolean add = false;
				
				if (s.equalsIgnoreCase("_NFLYING")) {
					NoFlyingCondition noFly = new NoFlyingCondition();
					conditions.getMpAndHpAndDp().add(noFly);
					hasConditions = true;
				}
				else if (s.startsWith("_RACE_") && Race.fromClient(s.replaceFirst("_RACE_", "")) != Race.NONE) {
					TargetRaceDamageModifier race = new TargetRaceDamageModifier();
					race.setRace(Race.fromClient(s.replaceFirst("_RACE_", "")).toString());
					add |= setModifiersGeneral(cs, race, current);
					if (add) {
						modifiers.getBackdamageAndFrontdamageAndAbnormaldamage().add(race);
						hasModifiers = true;
					}
				}
				else if (s.startsWith("_CLASS_") && PlayerClass.fromClient(s.replaceFirst("_CLASS_", "")) != null) {
					// TODO: Implement
				}
				else if (s.equalsIgnoreCase("_BACK") || s.equalsIgnoreCase("BACK")) {
					BackDamageModifier back = new BackDamageModifier();
					add |= setModifiersGeneral(cs, back, current);
					if (add) {
						modifiers.getBackdamageAndFrontdamageAndAbnormaldamage().add(back);
						hasModifiers = true;
					}
				}
				else if (s.equalsIgnoreCase("_FRONT") || s.equalsIgnoreCase("FRONT")) {
					FrontDamageModifier front = new FrontDamageModifier();
					add |= setModifiersGeneral(cs, front, current);
					if (add) {
						modifiers.getBackdamageAndFrontdamageAndAbnormaldamage().add(front);
						hasModifiers = true;
					}
				}
				else if (EffectType.fromClient(s) != null) {
					AbnormalDamageModifier abnormalMod = new AbnormalDamageModifier();
					abnormalMod.setState(EffectType.fromClient(s).toString());
					add |= setModifiersGeneral(cs, abnormalMod, current);
					if (add) {
						modifiers.getBackdamageAndFrontdamageAndAbnormaldamage().add(abnormalMod);
						hasModifiers = true;
					}
					else {
						AbnormalStateCondition abnormalCond = new AbnormalStateCondition();
						abnormalCond.setValue(EffectType.fromClient(s).toString());
						conditions.getMpAndHpAndDp().add(abnormalCond);
						hasConditions = true;
					}
				}
				else
					try {int poubelle = Integer.parseInt(s);} catch (Exception ex) {System.out.println("[MODIFIERS] Undeclared ActionModifier : " + s);}
			}
			
			if (hasModifiers)
				effect.setModifiers(modifiers);
			if (hasConditions)
				effect.setSubconditions(conditions);
		}
		return hasModifiers;
	}
	
	private boolean setModifiersGeneral(ClientSkill cs, ActionModifier mod, String current) {		
		boolean hasGeneral = false;
		// Set the general properties of the modifier
		if (getIntValue(cs, current, "reserved7") == 1) {mod.setMode("PERCENT");}
		if (getIntValue(cs, current, "reserved10") != 0) {
			if (getIntValue(cs, current, "reserved10") != 0) {mod.setValue(getIntValue(cs, current, "reserved10")); hasGeneral = true;}
			if (getIntValue(cs, current, "reserved9") != 0) {mod.setDelta(getIntValue(cs, current, "reserved9"));}
		}
		else { // if reserved10 instanceof Element
			if (getIntValue(cs, current, "reserved9") != 0) {mod.setValue(getIntValue(cs, current, "reserved9")); hasGeneral = true;}
			if (getIntValue(cs, current, "reserved8") != 0) {mod.setDelta(getIntValue(cs, current, "reserved8"));}
		}
		return hasGeneral;
	}

	
	
	private int getIntValue(ClientSkill cs, String current, String property) {
		int value = 0;
		try {	
			value = Integer.parseInt(JAXBHandler.getValue(cs, current + property).toString());
		} catch (Exception ex) {
			// System.out.println("[SKILLS] " + property + " is a String for : " + JAXBHandler.getValue(cs, "id").toString());
		}		
		return value;
	}
	
	private float getFloatValue(ClientSkill cs, String current, String property) {
		float value = 0;
		try {
			value = Float.parseFloat(JAXBHandler.getValue(cs, current + property).toString());
		} catch (Exception ex) {
			// System.out.println("[SKILLS] " + property + " is a String for : " + JAXBHandler.getValue(cs, "id").toString());
		}		
		return value;
	}
}