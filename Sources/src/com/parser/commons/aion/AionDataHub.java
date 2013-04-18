package com.parser.commons.aion;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.input.aion.animations.ClientAnimation;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;
import com.parser.input.aion.housing.ClientHousingObject;
import com.parser.input.aion.housing.ClientHousingCustomPart;
import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.level_data.LevelInfo;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.rides.ClientRide;
import com.parser.input.aion.skills.ClientSkill;
import com.parser.input.aion.skill_learn.ClientSkillTree;
import com.parser.input.aion.strings.ClientString;
import com.parser.input.aion.toypets.ClientToypet;
import com.parser.input.aion.world_maps.WorldMap;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.bindings.WayPoint;
import com.parser.commons.aion.properties.*;
import com.parser.commons.utils.*;

import com.parser.read.aion.animations.AionAnimationsParser;
import com.parser.read.aion.housing.*;
import com.parser.read.aion.items.*;
import com.parser.read.aion.levels.*;
import com.parser.read.aion.npcs.AionNpcsParser;
import com.parser.read.aion.recipes.AionRecipesParser;
import com.parser.read.aion.rides.AionRidesParser;
import com.parser.read.aion.skills.*;
import com.parser.read.aion.strings.*;
import com.parser.read.aion.toypets.AionToyPetsParser;
import com.parser.read.aion.world.*;

import com.parser.output.aion.item_name.Item;

public class AionDataHub {

	public Logger log = new Logger().getInstance();	
	public AionDataHub() {}
	public static final AionDataHub getInstance() {return SingletonHolder.instance;}
	
	/********************** SKILLS ***************************/
	
	public Map<String, ClientSkill> skills = new HashMap<String, ClientSkill>();
	public Map<String, ClientSkillTree> skillTrees = new HashMap<String, ClientSkillTree>();
	
	public Map<String, ClientSkill> getSkills() {
		if (skills.values().isEmpty())
			for (ClientSkill cs : new AionSkillsParser().parse())
				skills.put(JAXBHandler.getValue(cs, "name").toString().toUpperCase(), cs);
		return skills;
	}
	
	public int getSkillId(String s) {
		return (!Strings.isNullOrEmpty(s) && getSkills().get(s) != null) ? (Integer) JAXBHandler.getValue(getSkills().get(s), "id") : 0;
	}
	
	// JAXB Method : Will return the property "needed" of the skill which property "prop" matches the given "value", or null if no match is found
	public Object skillFinder(String needed, String prop, Object value) {
		for (ClientSkill client : getSkills().values()) {
			if (JAXBHandler.getValue(client, prop) != null && JAXBHandler.getValue(client, prop).toString().equalsIgnoreCase(value.toString())) {
				if (Strings.isNullOrEmpty(needed))
					return client;
				else if (JAXBHandler.getValue(client, needed) != null)
					return JAXBHandler.getValue(client, needed);
			}
		}
		return null;
	}
	
	public List<Integer> getAllSkillsContaining(String s) {		
		List<Integer> results = new ArrayList<Integer>();
		for (String ficelle : getSkills().keySet())
			if (ficelle.contains(s.toUpperCase()))
				results.add((Integer) JAXBHandler.getValue(skills.get(ficelle), "id"));
		return results;
	}
	
	public Map<String, ClientSkillTree> getSkillTrees() {
		if (skillTrees.values().isEmpty())
			skillTrees = index(new AionSkillTreeParser().parse(), on(ClientSkillTree.class).getSkill().toUpperCase());
		return skillTrees;
	}
	
	/********************** ITEMS ***************************/
	
	public Map<String, ClientItem> items = new HashMap<String, ClientItem>();
	public Map<String, Integer> itemNameIdMap = new HashMap<String, Integer>();
	
	public Map<String, ClientItem> getItems() {
		if (items.isEmpty())
			items = index(new AionItemsParser().parse(), on(ClientItem.class).getName().toUpperCase());
		return items;
	}
	
	// Accessing Item ID by a separate xml file
	public int getItemId(String name) {
		if (itemNameIdMap.isEmpty()) {
			for (Item ci : new AionItemsInternalParser().parse())
				itemNameIdMap.put(ci.getName().toUpperCase(), ci.getId());
		}
		return (!Strings.isNullOrEmpty(name) && itemNameIdMap.get(name.toUpperCase()) != null) ? itemNameIdMap.get(name.toUpperCase()) : 0;
	}
	
	/********************** NPCS ***************************/
	
