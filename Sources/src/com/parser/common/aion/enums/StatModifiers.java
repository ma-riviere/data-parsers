package com.parser.common.aion.enums;

import com.google.common.base.Strings;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public enum StatModifiers {

	// Items
	MAXHP,
	MAXMP,
	BLOCK,
	EVASION("DODGE"),
	CONCENTRATION,
	PARRY,
	SPEED,
	ATTACK_SPEED("ATTACKDELAY"),
	PHYSICAL_ATTACK("PHYATTACK"),
	PHYSICAL_ACCURACY("HITACCURACY"),
	PHYSICAL_CRITICAL("CRITICAL"),
	PHYSICAL_DEFENSE("PHYSICALDEFEND"),
	MAGICAL_ATTACK("MAGICALATTACK"),
	MAGICAL_ACCURACY("MAGICALHITACCURACY"),
	MAGICAL_CRITICAL("MAGICALCRITICAL"),
	// MAGICAL_RESIST("MAGICALRESIST"), Handled by the accessors
	MAGICAL_RESIST,
	EARTH_RESISTANCE("ELEMENTALDEFENDEARTH"),
	FIRE_RESISTANCE("ELEMENTALDEFENDFIRE"),
	WIND_RESISTANCE("ELEMENTALDEFENDAIR"),
	WATER_RESISTANCE("ELEMENTALDEFENDWATER"),
	LIGHT_RESISTANCE("ELEMENTALDEFENDLIGHT"),
	DARK_RESISTANCE("ELEMENTALDEFENDDARK"),
	BOOST_MAGICAL_SKILL("MAGICALSKILLBOOST"),
	BOOST_CASTING_TIME("BOOSTCASTINGTIME"),
	BOOST_HATE("BOOSTHATE"),
	FLY_TIME("MAXFP"),
	FLY_SPEED("FLYSPEED"),
	PVP_ATTACK_RATIO("PVPATTACKRATIO"),
	PVP_ATTACK_RATIO_PHYSICAL("PVPATTACKRATIO_PHYSICAL"),
	PVP_ATTACK_RATIO_MAGICAL("PVPATTACKRATIO_MAGICAL"),
	PVP_DEFEND_RATIO("PVPDEFENDRATIO"),
	PVP_DEFEND_RATIO_PHYSICAL("PVPDEFENDRATIO_PHYSICAL"),
	PVP_DEFEND_RATIO_MAGICAL("PVPDEFENDRATIO_MAGICAL"),
	BLEED_RESISTANCE("ARBLEED"),
	BLIND_RESISTANCE("ARBLIND"),
	CHARM_RESISTANCE("ARCHARM"),
	CONFUSE_RESISTANCE("ARCONFUSE"),
	CURSE_RESISTANCE("ARCURSE"),
	DISEASE_RESISTANCE("ARDISEASE"),
	FEAR_RESISTANCE("ARFEAR"),
	OPENAREIAL_RESISTANCE("AROPENAREIAL"),
	PARALYZE_RESISTANCE("ARPARALYZE"),
	PERIFICATION_RESISTANCE("ARPERIFICATION"),
	POISON_RESISTANCE("ARPOISON"),
	ROOT_RESISTANCE("ARROOT"),
	SILENCE_RESISTANCE("ARSILENCE"),
	SLEEP_RESISTANCE("ARSLEEP"),
	SLOW_RESISTANCE("ARSLOW"),
	SNARE_RESISTANCE("ARSNARE"),
	SPIN_RESISTANCE("ARSPIN"),
	STAGGER_RESISTANCE("ARSTAGGER"),
	STUMBLE_RESISTANCE("ARSTUMBLE"),
	STUN_RESISTANCE("ARSTUN"),
	PULLED_RESISTANCE("ARPULLED"),
	SILENCE_RESISTANCE_PENETRATION("SILENCE_ARP"),
	PARALYZE_RESISTANCE_PENETRATION("PARALYZE_ARP"),
	STAGGER_RESISTANCE_PENETRATION("STAGGER_ARP"),
	STUMBLE_RESISTANCE_PENETRATION("STUMBLE_ARP"),
	STUN_RESISTANCE_PENETRATION("STUN_ARP"),
	HEAL_BOOST("HEALSKILLBOOST"),
	MAGICAL_CRITICAL_RESIST("MAGICALCRITICALREDUCERATE"),
	MAGICAL_CRITICAL_DAMAGE_REDUCE("MAGICALCRITICALDAMAGEREDUCE"),
	PHYSICAL_CRITICAL_RESIST("PHYSICALCRITICALREDUCERATE"),
	PHYSICAL_CRITICAL_DAMAGE_REDUCE("PHYSICALCRITICALDAMAGEREDUCE"),
	MAGICAL_DEFEND("MAGICALDEFEND"),
	MAGIC_SKILL_BOOST_RESIST("MAGICALSKILLBOOSTRESIST"),
	POISON_RESISTANCE_PENETRATION("POISON_ARP"),
	BLEED_RESISTANCE_PENETRATION("BLEED_ARP"),
	SLEEP_RESISTANCE_PENETRATION("SLEEP_ARP"),
	ROOT_RESISTANCE_PENETRATION("ROOT_ARP"),
	BLIND_RESISTANCE_PENETRATION("BLIND_ARP"),
	CHARM_RESISTANCE_PENETRATION("CHARM_ARP"),
	DISEASE_RESISTANCE_PENETRATION("DISEASE_ARP"),
	FEAR_RESISTANCE_PENETRATION("FEAR_ARP"),
	SPIN_RESISTANCE_PENETRATION("SPIN_ARP"),
	CURSE_RESISTANCE_PENETRATION("CURSE_ARP"),
	CONFUSE_RESISTANCE_PENETRATION("CONFUSE_ARP"),
	PERIFICATION_RESISTANCE_PENETRATION("PERIFICATION_ARP"),
	OPENAREIAL_RESISTANCE_PENETRATION("OPENAREIAL_ARP"),
	SNARE_RESISTANCE_PENETRATION("SNARE_ARP"),
	SLOW_RESISTANCE_PENETRATION("SLOW_ARP"),
	
	// Skills
	ABNORMAL_RESISTANCE_ALL("ARALL"),
	ALLRESIST,
	ALLPARA,
	REGEN_HP("HPREGEN"),
	REGEN_MP("MPREGEN"),
	REGEN_FP("FPREGEN"),
	DEFORM_RESISTANCE("ARDEFORM"),
	POWER("STR"),
	WILL("WIL"),
	HEALTH("VIT"),
	AGILITY("AGI"),
	KNOWLEDGE("KNO"),
	ATTACK_RANGE("ATTACKRANGE"),
	PVE_ATTACK_RATIO("PVEATTACKRATIO"), //TODO: Check if not use under another name !!!!
	// BOOST_CHARGE_TIME("BOOSTCHARGETIME"), //TODO: Add
	KNOWIL,
	
	// Server custom
	BOOST_CRAFTING_XP_RATE,
	BOOST_GATHERING_XP_RATE,
	BOOST_GROUP_HUNTING_XP_RATE,
	BOOST_HUNTING_XP_RATE,
	BOOST_QUEST_XP_RATE,
	BOOST_DROP_RATE,
	BOOST_DURATION_BUFF,
	BOOST_RESIST_DEBUFF,
	BOOST_MANTRA_RANGE,
	BOOST_SPELL_ATTACK,
	BOOST_CASTING_TIME_ATTACK,
	HEAL_SKILL_BOOST,
	
	// Compacted Modifiers
	PMATTACK(new String[] {"PHYSICAL_ATTACK", "MAGICAL_ATTACK"}),
	PMDEFEND(new String[] {"PHYSICAL_DEFENSE", "MAGICAL_RESIST"}),
	ELEMENTALDEFENDALL(new String[] {"EARTH_RESISTANCE", "FIRE_RESISTANCE", "WIND_RESISTANCE", "WATER_RESISTANCE"}),
	ARSTUNLIKE(new String[] {"STUN_RESISTANCE", "STUMBLE_RESISTANCE", "STAGGER_RESISTANCE", "SPIN_RESISTANCE", "OPENAREIAL_RESISTANCE"}),
	ACTIVEDEFEND(new String[] {"EVASION", "PARRY", "BLOCK"}),
	ALLSPEED(new String[] {"SPEED", "FLY_SPEED"}),
	
	NONE;

	private String clientString = null;
	private String[] linked = null;
	
	private StatModifiers(String clientString, String[] linked) {
		this.clientString = clientString;
		this.linked = linked;
	}
	
	private StatModifiers(String[] linked) {
		this(null, linked);
	}
	
	private StatModifiers(String clientString) {
		this(clientString, null);
	}
	
	private StatModifiers() {
		this(null, null);
	}
	
	public String getClientString() {return clientString;}
	public List<String> getLinked() {
		List<String> meanings = new ArrayList<String>();
		if (this.linked != null)
			Collections.addAll(meanings, this.linked);
		return meanings;
	}
	
	public static List<String> unknownMod = new ArrayList<String>();
	
	public static StatModifiers fromClient(String string) {
		
		// Special corrections (Thanks NC ...)
		if (string.equalsIgnoreCase("MAGICALRESIST")) {string = "MAGICAL_RESIST";}
		// if (string.equalsIgnoreCase("HP")) {string = "MAXHP";}
		// if (string.equalsIgnoreCase("MP")) {string = "MAXMP";}
		if (string.equalsIgnoreCase("ERFIRE")) {string = "FIRE_RESISTANCE";}
		if (string.equalsIgnoreCase("ERAIR")) {string = "WIND_RESISTANCE";}
		if (string.equalsIgnoreCase("EREARTH")) {string = "EARTH_RESISTANCE";}
		if (string.equalsIgnoreCase("ERWATER")) {string = "WATER_RESISTANCE";}
		if (string.equalsIgnoreCase("DEX")) {string = "AGI";}
		if (string.equalsIgnoreCase("BOOSTSKILLCASTINGTIME")) {string = "BOOST_CASTING_TIME";}
		
		for (StatModifiers v : values()) {
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
			if (!unknownMod.contains(string.toUpperCase())) {
				System.out.println("[STAT MODIFIERS] No StatModifiers matching : " + string.toUpperCase());
				unknownMod.add(string.toUpperCase());
			}
		}
		return StatModifiers.NONE;
	}
	
	public static StatModifiers fromValue(String name) {
		for (StatModifiers v : values()) {
			if (v.toString().equalsIgnoreCase(name))
				return v;
		}
		return null;
	}
}
