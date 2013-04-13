package com.parser.common.aion.models;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import com.parser.common.aion.bindings.SourceSphere;
import com.parser.common.math.MathUtil;
import com.parser.common.utils.Util;
import com.parser.common.aion.AionDataCenter;

import com.parser.input.aion.npcs.ClientNpc;
import com.parser.input.aion.world_data.NpcInfo;
import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.mission.ClientSpawn;

public class SpawnData {
	
	private ClientSpawn cSpawn; // From mission0.xml
	private NpcInfo info = null; // From client_world_*.xml
	private int mapId = -1;
	private int npcId = -1;
	private SourceSphere ss = null; // From source_sphere.csv
	private Entity entity = null; // From mission0.xml
	
	private AionDataCenter data = new AionDataCenter().getInstance();
	private ClientNpc getNpc(int id) {return (id != 0) ? data.getClientNpcs().get(id) : null;}
	
	//TODO: Adjust precision over the count reports !!
	double PRECISION = 0.7;
	//TODO: Make a warn in Mathutil if x,y match but not z
	int BASE_RESPAWN_TIME = 300;
	
	public SpawnData(ClientSpawn cSpawn, int mapId) {
		this.cSpawn = cSpawn;
		this.mapId = mapId;
		if (info == null) {info = findInfo();}
		this.npcId = findNpcId();
	}
	
	public SpawnData(NpcInfo info, int mapId) {
		this.info = info;
		this.mapId = mapId;
		this.npcId = info.getNameid();
	}
	
	public ClientSpawn getCSpawn() {return cSpawn;}
	public int getMapId() {return this.mapId;}
	public int getNpcId() {return this.npcId;}
	
	public SourceSphere getSS() {return ss;}
	public NpcInfo getNpcInfo() {return info;}
	
	private Entity getEntity() {
		if (entity == null)
			entity = findEntity();
		return entity;
	}
	
	/***************************/
	
	private int findNpcId() {
		int id = -1;
		
		// if (getGatherableTemplate() != null) {id = getGatherableTemplate().getId();} // else ...
		if (cSpawn != null && !Strings.isNullOrEmpty(cSpawn.getNpc())) {id = data.getNpcIdByName(cSpawn.getNpc());}
		
		if (id <= 0 && getNpcInfo() != null)
			id = info.getNameid();
		
		if (id <= 0) {
			if (ss == null) {ss = findSS();}
			if (ss != null) {id = data.getNpcIdByName(ss.getName());}
		}
		
		return id;
	}
	
	private NpcInfo findInfo() {
		if (npcId > 0) {
			List<NpcInfo> list = new ArrayList<NpcInfo>(); 
			for (NpcInfo info : data.getNpcInfoByMap(mapId)) {
				if (info.getNameid() == npcId)
					list.add(info);
			}
			if (!list.isEmpty())
				return getClosestInfo(list);
		}
		else {
			List<NpcInfo> list1 = getInfoFromSD(); 
			if (!list1.isEmpty())
				return getClosestInfo(list1);
		}
		return null;
	}
	
	private List<NpcInfo> getInfoFromSD() {
		List<NpcInfo> results = new ArrayList<NpcInfo>();
		for (NpcInfo npc : data.getNpcInfoByMap(mapId)) {
			if (!Strings.isNullOrEmpty(cSpawn.getPos()))
				if (MathUtil.isIn3dRange(getX(), getY(), getZ(), npc.getPos().getX(), npc.getPos().getY(), npc.getPos().getZ(), 1.0))
					results.add(npc);
		}
		return results;
	}
	
	private NpcInfo getClosestInfo(List<NpcInfo> list) {
		Collections.sort(list, new Comparator<NpcInfo>() {	
			public int compare(NpcInfo o1, NpcInfo o2) {return Double.compare(MathUtil.getDistance(o1.getPos().getX(), o1.getPos().getY(), o1.getPos().getZ(), getX(), getY(), getZ()), MathUtil.getDistance(o2.getPos().getX(), o2.getPos().getY(), o2.getPos().getZ(), getX(), getY(), getZ()));}
		});
		return list.get(0);
	}
	
	private SourceSphere findSS() {
		if (npcId > 0) {
			List<SourceSphere> list = new ArrayList<SourceSphere>(); 
			if (data.getClientSpheres() != null && data.getClientSpheres().get(mapId) != null) {
				for (SourceSphere sphere : data.getClientSpheres().get(mapId)) {
					if (data.canBeUsed(sphere) && data.getNpcIdByName(sphere.getName()) == npcId)
						list.add(sphere);
				}
			}
			if (!list.isEmpty())
				return getClosestSS(list);
		}
		else {
			List<SourceSphere> list1 = getSSFromSD(); 
			if (!list1.isEmpty())
				return getClosestSS(list1);
		}
		return null;
	}
	
