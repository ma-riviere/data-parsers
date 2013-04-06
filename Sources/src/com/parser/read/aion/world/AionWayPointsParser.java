package com.parser.read.aion.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.parser.common.aion.bindings.WayPoint;

import com.parser.read.aion.AionReadingConfig;

public class AionWayPointsParser {

	File wp = new File(AionReadingConfig.WAYPOINTS);

	public AionWayPointsParser() {}
	
	public List<WayPoint> parse() {
		List<WayPoint> wpList = new ArrayList<WayPoint>();
		// TODO
		return wpList;
	}
}
