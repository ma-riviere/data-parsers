package com.parser.read.aion.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.parser.commons.aion.bindings.SourceSphere;

import com.parser.read.aion.AionReadingConfig;

public class AionSourceSphereParser {

	File file = new File(AionReadingConfig.SOURCE_SPHERE);

	public AionSourceSphereParser() {}
	
	public List<SourceSphere> parse() {
		List<SourceSphere> ssList = new ArrayList<SourceSphere>();

		try {
            Scanner scanner = new Scanner(file);
			System.out.println("\n[MAIN] [INFO] source_sphere.csv was found, parsing it !");
			
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
				SourceSphere ss = extractData(line);
				if (ss != null)
					ssList.add(ss);
            }
			
            scanner.close();
        } 
		catch (FileNotFoundException e) {
			System.out.println("\n[MAIN] [INFO] Could not find source_sphere.csv ! Check reading configs");
			e.printStackTrace();
		}
		
		System.out.println("[WALKERS] Parsed " + ssList.size() + " SourceSphere !");
		return ssList;
	}
	
	private SourceSphere extractData(String line) {
		SourceSphere ss = new SourceSphere();
		
		if (line.equalsIgnoreCase("name,type,zone,layer,x,y,z,r,wayPointName,conditionSpawn,version,country,clusterNum"))
			return null;
		
		String[] data = line.split(",");
		
		ss.setName(data[0]);
		ss.setType(data[1]);
		ss.setMap(data[2]);
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