	private List<SourceSphere> getSSFromSD() {
		List<SourceSphere> results = new ArrayList<SourceSphere>();
		if (data.getClientSpheres() != null && data.getClientSpheres().get(mapId) != null) {
			for (SourceSphere sphere : data.getClientSpheres().get(mapId)) {
				if (data.canBeUsed(sphere) && MathUtil.isIn3dRange(getX(), getY(), getZ(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius() + 1.5))
					results.add(sphere);
			}
		}
		return results;
	}
	
	private SourceSphere getClosestSS(List<SourceSphere> list) {
		Collections.sort(list, new Comparator<SourceSphere>() {	
			public int compare(SourceSphere o1, SourceSphere o2) {return Double.compare(MathUtil.getDistance(o1.getX(), o1.getY(), o1.getZ(), getX(), getY(), getZ()), MathUtil.getDistance(o2.getX(), o2.getY(), o2.getZ(), getX(), getY(), getZ()));}
		});
		// Limitating sphere use
		if (data.canBeUsed(list.get(0))) {
			data.reduceSphereCount(list.get(0));
			return list.get(0);
		}
		else
			return null;
	}
	
	private Entity findEntity() {
		List<Entity> list1 = getEntityFromSD();
		if (list1 == null || list1.isEmpty()) 
			return null;
		else
			return getClosestEntity(list1);
	}
	
	private List<Entity> getEntityFromSD() {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity ent : data.getClientEntities().get(mapId)) {
			if (!Strings.isNullOrEmpty(ent.getPos()))
				if (MathUtil.isIn3dRange(ent.getPos(), getX(), getY(), getZ(), 1.5))
					results.add(ent);
		}
		return results;
	}
	
	private Entity getClosestEntity(List<Entity> list) {
		Collections.sort(list, new Comparator<Entity>() {
			public int compare(Entity o1, Entity o2) {return Double.compare(MathUtil.getDistance(o1.getPos(), getX(), getY(), getZ()), MathUtil.getDistance(o2.getPos(), getX(), getY(), getZ()));}
		});
		return list.get(0);
	}
	
	public int getRespawnTime() {
		int respawnTime = BASE_RESPAWN_TIME;
		//TODO: fonction de hpgauge, isMonster ...
		return respawnTime;
	}
	
	public float getX() {
		if (getNpcInfo() != null)
			return info.getPos().getX();
		else if (cSpawn != null && cSpawn.getPos() != null) {
			String[] xyz = cSpawn.getPos().split(",");
			return MathUtil.toFloat3(xyz[0]);
		}
		else
			return 0;
	}
	
	public float getY() {
		if (getNpcInfo() != null)
			return info.getPos().getY();
		else if (cSpawn != null && cSpawn.getPos() != null) {
			String[] xyz = cSpawn.getPos().split(",");
			return MathUtil.toFloat3(xyz[1]);
		}
		else
			return 0;
	}
	
	public float getZ() {
		if (getNpcInfo() != null)
			return info.getPos().getZ();
		else if (cSpawn != null && cSpawn.getPos() != null) {
			String[] xyz = cSpawn.getPos().split(",");
			return MathUtil.toFloat3(xyz[2]);
		}
		else
			return 0;
	}
	
	public boolean canMove() {
		boolean canMove = true;
		ClientNpc npc = getNpc(npcId);
		if (npc != null && (int) (npc.getMoveSpeedNormalWalk() * 1000) <= 0)
			canMove = false;
		if (getNpcInfo() != null && info.getMovetype() != null && info.getMovetype().equalsIgnoreCase("false"))
			canMove = false;
		//TODO: Add checks on NpcType (Portal, ...)
		return canMove;
	}
	
	public boolean canBeStatic()  {
		boolean canBeStatic = false;
		if (npcId >= 700000)
			canBeStatic = true;
		//TODO: Other conditions
		return canBeStatic;
	}
	
	public int getStaticId() {
		if (getEntity() != null && entity.getEntityClass().equalsIgnoreCase("PlaceableObject"))
			return entity.getEntityId();
		else
			return -1;
	}
	
	public String getWalkerId() {
		if (ss == null)
			ss = findSS();
		if (ss != null && !Strings.isNullOrEmpty(ss.getWpName()))
			return ss.getWpName();
		else
			return null;
	}
}