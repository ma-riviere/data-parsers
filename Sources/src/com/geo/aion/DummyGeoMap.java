package com.geo.aion;

import com.geo.aion.geoEngine.math.Vector3f;
import com.geo.aion.geoEngine.models.GeoMap;
import com.geo.aion.geoEngine.scene.Spatial;

/**
 * @author ATracer
 */
public class DummyGeoMap extends GeoMap {

	public DummyGeoMap(String name, int worldSize) {
		super(name, worldSize);
	}

	@Override
	public final float getZ(float x, float y, float z, int instanceId) {
		return z;
	}

	@Override
	public final boolean canSee(float x, float y, float z, float targetX, float targetY, float targetZ, float limit, int instanceId) {
		return true;
	}

	@Override
	public Vector3f getClosestCollision(float x, float y, float z, float targetX, float targetY, float targetZ, boolean changeDirction, boolean fly, int instanceId) {
		return new Vector3f(targetX, targetY, targetZ);
	}

	@Override
	public void setDoorState(int instanceId, String name, boolean state) {

	}

	@Override
	public int attachChild(Spatial child) {
		return 0;
	}
}
