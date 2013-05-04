package com.parser.write.aion.levels;

import com.google.common.base.Strings;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javolution.util.FastMap;
import static ch.lambdaj.Lambda.*;

import com.geo.aion.GeoService;

import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.mission.ClientSpawn;
import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.world_data.NpcInfo;

import com.parser.commons.aion.AionDataHub;
import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.enums.spawns.ClientSpawnType;
import com.parser.commons.aion.enums.spawns.ClientEntityType;
import com.parser.commons.aion.enums.Race;
import com.parser.commons.aion.properties.AionProperties;
import com.parser.commons.aion.properties.LevelsProperties;
import com.parser.commons.aion.utils.ZUtils;
import com.parser.commons.utils.maths.MathUtil;
import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

import com.parser.write.DataProcessor;
import com.parser.write.aion.world.AionWalkersWriter;

import com.parser.output.aion.spawns2.*;

public class AionSpawnsWriter2 extends DataProcessor {
	
	List<Spawn> temp = new ArrayList<Spawn>();
	
	SpawnMap npcSpawnMap = new SpawnMap();
	SpawnMap instanceSpawnMap = new SpawnMap();
	SpawnMap gatherSpawnMap = new SpawnMap();
	SpawnMap siegesSpawnMap = new SpawnMap();
	SpawnMap riftsSpawnMap = new SpawnMap();
	SpawnMap staticSpawnMap = new SpawnMap();
	
	FastMap<String, List<ClientSpawn>> levelSpawns;
	List<Entity> levelEntities;
	
	List<SourceSphere> spheres = new ArrayList<SourceSphere>();
	Map<SourceSphere, Integer> usedSS = new HashMap<SourceSphere, Integer>();
	List<SourceSphere> toWrite = new ArrayList<SourceSphere>();
	
	static int BASE_RESPAWN_TIME = 295;
	static int BASE_GATHER_RESPAWN_TIME = 55;
	static int BASE_STATIC_RESPAWN_TIME = 5;
	static int MAX_ENTITY_DIST = 5;
	
	@Override
	public void collect() {
		levelSpawns = new FastMap<String, List<ClientSpawn>>(aion.getLevelSpawns());
		spheres = aion.getSpheres().get(map);
		// Loading usable entities
		for (Entity e : aion.getLevelEntities().get(map)) {
			if (!Strings.isNullOrEmpty(e.getPos()) && e.getEntityClass() != null && ClientEntityType.fromClient(e.getEntityClass()) != null && ClientEntityType.fromClient(e.getEntityClass()).canBeStatic())
				levelEntities.add(e);
		}
		aion.getWorldMaps();
		aion.getNpcs();
		aion.getStrings();
		
		if (AionProperties.USE_GEO) {GeoService.getInstance().initializeGeo();}
	}
	
	@Override
	public void transform() {
		for (String map : levelSpawns.keySet()) {
			int mapId = aion.getWorldId(map);
			if (mapId == 0) continue;
			initAllSpawns(mapId);
			Util.printSubSection(mapId + " : " + aion.getStringText("STR_ZONE_NAME_" + map));
			
			// Initializing client spawn data
			List<ClientSpawn> currentCSpawns = new LinkedList<ClientSpawn>();
			for (ClientSpawn cs : levelSpawns.get(map)) {
				if (ClientSpawnType.fromClient(cs.getType()) != null && ClientSpawnType.fromClient(cs.getType()).shouldBeSpawned())
					currentCSpawns.add(cs);
			}

			for (NpcInfo info : aion.getDataSpawns(map)) {
				createSpawn(info, map);
			}
			
			// Occupied LEVELS spawns
			for (ClientSpawn cSpawn : currentCSpawns) {
				if (Strings.isNullOrEmpty(cSpawn.getNpc())) continue; // Unidentified spawns will be dealt fith later ...
				createSpawn(cSpawn, map, true);
			}
			// Vacant LEVELS spawns
			for (ClientSpawn vSpawn : currentCSpawns) {
				if (!Strings.isNullOrEmpty(vSpawn.getNpc())) continue; // Those spawns have already been handled !
				createSpawn(vSpawn, map, false);
			}

			// FINALLY //
			scatterAllSpawns(map);
			addAllSpawns(mapId, map);
			log.reset();
		}
	}
	
	@Override
	public void create() {
		// Creating walkers needed for the maps
		AionWalkersWriter writer = new AionWalkersWriter();
		writer.writeFromSpawns(toWrite);
	}
	
