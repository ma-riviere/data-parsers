package com.parser.common;

public class MathUtil {

	public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
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
}
