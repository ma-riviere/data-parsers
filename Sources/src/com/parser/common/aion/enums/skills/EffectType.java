package com.parser.common.aion.enums.skills;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Viria
 */
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
	DISPEL,
	
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
	// PETRIFICATION, //TODO: Implement, converted to Paralyze for now
	SPIN,
	STAGGER,
	
	// Buff
	ALWAYSDODGE,
	ALWAYSBLOCK,
	ALWAYSRESIST,
	ALWAYSPARRY,
	
	/************ TODO *************/
	
	MAGICCOUNTERATK,
		
	INVULNERABLEWING,
		
	// Teleport related
	TARGETTELEPORT,
	RANDOMMOVELOC,
	RETURNHOME,
	RECALL_INSTANT,
	RETURNPOINT,
	
	ESCAPE,
	EVADE,
	
	// Attacks
	SKILLATK,
	DELAYEDFPATK_INSTANT,
	
	DELAYEDSKILL,
	
	// Shape
	SHAPECHANGE,
	POLYMORPH,
	DEFORM,
	
	NOFLY,
	FLYOFF,
	
	PULLED,
	FALL,
	
	CONVERTHEAL,
	
	SUMMON,
	SUMMONSKILLAREA,
	SUMMONTRAP,
	SUMMONSERVANT,
	SUMMONTOTEM,
	SUMMONHOMING,
	SUMMONGROUPGATE,
	SUMMONBINDINGGROUPGATE,
	SUMMONHOUSEGATE,
	SUMMONFUNCTIONALNPC,
	PETORDERUSEULTRASKILL,
	
	OPENAERIAL,
	CLOSEAERIAL,
	
	AURA,
		
	SHIELD,
	SANCTUARY,
	REFLECTOR,
	PROTECT,
	
	APBOOST,
	GATHERPOINTBOOST,
	EXTRACTGATHERPOINTBOOST,
	COMBINEPOINTBOOST,
	MENUISIERCOMBINEPOINTBOOST,
	BOOSTSKILLCOST,
	
	ONETIMEBOOSTSKILLATTACK,
		
	ABSOLUTESTATTOPCBUFF,
	ABSOLUTESTATTOPCDEBUFF,
	
	PROVOKER,
	HOSTILEUP,
	
	CHANGEHATEONATTACKED,
	
	RESURRECT,
	RESURRECTBASE,
	RESURRECTPOSITIONAL,
	
	REBIRTH,
	
	DUMMY,
	
	RIDEROBOT,
	CANNON,
	
	INTERVALSKILL,
	SKILLLAUNCHER,
	CONDSKILLLAUNCHER,
	DPTRANSFER,
	SWITCHHPMP_INSTANT,
	
	SKILLCOOLTIMERESET,
	ACTIVATE_ENSLAVE,
		
	SEARCH,
	HIDE,
	
	NONE; //TODO: Return this (make checks if NONE)
	
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
