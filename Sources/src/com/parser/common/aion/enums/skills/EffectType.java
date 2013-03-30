package com.parser.common.aion.enums.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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
	SHIELD_MASTERY("BufEffect", new String[] {"SHIELD_MASTERY", "SHIELDMASTERY"}),
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

	// Counter
	DISPELBUFFCOUNTERATK,
	MAGICCOUNTERATK,
	// Stats change
	
	
	BUFFSTUN,
	BUFFBIND,
	BUFFSILENCE,
	BUFFSLEEP,
	
	ALWAYSDODGE,
	ALWAYSBLOCK,
	ALWAYSRESIST,
	ALWAYSPARRY,
	
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
	SKILLATK_INSTANT,
	SPELLATK_INSTANT,
	NOREDUCESPELLATK_INSTANT,
	DELAYEDSPELLATK_INSTANT,
	FPATK_INSTANT,
	DELAYEDFPATK_INSTANT,
	MPATTACK_INSTANT,
	PROCATK_INSTANT,
	SPELLATKDRAIN_INSTANT,
	SKILLATKDRAIN_INSTANT,
	MOVEBEHINDATK,
	BACKDASHATK,
	DASHATK,
	DELAYEDSKILL,
	// Signets
	SIGNET,
	SIGNETBURST,
	CARVESIGNET,
	// Shape
	SHAPECHANGE,
	POLYMORPH,
	DEFORM,
	
	NOFLY,
	FLYOFF,
	
	PULLED,
	FALL,
	
	CONVERTHEAL,
	
	// Bad buffs
	STUN,
	SLEEP,
	FEAR,
	ROOT,
	SIMPLE_ROOT,
	SNARE,
	ABSOLUTESNARE,
	SILENCE,
	PARALYZE,
	DISEASE,
	STUMBLE,
	SLOW,
	ABSOLUTESLOW,
	BLIND,
	CONFUSE,
	BIND,
	
	PETRIFICATION,
	SPIN,
	STAGGER,
	
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
	
	DRBOOST, // New 4.0
	APBOOST,
	GATHERPOINTBOOST,
	EXTRACTGATHERPOINTBOOST,
	COMBINEPOINTBOOST,
	MENUISIERCOMBINEPOINTBOOST,
	BOOSTSKILLCOST,
	
	ONETIMEBOOSTSKILLATTACK(new String[] {"ONETIMEBOOSTSKILLATTACK", "ONETIMEBOOSTSKILLATK"}), // Different ?
		
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
	DEATHBLOW,
	
	SKILLCOOLTIMERESET,
	ACTIVATE_ENSLAVE,
		
	SEARCH,
	HIDE,
	
	NONE; //TODO: Return this (make checks if NONE)
	
	private String abstractCategory;
	private String[] clientStrings;
	
	private EffectType(String abstractCategory, String[] clientStrings) {
		this.abstractCategory = abstractCategory;
		this.clientStrings = clientStrings;
	}
	
	private EffectType(String abstractCategory) {
		this(abstractCategory, null);
	}
	
	//TODO: Should not happen (fill all)
	private EffectType(String[] clientStrings) {
		this(null, clientStrings);
	}
	
	private EffectType() {
		this(null, null);
	}
	
	public List<String> getClientStrings() {
		List<String> list = new ArrayList<String>();
		if (clientStrings != null)
			Collections.addAll(list, clientStrings);
		return list;
	}
	
	public String getAbstractCatetgory() {
		return abstractCategory != null ? abstractCategory : "noAbstractCategory";
	}
	
	/**
	 * Returns the ENUM matching the given client string
	 * If no Client String is bound to the enum, it will try to match the enum string value.
	 */
	public static EffectType fromClient(String string) {
		for (EffectType v : values()) {
			if (!v.getClientStrings().isEmpty()) {
				for (String client : v.getClientStrings()) {
					if (client.equalsIgnoreCase(string))
						return v;
				}
			} else {
				if (fromValue(string) != null)
					return fromValue(string);
			}
		}
		try {int value = Integer.parseInt(string);} catch (Exception e) {System.out.println("[SKILLS] No EffectType matching : " + string);}
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
