package com.parser.write.aion.levels;

import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.geo.aion.GeoService;

import com.parser.commons.aion.AionDataHub;
import com.parser.commons.aion.bindings.WayPoint;
import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.utils.maths.MathUtil;
import com.parser.commons.utils.maths.Point3D;

import com.parser.read.aion.world.AionWayPointsParser;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.walkers.*;

public class AionWalkersWriter extends AbstractWriter {

	NpcWalker walkers = new NpcWalker();
	Collection<WalkerTemplate> walkerList = walkers.getWalkerTemplate();
	
	List<WayPoint> wayPoints = null; // From waypoint.csv
	Map<Integer, List<SourceSphere>> sourceSpheres = null; // From source_sphere.csv
	
	WayPoint currentWP = null;
	
	boolean USE_GEO = true; // Move to properties
	int mapId = 0;
	
	// When called by AionSpawnsWriter
	boolean fromSpawns = false;
	List<SourceSphere> toWrite = null;
	
	AionDataHub data = new AionDataHub().getInstance();
	
	public void writeFromSpawns(List<SourceSphere> toWrite) {
		this.toWrite = toWrite;
		fromSpawns = true;
		
		parse();
		transform();
		marshall();
	}
	
	@Override
	public void parse() {
		sourceSpheres = data.getClientSpheres();
		wayPoints = new AionWayPointsParser().parse(); //TODO: Move to ADC
	}

	@Override
	public void transform() {
		for (int mapId : sourceSpheres.keySet()) {
			for (SourceSphere ss : sourceSpheres.get(mapId)) {
			
				if (toWrite != null) {
					for (SourceSphere ss2 : toWrite) {
						if (ss == ss2)
							computeWalker(ss);
					}
				}
				else {
					computeWalker(ss);
				}
			}
		}
	}
	
	private void computeWalker(SourceSphere ss) {
	
		if (!exists(ss.getWpName())) {
			WalkerTemplate wt = new WalkerTemplate();
			
			for (WayPoint wp : wayPoints)
					if (ss.getWpName().equalsIgnoreCase(wp.getName()))
						currentWP = wp;
				
			if (currentWP != null) {
			
				mapId = getWorldId(ss.getMap());

				// START //
				wt.setRouteId(currentWP.getName().toUpperCase());
				
				int pool = 0;
				for (int mapId : sourceSpheres.keySet())
					for (SourceSphere ss2 : sourceSpheres.get(mapId))
						if (ss2.getWpName().equalsIgnoreCase(currentWP.getName()))
							pool++;
				wt.setPool(pool != 0 ? pool : 1);
				
				organize(wt);
				
				if (currentWP.getStepsCount() != 0)
					setRouteSteps(wt);
				
				//TODO: More precision
				if (!wt.getRoutestep().isEmpty() && !MathUtil.isIn3dRange(wt.getRoutestep().get(0).getX(), wt.getRoutestep().get(0).getY(), wt.getRoutestep().get(0).getZ(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getX(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getY(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getZ(), 15))
					wt.setReversed("true");
				
				// END //
				currentWP = null;
				mapId = 0;
				
				if (wt.getRouteId() != null && !wt.getRoutestep().isEmpty())
					walkerList.add(wt);
			}
		}
	}

	@Override
	public void marshall() {
		if (fromSpawns)
			addOrder(AionWritingConfig.WALKERS_BINDINGS, AionWritingConfig.SPAWNS_WALKERS, walkers);
		else
			addOrder(AionWritingConfig.WALKERS_BINDINGS, AionWritingConfig.WALKERS, walkers);
		
		FileMarshaller.marshallFile(orders);
		System.out.println("\n[WALKERS] Walkers count: " + walkerList.size());
	}
	
	private int getWorldId(String s) {return data.getWorldId(s);}
	
	private boolean exists(String wpName) {
		for (WalkerTemplate walker : walkerList)
			if (walker.getRouteId().equalsIgnoreCase(wpName))
				return true;
		
		return false;
	}
	
	private void setRouteSteps(WalkerTemplate wt) {
		if (!currentWP.getSteps().isEmpty()) {
			int i = 0;
			for (Point3D p : currentWP.getSteps()) {
				Routestep newStep = new Routestep();
				newStep.setStep(++i);
				newStep.setX(p.getX());
				newStep.setY(p.getY());
				
				if (USE_GEO)
					newStep.setZ(GeoService.getInstance().getZ(mapId, newStep.getX(), newStep.getY()));
				else
					newStep.setZ(p.getZ());
				
				wt.getRoutestep().add(newStep);
			}
		}
	}

	private void organize(WalkerTemplate wt) {
	
		Map<String, Integer> mobs = new HashMap<String, Integer>();
		
		//TODO: same count as Pool ...
		for (int mapId : sourceSpheres.keySet()) {
			for (SourceSphere ss2 : sourceSpheres.get(mapId)) {
				if (ss2.getWpName().equalsIgnoreCase(currentWP.getName())) {
					if (mobs.keySet().contains(ss2.getName()))
						mobs.put(ss2.getName(), mobs.get(ss2.getName()) + 1);
					else
						mobs.put(ss2.getName(), 1);
				}
			}
		}
	
		String formation = null;
		
		for (String name : mobs.keySet()) {
			if (mobs.get(name) > 1 && mobs.get(name) <= 4)
				formation = "SQUARE";
			else if (mobs.get(name) > 4)
				formation = "CIRCLE";
		}
		
		if (formation != null)
			wt.setFormation(formation);
	
		if (wt.getFormation() != null) {
			
			String row = null;
			String[] rows = new String[mobs.keySet().size()];
			
			if (mobs.keySet().size() > 1) {
				int i = 0;
				for (String s : mobs.keySet())
					rows[i++] = mobs.get(s).toString();
				
				if (rows.length != 0)			
					row = Joiner.on(",").join(rows);
			}

			if (row != null)
				wt.setRows(row);
		}
	}
}
