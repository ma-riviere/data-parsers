package com.geo.aion;

import com.geo.aion.geoEngine.models.GeoMap;

/**
 * @author ATracer
 */
public interface GeoData {

	void loadGeoMaps();

	GeoMap getMap(int worldId);
}
