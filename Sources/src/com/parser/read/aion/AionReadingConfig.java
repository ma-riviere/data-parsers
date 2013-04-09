package com.parser.read.aion;

public class AionReadingConfig {

	public static final String VERSION = "aion40/";
	public static final String BASE_DIR = "../../Data/" + VERSION + "client/";
	public static final boolean READ_CUSTOM = true;
	
	/********************************** CUSTOM XML READING *************************************/
	
	// Items
	public static final String ITEMS_CUSTOM = "../../Data/Custom/Items/";
	
	public static final String ITEMS_INTERNAL = "../../Data/Mappings/items_name_id.xml";
	public static final String ITEMS_INTERNAL_BINDINGS = "com.parser.input.aion.p_items";
	
	/*************************************** CLIENT XMLs ********************************************/
	
	// Rides
	public static final String RIDE = BASE_DIR +"Data/rides/rides.xml";
	public static final String RIDE_BINDINGS = "com.parser.input.aion.rides";
	
	// Instances Cooltimes
	public static final String COOLTIMES = BASE_DIR + "Data/world/client_instance_cooltime.xml";
	public static final String COOLTIMES2 = BASE_DIR + "Data/world/client_instance_cooltime2.xml";
	public static final String COOLTIMES_BINDINGS = "com.parser.input.aion.cooltimes";

	// (Dir) Client Strings
	public static final String STRINGS_DATA = BASE_DIR + "Data/Strings/";
	public static final String STRINGS_L10N = BASE_DIR + "L10N/ENU/data/strings/";
	public static final String STRINGS_PREFIX = "client_strings_";
	public static final String STRINGS_BINDINGS = "com.parser.input.aion.strings";
	
	// World
	public static final String WORLD_MAPS = BASE_DIR + "Data/world/WorldId.xml";
	public static final String WORLD_DATA = BASE_DIR + "Data/world/";
	public static final String WORLD_DATA_PREFIX = "client_world_";
	public static final String WORLD_MAPS_BINDINGS = "com.parser.input.aion.world_maps";
	public static final String WORLD_DATA_BINDINGS = "com.parser.input.aion.world_data";
	
	public static final String WAYPOINTS = BASE_DIR + "Data/world/waypoint.csv";
	public static final String SOURCE_SPHERE = BASE_DIR + "Data/world/source_sphere.csv";
	
	// Skills
	public static final String SKILLS = BASE_DIR + "Data/skills/client_skills.xml";
	public static final String SKILLS_BINDINGS = "com.parser.input.aion.skills";
	
	public static final String SKILL_TREE = BASE_DIR + "Data/skills/client_skill_learns.xml";
	public static final String SKILL_TREE_BINDINGS = "com.parser.input.aion.skill_learn";
	
	// Recipes
	public static final String RECIPES = BASE_DIR + "Data/Items/client_combine_recipe.xml";
	public static final String RECIPES_BINDINGS = "com.parser.input.aion.recipes";
	
	// (Dir) Items
	public static final String ITEMS = BASE_DIR + "Data/Items/";
	public static final String ITEMS_PREFIX = "client_items_";
	public static final String ITEMS_BINDINGS = "com.parser.input.aion.items";
	
	// Npcs
	public static final String NPCS = BASE_DIR + "Data/Npcs/client_npcs.xml";
	public static final String NPCS_BINDINGS = "com.parser.input.aion.npcs";
	
	// Animations
	public static final String ANIMATIONS = BASE_DIR + "Data/Animations/custom_animation.xml";
	public static final String ANIMATIONS_BINDINGS = "com.parser.input.aion.animations";
	
	// Housing
	public static final String HOUSING_OBJECTS = BASE_DIR + "Data/Housing/client_housing_object.xml";
	public static final String HOUSING_PARTS = BASE_DIR + "Data/Housing/client_housing_custom_part.xml";
	public static final String HOUSING_BINDINGS = "com.parser.input.aion.housing";
	
	// Toypets
	public static final String TOYPETS = BASE_DIR + "Data/func_pet/toypets.xml";
	public static final String TOYPETS_BINDINGS = "com.parser.input.aion.toypets";
	
	// Levels
	public static final String MISSION0 = BASE_DIR + "Levels/";
	public static final String MISSION0_BINDINGS = "com.parser.input.aion.mission";
	public static final String LEVEL_DATA_BINDINGS = "com.parser.input.aion.level_data";
	public static final String MISSION0_PREFIX = "mission_";
	
	// Doc files
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
