package com.parser.common.aion.enums.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * @author Viria
 */
public enum EffectType {

	// Dispel
	DISPEL,
	DISPELBUFF,
	DISPELNPCBUFF,
    DISPELDEBUFF,
	DISPELNPCDEBUFF,
    DISPELDEBUFFPHYSICAL,
    DISPELDEBUFFMENTAL,
	// Counter
	DISPELBUFFCOUNTERATK,
	MAGICCOUNTERATK,
	// Stats change
	STATUP,
	STATBOOST,
	WEAPONSTATUP,
	WEAPONSTATBOOST,
	STATDOWN,
	
	BUFFSTUN,
	BUFFBIND,
	BUFFSILENCE,
	BUFFSLEEP,
	
	ALWAYSDODGE,
	ALWAYSBLOCK,
	ALWAYSRESIST,
	ALWAYSPARRY,
	
	INVULNERABLEWING,
	
	SUBTYPEBOOSTRESIST,
	SUBTYPEEXTENDDURATION,
	
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
	SPELLATK("AbstractOverTimeEffect"),
	SPELLATK_INSTANT,
	NOREDUCESPELLATK_INSTANT,
	DELAYEDSPELLATK_INSTANT,
	FPATK,
	FPATK_INSTANT,
	DELAYEDFPATK_INSTANT,
	MPATTACK,
	MPATTACK_INSTANT,
	PROCATK_INSTANT,
	SPELLATKDRAIN("AbstractOverTimeEffect"),
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
	
	// Heal
	HEAL("AbstractOverTimeEffect"),
	HEAL_INSTANT,
	FPHEAL("AbstractOverTimeEffect"),
	FPHEAL_INSTANT,
	MPHEAL("AbstractOverTimeEffect"),
	MPHEAL_INSTANT,
	DPHEAL("AbstractOverTimeEffect"),
	DPHEAL_INSTANT,
	PROCHEAL_INSTANT,
	PROCVPHEAL_INSTANT,
	PROCDPHEAL_INSTANT,
	PROCFPHEAL_INSTANT,
	PROCMPHEAL_INSTANT,
	CONVERTHEAL,
	CASEHEAL,
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
	POISON,
	BLIND,
	CONFUSE,
	CURSE,
	BIND,
	BLEED,
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
	EXTENDAURARANGE,
	
	SHIELD,
	SANCTUARY,
	REFLECTOR,
	PROTECT,
	
	DRBOOST, // New 4.0
	BOOSTDROPRATE,
	APBOOST,
	BOOSTSKILLCASTINGTIME,
	BOOSTSPELLATTACKEFFECT,
	XPBOOST,
	GATHERPOINTBOOST,
	EXTRACTGATHERPOINTBOOST,
	COMBINEPOINTBOOST,
	MENUISIERCOMBINEPOINTBOOST,
	BOOSTSKILLCOST,
	DEBOOSTHEALAMOUNT,
	BOOSTHEALEFFECT,
	ONETIMEBOOSTHEALEFFECT,
	ONETIMEBOOSTSKILLATTACK(new String[] {"ONETIMEBOOSTSKILLATTACK", "ONETIMEBOOSTSKILLATK"}), // Different ?
	ONETIMEBOOSTSKILLCRITICAL,
	
	ABSOLUTESTATTOPCBUFF,
	ABSOLUTESTATTOPCDEBUFF,
	
	PROVOKER,
	HOSTILEUP,
	SWITCHHOSTILE,
	BOOSTHATE,
	CHANGEHATEONATTACKED,

	HEALCASTORONTARGETDEAD,
	HEALCASTORONATTACKED,
	
	WPN_MASTERY,
	AMR_MASTERY,
	SHIELD_MASTERY(new String[] {"SHIELD_MASTERY", "SHIELDMASTERY"}),
	WPN_DUAL,
	
	NODEATHPENALTY,
	RESURRECT,
	RESURRECTBASE,
	RESURRECTPOSITIONAL,
	NORESURRECTPENALTY,
	REBIRTH,
	
	DUMMY,
	HIPASS,
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
	HIDE;
	
	private String abstractCategory;
	// private String subCategory;
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
		System.out.println("[SKILLS] No EffectType matching :" + string);
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
