package com.parser.commons.aion.enums.skills;

import java.util.List;
import java.util.ArrayList;

public enum EffectType {

	// AbstractOverTimeEffect
		// HealOverTimeEffect
		HEAL("AbstractOverTimeEffect"),
		FPHEAL("AbstractOverTimeEffect"),
		MPHEAL("AbstractOverTimeEffect"),
		DPHEAL("AbstractOverTimeEffect"),
	POISON("AbstractOverTimeEffect"),
	BLEED("AbstractOverTimeEffect"),
	SPELLATK("AbstractOverTimeEffect"),
	MPATTACK("AbstractOverTimeEffect"),
	FPATK("AbstractOverTimeEffect"),
	SPELLATKDRAIN("AbstractOverTimeEffect"),
	
	// AbstractHealEffect
	HEAL_INSTANT("AbstractHealEffect"),
	MPHEAL_INSTANT("AbstractHealEffect"),
	FPHEAL_INSTANT("AbstractHealEffect"),
	DPHEAL_INSTANT("AbstractHealEffect"),
	PROCHEAL_INSTANT("AbstractHealEffect"),
	PROCDPHEAL_INSTANT("AbstractHealEffect"),
	PROCFPHEAL_INSTANT("AbstractHealEffect"),
	PROCMPHEAL_INSTANT("AbstractHealEffect"),
	PROCVPHEAL_INSTANT("AbstractHealEffect"),
	HEALCASTORONTARGETDEAD("AbstractHealEffect"),
	HEALCASTORONATTACKED("AbstractHealEffect"),
	CASEHEAL("AbstractHealEffect"),
	
	// AbstractDispelEffect
	DISPELBUFF("AbstractDispelEffect"),
	DISPELNPCBUFF("AbstractDispelEffect"),
    DISPELDEBUFF("AbstractDispelEffect"),
	DISPELNPCDEBUFF("AbstractDispelEffect"),
    DISPELDEBUFFPHYSICAL("AbstractDispelEffect"),
    DISPELDEBUFFMENTAL("AbstractDispelEffect"),
	
	// DispelEffect
	DISPEL("DispelEffect"),
	EVADE("DispelEffect"),
	
	// BufEffect
	STATUP("BufEffect"),
	STATBOOST("BufEffect"),
	WEAPONSTATUP("BufEffect"),
	WEAPONSTATBOOST("BufEffect"),
	STATDOWN("BufEffect"),
	WPN_MASTERY("BufEffect"),
	AMR_MASTERY("BufEffect"),
	SHIELD_MASTERY("BufEffect"),
	WPN_DUAL("BufEffect"),
	BOOSTSKILLCASTINGTIME("BufEffect"),
	BOOSTSPELLATTACKEFFECT("BufEffect"),
	XPBOOST("BufEffect"),
	SUBTYPEBOOSTRESIST("BufEffect"),
	SUBTYPEEXTENDDURATION("BufEffect"),
	CURSE("BufEffect"),
	EXTENDAURARANGE("BufEffect"),
	BOOSTDROPRATE("BufEffect"),
	DEBOOSTHEALAMOUNT("BufEffect"),
	BOOSTHEALEFFECT("BufEffect"),
	ONETIMEBOOSTSKILLCRITICAL("BufEffect"),
	ONETIMEBOOSTHEALEFFECT("BufEffect"),
	SWITCHHOSTILE("BufEffect"),
	BOOSTHATE("BufEffect"),
	NORESURRECTPENALTY("BufEffect"),
	NODEATHPENALTY("BufEffect"),
	HIPASS("BufEffect"),
	
	HOSTILEUP,
	CHANGEHATEONATTACKED,
	ONETIMEBOOSTSKILLATTACK,
	
	// DamageEffect
	SKILLATK_INSTANT("DamageEffect"),
	SPELLATK_INSTANT("DamageEffect"),
	NOREDUCESPELLATK_INSTANT("DamageEffect"),
	DELAYEDSPELLATK_INSTANT("DamageEffect"),
	FPATK_INSTANT("DamageEffect"),
	MPATTACK_INSTANT("DamageEffect"),
	PROCATK_INSTANT("DamageEffect"),
	SPELLATKDRAIN_INSTANT("DamageEffect"),
	SKILLATKDRAIN_INSTANT("DamageEffect"),
	MOVEBEHINDATK("DamageEffect"),
	BACKDASHATK("DamageEffect"),
	DASHATK("DamageEffect"),
	SIGNETBURST("DamageEffect"),
	CARVESIGNET("DamageEffect"),
	DISPELBUFFCOUNTERATK("DamageEffect"),
	DEATHBLOW("DamageEffect"),
	
	DELAYEDFPATK_INSTANT,
	DELAYEDSKILL,
	MAGICCOUNTERATK,
	
	// SignetEffect
	SIGNET,
	
	// Alterations
	STUN,
	BUFFSTUN,
	SLEEP,
	BUFFSLEEP,
	FEAR,
	SIMPLE_ROOT,
	ROOT,
	SNARE,
	
