package com.parser.commons.aion.models;

import com.google.common.base.Strings;
import java.util.*;
import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.enums.spawns.ClientEntityType;
import com.parser.commons.utils.maths.MathUtil;
import com.parser.commons.utils.Util;
import com.parser.commons.aion.AionDataHub;

import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.world_data.NpcInfo;
import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.mission.ClientSpawn;

public class SpawnData {
	
	private AionDataHub data = new AionDataHub().getInstance();
	
	private ClientSpawn cSpawn;
	private NpcInfo info = null;
	private String map = null;
	
	private ClientNpc npc = null;
	private int npcId = -1;
	private int staticId = -1;
	private int rndWlk = -1;
	private String walkerId = null;
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private int h = 0;
	private static int BASE_RESPAWN_TIME = 300;
	private static int DISTANCE_LIMIT = 5;
	
	private SourceSphere ss = null;
	private Entity entity = null;
	
	public static Map<SourceSphere, Entity> ssEntities = new HashMap<SourceSphere, Entity>();
	public static List<SourceSphere> usedWP = new ArrayList<SourceSphere>();
	
	// Constructor for indentified WORLD spawns (nothing should be calculated here except if Walker or Static object)
	public SpawnData(int npcId, NpcInfo info, String map) {
		this.info = info;
		this.map = map;
		this.npcId = npcId;
		this.npc = data.getNpc(npcId);
		
		this.x = info.getPos().getX();
		this.y = info.getPos().getY();
		this.z = info.getPos().getZ();
		
		//TODO: Can walk ?
		
		if (canBeStatic()) {
			if (getEntity() != null)
				staticId = entity.getEntityId();
		}
	}
	
	// Constructor for indentified LEVELS spawns (nothing should be calculated here except if Walker or Static object)
	public SpawnData(int npcId, ClientSpawn cSpawn, String map) {
		this.cSpawn = cSpawn;
		this.map = map;
		this.npcId = npcId;
		this.npc = data.getNpc(npcId);
		
		if (!Strings.isNullOrEmpty(cSpawn.getPos())) {
			String[] xyz = cSpawn.getPos().split(",");
			this.x = MathUtil.toFloat3(xyz[0]);
			this.y = MathUtil.toFloat3(xyz[1]);
			this.z = MathUtil.toFloat3(xyz[2]);
		}
		if (canMove()) {
			if (cSpawn.getIidleRange() >= 0) {
				this.ss = findSS();
				if (ss != null && !Strings.isNullOrEmpty(ss.getWpName()))
					walkerId = ss.getWpName().toUpperCase();
				else if (cSpawn.getDir() >= 0 && cSpawn.getIidleRange() > 1)
					rndWlk = cSpawn.getIidleRange();
			}
		}
		else if (canBeStatic()) {
			if (getEntity() != null)
				staticId = entity.getEntityId();
		}
		setH(cSpawn);
	}
	
	// Constructor for unidentified LEVELS spawns (using spheres)
	public SpawnData(ClientSpawn cSpawn, String map) {
		this.cSpawn = cSpawn;
		this.map = map;
		
		if (!Strings.isNullOrEmpty(cSpawn.getPos())) {
			String[] xyz = cSpawn.getPos().split(",");
			this.x = MathUtil.toFloat3(xyz[0]);
			this.y = MathUtil.toFloat3(xyz[1]);
			this.z = MathUtil.toFloat3(xyz[2]);

			this.ss = findSS();
			if (ss != null) {
				this.npc = data.getNpcs().get(ss.getName().toUpperCase());
				if (npc != null)
					this.npcId = npc.getId();
			}
			
			if (npcId >= 200000) {
				if (canMove()) {
					if (cSpawn.getIidleRange() >= 0) {
						if (!Strings.isNullOrEmpty(ss.getWpName()))
							walkerId = ss.getWpName().toUpperCase();
					}
					//TODO: Can be rnd walkers ?
				}
				else if (canBeStatic()) {
					if (getEntity() != null)
						staticId = entity.getEntityId();
				}
				setH(cSpawn);
			}
		}
	}
	
	public int getNpcId() {return npcId;}
	public int getStaticId() {return staticId;}
	public int getRandomWalk() {return rndWlk;}
	public String getWalkerId() {return walkerId;}
	//TODO: Walker state && walker id
	public float getX() {return x;}
	public float getY() {return y;}
	public float getZ() {return z;}
	public int getH() {return h;}
	public int getRespawnTime() {
		int respawnTime = BASE_RESPAWN_TIME;
		//TODO: fonction de hpgauge, isMonster ...
		return respawnTime;
	}
	
	public String getMapId() {return map;}
	public NpcInfo getNpcInfo() {return info;}
	public ClientSpawn getCSpawn() {return cSpawn;}
	public SourceSphere getSS() {return ss;}
		
	private Entity getEntity() {
		if (entity == null)
			entity = findEntity();
		return entity;
	}
	
	/***************************/
	
