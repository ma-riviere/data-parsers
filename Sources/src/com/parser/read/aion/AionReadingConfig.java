package com.parser.read.aion;

public class AionReadingConfig {

	public static final String VERSION = "aion40/";
	public static final String BASE_DIR = "../../Input/" + VERSION + "client/";
	
	/********************************** CUSTOM XML READING *************************************/
	
	// Items
	public static final String ITEMS_CUSTOM = "../../Input/" + VERSION + "Custom/Items/";
	
	public static final String ITEMS_INTERNAL = "../../Input/" + VERSION + "Custom/items_name_id.xml";
	public static final String ITEMS_INTERNAL_BINDINGS = "com.parser.output.aion.item_name";
	
	/*************************************** CLIENT XMLs ********************************************/
	
	// Rides
	public static final String RIDE = BASE_DIR + "Data/rides/rides.xml";
	public static final String RIDE_BINDINGS = "com.parser.input.aion.rides";
	
	// (Dir) Client Strings
	public static final String STRINGS_DATA = BASE_DIR + "Data/Strings/";
	public static final String STRINGS_L10N = BASE_DIR + "L10N/ENU/data/strings/";
	public static final String STRINGS_PREFIX = "client_strings_";
	public static final String STRINGS_BINDINGS = "com.parser.input.aion.strings";
	
	// World --> World
	public static final String WORLD_MAPS = BASE_DIR + "Data/world/WorldId.xml";
	public static final String WORLD_DATA = BASE_DIR + "Data/world/";
	public static final String WORLD_DATA_PREFIX = "client_world_";
	public static final String WORLD_MAPS_BINDINGS = "com.parser.input.aion.world_maps";
	public static final String WORLD_DATA_BINDINGS = "com.parser.input.aion.world_data";

	
	// Skills --> Skills
	public static final String SKILLS = BASE_DIR + "Data/skills/client_skills.xml";
	public static final String SKILLS_BINDINGS = "com.parser.input.aion.skills";
	
	public static final String SKILL_TREE = BASE_DIR + "Data/skills/client_skill_learns.xml";
	public static final String SKILL_TREE_BINDINGS = "com.parser.input.aion.skill_learn";
	
	// (Dir) Items --> Items
	public static final String ITEMS = BASE_DIR + "Data/Items";
	public static final String ITEMS_PREFIX = "client_items_";
	public static final String ITEMS_BINDINGS = "com.parser.input.aion.items";
	
	// Npcs --> Npcs
	public static final String NPCS = BASE_DIR + "Data/Npcs/client_npcs.xml";
	public static final String NPCS_BINDINGS = "com.parser.input.aion.npcs";
	
	// Animations --> Items
	public static final String ANIMATIONS = BASE_DIR + "Data/Animations/custom_animation.xml";
	public static final String ANIMATIONS_BINDINGS = "com.parser.input.aion.animations";
	
	// Housing --> Housing
	public static final String HOUSING_OBJECTS = BASE_DIR + "Data/Housing/client_housing_object.xml";
	public static final String HOUSING_PARTS = BASE_DIR + "Data/Housing/client_housing_custom_part.xml";
	public static final String HOUSING_BINDINGS = "com.parser.input.aion.housing";
	
	// Toypets --> Toypets
	public static final String TOYPETS = BASE_DIR + "Data/func_pet/toypets.xml";
	public static final String TOYPETS_BINDINGS = "com.parser.input.aion.toypets";
	
	// Doc files --> Quest
	public static final String DOC_FILES = BASE_DIR + "Data/Dialogs/doc_item/";
	
	/** ##################################################### **/

	// toypets
	public static final String TOYPET_ITEMS = "toypet_item.xml";
	public static final String TOYPET_FEED = "toypet_feed.xml";
	public static final String TOYPET_DOPINGS = "toypet_doping.xml";

	// npc_tribe_relation
	public static final String NPC_TRIBE_RELATION = "npc_tribe_relation.xml";
	public static final String NPC_TRIBE_RELATION_BINDINGS = "com.parser.input.aion.npctriberelation";

	// npcs
	public static final String TITLES = "client_titles.xml";
	public static final String TITLES_BINDINGS = "com.parser.input.aion.titles";

