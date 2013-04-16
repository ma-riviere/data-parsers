package com.parser.commons.aion;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javolution.util.FastMap;

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
import com.parser.commons.aion.properties.*;
import com.parser.commons.utils.JAXBHandler;
import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

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
	
	public List<ClientItem> clientItems = new ArrayList<ClientItem>();
	// Data-Skills
	public List<ClientSkill> clientSkills = new ArrayList<ClientSkill>(); //TODO: Rework
	public List<ClientSkillTree> clientSkillTree = new ArrayList<ClientSkillTree>(); //TODO: Rework
	// Data-Npcs
	public FastMap<Integer, ClientNpc> clientNpcs = new FastMap<Integer, ClientNpc>();
	// Levels
	public FastMap<Integer, List<ClientSpawn>> clientSpawns = new FastMap<Integer, List<ClientSpawn>>();
	public FastMap<Integer, List<Entity>> clientEntities = new FastMap<Integer, List<Entity>>();
	public FastMap<Integer, LevelInfo> levelInfos = new FastMap<Integer, LevelInfo>();
	// Data-World
	public FastMap<Integer, List<NpcInfo>> worldNpcInfos = new FastMap<Integer, List<NpcInfo>>();
	public FastMap<Integer, List<SourceSphere>> clientSpheres = new FastMap<Integer, List<SourceSphere>>();
	public FastMap<SourceSphere, Integer> sphereUseCount = new FastMap<SourceSphere, Integer>();
	public FastMap<Integer, WorldMap> worldMaps = new FastMap<Integer, WorldMap>();
	
	// Special Maps
	public FastMap<String, ClientString> dataDescStringMap = new FastMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public FastMap<String, ClientString> l10nDescStringMap = new FastMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public  FastMap<String, Integer> rideNameIdMap = new FastMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public FastMap<String, Integer> itemNameIdMap = new FastMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public FastMap<String, Integer> skillNameIdMap = new FastMap<String, Integer>(); // Skills Name (String) <--> id (int)
	public FastMap<String, Integer> npcNameIdMap = new FastMap<String, Integer>(); // Npc Name (String) <--> id (int)
	public FastMap<String, Integer> recipeNameIdMap = new FastMap<String, Integer>(); // Recipes Name (String) <--> id (int)
	
	public FastMap<String, Integer> animationNameIdMap = new FastMap<String, Integer>(); // Client Animation Name (String) <--> Animation ID (Int)
	private FastMap<String, Integer> houseObjectNameIdMap = new FastMap<String, Integer>(); // House Object Name (String) <--> ID (int)
	private FastMap<String, Integer> houseDecoNameIdMap = new FastMap<String, Integer>(); // House Object Name (String) <--> ID (int)
	private FastMap<String, Integer> toyPetNameIdMap = new FastMap<String, Integer>(); // ToyPets Name (String) <--> ID (int)
	
	public static final AionDataCenter getInstance() {
		return SingletonHolder.instance;
	}
	
	public AionDataCenter() {}
	
	public List<ClientSkill> getClientSkills() {
		if (clientSkills.isEmpty())
			clientSkills = new AionSkillsParser().parse();
		return clientSkills;
	}
	
	public List<ClientItem> getClientItems() {
		if (clientItems.isEmpty())
			clientItems = new AionItemsParser().parse();
		return clientItems;
	}
	
	public List<ClientSkillTree> getClientSkillTree() {
		if (clientSkillTree.isEmpty())
			clientSkillTree = new AionSkillTreeParser().parse();
		return clientSkillTree;
	}
	
	public FastMap<Integer, ClientNpc> getClientNpcs() {
		if (clientNpcs.values().isEmpty())
			for (ClientNpc cn : new AionNpcsParser().parse())
				clientNpcs.put(cn.getId(), cn);
		return clientNpcs;
	}
	
	/********************** LEVELS ***************************/
	
	public FastMap<Integer, List<Entity>> getClientEntities() {
		if (clientEntities.values().isEmpty())
			for (Map.Entry<String, List<Entity>> entry : new AionMissionParser().parseEntities().entrySet())
				clientEntities.put(getWorldId(entry.getKey()), entry.getValue());
		return clientEntities;
	}
	
	public FastMap<Integer, List<ClientSpawn>> getClientSpawns() {
		if (clientSpawns.values().isEmpty())
			for (Map.Entry<String, List<ClientSpawn>> entry : new AionMissionParser().parseSpawns().entrySet())
				clientSpawns.put(getWorldId(entry.getKey()), entry.getValue());
		return clientSpawns;
	}
	
	public FastMap<Integer, LevelInfo> getLevelInfos() {
		if (levelInfos.values().isEmpty())
			for (Map.Entry<String, LevelInfo> entry : new AionLevelDataParser().parse().entrySet())
				levelInfos.put(getWorldId(entry.getKey()), entry.getValue());
		return levelInfos;
	}
	
	/********************** WORLD ***************************/
	
	public FastMap<Integer, List<SourceSphere>> getClientSpheres() {
		if (clientSpheres.values().isEmpty())
			for (Map.Entry<String, List<SourceSphere>> entry : new AionSourceSphereParser().parse().entrySet())
				clientSpheres.put(getWorldId(entry.getKey()), entry.getValue());
		return clientSpheres;
	}
	
	private void loadSphereUseCount() {
		for (Integer mapId : clientSpheres.keySet()) {
			for (SourceSphere ss : clientSpheres.get(mapId)) {
				if (ss.getClusterNum() <= 0)
					sphereUseCount.put(ss, -1);
				else
					sphereUseCount.put(ss, ss.getClusterNum());
			}
		}
	}
	
	public boolean canBeUsed(SourceSphere ss) {
		// return sphereUseCount.get(ss) != 0;
		return true;
	}
	
	public void reduceSphereCount(SourceSphere ss) {
		// sphereUseCount.put(ss, sphereUseCount.get(ss) -1);
	}
	
	public FastMap<Integer, List<NpcInfo>> getWorldNpcInfos() {
		if (worldNpcInfos.values().isEmpty()) {
			for (Map.Entry<String, List<NpcInfo>> entry : new AionWorldNpcParser().parseNpcInfos())
				worldNpcInfos.put(getWorldId(entry.getKey()), entry.getValue());
		}
		return worldNpcInfos;
	}
	
	public List<NpcInfo> getNpcInfoByMap(int mapId) {
		if (worldNpcInfos.containsKey(mapId))
			return worldNpcInfos.get(mapId);
		else {
			String file = WorldProperties.INPUT + WorldProperties.INPUT_PREFIX + getWorld(mapId).getValue().toLowerCase();
			return new AionWorldNpcParser().parseNpcInfos(file);
		}
	}
	
	public FastMap<Integer, WorldMap> getWorldMaps() {
		if (worldMaps.keySet().isEmpty()) {
			List<WorldMap> mapList = new AionWorldMapsParser().parse();
			for (WorldMap map : mapList)
				worldMaps.put(map.getId(), map);
		}
		return worldMaps;
	}
	
	public WorldMap getWorld(Object value) {
		
		if (value instanceof Integer) {
			if (getWorldMaps().keySet().contains(Integer.parseInt(value.toString())))
				return getWorldMaps().get(Integer.parseInt(value.toString()));
			else
				log.unique("[WORLD] No Map with ID : ", Integer.parseInt(value.toString()), false);
		}
		else if (value instanceof String) {
			for (WorldMap map : getWorldMaps().values()) {
				if (map.getValue().trim().equalsIgnoreCase(value.toString().trim()))
					return map;
				// else
					// log.unique("[WORLD] No Map named : ", value.toString().toUpperCase(), true);
			}
		}
		return null;
	}
	
	public int getWorldId(String s) {return (getWorld(s) != null) ? getWorld(s).getId() : 0;}
	
	
	/*******************
	 *** METHODS ***
	 *******************/
	
	public int getMatchingStringId(String c_string, int mult, int add) {
		if (dataDescStringMap.isEmpty()) {loadDataStrings();}
		if (l10nDescStringMap.isEmpty()) {loadL10NStrings();}
		
		if (l10nDescStringMap.get(c_string.toUpperCase()) != null)
			return l10nDescStringMap.get(c_string.toUpperCase()).getId() * mult + add;
		else if (dataDescStringMap.get(c_string.toUpperCase()) != null)
			return dataDescStringMap.get(c_string.toUpperCase()).getId() * mult + add;
		else
			log.unique("[STRING] No ID matching string : ", c_string.toUpperCase(), true);
		return 0;
	}
	
	public String getMatchingStringText(String c_string) {
		if (dataDescStringMap.isEmpty()) {loadDataStrings();}
		if (l10nDescStringMap.isEmpty()) {loadL10NStrings();}
		
		if (l10nDescStringMap.get(c_string.toUpperCase()) != null)
			return l10nDescStringMap.get(c_string.toUpperCase()).getBody();
		else if (dataDescStringMap.get(c_string.toUpperCase()) != null)
			return dataDescStringMap.get(c_string.toUpperCase()).getBody();
		else
			log.unique("[STRING] No Body matching string : ", c_string.toUpperCase(), true);
		return "Default String";
	}
	
	public int getRideIdByName(String name) {
		if (rideNameIdMap.isEmpty()) {loadRideNameIdMap();}
		
		if (rideNameIdMap.get(name.toUpperCase()) != null)
			return rideNameIdMap.get(name.toUpperCase());
		else
			log.unique("[RIDES] No Ride ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getItemIdByName(String name) {
		if (itemNameIdMap.isEmpty()) {loadItemNameIdMap();}
		
		if (itemNameIdMap.get(name.toUpperCase()) != null)
			return itemNameIdMap.get(name.toUpperCase());
		else
			log.unique("[ITEMS] No Item ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getSkillIdByName(String name) {
		if (skillNameIdMap.isEmpty()) {loadSkillNameIdMap();}
		
		if (skillNameIdMap.get(name.toUpperCase()) != null)
			return skillNameIdMap.get(name.toUpperCase());
		else
			log.unique("[SKILLS] No Skill ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public List<Integer> getAllSkillsContaining(String s) {
		if (skillNameIdMap.isEmpty()) {loadSkillNameIdMap();}
		
		List<Integer> results = new ArrayList<Integer>();
		for (String ficelle : skillNameIdMap.keySet())
			if (ficelle.contains(s.toUpperCase()))
				results.add(skillNameIdMap.get(ficelle));
		return results;
	}
	
	public int getNpcIdByName(String name) {
		if (npcNameIdMap.isEmpty()) {loadNpcNameIdMap();}
		
		if (npcNameIdMap.get(name.toUpperCase()) != null)
			return npcNameIdMap.get(name.toUpperCase());
		else
			log.unique("[NPCS] No Npc ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getRecipeIdByName(String name) {
		if (recipeNameIdMap.isEmpty()) {loadRecipeNameIdMap();}
		
		if (recipeNameIdMap.get(name.toUpperCase()) != null)
			return recipeNameIdMap.get(name.toUpperCase());
		else
			log.unique("[RECIPES] No Recipe ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getAnimationIdByName(String name) {
		if (animationNameIdMap.isEmpty()) {loadAnimationNameIdMap();}
		
		if (animationNameIdMap.get(name.toUpperCase()) != null)
			return animationNameIdMap.get(name.toUpperCase());
		else
			log.unique("[ANIMATIONS] No Animation ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getHousingObjectIdByName(String name) {
		if (houseObjectNameIdMap.isEmpty()) {loadHouseObjectNameIdMap();}
		
		if (houseObjectNameIdMap.get(name.toUpperCase()) != null)
			return houseObjectNameIdMap.get(name.toUpperCase());
		else
			log.unique("[HOUSING] No Housing Object matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getHousingPartIdByName(String name) {
		if (houseDecoNameIdMap.isEmpty()) {loadHousePartNameIdMap();}
		
		if (houseDecoNameIdMap.get(name.toUpperCase()) != null)
			return houseDecoNameIdMap.get(name.toUpperCase());
		else
			log.unique("[HOUSING] No Housing Parts matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	public int getToyPetIdByName(String name) {
		if (toyPetNameIdMap.isEmpty()) {loadToyPetNameIdMap();}
		
		if (toyPetNameIdMap.get(name.toUpperCase()) != null)
			return toyPetNameIdMap.get(name.toUpperCase());
		else
			log.unique("[TOYPETS] No ToyPet ID matching name : ", name.toUpperCase(), true);
		return 0;
	}
	
	// ***** JAXB Methods ****** //
	
	public Object skillFinder(String needed, String prop, Object value) {
		for (ClientSkill client : getClientSkills()) {
			if (JAXBHandler.getValue(client, prop) != null && JAXBHandler.getValue(client, prop).toString().equalsIgnoreCase(value.toString())) {
				if (Strings.isNullOrEmpty(needed))
					return client;
				else if (JAXBHandler.getValue(client, needed) != null)
					return JAXBHandler.getValue(client, needed);
			}
		}
		return null;
	}
	
	/*****************************************
	 ***      LOADING MEHTODS     ***
	 ****************************************/
	
	// Loading StringMap from Data
	public void loadDataStrings() {
		FastMap<String, List<ClientString>> clientStringMap = new AionDataStringParser().parse();
		if (clientStringMap.size() == 0) {return;}
		for (List<ClientString> lcs : clientStringMap.values())
			for (ClientString cs : lcs)
				if (cs != null)
					dataDescStringMap.put(cs.getName().toUpperCase(), cs);
	}
	
	// Loading StringMap from L10N
	public void loadL10NStrings() {
		FastMap<String, List<ClientString>> clientStringMap = new AionL10NStringParser().parse();
		if (clientStringMap.size() == 0) {return;}
		for (List<ClientString> lcs : clientStringMap.values())
			for (ClientString cs : lcs)
				if (cs != null)
					l10nDescStringMap.put(cs.getName().toUpperCase(), cs);
	}
	
	// Loading Ride Name <--> ID from client rides.xml
	public void loadRideNameIdMap() {
		List<ClientRide> clientRides = new AionRidesParser().parse();
		for (ClientRide cr : clientRides)
			rideNameIdMap.put(cr.getName().toUpperCase(), cr.getId());
	}
	
	// Loading Items Name <--> ID from an internal XML
	public void loadItemNameIdMap() {
		List<Item> clientItemsInternalList = new AionItemsInternalParser().parse();
		for (Item ci : clientItemsInternalList)
			itemNameIdMap.put(ci.getName().toUpperCase(), ci.getId());
	}
	
	// Loading Skill Name <--> ID from client XML
	public void loadSkillNameIdMap() {
		List<ClientSkill> clientSkills = getClientSkills();
		for (ClientSkill cs : clientSkills)
			skillNameIdMap.put(JAXBHandler.getValue(cs, "name").toString().toUpperCase(), (int) JAXBHandler.getValue(cs, "id"));
	}
	
	// Loading Npc Name <--> ID from client XML
	public void loadNpcNameIdMap() {
		List<ClientNpc> clientNpcs = new ArrayList<ClientNpc>(getClientNpcs().values());
		for (ClientNpc cn : clientNpcs)
			npcNameIdMap.put(cn.getName().toUpperCase(), cn.getId());
	}
	
	// Loading Recipes Name <--> ID from client XML
	public void loadRecipeNameIdMap() {
		List<ClientRecipe> clientRecipes = new AionRecipesParser().parse();
		for (ClientRecipe cr : clientRecipes)
			recipeNameIdMap.put(JAXBHandler.getValue(cr, "name").toString().toUpperCase(), (int) JAXBHandler.getValue(cr, "id"));
	}

	// Loading Animations Name <--> ID from client XML
	public void loadAnimationNameIdMap() {
		List<ClientAnimation> clientAnimations = new AionAnimationsParser().parse();
		for (ClientAnimation ca : clientAnimations)
			animationNameIdMap.put(ca.getName().toUpperCase(), ca.getId());
	}
	
	// Loading HouseObjects Name <--> ID from client XML
	public void loadHouseObjectNameIdMap() {
		List<ClientHousingObject> chos = new AionHousingObjectsParser().parse();
		for (ClientHousingObject cho : chos)
			houseObjectNameIdMap.put(cho.getName().toUpperCase(), cho.getId());
	}
	
	// Loading HouseParts Name <--> ID from client XML
	public void loadHousePartNameIdMap() {
		List<ClientHousingCustomPart> chps = new AionHousingPartsParser().parse();
		for (ClientHousingCustomPart chp : chps)
			houseDecoNameIdMap.put(chp.getName().toUpperCase(), chp.getId());
	}
	
	// Loading HouseParts Name <--> ID from client XML
	public void loadToyPetNameIdMap() {
		List<ClientToypet> cpts = new AionToyPetsParser().parse();
		for (ClientToypet cpt : cpts)
			toyPetNameIdMap.put(cpt.getName().toUpperCase(), cpt.getId());
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder 	{
		protected static final AionDataCenter instance = new AionDataCenter();
	}
}
