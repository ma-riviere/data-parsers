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

import com.parser.input.aion.world_data.NpcInfo;
import com.parser.input.aion.mission.Entity;
import com.parser.input.aion.mission.ClientSpawn;

public class SpawnData {
	
	//TODO: Use it like this everywhere
	private AionDataCenter data = new AionDataCenter().getInstance();
	//TODO: Adjust precision over the count reports !!
	double PRECISION = 2.0;
	double PRECISION_Z = 10.0;
	//TODO: Make a warn in Mathutil if x,y match but not z
	
	boolean lockSS = false;
	boolean lockInfo = false;
	

	private ClientSpawn cSpawn; // From mission0.xml
	private String mapName;
	
	private int npcId = -1;
	private SourceSphere ss = null; // From source_sphere.csv
	private NpcInfo info = null; // From client_world_*.xml
	private Entity entity = null; // From mission0.xml
	
	public SpawnData(ClientSpawn cSpawn, String mapName, int npcId) {
		this.cSpawn = cSpawn;
		this.mapName = mapName;
		this.npcId = npcId;
		this.ss = getSS();
		this.info = getNpcInfo();
		this.entity = getEntity();
	}
	
	public SpawnData(ClientSpawn cSpawn, String mapName) {
		this(cSpawn, mapName, getNpcId(), getSS(), getNpcInfo(), getEntity());
	}
	
	public ClientSpawn getCSpawn() {return this.cSpawn;}
	public setCSpawn(ClientSpawn value) {this.cSpawn = value;}
	
	public String getMapName() {return this.mapName;}
	public setMapName(String value) {this.mapName = value;}
	
	public int getNpcId() {
		if (npcId == -1)
			return findNpcId();
		else
			return npcId;
	}
	
	public SourceSphere getSS() {
		if (this.ss == null)
			return findSphere();
		else
			return this.ss;
	}
	
	public NpcInfo getNpcInfo() {
		if (this.info == null)
			return findInfo();
		else
			return this.info;
	}
	
	public Entity getEntity() {
		if (this.entity == null)
			return findEntity();
		else
			return this.entity;
	}	
	
	/***************************/
	
	private int findNpcId() {
		int id = -1;
		if (!Strings.isNullOrEmpty(getCSpawn().getNpc()))
			id = data.getNpcIdByName(getCSpawn().getNpc());
		
		if (id <= 0 && getNpcInfo() != null)
			id = getNpcInfo().getNameid();
		
		if (id <= 0 && getSS() != null)
			id = data.getNpcIdByName(getSS().getName());
		
		return id;
	}
	
	private SourceSphere findSphere() {		
		List<SourceSphere> list1 = getSSFromSpawn(); 
		
		if (list1.isEmpty() && !lockInfo) {
			lockSS = true;
			List<SourceSphere> list2 = getSSFromInfo();
			if (list2.isEmpty()) {
				lockSS = false;
				return null;
			}
			else {
				lockSS = false;
				System.out.println("[SPAWNS] " + list2.size() + " SourceSphere matched from NPC_INFO !");
				return getClosestSS(list2);
			}
		}
		else {
			System.out.println("[SPAWNS] " + list1.size() + " SourceSphere matched from SPAWN !");
			return getClosestSS(list1);
		}
	}
	