	private void setH(ClientSpawn cSpawn) {
		if (getRandomWalk() == -1 && getWalkerId() == null && getStaticId() == -1) {
			if (cSpawn.getDir() != null && cSpawn.getDir() >= 0)
				this.h = MathUtil.degreeToHeading(cSpawn.getDir());
			else
				this.h = MathUtil.degreeToHeading(cSpawn.getIidleRange());
		}
	}
	
	private SourceSphere findSS() {
	
		if (ss != null) return ss;
		List<SourceSphere> list = new ArrayList<SourceSphere>(); 
		
		if (data.getSpheres() != null && data.getSpheres().get(map) != null && !data.getSpheres().get(map).isEmpty()) {
			for (SourceSphere sphere : data.getSpheres().get(map)) {
				// Searching SS with already known npcId
				if (npcId >= 200000) {
					if (!usedWP.contains(sphere) && data.getNpcs().get(sphere.getName().toUpperCase()) != null && data.getNpcs().get(sphere.getName().toUpperCase()).getId() == npcId && MathUtil.isIn3dRange(getX(), getY(), getZ(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius()))
						list.add(sphere);
				}
				// Trying to find npcId based on SS (matched by position)
				else if (npcId == -1) {
					 // TODO: Ignore walker's spheres ?
					 // TODO: Remove already spawned
					if (!usedWP.contains(sphere) && MathUtil.isIn3dRange(getX(), getY(), getZ(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius()))
						list.add(sphere);
				}
			}
		}
		if (!list.isEmpty()) {
			SourceSphere smallest = selectMin(list, on(SourceSphere.class).getRadius());
			usedWP.add(smallest);
			return smallest; //TODO: Rework spawns xml : not only one npc per spot ...
		}
		return null;
	}
	
	private Entity findEntity() {
		List<Entity> levelEntities = new ArrayList<Entity>();
		List<Entity> list = new ArrayList<Entity>();
		
		// Loading usable entities
		for (Entity e : data.getLevelEntities().get(map)) {
			if (!Strings.isNullOrEmpty(e.getPos()) && e.getEntityClass() != null && ClientEntityType.fromClient(e.getEntityClass()) != null && ClientEntityType.fromClient(e.getEntityClass()).canBeStatic())
				levelEntities.add(e);
		}
		
		// If the SourceSphere this spawn belongs to already has a designated staticID, use it
		// TODO: Remove with the new spawn system
		if (ssEntities.containsKey(ss))
			return ssEntities.get(ss);
		
		// Get entity based on client mesh name (can't always match, but a lot faster)
		for (Entity ent : levelEntities) {
			if (npc != null && npc.getMesh() != null && ent.getProperties() != null && ent.getProperties().getFileLadderCGF() != null && ent.getProperties().getFileLadderCGF().toUpperCase().contains(npc.getMesh().toUpperCase()))
				list.add(ent);
		}
		if (!list.isEmpty())
			return getClosestEntity(list);
		else
			return getClosestEntity(levelEntities);
	}
	
	private Entity getClosestEntity(List<Entity> list) {
		Collections.sort(list, new Comparator<Entity>() {
			public int compare(Entity o1, Entity o2) {return Double.compare(MathUtil.getDistance(o1.getPos(), getX(), getY(), getZ()), MathUtil.getDistance(o2.getPos(), getX(), getY(), getZ()));}
		});
		
		Entity result = list.get(0);
		if (MathUtil.isCloseEnough(result.getPos(), getX(), getY(), getZ(), 0, DISTANCE_LIMIT))
			return result;
		else
			return null;
	}
	
	//TODO: More precision to avoid rnd_walkers with big iidle range !!!
	public boolean canMove() {
		boolean canMove = true;
		if (npc != null && (int) (npc.getMoveSpeedNormalWalk() * 1000) <= 0)
			canMove = false;
		else if (info != null && info.getMovetype() != null && info.getMovetype().equalsIgnoreCase("false"))
			canMove = false;
		//TODO: Add checks on NpcType (Portal, ...)
		return canMove;
	}
	
	//TODO: More conditions for more precision and lower parsing time !!!
	public boolean canBeStatic()  {
		if (npc.getCursorType().equalsIgnoreCase("action"))
			return true;
		
		return false;
	}
	
	public static ClientSpawn findMatchingCS(NpcInfo info, List<ClientSpawn> cSpawns) {
		List<ClientSpawn> list = new ArrayList<ClientSpawn>(); 
		for (ClientSpawn cs : cSpawns) {
			if (MathUtil.isCloseEnough(cs.getPos(), info.getPos().getX(), info.getPos().getY(), info.getPos().getZ(), 2, 8))
				list.add(cs);
		}
		if (!list.isEmpty())
			return getClosestCS(list, info);
		else
			return null;
	}
	
	private static ClientSpawn getClosestCS(List<ClientSpawn> list, NpcInfo info) {
		Collections.sort(list, new Comparator<ClientSpawn>() {
			public int compare(ClientSpawn o1, ClientSpawn o2) {return Double.compare(MathUtil.getDistance(o1.getPos(), info.getPos().getX(), info.getPos().getY(), info.getPos().getZ()), MathUtil.getDistance(o2.getPos(), info.getPos().getX(), info.getPos().getY(), info.getPos().getZ()));}
		});
		return list.get(0);
	}
}