	public Map<String, ClientNpc> npcs = new HashMap<String, ClientNpc>();
	
	public Map<String, ClientNpc> getNpcs() {
		if (npcs.values().isEmpty())
			npcs = index(new AionNpcsParser().parse(), on(ClientNpc.class).getName().toUpperCase());
		return npcs;
	}
	
	public ClientNpc getNpc(int id) {
		return selectUnique(getNpcs(), having(on(ClientNpc.class).getId(), equalTo(id)));
	}
	
	public int getNpcId(String s) {
		return (!Strings.isNullOrEmpty(s) && getNpcs().get(s.toUpperCase()) != null) ? getNpcs().get(s.toUpperCase()).getId() : 0;
	}
	
	/*********************** RIDES ****************************/
	
	public  Map<String, ClientRide> rides = new HashMap<String, ClientRide>();
	
	public Map<String, ClientRide> getRides() {
		if (rides.values().isEmpty()) 
			rides = index(new AionRidesParser().parse(), on(ClientRide.class).getName().toUpperCase());
		return rides;
	}
	
	/********************** RECIPES *************************/
	
	public Map<String, ClientRecipe> recipes = new HashMap<String, ClientRecipe>();
	
	public Map<String, ClientRecipe> getRecipes() {
		if (recipes.values().isEmpty()) 
			for (ClientRecipe cr : new AionRecipesParser().parse())
				recipes.put(JAXBHandler.getValue(cr, "name").toString().toUpperCase(), cr);
		return recipes;
	}
	
	public int getRecipeId(String s) {
		return (!Strings.isNullOrEmpty(s) && getRecipes().get(s) != null) ? (Integer) JAXBHandler.getValue(getRecipes().get(s), "id") : 0;
	}
	
	/********************* ANIMATIONS ***********************/
	
	public Map<String, ClientAnimation> animations = new HashMap<String, ClientAnimation>();
	
	public Map<String, ClientAnimation> getAnimations() {
		if (animations.values().isEmpty()) 
			animations = index(new AionAnimationsParser().parse(), on(ClientAnimation.class).getName().toUpperCase());
		return animations;
	}
	
	/********************* HOUSING ***********************/
	
	private Map<String, ClientHousingObject> houseObjects = new HashMap<String, ClientHousingObject>();
	private Map<String, ClientHousingCustomPart> houseParts = new HashMap<String, ClientHousingCustomPart>();
	
	public Map<String, ClientHousingObject> getHouseObjects() {
		if (houseObjects.values().isEmpty()) 
			houseObjects = index(new AionHousingObjectsParser().parse(), on(ClientHousingObject.class).getName().toUpperCase());
		return houseObjects;
	}
	
	public Map<String, ClientHousingCustomPart> getHouseParts() {
		if (houseParts.values().isEmpty()) 
			houseParts = index(new AionHousingPartsParser().parse(), on(ClientHousingCustomPart.class).getName().toUpperCase());
		return houseParts;
	}
	
	/********************* TOYPETS ***********************/
	
	private Map<String, ClientToypet> toyPets = new HashMap<String, ClientToypet>();
	
	public Map<String, ClientToypet> getToyPets() {
		if (toyPets.values().isEmpty()) 
			toyPets = index(new AionToyPetsParser().parse(), on(ClientToypet.class).getName().toUpperCase());
		return toyPets;
	}
	
	/********************** LEVELS ***************************/
	
	public Map<String, List<ClientSpawn>> spawns = new HashMap<String, List<ClientSpawn>>();
	public Map<String, List<Entity>> entities = new HashMap<String, List<Entity>>();
	public Map<String, LevelInfo> levelInfos = new HashMap<String, LevelInfo>();
	
	public Map<String, List<Entity>> getLevelEntities() {
		if (entities.values().isEmpty())
			entities = new AionMissionParser().parseEntities();
		return entities;
	}
	
	public Map<String, List<ClientSpawn>> getLevelSpawns() {
		if (spawns.values().isEmpty())
			spawns = new AionMissionParser().parseSpawns();
		return spawns;
	}
	
	public Map<String, LevelInfo> getLevelInfos() {
		if (levelInfos.values().isEmpty())
			levelInfos = new AionLevelDataParser().parse();
		return levelInfos;
	}
	
	/********************** STRINGS *************************/
	
	public Map<String, ClientString> dataStrings = new HashMap<String, ClientString>();
	public Map<String, ClientString> l10nStrings = new HashMap<String, ClientString>();
	public Map<String, ClientString> strings = new HashMap<String, ClientString>();
	
