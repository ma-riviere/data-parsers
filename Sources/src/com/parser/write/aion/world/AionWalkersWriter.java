package com.parser.write.aion.world;

import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static ch.lambdaj.Lambda.*;

import com.geo.aion.GeoService;

import com.parser.commons.aion.bindings.WayPoint;
import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.properties.AionProperties;
import com.parser.commons.aion.properties.WorldProperties;
import com.parser.commons.utils.maths.MathUtil;
import com.parser.commons.utils.maths.Point3D;

import com.parser.write.DataProcessor;

import com.parser.output.aion.walkers.*;

public class AionWalkersWriter extends DataProcessor {

	NpcWalker walkers = new NpcWalker();
	Collection<WalkerTemplate> walkerList = walkers.getWalkerTemplate();
	// Map<Integer, List<SourceSphere>> sourceSpheres;
	List<SourceSphere> ssList;
	
	// When called by AionSpawnsWriter
	boolean fromSpawns = false;
	List<SourceSphere> toWrite = null;
	
	public void writeFromSpawns(List<SourceSphere> toWrite) {
		this.toWrite = toWrite;
		fromSpawns = true;
		
		collect(); transform(); create();
	}
	
	@Override
	public void collect() {
		ssList = new ArrayList<SourceSphere>(flatten(aion.getSpheres().values()));
		aion.getWayPoints();
	}

	@Override
	public void transform() {
		for (SourceSphere ss : ssList) {
			if (toWrite != null) {
				for (SourceSphere ss2 : toWrite)
					if (ss == ss2)
						computeWalker(ss);
			}
			else
				computeWalker(ss);
		}
	}
	
	private void computeWalker(SourceSphere ss) {
	
		// Check if walk route hasn't been created yet, since multiple SS can have the same route
		if (!exists(ss.getWpName())) {
			WalkerTemplate wt = new WalkerTemplate();
			
			WayPoint currentWP = aion.getWayPoints().get(ss.getWpName().toUpperCase());
			if (currentWP == null) return;

			wt.setRouteId(currentWP.getName().toUpperCase());
			
			//TODO: Test
			int pool = sum(select(ssList, having(on(SourceSphere.class).getWpName().equalsIgnoreCase(currentWP.getName())))).intValue();
			wt.setPool(pool != 0 ? pool : 1);
			
			organize(wt, currentWP);
			
			if (currentWP.getStepsCount() != 0) {
				int mapId = aion.getWorldId(ss.getMap());
				setRouteSteps(wt, currentWP, mapId);
			}
			
			//TODO: More precision
			if (!wt.getRoutestep().isEmpty() && !MathUtil.isIn3dRange(wt.getRoutestep().get(0).getX(), wt.getRoutestep().get(0).getY(), wt.getRoutestep().get(0).getZ(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getX(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getY(), wt.getRoutestep().get(wt.getRoutestep().size() -1).getZ(), 15))
				wt.setReversed("true");
			
			if (wt.getRouteId() != null && !wt.getRoutestep().isEmpty())
				walkerList.add(wt);
		}
	}

	@Override
	public void create() {
		String OUTPUT = fromSpawns ? WorldProperties.WALKERS_FROM_SPAWNS : WorldProperties.WALKERS;
		addOrder(OUTPUT, WorldProperties.WALKERS_BINDINGS, walkers);
		write(orders);
		System.out.println("\n[WALKERS] Walkers count: " + walkerList.size());
	}
	
	private boolean exists(String wpName) {
		for (WalkerTemplate walker : walkerList)
			if (walker.getRouteId().equalsIgnoreCase(wpName))
				return true;
		
		return false;
	}
	
	private void setRouteSteps(WalkerTemplate wt, WayPoint currentWP, int mapId) {
		if (!currentWP.getSteps().isEmpty()) {
			int i = 0;
			for (Point3D p : currentWP.getSteps()) {
				Routestep newStep = new Routestep();
				newStep.setStep(++i);
				newStep.setX(p.getX());
				newStep.setY(p.getY());
				
				// spot.setZ(ZUtils.getBestZ(sd)); TODO
				if (AionProperties.USE_GEO)
					newStep.setZ(GeoService.getInstance().getZ(mapId, newStep.getX(), newStep.getY()));
				else
					newStep.setZ(p.getZ());
				
				wt.getRoutestep().add(newStep);
			}
		}
	}

	private void organize(WalkerTemplate wt, WayPoint currentWP) {
	
		Map<String, Integer> mobs = new HashMap<String, Integer>();
		
		for (SourceSphere ss : ssList)
			mobs.put(ss.getName(), sum(select(ssList, having(on(SourceSphere.class).getWpName().equalsIgnoreCase(currentWP.getName())))).intValue());
	
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
