package com.parser.commons.aion.utils;

import com.google.common.base.Strings;

public class ZUtils {

	/**
	 * Objects that have correct Z : WATERVOLUME
	 * Entities that have correct Z : 
	 *

	private void adjustZ(Spot spot, SpawnData sd) {
		int MAX_VAR = 75;
		float oldZ = spot.getZ();
		float result = oldZ;
		
		
		float entityZ = getMeanEntityZ(sd);
		if (Math.abs((int) (entityZ * 1) - (int) (oldZ * 1)) <= MAX_VAR)
			result = entityZ;
		else if (USE_GEO) {
			float geoZ = GeoService.getInstance().getZ(sd.getMapId(), spot.getX(), spot.getY());
			if (Math.abs((int) (geoZ * 1) - (int) (oldZ * 1)) <= 25)
				result = geoZ;
			else
				log.unique("[WARN] Reverting to oldZ for Npc : ", sd.getNpcId(), false);
		}
		spot.setZ(result);
	}
	
	//TODO: Get entities from a big distance (maybe some object types too)
	// Sort them by distance, take the z of the closest, mean with big range creates issues
	private float getMeanEntityZ(SpawnData sd) {
		List<Float> results = new ArrayList<Float>();
		for (Entity ent : data.getClientEntities().get(sd.getMapId())) {
			if (!Strings.isNullOrEmpty(ent.getPos())) {
				String[] xyz = ent.getPos().split(",");
				if (MathUtil.getDistance(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), sd.getX(), sd.getY()) <= 50 && MathUtil.getDistanceZ(Float.parseFloat(xyz[2]), sd.getZ()) <= 50)
					results.add(ent);
			}
		}
		if (results.isEmpty()) {
			log.unique("[WARN] No nearby entities found for Npc : ", sd.getNpcId(), false);
			return 99999.99f;
		}
		else {
			float sum = 0.0f;
			for (Float f : results)
				sum += f;
			return (sum / (float) (results.size()));
		}
	}*/
}