	SILENCE,
	BUFFSILENCE,
	PARALYZE,
	DISEASE,
	STUMBLE,
	SLOW,
	BLIND,
	CONFUSE,
	BIND,
	BUFFBIND,
	SPIN,
	STAGGER,
	
	// Buff
	ALWAYSDODGE,
	ALWAYSBLOCK,
	ALWAYSRESIST,
	ALWAYSPARRY,
	INVULNERABLEWING,
	
	// Shape
	SHAPECHANGE("TransformEffect"),
	POLYMORPH("TransformEffect"),
	DEFORM("TransformEffect"),
	
	// ShieldEffect
	SHIELD("ShieldEffect"),
	CONVERTHEAL("ShieldEffect"),
	PROVOKER("ShieldEffect"),
	REFLECTOR("ShieldEffect"),
	PROTECT("ShieldEffect"),
	
	// ResurrectEffect
	RESURRECT("ResurrectEffect"),
	RESURRECTBASE("ResurrectEffect"),
	RESURRECTPOSITIONAL("ResurrectEffect"),
	REBIRTH,
	
	// SummonEffect
	SUMMON("SummonEffect"),
	SUMMONTRAP("SummonEffect"),
	SUMMONSERVANT("SummonEffect"),
	SUMMONHOMING("SummonEffect"),
	SUMMONGROUPGATE("SummonEffect"),
	SUMMONBINDINGGROUPGATE("SummonEffect"),
	SUMMONSKILLAREA("SummonEffect"),
	SUMMONTOTEM("SummonEffect"),
	SUMMONHOUSEGATE("SummonEffect"),
	
	PETORDERUSEULTRASKILL,
	
	// Misc
	SEARCH,
	HIDE,
	AURA,
	NOFLY,
	OPENAERIAL,
	CLOSEAERIAL,
	PULLED,
	FALL,
	SANCTUARY,
	RANDOMMOVELOC,
	RETURNHOME,
	RETURNPOINT,
	RECALL_INSTANT,
	
	BOOSTSKILLCOST,
	SWITCHHPMP_INSTANT,
	DPTRANSFER,
	
	SKILLLAUNCHER,
	CONDSKILLLAUNCHER,
	
	// Not implemented
	INTERVALSKILL, // TODO (Déclenchement d'un effet (reserved1) a intervalle régulier
	TARGETTELEPORT, // TODO
	APBOOST, // TODO
	GATHERPOINTBOOST, // TODO
	EXTRACTGATHERPOINTBOOST, // TODO
	COMBINEPOINTBOOST, // TODO
	MENUISIERCOMBINEPOINTBOOST, // TODO
	
	SKILLCOOLTIMERESET,
	ACTIVATE_ENSLAVE, // Should charm target npc, make it your slave
	
	ABSOLUTESTATTOPCBUFF,
	ABSOLUTESTATTOPCDEBUFF,
	SUMMONFUNCTIONALNPC, // Summons a multi-function NPC
	DUMMY, // For tests, reduce fly cooltime
	RIDEROBOT,
	CANNON,
	FLYOFF, // Imposibility to stop flying
	// PETRIFICATION, // Converted to Paralyze for now, to implement
	// ESCAPE, // Set as equivalent to RETURNHOME (check if true)
	
	NONE;
	
	private String abstractCategory;
	
	private EffectType(String abstractCategory) {
		this.abstractCategory = abstractCategory;
	}
	
	private EffectType() {
		this(null);
	}
	
	public String getAbstractCatetgory() {
		return abstractCategory != null ? abstractCategory : "noAbstractCategory";
	}
	
	public static List<String> unkEffect = new ArrayList<String>();
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static EffectType fromClient(String string) {
		
		// Special corrections (Thanks NC ...)
		if (string.equalsIgnoreCase("DRBOOST")) {string = "BOOSTDROPRATE";}
		if (string.equalsIgnoreCase("ABSOLUTESNARE")) {string = "SNARE";}
		if (string.equalsIgnoreCase("ABSOLUTESLOW")) {string = "SLOW";}
		if (string.equalsIgnoreCase("ONETIMEBOOSTSKILLATK")) {string = "ONETIMEBOOSTSKILLATTACK";}
		if (string.equalsIgnoreCase("SHIELDMASTERY")) {string = "SHIELD_MASTERY";}
		if (string.equalsIgnoreCase("PETRIFICATION")) {string = "PARALYZE";}
		if (string.equalsIgnoreCase("ESCAPE")) {string = "RETURNHOME";}
		
		for (EffectType v : values()) {
			if (v.toString().equalsIgnoreCase(string))
				return v;
			else
				if (fromValue(string) != null)
					return fromValue(string);
		}
		try {int value = Integer.parseInt(string);} 
		catch (Exception e) {
			if (!unkEffect.contains(string.toUpperCase())) {
				System.out.println("[EFFECT TYPE] No EffectType matching : " + string.toUpperCase());
				unkEffect.add(string.toUpperCase());
			}
		}
		return null;
	}
	
	public static EffectType fromValue(String name) {
		for (EffectType v : values()) {
				if (v.toString().equalsIgnoreCase(name))
					return v;
		}
		return null;
	}
}
