package com.parser.read.aion.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.parser.common.aion.bindings.SourceSphere;

import com.parser.read.aion.AionReadingConfig;

public class AionSourceSphereParser {

	File ss = new File(AionReadingConfig.SOURCE_SPHERE);

	public AionSourceSphereParser() {}
	
	public List<SourceSphere> parse() {
		List<SourceSphere> ssList = new ArrayList<SourceSphere>();
		// TODO
		return ssList;
	}
}
