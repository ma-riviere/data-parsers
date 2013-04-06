package com.parser.common.math;

public class MathUtil {

	public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static double getDistance(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static double getDistanceZ(float z1, float z2) {
		float dz = z1 - z2; 
		return Math.sqrt(dz * dz);
	}
	
	public static boolean isCloseEnough(String pos, float x, float y, float z, double MAX_DIST, double MAX_Z_DIST) {
		boolean isClose = false; boolean isCloseZ = false;
		String[] xyz = pos.split(",");
		if (getDistance(x, y, toFloat3(xyz[0]), toFloat3(xyz[1])) <= MAX_DIST)
			isClose = true;
		if (getDistanceZ(z, toFloat3(xyz[2])) <= MAX_Z_DIST)
			isCloseZ = true;
		return isClose && isCloseZ;
	}

	public static boolean isIn3dRange(final float obj1X, final float obj1Y, final float obj1Z, final float obj2X,
		final float obj2Y, final float obj2Z, float range) {
		float dx = (obj2X - obj1X);
		float dy = (obj2Y - obj1Y);
		float dz = (obj2Z - obj1Z);
		return dx * dx + dy * dy + dz * dz < range * range;
	}

	
	public final static float headingToDegree(byte h) {
		return (float) h * 3;
	}

	public final static int degreeToHeading(float angle) {
		return (int) (angle / 3);
	}
	
	public final static float toFloat3(String s) {
		return Math.round(Float.parseFloat(s) * (1000.00f)) / (1000.00f);
	}
}