	private List<SourceSphere> getSSFromSpawn() {
		List<SourceSphere> results = new ArrayList<SourceSphere>();
		for (SourceSphere sphere : data.getSourceSphereList()) { //TODO
			if (Strings.isNullOrEmpty(getCSpawn().getPos()))
				if (MathUtil.isCloseEnough(getCSpawn().getPos(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius() + PRECISION, sphere.getRadius() + PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private List<SourceSphere> getSSFromInfo() {
		List<SourceSphere> results = new ArrayList<SourceSphere>();
		for (SourceSphere sphere : data.getSourceSphereList()) {
			if (getNpcInfo() != null)
				if (MathUtil.isCloseEnough(getNpcInfo().getX(), getNpcInfo().getY(), getNpcInfo().getZ(), sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius() + PRECISION, sphere.getRadius() + PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private SourceSphere getClosestSS(List<SourceSphere> list) {
		Collections.sort(list, new Comparator<SourceSphere>() {	
			public int compare(SourceSphere o1, SourceSphere o2) {return getDistance(o1, getCSpawn().getPos()).compareTo(getDistance(o2, getCSpawn().getPos()));}
		});
		return list.get(0);
	}
	
	private NpcInfo findInfo() {
		
		List<NpcInfo> list1 = getInfoFromSpawn(); 
		
		if (list1.isEmpty() && !lockSS) {
			lockInfo = true;
			List<NpcInfo> list2 = getInfoFromSS();
			if (list2.isEmpty()) {
				lockInfo = false;
				return null;
			}
			else {
				lockInfo = false;
				System.out.println("[SPAWNS] " + list2.size() + " NpcInfo matched from SOURCE_SPHERE !");
				return getClosestInfo(list2);
			}
		}
		else {
			System.out.println("[SPAWNS] " + list1.size() + " NpcInfo matched from SPAWN !");
			return getClosestInfo(list1);
		}
	}
	
	private List<NpcInfo> getInfoFromSpawn() {
		List<NpcInfo> results = new ArrayList<NpcInfo>();
		for (NpcInfo npc : data.getNpcInfoByMap(getMapName())) {
			if (Strings.isNullOrEmpty(getCSpawn().getPos()))
				if (MathUtil.isCloseEnough(getCSpawn().getPos(), npc.getPos().getX(), npc.getPos().getY(), npc.getPos().getZ(), PRECISION, PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private List<NpcInfo> getInfoFromSS() {
		List<NpcInfo> results = new ArrayList<NpcInfo>();
		for (NpcInfo npc : data.getNpcInfoByMap(getMapName())) {
			if (getSS() != null)
				if (MathUtil.isCloseEnough(getSS().getX(), getSS().getY(), getSS().getZ(), npc.getPos().getX(), npc.getPos().getY(), npc.getPos().getZ(), getSS().getRadius() + PRECISION, getSS().getRadius() + PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private NpcInfo getClosestInfo(List<NpcInfo> list) {
		Collections.sort(list, new Comparator<NpcInfo>() {	
			public int compare(NpcInfo o1, NpcInfo o2) {return getDistance(o1, getCSpawn().getPos()).compareTo(getDistance(o2, getCSpawn().getPos()));}
		});
		return list.get(0);
	}
	
	private Entity findEntity() {
		List<Entity> list1 = getEntityFromSpawn();
		
		if (list1.isEmpty()) {
			List<Entity> list2 = getEntityFromSphere();
			
			if (list2.isEmpty()) {
				List<Entity> list3 = getEntityFromInfo();
				
				if (list3.isEmpty())
					return null;
				else
					return getClosestEntity(list3);
			}
			else {
				return getClosestEntity(list2);
			}
		}
		else {
			return getClosestEntity(list1);
		}
	}
	
	private List<Entity> getEntityFromSpawn() {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity ent : data.getClientEntitiesByMap(mapName)) { //TODO
		
		}
		return results;
	}
	
	private List<Entity> getEntityFromSphere() {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity ent : data.getClientEntitiesByMap(mapName)) {
			if (getSS() != null && !Strings.isNullOrEmpty(ent.getPos()))
				if (MathUtil.isCloseEnough(ent.getPos(), getSS().getX(), getSS().getY(), getSS().getZ(), getSS().getRadius() + PRECISION, getSS().getRadius() + PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private List<Entity> getEntityFromInfo() {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity ent : data.getClientEntitiesByMap(mapName)) {
			if (getInfo() != null && !Strings.isNullOrEmpty(ent.getPos()))
				if (MathUtil.isCloseEnough(ent.getPos(), getInfo().getPos().getX(), getInfo().getPos().getY(), getInfo().getPos().getZ(), getSS().getRadius() + PRECISION, getSS().getRadius() + PRECISION_Z))
					results.add(npc);
		}
		return results;
	}
	
	private getClosestEntity(List<Entity> list) {
		Collections.sort(list, new Comparator<Entity>() {	
			public int compare(Entity o1, Entity o2) {return MathUtil.getDistance(o1.getPos(), getCSpawn().getPos()).compareTo(MathUtil.getDistance(o2.getPos(), getCSpawn().getPos());}
		});
		return list.get(0);
	}
	
	public double getDistance(Object obj, String pos) {
		if (obj != null && !Strings.isNullOrEmpty(pos)) {
			String[] xyz = pos.split(",");
			int x = 0;
			int y = 0;
			int z = 0;
			
			if (obj instanceof SourceSphere) {
				SourceSphere s = (SourceSphere) obj;
				x = s.getX();
				y = s.getY();
				z = s.getZ();
			}
			else if (obj instanceof NpcInfo) {
				NpcInfo ni = (NpcInfo) obj;
				x = ni.getPos().getX();
				y = ni.getPos().getY();
				z = ni.getPos().getZ();
			}
			
			float dx = Float.parseFloat(xyz1[0]) - Float.parseFloat(xyz2[0]);
			float dy = Float.parseFloat(xyz1[1]) - Float.parseFloat(xyz2[1]);
			float dz = Float.parseFloat(xyz1[2]) - Float.parseFloat(xyz2[2]);
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Double.MAX_VALUE;
	}
}
