package com.parser.read.aion.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.parser.commons.aion.bindings.WayPoint;
import com.parser.commons.utils.maths.Point3D;

import com.parser.read.aion.AionReadingConfig;

public class AionWayPointsParser {

	File file = new File(AionReadingConfig.WAYPOINTS);

	public AionWayPointsParser() {}
	
	public List<WayPoint> parse() {
		List<WayPoint> wpList = new ArrayList<WayPoint>();
		
		try {
            Scanner scanner = new Scanner(file);
			System.out.println("\n[MAIN] [INFO] waypoint.csv was found, parsing it !");
 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
				WayPoint wp = extractData(line);
				if (wp != null)
					wpList.add(wp);
            }
			
            scanner.close();
        } 
		catch (FileNotFoundException e) {
			System.out.println("\n[MAIN] [INFO] Could not find waypoint.csv ! Check reading configs");
			e.printStackTrace();
		}
		
		System.out.println("[WALKERS] Parsed " + wpList.size() + " WayPoints !");
		return wpList;
	}
	
	private WayPoint extractData(String line) {
		WayPoint wp = new WayPoint();
		
		if (line.equalsIgnoreCase("name,num,x,y,z"))
			return null;
		
		String[] data = line.split(",");
		
		if ((data.length - 2) % 3  != 0) {
			List<String> list = new ArrayList<String>(Arrays.asList(data));
			list.remove(list.get(1));
			list.remove(list.get(2));
			data = list.toArray(new String[list.size()]);
		}
		
		wp.setName(data[0]);
		wp.setStepsCount(Integer.parseInt(data[1]));
		
		for (int i = 2; (i + 3) <= data.length; i += 3)
			wp.getSteps().add(new Point3D(Float.parseFloat(data[i]), Float.parseFloat(data[i+1]), Float.parseFloat(data[i+2])));
		
		return wp;
	}
}
