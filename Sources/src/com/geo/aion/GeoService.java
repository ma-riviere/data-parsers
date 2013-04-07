package com.geo.aion;

/**
 * @author ATracer
 */
public class GeoService {

	private GeoData geoData;

	public void initializeGeo() {
		geoData = new RealGeoData();
		geoData.loadGeoMaps();
	}

	public float getZ(int worldId, float x, float y) {
		return geoData.getMap(worldId).getZ(x, y);
	}

	public static final GeoService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static final class SingletonHolder {
		protected static final GeoService instance = new GeoService();
	}
}
