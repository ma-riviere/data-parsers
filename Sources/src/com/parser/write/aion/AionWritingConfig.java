package com.parser.write.aion;

public class AionWritingConfig {

	public static final String VERSION = "aion40/";
	public static final String BASE_DIR = "../../Output/" + VERSION;
	
	public static final boolean WRITE_CUSTOM = true;
	
	/********************** CLIENT XML WRITING ******************************/
	
	// Items
	public static final String CLIENT_ITEMS = BASE_DIR + "Custom/client_items_merged.xml";
	
	/********************* INTERNAL XML WRITING ****************************/
	
	// Items
	public static final String ITEMS_NAME_ID = BASE_DIR + "Custom/items_name_id.xml";
	public static final String ITEMS_NAME_ID_BINDINGS = "com.parser.output.aion.item_name";
	
	/*************************** TEST XMLs *********************************/
	// Height Maps
	public static final String POINTS = BASE_DIR + "Tests/height_map.xml";
	public static final String POINTS_BINDINGS = "com.parser.output.aion.height_map";
	
	/*************************** SERVER XMLs *********************************/
	
	// Rides
	public static final String RIDE = BASE_DIR + "Ride/ride.xml";
	public static final String RIDE_BINDINGS = "com.parser.output.aion.rides";
	
	// Instances Cooltimes
	public static final String COOLTIMES = BASE_DIR + "instance_cooltimes/instance_cooltimes.xml";
	public static final String COOLTIMES_BINDINGS = "com.parser.output.aion.cooltimes";
	
	// Skills
	public static final String SKILLS = BASE_DIR + "Skills/skill_templates.xml";
	public static final String SKILLS_BINDINGS = "com.parser.output.aion.skills";
	
	// Items
	public static final String ITEMS = BASE_DIR + "items/item_templates.xml";
	public static final String ITEMS_BINDINGS = "com.parser.output.aion.items";
	
	// Npcs
	public static final String NPCS = BASE_DIR + "npcs/npc_templates.xml";
	public static final String NPCS_BINDINGS = "com.parser.output.aion.npcs";
	
	/** ############################################################## **/

	// npc shouts
	public static final String NPC_SHOUTS = "npc_shouts.xml";
	public static final String NPC_SHOUTS_BINDINGS = "com.parser.output.aion.npcshouts";

	// pets
	public static final String PETS = "pets.xml";
	public static final String PETS_BINDINGS = "com.parser.output.aion.pets";

	// tribe_relations
	public static final String NPC_TRIBE_RELATIONS = "tribe_relations.xml";
	public static final String NPC_TRIBE_RELATIONS_BINDINGS = "com.parser.output.aion.triberelations";
	
	// items
	public static final String ITEMSOPTIONS = "item_random_bonuses.xml";
	public static final String ITEMSOPTIONS_BINDINGS = "com.parser.output.aion.itembonuses";
	
	// assembly items
	public static final String ASSEMBLYITEMS = "assembly_items.xml";
	public static final String ASSEMBLYITEMS_BINDINGS = "com.parser.output.aion.assemblyitems";

	// quest_data
	public static final String QUESTS = "quest_data.xml";
	public static final String QUESTS_BINDINGS = "com.parser.output.aion.questdata";

	// zone_data
	public static final String ZONES = "zonedir/zones_";
	public static final String ZONES_BINDINGS = "com.parser.output.aion.zones";

	// player titles
	public static final String TITLES = "player_titles.xml";
	public static final String TITLES_BINDINGS = "com.parser.output.aion.playertitles";
	
	// housing objects
	public static final String HOUSING_OBJECTS = "housing_objects.xml";
	public static final String HOUSING_OBJECTS_BINDINGS = "com.parser.output.aion.housingobjects";
	
	// house instances
	public static final String HOUSES = "houses.xml";
	// house buildings
	public static final String HOUSE_BUILDINGS = "house_buildings.xml";
	public static final String HOUSES_BINDINGS = "com.parser.output.aion.houses";
	
	// house custom parts
	public static final String HOUSE_PARTS = "house_parts.xml";
	public static final String HOUSE_PARTS_BINDINGS = "com.parser.output.aion.houseparts";

	// item sets
	public static final String ITEMSETS = "item_sets.xml";
	public static final String ITEMSETS_BINDINGS = "com.parser.output.aion.itemsets";
	
	// system mails
	public static final String MAIL = "mail_templates.xml";
	public static final String SYSMAIL_BINDINGS = "com.parser.output.aion.sysmail";

	// challenge quests
	public static final String CHALLENGE_TASKS = "challenge_tasks.xml";
	public static final String CHALLENGE_TASKS_BINDINGS = "com.parser.output.aion.questdata.challenge";

	// material zones
	public static final String MATERIALS = "material_templates.xml";
	public static final String MATERIALS_BINDINGS = "com.parser.output.aion.materialtemplates";

	// weather table
	public static final String WEATHER = "weather_table.xml";
	public static final String WEATHER_BINDINGS = "com.parser.output.aion.weather";

}
