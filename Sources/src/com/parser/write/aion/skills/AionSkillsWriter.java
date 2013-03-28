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
	Map<String, ClientItem> stigmaItemMap = new HashMap<String, ClientItem>();
	List<Integer> effectIds = null; //TODO
	
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
		// for skillBaseList if JAXBHandler.getValue(cs, "effect"+a+"_type") != null
		// et = EffectType.fromClient(JAXBHandler.getValue(cs, "effect"+a+"_type").toString()) !
		// if et != null && et == EffectType.DISPEL
		// effectIds.addAll(getEffectIds(cs, "effect"+a+"_");
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
			if (JAXBHandler.getValue(cs, "dispel_category") == null) // TODO: if debuf, check all dispellable effectids, if inside, false
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
		int level1 = 1;
		int level2 = 1;
		
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
			if (JAXBHandler.getValue(cs, "self_chain_count") != null && JAXBHandler.getValue(cs, "self_chain_count") != 0) {chain.setSelfcount((Integer) JAXBHandler.getValue(cs, "self_chain_count"));}
			if (JAXBHandler.getValue(cs, "prechain_count") != null && JAXBHandler.getValue(cs, "prechain_count") != 0) {chain.setPrecount((Integer) JAXBHandler.getValue(cs, "prechain_count"));}
			if (JAXBHandler.getValue(cs, "chain_time") != null && JAXBHandler.getValue(cs, "chain_time") != 0) {chain.setTime((Integer) JAXBHandler.getValue(cs, "chain_time"));}
			if (JAXBHandler.getValue(cs, "prechain_category_name") != null) {chain.setPrecategory(JAXBHandler.getValue(cs, "prechain_category_name").toString().toUpperCase());}
			if (JAXBHandler.getValue(cs, "chain_category_name") != null) {chain.setCategory(JAXBHandler.getValue(cs, "chain_category_name").toString().toUpperCase());}
			if (chain.getSelfcount() != null || chain.getPrecount() != null || chain.getTime() != null || chain.getPrecategory() != null || chain.getCategory() != null) {
				conditions.getMpAndHpAndDp().add(chain);
				hasConditions = true;
			}
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
			if (et == EffectType.MPHEAL) {
				MPHealEffect mpHeal = new MPHealEffect();
				if (setGeneral(cs, mpHeal, a) | setAbstractOverTimeEffect(cs, mpHeal, current)) {
					effects.getRootAndStunAndSleep().add(mpHeal);
					compute = true;
				}
			}
			if (et == EffectType.FPHEAL) {
				FPHealEffect fpHeal = new FPHealEffect();
				if (setGeneral(cs, fpHeal, a) | setAbstractOverTimeEffect(cs, fpHeal, current)) {
					effects.getRootAndStunAndSleep().add(fpHeal);
					compute = true;
				}
			}
			if (et == EffectType.DPHEAL) {
				DPHealEffect dpHeal = new DPHealEffect();
				if (setGeneral(cs, dpHeal, a) | setAbstractOverTimeEffect(cs, dpHeal, current)) {
					effects.getRootAndStunAndSleep().add(dpHeal);
					compute = true;
				}
			}
			// SpellAtackEffect
			if (et == EffectType.SPELLATK) {
				SpellAttackEffect spell = new SpellAttackEffect();
				if (setGeneral(cs, spell, a) | setAbstractOverTimeEffect(cs, spell, current)) {
					effects.getRootAndStunAndSleep().add(spell);
					compute = true;
				}
			}
			// SpellAtkDrainEffect
			if (et == EffectType.SPELLATKDRAIN) {
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
			if (et == EffectType.MPATTACK) {
				MpAttackEffect mpAttack = new MpAttackEffect();
				if (setGeneral(cs, mpAttack, a) | setAbstractOverTimeEffect(cs, mpAttack, current)) {
					effects.getRootAndStunAndSleep().add(mpAttack);
					compute = true;
				}
			}
			// FpAttackEffect
			if (et == EffectType.FPATK) {
				MpAttackEffect fpAttack = new MpAttackEffect();
				if (setGeneral(cs, fpAttack, a) | setAbstractOverTimeEffect(cs, fpAttack, current)) {
					effects.getRootAndStunAndSleep().add(fpAttack);
					compute = true;
				}
			}
			// BleedEffect
			if (et == EffectType.BLEED) {
				BleedEffect bleed = new BleedEffect();
				if (setGeneral(cs, bleed, a) | setAbstractOverTimeEffect(cs, bleed, current)) {
					effects.getRootAndStunAndSleep().add(bleed);
					compute = true;
				}
			}
			// PoisonEffect
			if (et == EffectType.POISON) {
				PoisonEffect poison = new PoisonEffect();
				if (setGeneral(cs, poison, a) | setAbstractOverTimeEffect(cs, poison, current)) {
					effects.getRootAndStunAndSleep().add(poison);
					compute = true;
				}
			}
		}
		if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractHealEffect")) {
			// HealInstantEffect
			if (et == EffectType.HEAL_INSTANT) {
				HealInstantEffect e = new HealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// MPHealInstantEffect
			if (et == EffectType.MPHEAL_INSTANT) {
				MPHealInstantEffect e = new MPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DPHealInstantEffect
			if (et == EffectType.DPHEAL_INSTANT) {
				DPHealInstantEffect e = new DPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// FPHealInstantEffect
			if (et == EffectType.FPHEAL_INSTANT) {
				FPHealInstantEffect e = new FPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcHealInstantEffect
			if (et == EffectType.PROCHEAL_INSTANT) {
				ProcHealInstantEffect e = new ProcHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcMPHealInstantEffect
			if (et == EffectType.PROCMPHEAL_INSTANT) {
				ProcMPHealInstantEffect e = new ProcMPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcDPHealInstantEffect
			if (et == EffectType.PROCDPHEAL_INSTANT) {
				ProcDPHealInstantEffect e = new ProcDPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcFPHealInstantEffect
			if (et == EffectType.PROCFPHEAL_INSTANT) {
				ProcFPHealInstantEffect e = new ProcFPHealInstantEffect();
				if (setGeneral(cs, e, a) | setAbstractHealEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// ProcVPHealInstantEffect
			if (et == EffectType.PROCVPHEAL_INSTANT) {
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
			if (et == EffectType.CASEHEAL) {
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
			if (et == EffectType.HEALCASTORONATTACKED) {
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
			if (et == EffectType.HEALCASTORONTARGETDEAD) {
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
		if (et.getAbstractCatetgory().equalsIgnoreCase("AbstractDispelEffect")) {
			// DispelDebuffEffect
			if (et == EffectType.DISPELDEBUFF) {
				DispelDebuffEffect e = new DispelDebuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelNpcDebuffEffect
			if (et == EffectType.DISPELNPCDEBUFF) {
				DispelNpcDebuffEffect e = new DispelNpcDebuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelDebuffPhysicalEffect
			if (et == EffectType.DISPELDEBUFFPHYSICAL) {
				DispelDebuffPhysicalEffect e = new DispelDebuffPhysicalEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelDebuffMentalEffect
			if (et == EffectType.DISPELDEBUFFMENTAL) {
				DispelDebuffMentalEffect e = new DispelDebuffMentalEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelBuffEffect
			if (et == EffectType.DISPELBUFF) {
				DispelBuffEffect e = new DispelBuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
			// DispelNpcBuffEffect
			if (et == EffectType.DISPELNPCBUFF) {
				DispelNpcBuffEffect e = new DispelNpcBuffEffect();
				if (setGeneral(cs, e, a) | setAbstractDispelEffect(cs, e, current)) {
					effects.getRootAndStunAndSleep().add(e);
					compute = true;
				}
			}
		}
		// DispelEffect
		if (et == EffectType.DISPEL) {
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
		if (et.getAbstractCatetgory().equalsIgnoreCase("BufEffect")) {
			// WeaponStatupEffect
			// ShieldMasteryEffect
			// OneTimeBoostHealEffect
			// SubTypeExtendDurationEffect
			// ExtendAuraRangeEffect
			// NoDeathPenaltyEffect
			// StatboostEffect
			// HiPassEffect
			// OneTimeBoostSkillCriticalEffect
			// BoostSpellAttackEffect
			// CurseEffect
			// DeboostHealEffect
			// XPBoostEffect
			// NoResurrectPenaltyEffect
			// BoostSkillCastingTimeEffect
			// BoostDropRateEffect
			// WeaponStatboostEffect
			// WeaponMasteryEffect
			if (et == EffectType.WPN_MASTERY) {
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
			if (et == EffectType.AMR_MASTERY) {
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
			// StatupEffect
			// StatdownEffect
			// SwitchHostileEffect
			// BoostHateEffect
			// SubTypeBoostResistEffect
			// BoostHealEffect
			// WeaponDualEffect
		}
		
		return compute;
	}
	
	private boolean setGeneral(ClientSkill cs, Effect effect, int a) {
		boolean hasGeneralEffects = false;
		String current = "effect" + a + "_";
		
		/** TODO
		protected SubEffect subeffect;
		protected ActionModifiers modifiers;
		protected List<Change> change;
		protected Conditions conditions;
		protected Conditions subconditions;
		**/
		// hasGeneralEffects |= setChange(cs, effect, current);
		
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
	
	private boolean setAbstractOverTimeEffect(ClientSkill cs, AbstractOverTimeEffect effect, String current) {
		boolean hasAbstractEffects = false;
		if (JAXBHandler.getValue(cs, current + "checktime") != null && JAXBHandler.getValue(cs, current + "checktime") != 0) {
			effect.setChecktime((Integer) JAXBHandler.getValue(cs, current + "checktime"));
			hasAbstractEffects = true;
		}
		if (JAXBHandler.getValue(cs, current + "reserved6") != null) {
			int value = getIntValue(cs, current, "reserved6");
			if (value == 1) {
				effect.setPercent(true);
				hasAbstractEffects = true;
			}
		}
		hasAbstractEffects |= setDelta(cs, effect, current, "reserved8");
		hasAbstractEffects |= setValue(cs, effect, current, "reserved9");
		return hasAbstractEffects;
	}
	
	private boolean setAbstractHealEffect(ClientSkill cs, AbstractHealEffect effect, String current) {
		boolean hasAbstractEffects = false;
		if (JAXBHandler.getValue(cs, current + "reserved6") != null) {
			int value = getIntValue(cs, current, "reserved6");
			if (value == 1) {
				effect.setPercent(true);
				hasAbstractEffects = true;
			}
		}
		hasAbstractEffects |= setDelta(cs, effect, current, "reserved1");
		hasAbstractEffects |= setValue(cs, effect, current, "reserved2");
		return hasAbstractEffects;
	}
	
	private boolean setAbstractDispelEffect(ClientSkill cs, AbstractDispelEffect effect, String current) {
		boolean hasAbstractEffects = false;
	
		if (JAXBHandler.getValue(cs, current + "reserved16") != null) {
			int value = getIntValue(cs, current, "reserved16");
			if (value != 0) {
				effect.setDispelLevel(value);
				hasAbstractEffects = true;
			}
		}
		if (JAXBHandler.getValue(cs, current + "reserved17") != null) {
			int value = getIntValue(cs, current, "reserved17");
			if (value != 0) {
				effect.setDpower(value);
				hasAbstractEffects = true;
			}
		}
		if (JAXBHandler.getValue(cs, current + "reserved18") != null) {
			int value = getIntValue(cs, current, "reserved18");
			if (value != 0) {
				effect.setPower(value);
				hasAbstractEffects = true;
			}
		}
		hasAbstractEffects |= setDelta(cs, effect, current, "reserved1");
		hasAbstractEffects |= setValue(cs, effect, current, "reserved2");
		return hasAbstractEffects;
	}
	
	private boolean setBufEffect(ClientSkill cs, BufEffect effect, String current) {
		boolean hasAbstractEffects = false;
	
		// if (JAXBHandler.getValue(cs, current + "reserved16") != null) {
			// int value = getIntValue(cs, current, "reserved16");
			// if (value != 0) {
				effect.setMaxstat(true); // TODO !!!!!!!!!!!!!!!!
				// hasAbstractEffects = true;
			// }
		// }
		// hasAbstractEffects |= setDelta(cs, effect, current, "reserved1");
		// hasAbstractEffects |= setValue(cs, effect, current, "reserved2");
		return hasAbstractEffects;
	}
	
	private boolean setValue(ClientSkill cs, Effect effect, String current, String property) {
		boolean hasValue = false;
		if (JAXBHandler.getValue(cs, current + property) != null) {
			int value = getIntValue(cs, current, property);
			if (value != 0) {
				effect.setValue(value);
				hasValue = true;
			}
		}
		return hasValue;
	}
	
	private boolean setDelta(ClientSkill cs, Effect effect, String current, String property) {
		boolean hasDelta = false;
		if (JAXBHandler.getValue(cs, current + property) != null) {
			int delta = getIntValue(cs, current, property);
			if (delta != 0) {
				effect.setDelta(delta);
				hasDelta = true;
			}
		}
		return hasDelta;
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
	
	private boolean setChange(ClientSkill cs, Effect e, String current) {
		Change change = new Change();
		List<Change> changes = new ArrayList<Change>();
		Map<ModifiersEnum, Integer> modifiers = new HashMap<ModifiersEnum, Integer>(); //TODO: Rename StatModifiers
		int value = 0;
		
		if (e instanceof ArmorMasteryEffect || e instanceof ShielMasteryEffect)
			modifiers.add(ModifiersEnum.PHYSICAL_DEFENSE, (Integer) JAXBHandler.getValue(cs, current + ""));
		else if (e instanceof WeaponMasteryEffect)
			modifiers.add(ModifiersEnum.PHYSICAL_ATTACK, (Integer) JAXBHandler.getValue(cs, current + ""));
		else if (e instanceof BoostDropRateEffect)
			modifiers.add(ModifiersEnum.BOOST_DROP_RATE, (Integer) JAXBHandler.getValue(cs, current + ""));
		if (modifiers.isEmpty()) {
			//modifiers = getClientModAndValue(cs, current); (13, 14, 18) based on a map linking reserveds (stat & value)
			//	In this one : If ...ALL (like ElementalDefendAll) make separate change for each included in "all"
		}
		if (!modifiers.isEmpty()) {
			for (ModifiersEnum mod : modifiers)
				change.set();
				change.setValue(); //getValue
				change.setFunction(); //TODO: from client or guessed (add in the enum)
				changes.add(change);			
		}
		if (!changes.isEmpty()) {
			e.getChange().addAll(changes);
			return true;
		} else {
			return false;
		}
	}
	
	private int getIntValue(ClientSkill cs, String current, String property) {
		int value = 0;
		try {	
			value = Integer.parseInt(JAXBHandler.getValue(cs, current + property).toString());
		} catch (Exception ex) {
			System.out.println("[SKILLS] " + property + " is a String for : " + JAXBHandler.getValue(cs, "id").toString());
		}		
		return value;
	}
	
	private float getFloatValue(ClientSkill cs, String current, String property) {
		float value = 0;
		try {
			value = Float.parseFloat(JAXBHandler.getValue(cs, current + property).toString());
		} catch (Exception ex) {
			System.out.println("[SKILLS] " + property + " is a String for : " + JAXBHandler.getValue(cs, "id").toString());
		}		
		return value;
	}
}