package com.parser.common.aion;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parser.input.aion.animations.ClientAnimation;
import com.parser.input.aion.housing.ClientHousingObject;
import com.parser.input.aion.housing.ClientHousingCustomPart;
import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;
import com.parser.input.aion.p_items.Item;
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
import com.parser.input.aion.world_data.NpcInfos;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.common.aion.bindings.SourceSphere;
import com.parser.common.utils.JAXBHandler;
import com.parser.common.utils.Logger;
import com.parser.common.utils.Util;

import com.parser.read.aion.animations.AionAnimationsParser;
import com.parser.read.aion.housing.AionHousingObjectsParser;
import com.parser.read.aion.housing.AionHousingPartsParser;
import com.parser.read.aion.items.AionItemsParser;
import com.parser.read.aion.items.AionItemsInternalParser;
import com.parser.read.aion.levels.AionLevelDataParser;
import com.parser.read.aion.levels.AionMissionParser;
import com.parser.read.aion.npcs.AionNpcsParser;
import com.parser.read.aion.recipes.AionRecipesParser;
import com.parser.read.aion.rides.AionRidesParser;
import com.parser.read.aion.skills.AionSkillsParser;
import com.parser.read.aion.skills.AionSkillTreeParser;
import com.parser.read.aion.strings.AionDataStringParser;
import com.parser.read.aion.strings.AionL10NStringParser;
import com.parser.read.aion.toypets.AionToyPetsParser;
import com.parser.read.aion.world.AionWorldMapsParser;
import com.parser.read.aion.world.AionWorldNpcParser;
import com.parser.read.aion.world.AionSourceSphereParser;


public class AionDataCenter {

	public static Logger log = new Logger().getInstance();
	
	// TODO: Move all main lists/maps to Map<Integer, List<Client>>
	// TODO: Move needed data to models ? Like SpawnData.java ???
	public static List<ClientSkill> clientSkills = new ArrayList<ClientSkill>();
	public static List<ClientItem> clientItems = new ArrayList<ClientItem>();
	public static List<ClientSkillTree> clientSkillTree = new ArrayList<ClientSkillTree>();
	
	public static Map<Integer, ClientNpc> clientNpcs = new HashMap<Integer, ClientNpc>();
	// Spawns
	public static Map<Integer, List<NpcInfo>> worldNpcInfos = new HashMap<Integer, List<NpcInfo>>();
	public static Map<Integer, ClientSpawn> clientSpawns = new HashMap<Integer, ClientSpawn>();
	public static Map<Integer, List<Entity>> clientEntities = new HashMap<Integer, List<Entity>>();
	public static Map<Integer, List<SourceSphere>> clientSpheres = new HashMap<Integer, List<SourceSphere>>();
	
	public static Map<String, WorldMap> worldMaps = new HashMap<String, WorldMap>();
	public static Map<String, LevelInfo> levelInfos = new HashMap<String, LevelInfo>();
	
	// Special Maps
	public static Map<String, ClientString> dataDescStringMap = new HashMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public static Map<String, ClientString> l10nDescStringMap = new HashMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public static Map<String, Integer> rideNameIdMap = new HashMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public static Map<String, Integer> itemNameIdMap = new HashMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public static Map<String, Integer> skillNameIdMap = new HashMap<String, Integer>(); // Skills Name (String) <--> id (int)
	public static Map<String, Integer> npcNameIdMap = new HashMap<String, Integer>(); // Npc Name (String) <--> id (int)
	public static Map<String, Integer> recipeNameIdMap = new HashMap<String, Integer>(); // Recipes Name (String) <--> id (int)
	
