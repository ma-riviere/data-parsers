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
import com.parser.input.aion.p_items.Item;
import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.recipes.ClientRecipe;
import com.parser.input.aion.rides.ClientRide;
import com.parser.input.aion.skills.ClientSkill;
import com.parser.input.aion.skill_learn.ClientSkillTree;
import com.parser.input.aion.strings.ClientString;
import com.parser.input.aion.toypets.ClientToypet;
import com.parser.input.aion.world.Data;

import com.parser.common.JAXBHandler;
import com.parser.read.aion.animations.AionAnimationsParser;
import com.parser.read.aion.housing.AionHousingObjectsParser;
import com.parser.read.aion.housing.AionHousingPartsParser;
import com.parser.read.aion.items.AionItemsParser;
import com.parser.read.aion.items.AionItemsInternalParser;
import com.parser.read.aion.npcs.AionNpcsParser;
import com.parser.read.aion.recipes.AionRecipesParser;
import com.parser.read.aion.rides.AionRidesParser;
import com.parser.read.aion.skills.AionSkillsParser;
import com.parser.read.aion.skills.AionSkillTreeParser;
import com.parser.read.aion.strings.AionDataStringParser;
import com.parser.read.aion.strings.AionL10NStringParser;
import com.parser.read.aion.toypets.AionToyPetsParser;
import com.parser.read.aion.world.AionWorldDataParser;


public class AionDataCenter {
	
	// ClientObjects lists (TODO)
	public static List<ClientSkill> clientSkills = new ArrayList<ClientSkill>();
	public static List<ClientItem> clientItems = new ArrayList<ClientItem>();
	public static List<ClientSkillTree> clientSkillTree = new ArrayList<ClientSkillTree>();
	// Special Maps
	public static Map<String, ClientString> dataDescStringMap = new HashMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public static Map<String, ClientString> l10nDescStringMap = new HashMap<String, ClientString>(); // Client String <--> Real Text or NameID
	public static Map<String, Integer> rideNameIdMap = new HashMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public static Map<String, Integer> itemNameIdMap = new HashMap<String, Integer>(); // Items Name (String) <--> id (Int)
	public static Map<String, Integer> skillNameIdMap = new HashMap<String, Integer>(); // Skills Name (String) <--> id (int)
	public static Map<String, Integer> npcNameIdMap = new HashMap<String, Integer>(); // Npc Name (String) <--> id (int)
	public static Map<String, Integer> recipeNameIdMap = new HashMap<String, Integer>(); // Recipes Name (String) <--> id (int)
	public static Map<String, Data> descWorldIdMap = new HashMap<String, Data>(); // Client World Name (String) <--> World ID (Int)
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
			System.out.println("[STRING] No ID matching string : " + c_string.toUpperCase());
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
			System.out.println("[STRING] No Body matching string : " + c_string.toUpperCase());
		return "Default String";
	}
	
	public int getRideIdByName(String name) {
		if (rideNameIdMap.isEmpty()) {loadRideNameIdMap();}
		
		if (rideNameIdMap.get(name.toUpperCase()) != null)
			return rideNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[RIDES] No Ride ID matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getItemIdByName(String name) {
		if (itemNameIdMap.isEmpty()) {loadItemNameIdMap();}
		
		if (itemNameIdMap.get(name.toUpperCase()) != null)
			return itemNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[ITEMS] No Item ID matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getSkillIdByName(String name) {
		if (skillNameIdMap.isEmpty()) {loadSkillNameIdMap();}
		
		if (skillNameIdMap.get(name.toUpperCase()) != null)
			return skillNameIdMap.get(name.toUpperCase());
		// else
			// System.out.println("[SKILLS] No Skill ID matching name : " + name.toUpperCase());
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
			System.out.println("[NPCS] No Npc ID matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getRecipeIdByName(String name) {
		if (recipeNameIdMap.isEmpty()) {loadRecipeNameIdMap();}
		
		if (recipeNameIdMap.get(name.toUpperCase()) != null)
			return recipeNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[RECIPES] No Recipe ID matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getWorldIdByName(String name) {
		if (descWorldIdMap.isEmpty()) {loadDescWorldIdMap();}
		
		if (descWorldIdMap.containsKey(name.toUpperCase()))
			return descWorldIdMap.get(name.toUpperCase()).getId();
		else
			System.out.println("[WORLD] No MapID for client_map : " + name.toUpperCase());
		return 0;
	}
	
	public int getAnimationIdByName(String name) {
		if (animationNameIdMap.isEmpty()) {loadAnimationNameIdMap();}
		
		if (animationNameIdMap.get(name.toUpperCase()) != null)
			return animationNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[ANIMATIONS] No Animation ID matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getHousingObjectIdByName(String name) {
		if (houseObjectNameIdMap.isEmpty()) {loadHouseObjectNameIdMap();}
		
		if (houseObjectNameIdMap.get(name.toUpperCase()) != null)
			return houseObjectNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[HOUSING] No Housing Object matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getHousingPartIdByName(String name) {
		if (houseDecoNameIdMap.isEmpty()) {loadHousePartNameIdMap();}
		
		if (houseDecoNameIdMap.get(name.toUpperCase()) != null)
			return houseDecoNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[HOUSING] No Housing Parts matching name : " + name.toUpperCase());
		return 0;
	}
	
	public int getToyPetIdByName(String name) {
		if (toyPetNameIdMap.isEmpty()) {loadToyPetNameIdMap();}
		
		if (toyPetNameIdMap.get(name.toUpperCase()) != null)
			return toyPetNameIdMap.get(name.toUpperCase());
		else
			System.out.println("[TOYPETS] No ToyPet ID matching name : " + name.toUpperCase());
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
		List<ClientNpc> clientNpcs = new AionNpcsParser().parse();
		for (ClientNpc cn : clientNpcs)
			npcNameIdMap.put(cn.getName().toUpperCase(), cn.getId());
	}
	
	// Loading Recipes Name <--> ID from client XML
	public void loadRecipeNameIdMap() {
		List<ClientRecipe> clientRecipes = new AionRecipesParser().parse();
		for (ClientRecipe cr : clientRecipes)
			recipeNameIdMap.put(JAXBHandler.getValue(cr, "name").toString().toUpperCase(), (int) JAXBHandler.getValue(cr, "id"));
	}
	
	public void loadDescWorldIdMap() {
		List<Data> clientWorldList = new AionWorldDataParser().parse();
		for (Data wd : clientWorldList)
			descWorldIdMap.put(wd.getValue().toUpperCase(), wd);
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
