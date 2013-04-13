package com.parser.common.aion.bindings;

public class SourceSphere {

	public String name; // Concerned NPC name
	public String type;  // Concerned NPC type
	public String map; // Map Name
	public int layer; // For multi-layer maps, like Reshanta
	public float x;
	public float y;
	public float z;
	public double r;
	public String wpName; // WayPoint name (for walkers)
	public int conditionSpawn; // Related to sieges ? Protectors ? Seasoned ?
	public int version; // Game version concerned by this spawn
	public int country; // Country Code
	public int clusterNum = 0; // Number of NPC supported by this SS
	
	public SourceSphere() {}
	
	public SourceSphere(String name, String type, String map, int layer, float x, float y, float z, double r, String wpName, int conditionSpawn, int version, int country, int clusterNum) {
		this.name = name;
		this.type = type;
		this.map = map;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.wpName = wpName;
		this.conditionSpawn = conditionSpawn;
		this.version = version;
		this.country = country;
		this.clusterNum = clusterNum;
	}
	
	public String getName() {return name;}
	public void setName(String value) {this.name = value;}
	
	public String getType() {return type;}
	public void setType(String value) {this.type = value;}
	
	public String getMap() {return map;}
	public void setMap(String value) {this.map = value;}
	
	public int getLayer() {return layer;}
	public void setLayer(int value) {this.layer = value;}
	
	public float getX() {return x;}
	public void setX(float value) {this.x = value;}
	
	public float getY() {return y;}
	public void setY(float value) {this.y = value;}
	
	public float getZ() {return z;}
	public void setZ(float value) {this.z = value;}
	
	public double getRadius() {return r;}
	public void setRadius(double value) {this.r = value;}
	
	public String getWpName() {return wpName;}
	public void setWpName(String value) {this.wpName = value;}
	
	public int getConditionSpawn() {return conditionSpawn;}
	public void setConditionSpawn(int value) {this.conditionSpawn = value;}
	
	public int getVersion() {return version;}
	public void setVersion(int value) {this.version = value;}
	
	public int getCountry() {return country;}
	public void setCountry(int value) {this.country = value;}
	
	public int getClusterNum() {return clusterNum;}
	public void setClusterNum(int value) {this.clusterNum = value;}
	

}
