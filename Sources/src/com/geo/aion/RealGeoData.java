package com.geo.aion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.commons.aion.AionDataHub;
import com.parser.commons.utils.Util;
import com.parser.input.aion.world_maps.WorldMap;
import com.parser.input.aion.level_data.LevelInfo;

import com.geo.aion.geoEngine.GeoWorldLoader;
import com.geo.aion.geoEngine.models.GeoMap;
import com.geo.aion.geoEngine.scene.Spatial;

/**
 * @author ATracer
 */
public class RealGeoData implements GeoData {
	
	AionDataHub data = new AionDataHub().getInstance();

	private Map<Integer, GeoMap> geoMaps = new HashMap<Integer, GeoMap>();
	private List<WorldMap> maps = new ArrayList<WorldMap>(data.getWorldMaps().values());
	private Map<Integer, LevelInfo> infoMap = new HashMap<Integer, LevelInfo>(data.getLevelInfos());
	
	String geoDir = "../../Input/geo/aion/meshs.geo"; //TODO to property

	@Override
	public void loadGeoMaps() {
		Map<String, Spatial> models = loadMeshes();
		loadWorldMaps(models);
		models.clear();
		models = null;
		System.out.println("[GEODATA] Loaded " + geoMaps.size() + " geo maps !");
	}

	protected void loadWorldMaps(Map<String, Spatial> models) {
		System.out.println("[GEODATA] Loading geo maps..");
		
		List<Integer> mapsWithErrors = new ArrayList<Integer>();
		Util.printProgressBarHeader(infoMap.keySet().size());

		for (WorldMap map : maps) {
			LevelInfo info = infoMap.get(data.getWorldId(map.getValue().toUpperCase()));
			if (info == null)
				continue;
			GeoMap geoMap = new GeoMap(Integer.toString(map.getId()), ((int) info.getHeightmapXSize() + info.getHeightmapYSize()));
			try {
				if (GeoWorldLoader.loadWorld(map.getId(), models, geoMap)) {
					geoMaps.put(map.getId(), geoMap);
				}
			}
			catch (Throwable t) {
				mapsWithErrors.add(map.getId());
				geoMaps.put(map.getId(), DummyGeoData.DUMMY_MAP);
			}
			Util.printCurrentProgress();
		}
		Util.printEndProgress();

		if (mapsWithErrors.size() > 0) {
			System.out.println("[GEODATA] Some maps were not loaded correctly and reverted to dummy implementation : ");
			System.out.println(mapsWithErrors.toString());
		}
	}

	protected Map<String, Spatial> loadMeshes() {
		System.out.println("[GEODATA] Loading meshes..");
		Map<String, Spatial> models = null;
		try {
			models = GeoWorldLoader.loadMeshs(geoDir);
		}
		catch (IOException e) {
			throw new IllegalStateException("[GEODATA] Problem loading meshes", e);
		}
		return models;
	}

	@Override
	public GeoMap getMap(int worldId) {
		GeoMap geoMap = geoMaps.get(worldId);
		return geoMap != null ? geoMap : DummyGeoData.DUMMY_MAP;
	}
}