	private void createSpawn(NpcInfo ws, String map) {
		
		Spawn s = new Spawn();
		s.setX(ws.getPos().getX());
		s.setY(ws.getPos().getY());
		s.setZ(ws.getPos().getZ());
		
		// Fixing Z with Geo && useful level data (TODO)
		// if (spot.getStaticId() == null)
			// spot.setZ(ZUtils.getBestZ(sd));
		
		List<Occupant> occupants = new ArrayList<Occupant>();
		occupants.add(createOccupant(ws, map));
		
		Spawn existing = findExistingSpawn(s);
		if (existing != null) {
			addOccupants(existing, occupants);
		}
		else {
			addOccupants(s, occupants);
		}
	}
	
	private void createSpawn(ClientSpawn cs, String map, boolean canAdd2Existing) {
		if (Strings.isNullOrEmpty(cs.getPos())) return;
		
		Spawn s = new Spawn();
		String[] xyz = cs.getPos().split(",");
		s.setX(MathUtil.toFloat3(xyz[0]));
		s.setY(MathUtil.toFloat3(xyz[1]));
		s.setZ(MathUtil.toFloat3(xyz[2]));
		
		// Fixing Z with Geo && useful level data (TODO)
		// if (spot.getStaticId() == null)
			// spot.setZ(ZUtils.getBestZ(sd));
		
		if (cs.getDir() >= 0)
			s.setH(MathUtil.degreeToHeading(cs.getDir()));
		else if (cs.getIidleRange() >= 0)
			s.setH(MathUtil.degreeToHeading(cs.getIidleRange()));
		
		List<Occupant> occupants = findOccupants(cs, map);
		if (occupants == null || occupants.isEmpty()) {
			System.out.println("[SPAWNS] No occupants could be found for cSpawn : " + cs.getName());
			return;
		}
		
		Spawn existing = findExistingSpawn(s);
		if (existing != null) {
			if (canAdd2Existing)
				addOccupants(existing, occupants);
		}
		else {
			addOccupants(s, occupants);
		}
	}
	
	// Compare currently created spawn to previously added to find similarities
	private Spawn findExistingSpawn(Spawn s) {
		for (Spawn _s : temp)
			if (MathUtil.isCloseEnough(s.getX(), s.getY(), s.getZ(), _s.getX(), _s.getY(), _s.getZ(), 2, 9)) // WARN: Check the precision
				return _s;
		return null;
	}
	
	// Find the occupants of the given cSpawn
	private List<Occupant> findOccupants(ClientSpawn cs, String map) {
		List<Occupant> results = new ArrayList<Occupant>();
		
		// Spawn is occupied
		if (!Strings.isNullOrEmpty(cs.getNpc())) {
			if (ClientSpawnType.fromClient(cs.getType()) == ClientSpawnType.SP) {
				ClientNpc npc = aion.getNpcs().get(cs.getNpc().toUpperCase());
				if (npc != null) results.add(createOccupant(cs, map));
			}
			else if (ClientSpawnType.fromClient(cs.getType()) == ClientSpawnType.HSP) {
				// Gatherable gather = aion.getGatherables().get(cs.getNpc().toUpperCase());
				// if (gather != null) results.add(createOccupant(gather.getId()));
			}
		}
		
		// Spawn is vacant
		else
			results.addAll(findOccupantsFromSpheres(cs, map));
		
		return results;
	}
	