	// quests monsters (drops)
	public static final String QUEST_MONSTER = "quest_monster.csv";
	
	// quests
	public static final String QUESTS = "quest.xml";
	public static final String QUESTS_BINDINGS = "com.parser.input.aion.quest";

	// npc_templates
	public static final String NPCTEMPLATES = "npc_templates.xml";
	public static final String NPCTEMPLATES_BINDINGS = "com.parser.output.aion.npctemplates";

	// npc_templates
	public static final String AH_NPCTEMPLATES = "ah_npc_templates.xml";
	public static final String AH_NPCTEMPLATES_BINDINGS = "com.parser.output.aion.ah.npctemplates";

	// items
	public static final String ITEMOPTIONS = "client_item_random_option.xml";
	public static final String ITEMOPTIONS_BINDINGS = "com.parser.input.aion.itemoptions";
	
	// assembly items
	public static final String ASSEMBLYITEMS = "client_assembly_items.xml";
	public static final String ASSEMBLYITEMS_BINDINGS = "com.parser.input.aion.assemblyitems";

	// skill_learns
	public static final String SKILLLEARNS = "client_skill_learns.xml";
	public static final String SKILLLEARNS_BINDINGS = "com.parser.input.aion.skilllearns";

	// manastone_slots
	public static final String MANASTONESLOTS = "manastone_slots.xml";
	public static final String MANASTONESLOTS_BINDINGS = "com.parser.output.aion.manastoneslots";

	// npc_stats
	public static final String NPC_STATS = "npc_stats.xml";
	public static final String NPC_STATS_BINDINGS = "com.parser.output.aion.npcstats";

	// zone
	public static final String ZONE = "zone";
	public static final String ZONE_BINDINGS = "com.parser.input.aion.zones";

	// server zone
	public static final String SERVER_ZONE = "zones";
	public static final String SERVER_ZONE_BINDINGS = "com.parser.output.aion.zones";
	
	// doc_item
	public static final String DOC_FILES_BINDINGS = "com.parser.input.aion.docs";
	
	// housing lands
	public static final String HOUSE_LANDS = "client_housing_land.xml";
	public static final String HOUSE_LANDS_BINDINGS = "com.parser.input.aion.housing.land";
	
	// housing towns
	public static final String HOUSE_TOWNS = "client_housing_town.xml";
	public static final String HOUSE_TOWNS_BINDINGS = "com.parser.input.aion.housing.town";
	
	// housing addresses
	public static final String HOUSE_ADDRESSES = "Client_Housing_address.xml";
	public static final String HOUSE_ADDRESSES_BINDINGS = "com.parser.input.aion.housing.address";
	
	// housing buildings
	public static final String HOUSE_BUILDINGS = "client_housing_building.xml";
	public static final String HOUSE_BUILDINGS_BINDINGS = "com.parser.input.aion.housing.building";

	// item sets
	public static final String ITEMSETS = "client_setitem.xml";
	public static final String ITEMSETS_BINDINGS = "com.parser.input.aion.setitem";

	// challenge quests
	public static final String CHALLENGE_TASKS = "challenge_task.xml";
	public static final String CHALLENGE_TASKS_BINDINGS = "com.parser.input.aion.challenge";

	// materials
	public static final String MATERIALS = "materials.xml";
	public static final String MATERIALS_BINDINGS = "com.parser.input.aion.materials";

	// server npc skills template
	public static final String NPCSKILLS = "npc_skills.xml";
	public static final String NPCSKILLS_BINDINGS = "com.parser.output.aion.npcskills";

	// server skill tree
	public static final String SKILLTREE = "skill_tree.xml";
	public static final String SKILLTREE_BINDINGS = "com.parser.output.aion.skilltree";

	// server good lists
	public static final String GOODLIST = "goodslists.xml";
	public static final String GOODLIST_BINDINGS = "com.parser.output.aion.goodlists";

	// server decomposable items
	public static final String DECOMPOSABLES = "decomposable_items.xml";
	public static final String DECOMPOSABLES_BINDINGS = "com.parser.output.aion.decomposables";

}
