package com.parser.write.aion.rides;

import java.util.Collection;
import java.util.List;

import com.parser.common.aion.bindings.WayPoint;
import com.parser.common.aion.bindings.SourceSphere;

import com.parser.read.aion.world.AionWayPointsParser;
import com.parser.read.aion.world.AionSourceSphereParser;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.walkers.*;

public class AionWalkersWriter extends AbstractWriter {

	NpcWalker walkers = new NpcWalker();
	Collection<WalkerTemplate> walkerList = walkers.getWalkerTemplate();
	
	List<WayPoint> wayPoints; // From waypoint.csv
	List<SourceSphere> sourceSpheres; // From source_sphere.csv
	
	// When called by AionSpawnsWriter
	boolean fromSpawns = false;
	
	public void parseFromSpawns(List<Integer> toMake) {
		fromSpawns = true;
		parse();
		transform();
		marshall();
	}
	
	@Override
	public void parse() {
		wayPoints = new AionWayPointsParser().parse();
		sourceSpheres = new AionSourceSphereParser().parse();
	}

	@Override
	public void transform() {
		for (WayPoint wp : wayPoints) {
			WalkerTemplate wt = new WalkerTemplate();
			// TODO
			walkerList.add(wt);
		}
	}

	@Override
	public void marshall() {
		if (fromSpawns)
			addOrder(AionWritingConfig.WALKERS_BINDINGS, AionWritingConfig.SPAWNS_WALKERS, walkers);
		else
			addOrder(AionWritingConfig.WALKERS_BINDINGS, AionWritingConfig.WALKERS, walkers);
		
		FileMarhshaller.marshallFile(orders);
		System.out.println("[WALKERS] Walkers count: " + walkerList.size());
	}

}