	// Create a list of occupants for vacant spawns
	private List<Occupant> findOccupantsFromSpheres(ClientSpawn cs, String map) {
		List<Occupant> results = new LinkedList<Occupant>();
		for (SourceSphere sphere : spheres) {
			if (canBeUsed(sphere) && sphere.getVersion() == 0 && sphere.getCountry() == -1 && shouldBeAdded(sphere, results) && MathUtil.isIn3dRange(cs.getPos(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius()))
				results.add(createOccupant(sphere, map));
		}
		return results;	
	}
	
	private boolean shouldBeAdded(SourceSphere sphere, List<Occupant> current) {
		for (Occupant occ : current)
			if (occ.getId() == aion.getNpcId(sphere.getName().toUpperCase()))
				return false;
		return true;
	}
	
	private Occupant createOccupant(SourceSphere sphere, String map) {
		int npcId = aion.getNpcId(sphere.getName().toUpperCase());
		Occupant result = createOccupant(aion.getNpcId(sphere.getName().toUpperCase()));
		if (!makeItWalk(result, sphere))
			if (makeItStatic(result, npcId, map, sphere.getX(), sphere.getY(), sphere.getZ()))
				result.setRespawnTime(BASE_STATIC_RESPAWN_TIME);
				
		addComment(result, "[VACANT CS] :: " + aion.getNpcName(npcId));
		return result;
	}
	
	private Occupant createOccupant(NpcInfo info, String map) {
		int npcId = info.getNameid();
		Occupant result = createOccupant(npcId);
		// SourceSphere owner = matchSphere(npcId, map, info.getPos().getX(), info.getPos().getY(), info.getPos().getZ());
		// if (owner != null) {
			// if (!makeItWalk(result, owner))
				// if (makeItStatic(result, npcId, map, info.getPos().getX(), info.getPos().getY(), info.getPos().getZ()))
					// result.setRespawnTime(BASE_STATIC_RESPAWN_TIME);
		// }
		result.setRespawnTime(BASE_STATIC_RESPAWN_TIME);
		addComment(result, "[WORLD] :: " + aion.getNpcName(npcId));
		return result;
	}
	
	private Occupant createOccupant(ClientSpawn cs, String map) {
		int npcId = aion.getNpcId(cs.getNpc());
		Occupant result = createOccupant(npcId);
		String[] xyz = cs.getPos().split(",");
		SourceSphere owner = matchSphere(npcId, map, MathUtil.toFloat3(xyz[0]), MathUtil.toFloat3(xyz[1]), MathUtil.toFloat3(xyz[2]));
		if (owner != null)
			makeItWalk(result, owner);

		if (result.getWalkerId() == null) {
			if (!makeItWander(result, cs)) {
				if (makeItStatic(result, npcId, map, MathUtil.toFloat3(xyz[0]), MathUtil.toFloat3(xyz[1]), MathUtil.toFloat3(xyz[2])))
					result.setRespawnTime(BASE_STATIC_RESPAWN_TIME);
			}
		}
		addComment(result, "[OCCUPIED CS] :: " + aion.getNpcName(npcId));
		return result;
	}
	
	// Create an occupant from NpcId
	private Occupant createOccupant(int id) {
		Occupant result = new Occupant();
		ClientNpc npc = aion.getNpc(id);
		
		if (id > 400000 && id <= 410000) {
			result.setType("GATHER");
			result.setRespawnTime(BASE_GATHER_RESPAWN_TIME); //TODO: get GatherTemplate : if big one (several gathers), bigger respawn time
			// addComment(result, "[GATHER] :: " + aion.getGatherables().get(npcId));
		}
		else if (npc != null) {
			if (!Strings.isNullOrEmpty(npc.getAbyssNpcType()) && getMod(npc.getAbyssNpcType()) != null) {
				Matcher siegeM = Pattern.compile("\\d{4}").matcher(npc.getName());
				if (siegeM.find()) {
					result.setSiegeId(Integer.parseInt(siegeM.group()));
					
					result.setType("SIEGE");
					
					if (npc.getRaceType() != null)
						result.setRace(getRace(npc.getRaceType()));
					else if (npc.getGameLang() != null)
						result.setRace(getRace(npc.getGameLang()));
						
					result.setMod(getMod(npc.getAbyssNpcType()));

					if (result.getMod().toUpperCase() != "SIEGE")
						result.setRespawnTime(BASE_RESPAWN_TIME);
				}
				// else
					// System.out.println("[SPAWNS] Could not retreive SiegeId from Npc name : " + npc.getName());
			}
			// By default, it will be an NPC
			if (result.getType() == null) {
				result.setType("NPC");
				result.setRespawnTime(computeRespawnTime(npc));
				if (isNamed(npc)) {
					result.setMax(1); // Max one spawned
					result.setChance(30); // 70% less chance to be spawned compared to the others
				}
			}
		}	
		result.setId(id);
		
		return result;
	}
	
	private String getRace(String s) {
		String result = "NONE";
		switch (s.toUpperCase()) {
			case "DRAKAN":
			case "DRAGON":
			case "DRAGON_CASTLE_DOOR":
				result = "BALAUR"; break;
			case "LIGHT":
			case "PC_LIGHT":
			case "PC_LIGHT_CASTLE_DOOR":
			case "GCHIEF_LIGHT":
				result = "ELYOS"; break;
			case "DARK":
			case "PC_DARK":
			case "PC_DARK_CASTLE_DOOR":
			case "GCHIEF_DARK":
				result = "ASMODIANS"; break;
			default:
				System.out.println("[SPAWNS] No siege race declared for : " + s.toUpperCase());
		}
		return result;
	}
	
	private String getMod(String type) {
		String result = null;
		switch (type.toUpperCase()) {
			case "DEFENDER":
				result = "PEACE"; break;
			case "ARTIFACT":
			case "TELEPORTER":
			case "ETC":
				result = "BOTH"; break;
			case "GUARD":
			case "DOORREPAIR":
			case "DOOR":
			case "BOSS":
			// case "RAID":
			// case "BARRIER":
			case "SHIELDNPC_ON":
				result="SIEGE"; break;
			default:
				System.out.println("[SPAWNS] No siege mod declared for : " + type.toUpperCase());
		}
		return result;
	}
	
	private boolean isNamed(ClientNpc npc) {
		return npc.getName().toUpperCase().contains("NAMED");
	}
	
	private int  computeRespawnTime(ClientNpc npc) {
		int time = BASE_RESPAWN_TIME;
		// TODO
		return time;
	}
	
	// Finding most appropriate owner sphere for a Spawn not generated using spheres
	private SourceSphere matchSphere(int npcId, String map, float x, float y, float z) {
		List<SourceSphere> results = new ArrayList<SourceSphere>(); 
		
		if (spheres != null) {
			for (SourceSphere sphere : spheres) {
				if (canBeUsed(sphere) && aion.getNpcId(sphere.getName().toUpperCase()) == npcId && MathUtil.isIn3dRange(x, y, z, sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius()))
					results.add(sphere);
			}
		}
		if (!results.isEmpty()) {
			SourceSphere smallest = selectMin(results, on(SourceSphere.class).getRadius());
			incrementCount(smallest);
			return smallest;
		}
		return null;
	}
	
	private boolean canBeUsed(SourceSphere sphere) {
		for (SourceSphere ss : usedSS.keySet())
			if (ss == sphere && usedSS.get(ss) >= ss.getClusterNum())
				return false;
		return true;
	}
	
	private void incrementCount(SourceSphere sphere) {
		int count = 1;
		if (usedSS.containsKey(sphere))
			count += usedSS.get(sphere);
		usedSS.remove(sphere);
		usedSS.put(sphere, count);
	}
	
	private boolean makeItWalk(Occupant occ, SourceSphere ss) {
		if (!Strings.isNullOrEmpty(ss.getWpName()) && occ.getType().equalsIgnoreCase("SIEGE") || occ.getType().equalsIgnoreCase("NPC")) {
			occ.setWalkerId(ss.getWpName().toUpperCase());
			// occ.setState();
			// occ.setWalkerIndex();
			toWrite.add(ss);
			return true;
		}
		return false;
	}
	
	private boolean makeItWander(Occupant occ, ClientSpawn cs) {
		if (occ.getType().equalsIgnoreCase("SIEGE") || occ.getType().equalsIgnoreCase("NPC")) {
			if (cs.getDir() != -1 && cs.getIidleRange() > 1 && cs.getIidleRange() <= 20) {
				occ.setRandomWalk(cs.getIidleRange());
				return true;
			}
		}
		return false;
	}
	
	private boolean makeItStatic(Occupant occ, int npcId, String map, float x, float y, float z) {
		List<Entity> results = new ArrayList<Entity>();
		ClientNpc npc = aion.getNpc(npcId);
		if (npc == null) return false;

		// Get entity based on client mesh name (can't always match, but a lot faster)
		for (Entity ent : levelEntities) {
			if (npc != null && npc.getMesh() != null && ent.getProperties() != null && ent.getProperties().getFileLadderCGF() != null && ent.getProperties().getFileLadderCGF().toUpperCase().contains(npc.getMesh().toUpperCase()))
				if (MathUtil.isCloseEnough(ent.getPos(), x, y, z, 0, MAX_ENTITY_DIST))
					results.add(ent);
		}
		if (!results.isEmpty()) {
			occ.setStaticId(getClosestEntity(results, x, y, z).getEntityId());
			return true;
		}
		else {
			for (Entity ent : levelEntities) {
				if (MathUtil.isCloseEnough(ent.getPos(), x, y, z, 0, MAX_ENTITY_DIST))
					results.add(ent);
			}
			if (!results.isEmpty()) {
				occ.setStaticId(getClosestEntity(results, x, y, z).getEntityId());
				return true;
			}
		}
		return false;
	}
	
	private Entity getClosestEntity(List<Entity> list, float x, float y, float z) {
		Collections.sort(list, new Comparator<Entity>() {
			public int compare(Entity o1, Entity o2) {return Double.compare(MathUtil.getDistance(o1.getPos(), x, y, z), MathUtil.getDistance(o2.getPos(), x, y, z));}
		});	
		return list.get(0);
	}
	
	// Add given occupants to an existing Spawn, and add the spawn to the temporary list
	private void addOccupants(Spawn s, List<Occupant> occupants) {
		temp.remove(s);
		for (Occupant occ : occupants) {
			if (!s.getOccupant().contains(occ))
				s.getOccupant().add(occ);
		}
		temp.add(s);
	}
	
	private void initAllSpawns(int mapId) {
		// Npc
		npcSpawnMap = new SpawnMap();
		npcSpawnMap.setMapId(mapId);
		// Instances
		instanceSpawnMap = new SpawnMap();
		instanceSpawnMap.setMapId(mapId);
		// Gather
		gatherSpawnMap = new SpawnMap();
		gatherSpawnMap.setMapId(mapId);
		// Sieges
		siegesSpawnMap = new SpawnMap();
		siegesSpawnMap.setMapId(mapId);
		// Rifts
		riftsSpawnMap = new SpawnMap();
		riftsSpawnMap.setMapId(mapId);
		// Static
		staticSpawnMap = new SpawnMap();
		staticSpawnMap.setMapId(mapId);
	}
	
	private void scatterAllSpawns(String map) {
		if (aion.isInstance(map.toUpperCase()))
			instanceSpawnMap.getSpawn().addAll(temp);
		for (Spawn s : temp) {
			if (isGather(s))
				gatherSpawnMap.getSpawn().add(s);
			else if (isSiege(s))
				siegesSpawnMap.getSpawn().add(s);
			else if (isStatic(s))
				staticSpawnMap.getSpawn().add(s);
			else
				npcSpawnMap.getSpawn().add(s);
		}
	}
	
	private boolean isGather(Spawn s) {
		for (Occupant occ : s.getOccupant())
			if (!occ.getType().equalsIgnoreCase("GATHER"))
				return false;
		return true;
	}
	
	private boolean isStatic(Spawn s) {
		for (Occupant occ : s.getOccupant())
			if (occ.getStaticId() == null)
				return false;
		return true;
	}
	
	private boolean isSiege(Spawn s) {
		for (Occupant occ : s.getOccupant())
			if (!occ.getType().equalsIgnoreCase("SIEGE"))
				return false;
		return true;
	}
	
	private void addAllSpawns(int mapId, String map) {
		// orderAllSpawns();
		
		Spawns spawns = new Spawns();
		// Npcs
		if (!npcSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(npcSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(npcSpawnMap) + " Npc Spots");
			addOrder(LevelsProperties.SPAWNS + "Npcs/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Instances
		if (!instanceSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(instanceSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(instanceSpawnMap) + " Instance Spots");
			addOrder(LevelsProperties.SPAWNS + "Instances/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Gather
		if (!gatherSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(gatherSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(gatherSpawnMap) + " Gather Spots");
			addOrder(LevelsProperties.SPAWNS + "Gather/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Siege
		if (!siegesSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(siegesSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(siegesSpawnMap) + " Siege Spots");
			addOrder(LevelsProperties.SPAWNS + "Sieges/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Rifts
		if (!riftsSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(riftsSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(riftsSpawnMap) + " Rifts Spots");
			addOrder(LevelsProperties.SPAWNS + "Rifts/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
			spawns = new Spawns();
		}
		// Statics
		if (!staticSpawnMap.getSpawn().isEmpty()) {
			spawns.setSpawnMap(staticSpawnMap);
			System.out.println("[SPAWNS] Finally, added " + getSize(staticSpawnMap) + " Static Spots");
			addOrder(LevelsProperties.SPAWNS + "Statics/" + getXml(mapId, map), LevelsProperties.SPAWNS_BINDINGS, spawns);
		}
		// END
		spawns = new Spawns();
	}
	
	private int getSize(SpawnMap sm) {
		int c = 0;
		for (Spawn s : sm.getSpawn())
			c += s.getOccupant().size();
		return c;
	}
	
	/*
	private void orderAllSpawns() {
		order(npcSpawnMap);
		order(instanceSpawnMap);
		order(gatherSpawnMap);
		order(siegesSpawnMap);
		order(riftsSpawnMap);
		order(staticSpawnMap);
	}
	
	private void order(SpawnMap sm) {
		Collections.sort(sm.getSpawn(), new Comparator<Spawn>() {	
			public int compare(Spawn o1, Spawn o2) {return o1.getNpcId().compareTo(o2.getNpcId());}
		});
	}*/
	
	private String getXml(int mapId, String map) {
		return mapId + "_" + aion.getStringText("STR_ZONE_NAME_" + map);
	}
}