	public static Map<String, Integer> animationNameIdMap = new HashMap<String, Integer>(); // Client Animation Name (String) <--> Animation ID (Int)
	private static Map<String, Integer> houseObjectNameIdMap = new HashMap<String, Integer>(); // House Object Name (String) <--> ID (int)
	private static Map<String, Integer> houseDecoNameIdMap = new HashMap<String, Integer>(); // House Object Name (String) <--> ID (int)
	private static Map<String, Integer> toyPetNameIdMap = new HashMap<String, Integer>(); // ToyPets Name (String) <--> ID (int)
	
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
		if (clientItems.isEmpty()) {
			for (List<ClientItem> lci : new AionItemsParser().parse().values())
				clientItems.addAll(lci);
		}
		return clientItems;
	}
	
	public List<ClientSkillTree> getClientSkillTree() {
		if (clientSkillTree.isEmpty())
			clientSkillTree = new AionSkillTreeParser().parse();
		return clientSkillTree;
	}
	
	public Map<Integer, ClientNpc> getClientNpcs() {
		if (clientNpcs.values().isEmpty()) {
			for (ClientNpc cn : new AionNpcsParser().parse())
				clientNpcs.put(cn.getId(), cn);
		}
		return clientNpcs;
	}
	
	public Map<Integer, List<Entity>> getClientEntities() {
		if (clientEntities.values().isEmpty()) {
			Map<String, Mission> missionMap = new AionMissionParser().parseRoot();
			for (String mapName : missionMap) {
				List<Entity> temp = new ArrayList<Entity>();
				int mapId = getWorldId(Util.getDirName(mapName));
				if (mapId > 0) {
					if (clientEntities.contains(mapId))
						temp = clientEntities.get(mapId);
					temp.addAll(missionMap.get(mapName).getObjects().getEntity());
					clientEntities.put(mapId, temp);
				}
			}
		}
		return clientEntities;
	}
	
	public Map<Integer, List<SourceSphere>> getClientSpheres() {
		if (clientSpheres.values().isEmpty()) {
			for (SourceSphere ss : new AionSourceSphereParser().parse()) {
				List<SourceSphere> temp = new ArrayList<SourceSphere>();
				int mapId = getWorldId(ss.getMap());
				if (mapId > 0) {
					if (clientSpheres.contains(mapId)) {
						temp = clientSpheres.get(mapId);
						temp.add(ss);
					}
					else
						temp.add(ss);
					
					clientSpheres.put(mapId, temp);
				}
			}
		}
		return clientSpheres;
	}
	
	//// Levels ////
	
	public Map<Integer, List<NpcInfo>> getLevelData() {
		int mapId = 0;
		if (worldNpcInfos.keySet().isEmpty()) {
			Map<String, List<NpcInfos>> temp = new AionWorldNpcParser().parse();
			for (String mapName : temp.keySet()) {
				mapId = getWorldId(Util.getFileName(mapName));
				List<NpcInfo> npcInfoList = new ArrayList<NpcInfo>();
				for (NpcInfos npcs : temp.get(mapName)) {
					npcInfoList.addAll(npcs.getNpcInfo()); 
				}
				worldNpcInfos.put(mapId, npcInfoList);
				npcInfoList.clear();
			}
		}
		return worldNpcInfos;
	}
	
	public List<NpcInfo> getLevelDataByMap(String name) {
		List<NpcInfo> data = new ArrayList<NpcInfo>();
		if (worldNpcInfos.containsKey(getWorldId(Util.getDirName(name)))) {
			data = worldNpcInfos.get(getWorldId(Util.getDirName(name)));
		}
		else {
			for (NpcInfos infos : new AionWorldNpcParser().parseFile(name)) {
				data.addAll(infos.getNpcInfo());
			}
		}
		return data;
	}
	
	//// World ////
	
	public Map<Integer, List<NpcInfo>> getWorldNpcInfos() {
		int mapId = 0;
		if (worldNpcInfos.keySet().isEmpty()) {
			Map<String, List<NpcInfos>> temp = new AionWorldNpcParser().parse();
			for (String mapName : temp.keySet()) {
				mapId = getWorldId(Util.getFileName(mapName));
				List<NpcInfo> npcInfoList = new ArrayList<NpcInfo>();
				for (NpcInfos npcs : temp.get(mapName)) {
					npcInfoList.addAll(npcs.getNpcInfo()); 
				}
				worldNpcInfos.put(mapId, npcInfoList);
				npcInfoList.clear();
			}
		}
		return worldNpcInfos;
	}
	
	public List<NpcInfo> getNpcInfoByMap(String name) {
		List<NpcInfo> data = new ArrayList<NpcInfo>();
		if (worldNpcInfos.containsKey(getWorldId(Util.getDirName(name)))) {
			data = worldNpcInfos.get(getWorldId(Util.getDirName(name)));
		}
		else {
			for (NpcInfos infos : new AionWorldNpcParser().parseFile(name)) {
				data.addAll(infos.getNpcInfo());
			}
		}
		return data;
	}
	
	public Map<String, WorldMap> getWorldMaps() {
		if (worldMaps.values().isEmpty()) {
			List<WorldMap> mapList = new AionWorldMapsParser().parse();
			for (WorldMap map : mapList)
				worldMaps.put(map.getValue().toUpperCase(), map);
		}
		return worldMaps;
	}
	
	public WorldMap getWorld(Object value) {
		
		if (value instanceof String) {
			if (getWorldMaps().keySet().contains(value.toString().toUpperCase()))
				return getWorldMaps().get(value.toString().toUpperCase());
			else
				log.unique("[WORLD] No Map named : ", value.toString().toUpperCase(), true);
		}
		else if (value instanceof Integer) {
			for (WorldMap map : getWorldMaps().values()) {
				if (map.getId().equals(Integer.parseInt(value.toString())))
					return map;
				else
					log.unique("[WORLD] No Map with ID : ", Integer.parseInt(value.toString()), false);
			}
		}
		return null;
	}
	
	public int getWorldId(String s) {return (getWorld(s) != null) ? getWorld(s).getId() : 0;}
	
	public Map<String, LevelInfo> getLevelInfos() {
		Map<String, LevelInfo> infos = new HashMap<String, LevelInfo>();
		if (levelInfos.keySet().isEmpty()) {
			Map<String, LevelData> dataMap = new AionLevelDataParser().parseRoot();
			for (LevelData data : dataMap.values())
				infos.put(data.getLevelInfo().getName().toUpperCase(), data.getLevelInfo());
		}
		return infos;
	}
	
	
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
		Map<String, List<ClientString>> clientStringMap = new AionDataStringParser().parse();
		if (clientStringMap.size() == 0) {return;}
		for (List<ClientString> lcs : clientStringMap.values())
			for (ClientString cs : lcs)
				if (cs != null)
					dataDescStringMap.put(cs.getName().toUpperCase(), cs);
	}
	
	// Loading StringMap from L10N
	public void loadL10NStrings() {
		Map<String, List<ClientString>> clientStringMap = new AionL10NStringParser().parse();
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
