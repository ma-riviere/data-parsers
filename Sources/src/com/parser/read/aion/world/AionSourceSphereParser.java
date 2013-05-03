package com.parser.read.aion.world;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;

import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.properties.WorldProperties;

import com.parser.read.TextParser;

public class AionSourceSphereParser extends TextParser {

	public AionSourceSphereParser() {}
	
	public FastMap<String, List<SourceSphere>> parse() {
		FastMap<String, List<SourceSphere>> spheres = new FastMap<String, List<SourceSphere>>();
		
		File file = new File(WorldProperties.INPUT_SPHERE);
		List<String> lines = parseFile(file).getLines();
		
		for (String line : lines) {
			if (lines.indexOf(line) != 0) {
				SourceSphere ss = extractData(line);
				List<SourceSphere> temp = new ArrayList<SourceSphere>();
				if (spheres.containsKey(ss.getMap()))
					temp = spheres.get(ss.getMap());
				
				temp.add(ss);
				spheres.put(ss.getMap(), temp);
			}
		}
		
		return spheres;
	}
	
	private SourceSphere extractData(String line) {
		SourceSphere ss = new SourceSphere();
		String[] data = line.split(",");
		
		ss.setName(data[0]);
		ss.setType(data[1]);
		ss.setMap(data[2].toUpperCase());
		ss.setLayer(Integer.parseInt(data[3]));
		ss.setX(Float.parseFloat(data[4]));
		ss.setY(Float.parseFloat(data[5]));
		ss.setZ(Float.parseFloat(data[6]));
		ss.setRadius(Double.parseDouble(data[7]));
		ss.setWpName(data[8]);
		ss.setConditionSpawn(Integer.parseInt(data[9]));
		ss.setVersion(Integer.parseInt(data[10]));
		ss.setCountry(Integer.parseInt(data[11]));
		if (data.length == 13)
			ss.setClusterNum(Integer.parseInt(data[12]));
		else
			ss.setClusterNum(0);
		
		return ss;
	}
}
