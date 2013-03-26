package com.parser.write.aion.skills;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
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
	Map<String, ClientItem> stigmaItemMap = null;
	
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
				st.setPenaltySkillId(getSkillId(JAXBHandler.getValue(cs, "penalty_skill_succ").toString()));
			
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
			if (JAXBHandler.getValue(cs, "target_slot") != null)
				st.setTslot(TargetSlot.fromClient(JAXBHandler.getValue(cs, "target_slot").toString()).toString());
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
			if ((Integer) JAXBHandler.getValue(cs, "delay_time") != null)
				st.setCooldown((int)((Integer) JAXBHandler.getValue(cs, "delay_time") / 100f));
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
			if (JAXBHandler.getValue(cs, "dispel_category") == null) // TODO: If Debuff & require dispel level
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
				if (JAXBHandler.getValue(cs, "add_wpn_range") != 0) {
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
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, hp);
				}
				
				if (JAXBHandler.getValue(cs, "cost_parameter") != null && StringUtils.containsIgnoreCase(JAXBHandler.getValue(cs, "cost_parameter").toString(), "MP") && !isPeriodic(cs) && getCost(cs) != 0) {
					MpCondition mp = new MpCondition();
					hasStartConditions = setConditions(cs, start, mp);
				}
				
				if (JAXBHandler.getValue(cs, "cost_dp") != null && JAXBHandler.getValue(cs, "cost_dp") != 0) {
					DpCondition dp = new DpCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, dp);
				}
				
				WeaponCondition weap = new WeaponCondition();
				hasStartConditions = setConditions(cs, start, weap);
				
				if (JAXBHandler.getValue(cs, "required_leftweapon") != null) {
					ArmorCondition armor = new ArmorCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, armor);
				}
				
				if (JAXBHandler.getValue(cs, "use_arrow") != null && (Integer) JAXBHandler.getValue(cs, "use_arrow") != 0) {
					hasStartConditions = setConditions(cs, start, new ArrowCheckCondition());
				}
				
				if (JAXBHandler.getValue(cs, "self_flying_restriction") != null) {
					SelfFlyingCondition selfFlying = new SelfFlyingCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, selfFlying);
				}
				
				if (JAXBHandler.getValue(cs, "target_species_restriction") != null
					&& (JAXBHandler.getValue(cs, "target_species_restriction").toString().equalsIgnoreCase("PC") || JAXBHandler.getValue(cs, "target_species_restriction").toString().equalsIgnoreCase("NPC"))) {
					TargetCondition target = new TargetCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, target);
				}
				
				if (JAXBHandler.getValue(cs, "target_flying_restriction") != null) {
					TargetFlyingCondition targetFlying = new TargetFlyingCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, targetFlying);
				}
				
				if (JAXBHandler.getValue(cs, "nouse_combat_state") != null && (Integer) JAXBHandler.getValue(cs, "nouse_combat_state") != 0) {
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, new CombatCheckCondition());
				}
				
				ChainCondition chain = new ChainCondition();
				if (!hasStartConditions)
					hasStartConditions = setConditions(cs, start, chain);
				
				if (JAXBHandler.getValue(cs, "allow_use_form_category") != null) {
					FormCondition form = new FormCondition();
					if (!hasStartConditions)
						hasStartConditions = setConditions(cs, start, form);
				}
				
			if (hasStartConditions)			
				st.setStartconditions(start);
			
			// Use Conditions
			Conditions use = new Conditions();
				boolean hasUseConditions = false;
			
				if (JAXBHandler.getValue(cs, "target_stop") != null && (Integer) JAXBHandler.getValue(cs, "target_stop") == 0) {
					PlayerMovedCondition move = new PlayerMovedCondition();
					if (!hasUseConditions)
						hasUseConditions = setConditions(cs, use, move);
				}
				
			if (hasUseConditions)			
				st.setUseconditions(use);
			
			// Use Equipments Conditions
			Conditions equip = new Conditions();
			boolean hasEquipConditions = false;
				
				if (JAXBHandler.getValue(cs, "required_leftweapon") != null
					&& JAXBHandler.getValue(cs, "sub_type") != null && JAXBHandler.getValue(cs, "sub_type").toString().equalsIgnoreCase("buff")) {
					ArmorCondition armor = new ArmorCondition();
					if (!hasEquipConditions)
						hasEquipConditions = setConditions(cs, equip, armor);
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
						if (!hasEffects)
							hasEffects = computeEffects(cs, effects, EffectType.fromClient(JAXBHandler.getValue(cs, "effect"+a+"_type").toString()), a);
					}
				}
				
				/* EFFECTS CONDITIONS
				if (!Strings.isNullOrEmpty(cs.get()) && cs.get().equalsIgnoreCase("_nflying")) {equip.getMpAndHpAndDp().add(new NoFlyingCondition()); hasEquipConditions = true;} // Effects (TODO)
				if (!Strings.isNullOrEmpty(cs.get()) && cs.get().equalsIgnoreCase("_flying")) {equip.getMpAndHpAndDp().add(new OnFlyCondition()); hasEquipConditions = true;} // Effects (TODO)
				if (cs.get()) { // Effects (TODO) effect1_reserved16 (if Spellatk_inst)
					AbnormalStateCondition abnormal = new AbnormalStateCondition();
					hasEquipConditions = setConditions(cs, start, move);
				}
				if (cs.get()) {equip.getMpAndHpAndDp().add(new BackCondition()); hasEquipConditions = true;} // Effects (TODO) effect1_reserved16 (_back)
				*/
				
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
					// if (getChecktimeDelta(cs) != 0) {mp.setDelta(getChecktimeDelta(cs));} // TODO : Add ?
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
		System.out.println("[SKILLS] Skills written : " + templateList.size());
	}
	
	/******* EXTRA ********/
	
	private String getName(String s) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringText(s) : "";}
	private int getNameId(String s, int mult, int plus) {return (s != null) ? new AionDataCenter().getInstance().getMatchingStringId(s, mult, plus) : 0;}
	private int getItemId(String s) {return (s != null) ? new AionDataCenter().getInstance().getItemIdByName(s) : 0;}
	private int getSkillId(String s) {return (s != null) ? new AionDataCenter().getInstance().getSkillIdByName(s) : 0;}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	private int getWorld(String s) {return (s != null) ? new AionDataCenter().getInstance().getWorldIdByName(s) : 0;}
	
	private int getSkillLevel(ClientSkill cs) {
		int level1 = 0;
		int level2 = 0;
		
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
			System.out.println("[SKILLS] Stigma method 1 returned a result for : " + name.toUpperCase() + " but not 2");
			return rm1;
		}
		if (rm1 == null && rm2 != null) {
			System.out.println("[SKILLS] Stigma method 2 returned a result for : " + name.toUpperCase() + " but not 1");
			return rm2;
		}
		
		return null;
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
			conditions.getMpAndHpAndDp().add(hp);
			hasConditions = true;
		}
		else if (c instanceof MpCondition) {
			MpCondition mp = (MpCondition) c;
			mp.setValue(getCost(cs));
			mp.setDelta(getDelta(cs));
			if (JAXBHandler.getValue(cs, "cost_parameter").toString().equalsIgnoreCase("MP_RATIO")) {mp.setRatio(true);}
			conditions.getMpAndHpAndDp().add(mp);
			hasConditions = true;
		}
		else if (c instanceof DpCondition) {
			DpCondition dp = (DpCondition) c;
			dp.setValue((Integer) JAXBHandler.getValue(cs, "cost_dp"));
			conditions.getMpAndHpAndDp().add(dp);
			hasConditions = true;
		}
		else if (c instanceof WeaponCondition) {
			WeaponCondition weap = (WeaponCondition) c;
			if (JAXBHandler.getValue(cs, "required_sword") != null && JAXBHandler.getValue(cs, "required_sword") == 1) {weap.getWeapon().add(WeaponType.SWORD_1H.toString()); hasConditions = true;}	
			if (JAXBHandler.getValue(cs, "required_mace") != null && JAXBHandler.getValue(cs, "required_mace") == 1) {weap.getWeapon().add(WeaponType.MACE_1H.toString()); hasConditions = true;}	
			if (JAXBHandler.getValue(cs, "required_dagger") != null && JAXBHandler.getValue(cs, "required_dagger") == 1) {weap.getWeapon().add(WeaponType.DAGGER_1H.toString()); hasConditions = true;}	
			if (JAXBHandler.getValue(cs, "required_2hsword") != null && JAXBHandler.getValue(cs, "required_2hsword") == 1) {weap.getWeapon().add(WeaponType.SWORD_2H.toString()); hasConditions = true;}	
			if (JAXBHandler.getValue(cs, "required_polearm") != null && JAXBHandler.getValue(cs, "required_polearm") == 1) {weap.getWeapon().add(WeaponType.POLEARM_2H.toString()); hasConditions = true;}	
			if (JAXBHandler.getValue(cs, "required_staff") != null && JAXBHandler.getValue(cs, "required_staff") == 1) {weap.getWeapon().add(WeaponType.STAFF_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_bow") != null && JAXBHandler.getValue(cs, "required_bow") == 1) {weap.getWeapon().add(WeaponType.BOW.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_orb") != null && JAXBHandler.getValue(cs, "required_orb") == 1) {weap.getWeapon().add(WeaponType.ORB_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_book") != null && JAXBHandler.getValue(cs, "required_book") == 1) {weap.getWeapon().add(WeaponType.BOOK_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_gun") != null && JAXBHandler.getValue(cs, "required_gun") == 1) {weap.getWeapon().add(WeaponType.GUN_1H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_cannon") != null && JAXBHandler.getValue(cs, "required_cannon") == 1) {weap.getWeapon().add(WeaponType.CANON_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_harp") != null && JAXBHandler.getValue(cs, "required_harp") == 1) {weap.getWeapon().add(WeaponType.HARP_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_keyblade") != null && JAXBHandler.getValue(cs, "required_keyblade") == 1) {weap.getWeapon().add(WeaponType.KEYBLADE_2H.toString()); hasConditions = true;}
			if (JAXBHandler.getValue(cs, "required_keyhammer") != null && JAXBHandler.getValue(cs, "required_keyhammer") == 1) {weap.getWeapon().add(WeaponType.KEYHAMMER_2H.toString()); hasConditions = true;}
			// if (JAXBHandler.getValue(cs, "required_ride_robot") == 1) {weap.getWeapon().add(WeaponType.RIDE_ROBOT.toString()); hasConditions = true;}
			conditions.getMpAndHpAndDp().add(weap);
		}
		else if (c instanceof ArmorCondition) {
			ArmorCondition armor = (ArmorCondition) c;
			armor.setArmor(ArmorType.fromClient(JAXBHandler.getValue(cs, "required_leftweapon").toString()).toString());
			conditions.getMpAndHpAndDp().add(armor);
			hasConditions = true;
		}
		else if (c instanceof ArrowCheckCondition) {
			ArrowCheckCondition arrow = (ArrowCheckCondition) c;
			conditions.getMpAndHpAndDp().add(arrow);
			hasConditions = true;
		}
		else if (c instanceof PlayerMovedCondition) {
			PlayerMovedCondition move = (PlayerMovedCondition) c;
			move.setAllow(false);
			conditions.getMpAndHpAndDp().add(move);
			hasConditions = true;
		}/*
		else if (c instanceof AbnormalStateCondition) {
			AbnormalStateCondition abnormal = (AbnormalStateCondition) c;
			// abnormal.setValue(AbnormalState.fromClient(cs.get())); // TODO
			conditions.getMpAndHpAndDp().add(abnormal);
			hasConditions = true;
		}*/
		else if (c instanceof TargetCondition) {
			TargetCondition target = (TargetCondition) c;
			target.setValue(TargetAttribute.fromClient(JAXBHandler.getValue(cs, "target_species_restriction").toString()).toString());
			conditions.getMpAndHpAndDp().add(target);
			hasConditions = true;
		}
		else if (c instanceof TargetFlyingCondition) {
			TargetFlyingCondition targetFlying = (TargetFlyingCondition) c;
			targetFlying.setRestriction(FlyingRestriction.fromClient(JAXBHandler.getValue(cs, "target_flying_restriction").toString()).toString());
			conditions.getMpAndHpAndDp().add(targetFlying);
			hasConditions = true;
		}
		else if (c instanceof SelfFlyingCondition) {
			SelfFlyingCondition selfFlying = (SelfFlyingCondition) c;
			selfFlying.setRestriction(FlyingRestriction.fromClient(JAXBHandler.getValue(cs, "self_flying_restriction").toString()).toString());
			conditions.getMpAndHpAndDp().add(selfFlying);
			hasConditions = true;
		}/*
		else if (c instanceof OnFlyCondition) {
			OnFlyCondition onFly = (OnFlyCondition) c;
			// TODO (get from a Map<Condition, value> for each Item ?)
			conditions.getMpAndHpAndDp().add(onFly);
			hasConditions = true;
		}
		else if (c instanceof NoFlyingCondition) {
			NoFlyingCondition noFly = (NoFlyingCondition) c;
			// TODO
			conditions.getMpAndHpAndDp().add(noFly);
			hasConditions = true;
		}*/
		else if (c instanceof ChainCondition) {
			ChainCondition chain = (ChainCondition) c;
			if (JAXBHandler.getValue(cs, "self_chain_count") != null && JAXBHandler.getValue(cs, "self_chain_count") != 0) {
				chain.setSelfcount((Integer) JAXBHandler.getValue(cs, "self_chain_count"));
				hasConditions = true;
			}
			if (JAXBHandler.getValue(cs, "prechain_count") != null && JAXBHandler.getValue(cs, "prechain_count") != 0) {
				chain.setPrecount((Integer) JAXBHandler.getValue(cs, "prechain_count"));
				hasConditions = true;
			}
			if (JAXBHandler.getValue(cs, "chain_time") != null && JAXBHandler.getValue(cs, "chain_time") != 0) {
				chain.setTime((Integer) JAXBHandler.getValue(cs, "chain_time"));
				hasConditions = true;
			}
			if (JAXBHandler.getValue(cs, "prechain_category_name") != null) {
				chain.setPrecategory(JAXBHandler.getValue(cs, "prechain_category_name").toString().toUpperCase());
				hasConditions = true;
			}
			if (JAXBHandler.getValue(cs, "chain_category_name") != null) {
				chain.setCategory(JAXBHandler.getValue(cs, "chain_category_name").toString().toUpperCase());
				hasConditions = true;
			}
			conditions.getMpAndHpAndDp().add(chain);
		}/*
		else if (c instanceof BackCondition) {
			BackCondition back = (BackCondition) c;
			// TODO
			conditions.getMpAndHpAndDp().add(back);
			hasConditions = true;
		}*/
		else if (c instanceof FormCondition) {
			FormCondition form = (FormCondition) c;
			form.setValue(TransformType.fromClient(JAXBHandler.getValue(cs, "allow_use_form_category").toString()).toString());
			conditions.getMpAndHpAndDp().add(form);
			hasConditions = true;
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
		boolean hasEffects = false;
		String current = "effect" + a + "_";
		
		if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractOverTimeEffect")) {
			
			if (et == EffectType.HEAL) {
				HealEffect heal = new HealEffect();
				hasEffects = setGeneral(cs, heal, a);
				hasEffects = setAbstractOverTimeEffect(cs, heal, current);
				effects.getRootAndStunAndSleep().add(heal);
			}
			if (et == EffectType.MPHEAL) {
				MPHealEffect mpHeal = new MPHealEffect();
				hasEffects = setGeneral(cs, mpHeal, a);
				hasEffects = setAbstractOverTimeEffect(cs, mpHeal, current);
				effects.getRootAndStunAndSleep().add(mpHeal);
			}
			if (et == EffectType.FPHEAL) {
				FPHealEffect fpHeal = new FPHealEffect();
				hasEffects = setGeneral(cs, fpHeal, a);
				hasEffects = setAbstractOverTimeEffect(cs, fpHeal, current);
				effects.getRootAndStunAndSleep().add(fpHeal);
			}
			if (et == EffectType.DPHEAL) {
				DPHealEffect dpHeal = new DPHealEffect();
				hasEffects = setGeneral(cs, dpHeal, a);
				hasEffects = setAbstractOverTimeEffect(cs, dpHeal, current);
				effects.getRootAndStunAndSleep().add(dpHeal);
			}
		}
		
		return hasEffects;
	}
	
	private boolean setGeneral(ClientSkill cs, Effect effect, int a) {
		boolean hasGeneralEffects = false;
		String current = "effect" + a + "_";
		
		/**
		protected SubEffect subeffect;
		protected ActionModifiers modifiers;
		protected List<Change> change;
		protected Conditions conditions;
		protected Conditions subconditions;
		**/
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
		// effect.setCritadddmg1();
		// effect.setCritadddmg2();
		// effect.setCritprobmod2();
		if (JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != null
			&& (Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != 0 && (Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2") != 100) {
			effect.setPreeffectProb((Integer) JAXBHandler.getValue(cs, current + "cond_preeffect_prob2"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "cond_preeffect") != null) {
			effect.setPreeffect(JAXBHandler.getValue(cs, current + "cond_preeffect").toString().replaceFirst("e", ""));
			hasGeneralEffects = true;
		}
		// effect.setElement(); //SkillElement
		// effect.setHittypeprob2();
		// effect.setHittype(); //HitType
		// effect.setMrresist(); //boolean
		// effect.setAccmod1();
		// effect.setAccmod2();
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
		 //TODO
		// effect.setRandomtime();
		if (JAXBHandler.getValue(cs, current + "remain1") != null && (Integer) JAXBHandler.getValue(cs, current + "remain1") != 0) {
			effect.setDuration1((Integer) JAXBHandler.getValue(cs, current + "remain1"));
			hasGeneralEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "remain2") != null && (Integer) JAXBHandler.getValue(cs, current + "remain2") != 0) {
			effect.setDuration2((Integer) JAXBHandler.getValue(cs, current + "remain2"));
			hasGeneralEffects = true;
		}
		// effect.setOnfly();
		
		// Delta & Value (input propety changes with ???)
		
		// TODO: Selon quoi ce paramètre n'est plus Delta (move to Abstract if its the condition)
		if (JAXBHandler.getValue(cs, current + "reserved8") != null) {
			int delta = 0;
			try {
				delta = Integer.parseInt(JAXBHandler.getValue(cs, current + "reserved8").toString());
			} catch (Exception ex) {
				System.out.println("[SKILLS] Delta is a String for : " + JAXBHandler.getValue(cs, "name").toString());
			}
			if (delta != 0) {
				effect.setDelta(delta);
				hasGeneralEffects = true;
			}
		}
		// TODO: Selon quoi ce paramètre n'est plus Value (move to Abstract if its the condition)
		if (JAXBHandler.getValue(cs, current + "reserved9") != null) {
			int value = 0;
			try {
				value = Integer.parseInt(JAXBHandler.getValue(cs, current + "reserved9").toString());
			} catch (Exception ex) {
				System.out.println("[SKILLS] Value is a String for : " + JAXBHandler.getValue(cs, "name").toString());
			}
			if (value != 0) {
				effect.setValue(value);
				hasGeneralEffects = true;
			}
		}
		
		return hasGeneralEffects;
	}
	
	private boolean setAbstractOverTimeEffect(ClientSkill cs, AbstractOverTimeEffect effect, String current) {
		boolean hasEffects = false;
		if (JAXBHandler.getValue(cs, current + "checktime") != null && JAXBHandler.getValue(cs, current + "checktime") != 0) {
			effect.setChecktime((Integer) JAXBHandler.getValue(cs, current + "checktime"));
			hasEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "reserved6") != null && JAXBHandler.getValue(cs, current + "reserved6") == 1) {
			effect.setPercent(true);
			hasEffects = true;
		}
		return hasEffects;
	}
}