	// Replacing koorean Strings by it's traduction, if exists
	public Map<String, ClientString> getStrings() {
		if (dataStrings.values().isEmpty()) dataStrings = index(new AionDataStringParser().parse(), on(ClientString.class).getName().toUpperCase());
		if (l10nStrings.values().isEmpty()) l10nStrings = index(new AionL10NStringParser().parse(), on(ClientString.class).getName().toUpperCase());
		
		for (String s : dataStrings.keySet()) {
			if (l10nStrings.containsKey(s))
				strings.put(s, l10nStrings.get(s));
			else
				strings.put(s, dataStrings.get(s));
		}
		return strings;
	}
	
	public int getStringId(String s) {
		return (!Strings.isNullOrEmpty(s) && getStrings().get(s.toUpperCase()) != null) ? getStrings().get(s.toUpperCase()).getId() : 0;
	}
	
	public String getStringText(String s) {
		return (!Strings.isNullOrEmpty(s) && getStrings().get(s.toUpperCase()) != null) ? getStrings().get(s.toUpperCase()).getBody() : StringUtils.EMPTY;
	}
	
	/********************** WORLD ***************************/
	
	public Map<String, WorldMap> worldMaps = new HashMap<String, WorldMap>();
	public Map<String, List<NpcInfo>> dataSpawns = new HashMap<String, List<NpcInfo>>();
	
	public Map<String, List<SourceSphere>> spheres = new HashMap<String, List<SourceSphere>>();
		public Map<SourceSphere, Integer> sphereUseCount = new HashMap<SourceSphere, Integer>();
	public Map<String, WayPoint> waypoints = new HashMap<String, WayPoint>();
	
	public Map<ClientInstanceCooltime, ClientInstanceCooltime2> cooltimes = new HashMap<ClientInstanceCooltime, ClientInstanceCooltime2>();
	
	public Map<String, WorldMap> getWorldMaps() {
		if (worldMaps.values().isEmpty())
			worldMaps = index(new AionWorldMapsParser().parse(), on(WorldMap.class).getValue().toUpperCase());
		return worldMaps;
	}
	
	public int getWorldId(String map) {
		return (!Strings.isNullOrEmpty(map) && getWorldMaps().get(map.toUpperCase()) != null) ? getWorldMaps().get(map.toUpperCase()).getId() : 0;
	}
	
	public Map<String, List<NpcInfo>> getDataSpawns() {
		if (dataSpawns.values().isEmpty())
			dataSpawns = new AionClientWorldParser().parseNpcInfos();
		return dataSpawns;
	}
	
	public List<NpcInfo> getDataSpawns(String map) {
		if (!dataSpawns.containsKey(map.toUpperCase())) {
			String file = WorldProperties.INPUT + WorldProperties.CLIENT_WORLD_PREFIX + map.toLowerCase();
			dataSpawns.put(map, new AionClientWorldParser().parseNpcInfos(file));
		}
		return dataSpawns.get(map.toUpperCase());
	}
	
	public Map<String, List<SourceSphere>> getSpheres() {
		if (spheres.values().isEmpty())
			spheres = new AionSourceSphereParser().parse();
		return spheres;
	}
	
	public void loadSphereUseCount() {
		for (SourceSphere ss : new ArrayList<SourceSphere>(flatten(spheres.values())))
			sphereUseCount.put(ss, ss.getClusterNum() > 0 ? ss.getClusterNum() : -1);
	}
	
	public boolean canBeUsed(SourceSphere ss) {
		// return sphereUseCount.get(ss) != 0;
		return true;
	}
	
	public void reduceSphereCount(SourceSphere ss) {
		// sphereUseCount.put(ss, sphereUseCount.get(ss) -1);
	}
	
	public Map<String, WayPoint> getWayPoints() {
		if (waypoints.values().isEmpty())
			waypoints = index(new AionWayPointsParser().parse(), on(WayPoint.class).getName().toUpperCase());
		return waypoints;
	}
	
	public Map<ClientInstanceCooltime, ClientInstanceCooltime2> getCooltimes() {
		if (cooltimes.values().isEmpty())
			cooltimes = index(new AionCooltimesParser().parse(), selectUnique(new AionCooltimes2Parser().parse(), having(on(ClientInstanceCooltime2.class).getId() == on(ClientInstanceCooltime.class).getId())));
		return cooltimes;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder 	{
		protected static final AionDataHub instance = new AionDataHub();
	}
}