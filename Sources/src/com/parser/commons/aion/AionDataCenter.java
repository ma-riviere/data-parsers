package com.parser.commons.aion;

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.input.aion.animations.ClientAnimation;
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
import com.parser.commons.aion.bindings.WayPoint; //TODO
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

public class AionDataCenter {

	public Logger log = new Logger().getInstance();	
	public AionDataCenter() {}
	public static final AionDataCenter getInstance() {return SingletonHolder.instance;}
	
	/********************** SKILLS ***************************/
	
	public Map<String, ClientSkill> skills = new HashMap<String, ClientSkill>();
	public Map<String, ClientSkillTree> skillTrees = new HashMap<String, ClientSkillTree>();
	
	public Map<String, ClientSkill> getSkills() {
		if (skills.isEmpty())
			for (ClientSkill cs : AionSkillParser.parse())
				skills.put(JAXBHandler.getValue(cs, "name").toString().toUpperCase(), cs);
		return skills;
	}
	
	// JAXB Method : Will return the property "needed" of the skill which property "prop" matches the given "value", or null if no match is found
	public Object skillFinder(String needed, String prop, Object value) {
		for (ClientSkill client : getClientSkills().values()) {
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
		for (String ficelle : getClientSkills().keySet())
			if (ficelle.contains(s.toUpperCase()))
				results.add(skills.get(ficelle));
		return results;
	}
	
	public Map<String, ClientSkillTree> getSkillTrees() {
		if (skillTrees.isEmpty())
			skillTrees = index(new AionSkillTreeParser().parse(), on(ClientSkillTree.class).getName().toUpperCase());
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
		if (itemNameIdMap.values().isEmpty())
			for (Item ci : new AionItemsInternalParser().parse())
				itemNameIdMap.put(ci.getName().toUpperCase(), ci.getId());
		
		int id = itemNameIdMap.get(name.toUpperCase());
		if (id != null) return itemNameIdMap.get(name.toUpperCase());

		log.unique("[ITEMS] No Item ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	/********************** NPCS ***************************/
	
	public Map<String, ClientNpc> npcs = new HashMap<String, ClientNpc>();
	
	public Map<Integer, ClientNpc> getNpcs() {
		if (npcs.values().isEmpty())
			npcs = index(new AionNpcsParser().parse(), on(ClientNpc.class).getName().toUpperCase());
		return npcs;
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
				recipes.put(JAXBHandler.getValue(cr, "name").toString().toUpperCase(), (int) JAXBHandler.getValue(cr, "id"));
		return recipes;
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
	
	public Map<Integer, List<ClientSpawn>> spawns = new HashMap<Integer, List<ClientSpawn>>();
	public Map<Integer, List<Entity>> entities = new HashMap<Integer, List<Entity>>();
	public Map<Integer, LevelInfo> levelInfos = new HashMap<Integer, LevelInfo>();
	
	public Map<Integer, List<Entity>> getClientEntities() {
		if (entities.values().isEmpty())
			for (Map.Entry<String, List<Entity>> entry : new AionMissionParser().parseEntities().entrySet())
				entities.put(getWorldId(entry.getKey()), entry.getValue());
		return entities;
	}
	
	public Map<Integer, List<ClientSpawn>> getClientSpawns() {
		if (spawns.values().isEmpty())
			for (Map.Entry<String, List<ClientSpawn>> entry : new AionMissionParser().parseSpawns().entrySet())
				spawns.put(getWorldId(entry.getKey()), entry.getValue());
		return spawns;
	}
	
	public Map<Integer, LevelInfo> getLevelInfos() {
		if (levelInfos.values().isEmpty())
			for (Map.Entry<String, LevelInfo> entry : new AionLevelDataParser().parse().entrySet())
				levelInfos.put(getWorldId(entry.getKey()), entry.getValue());
		return levelInfos;
	}
	
	/********************** STRINGS *************************/
	
	public Map<String, ClientString> dataStrings = new HashMap<String, ClientString>();
	public Map<String, ClientString> l10nStrings = new HashMap<String, ClientString>();
	
	public int getClientStringId(String c_string, int mult, int add) {
		if (dataStrings.values().isEmpty()) dataStrings = index(new AionDataStringParser().parse(), on(ClientString.class).getName().toUpperCase());
		if (l10nStrings.values().isEmpty()) l10nStrings = index(new AionL10NStringParser().parse(), on(ClientString.class).getName().toUpperCase());
		
		ClientString l10n = l10nStrings.get(c_string.toUpperCase());
		if (!Strings.isNullOrEmpty(l10n))
			return (l10n.getId() * mult) + add;
		
		ClientString data = dataStrings.get(c_string.toUpperCase());
		if (!Strings.isNullOrEmpty(data))
			return (data.getId() * mult) + add;
		
		log.unique("[STRINGS] No ID matching string : ", c_string.toUpperCase(), true);
		return 0;
	}
	
	public String getClientStringText(String c_string) {
		if (dataStrings.values().isEmpty()) dataStrings = index(new AionDataStringParser().parse(), on(ClientString.class).getName().toUpperCase());
		if (l10nStrings.values().isEmpty()) l10nStrings = index(new AionL10NStringParser().parse(), on(ClientString.class).getName().toUpperCase());

		ClientString l10n = l10nStrings.get(c_string.toUpperCase());
		if (!Strings.isNullOrEmpty(l10n))
			return l10n.getBody();
		
		ClientString data = dataStrings.get(c_string.toUpperCase());
		if (!Strings.isNullOrEmpty(data))
			return data.getBody();
		
		log.unique("[STRINGS] No Body matching string : ", c_string.toUpperCase(), true);
		return 0;
	}	
	
	/********************** WORLD ***************************/
	
	public Map<Integer, List<NpcInfo>> dataSpawns = new HashMap<Integer, List<NpcInfo>>();
	public Map<Integer, List<SourceSphere>> spheres = new HashMap<Integer, List<SourceSphere>>();
	public Map<SourceSphere, Integer> sphereUseCount = new HashMap<SourceSphere, Integer>();
	public Map<Integer, WorldMap> worldMaps = new HashMap<Integer, WorldMap>();
	
	public Map<String, WorldMap> getWorldMaps() {
		if (worldMaps.values().isEmpty())
			index(new AionWorldMapsParser().parse(), on(WorldMap.class).getValue().toUpperCase());
		return worldMaps;
	}
	
	public Map<Integer, List<SourceSphere>> getSpheres() {
		if (spheres.values().isEmpty())
			for (Map.Entry<String, List<SourceSphere>> entry : new AionSourceSphereParser().parse().entrySet())
				spheres.put(getWorldId(entry.getKey()), entry.getValue());
		return spheres;
	}
	
	public void loadSphereUseCount() {
		for (SourceSphere ss : flatten(spheres.values()))
			sphereUseCount.put(ss, ss.getClusterNum() > 0 ? ss.getClusterNum() : -1);
	}
	
	public boolean canBeUsed(SourceSphere ss) {
		// return sphereUseCount.get(ss) != 0;
		return true;
	}
	
	public void reduceSphereCount(SourceSphere ss) {
		// sphereUseCount.put(ss, sphereUseCount.get(ss) -1);
	}
	
	public Map<Integer, List<NpcInfo>> getDataSpawns() {
		if (dataSpawns.values().isEmpty()) {
			for (Map.Entry<String, List<NpcInfo>> entry : new AionWorldNpcParser().parseNpcInfos())
				dataSpawns.put(getWorldId(entry.getKey()), entry.getValue());
		}
		return dataSpawns;
	}
	
	public List<NpcInfo> getDataSpawns(int mapId) {
		if (dataSpawns.containsKey(mapId))
			return dataSpawns.get(mapId);
		else {
			String file = WorldProperties.INPUT + WorldProperties.INPUT_PREFIX + getWorld(mapId).getValue().toLowerCase();
			return new AionWorldNpcParser().parseNpcInfos(file);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder 	{
		protected static final AionDataCenter instance = new AionDataCenter();
	}
}