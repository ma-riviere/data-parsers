package com.parser.write.aion.levels;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.parser.write.DataProcessor;

import com.parser.read.BinaryParser;
import com.parser.read.BinaryParserData;

import com.parser.input.aion.level_data.LevelData;

import com.parser.commons.utils.Util;
import com.parser.commons.aion.AionDataHub;
import com.parser.commons.aion.properties.LevelsProperties;

import com.parser.output.aion.height_map.*;

public class AionHeightMapWriter extends DataProcessor {

	AionDataHub data = new AionDataHub().getInstance();

	Points points = new Points();
	Collection<Point> pointList = points.getPoint();
	
	Map<String, LevelData> infoMap = new HashMap<String, LevelData>();
	
	static final int MIN_X = 0;
	static final int MIN_Y = 0;
	int MAX_X = 1536;
	int x = 1536;
	int MAX_Y = 1536;
	int y = 1536;
	static final int STEP = 1;
	
	BinaryParserData bpd = null;
		
	@Override
	public void collect() {
		String HEIGHT_MAP_PATH = "../../Input/aion40/Client/Levels/df1/terrain/land_map.h32";
		bpd = new BinaryParser().parseFile(HEIGHT_MAP_PATH);
		infoMap = data.getLevelData();
	}

	@Override
	public void transform() {
		if (bpd.getSize() % 3 != 0) {
			System.out.println("[ERROR] Buffer size is not valid, more than 3 bytes may be used for map : " + bpd.getPath(1));
			return;
		}
		int mapId = data.getWorldId(bpd.getPath(1));
		if (mapId == 0) {
			System.out.println("Invalid map name " + bpd.getPath(1));
			return;
		}
		
		// MAX_X = infoMap.getLevelInfo().get(bpd.getPath(1)).getHeightmapXSize();
		// MAX_Y = infoMap.getLevelInfo().get(bpd.getPath(1)).getHeightmapYSize();
		
		int count = bpd.getSize() / 3;
		
		//TODO: Write GeoZ too to compare
		for (int i = 0; i<count; i++){
			Point p = new Point();
			p.setX((float)x);
			p.setY((float)y);
			p.setZ((float) (bpd.getBuffer().getShort() / 32f));
			p.setLayer(bpd.getBuffer().get());
			updateXY();
			pointList.add(p);
		}
	}
	
/*	private void iterateOver(int x, int y, boolean ascending) {
		if (x <= MAX_X - 1) x++;
		else {
			x
		}
	}*/
	
	private void updateXY() {
		if (x > MIN_X)
			x--;
		else {
			x = 1536;
			if (y > MIN_Y)
				y--;
			else
				y = 1536;
		}
	}
	
	@Override
	public void create() {
		addOrder(LevelsProperties.HEIGHTMAP, LevelsProperties.HEIGHTMAP_BINDINGS, points);
		System.out.println("\n[POINTS] Points count : " + pointList.size());
	}
}
