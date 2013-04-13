package com.parser.common.math;

import com.google.common.base.Strings;

public class MathUtil {

	public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static double getDistance(String pos, float x1, float y1, float z1) {
		if (!Strings.isNullOrEmpty(pos)) {
			String[] xyz = pos.split(",");
			
			float dx = x1 - Float.parseFloat(xyz[0]);
			float dy = y1 - Float.parseFloat(xyz[1]);
			float dz = z1 - Float.parseFloat(xyz[2]);
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Double.POSITIVE_INFINITY;
	}
	
	public static double getDistance(String pos1, String pos2) {
		if (!Strings.isNullOrEmpty(pos1) && !Strings.isNullOrEmpty(pos2)) {
			String[] xyz1 = pos1.split(",");
			String[] xyz2 = pos2.split(",");
			
			float dx = Float.parseFloat(xyz1[0]) - Float.parseFloat(xyz2[0]);
			float dy = Float.parseFloat(xyz1[1]) - Float.parseFloat(xyz2[1]);
			float dz = Float.parseFloat(xyz1[2]) - Float.parseFloat(xyz2[2]);
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Double.POSITIVE_INFINITY;
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

	public static boolean isIn3dRange(float obj1X, float obj1Y, float obj1Z, float obj2X, float obj2Y, float obj2Z, double range) {
		float dx = (obj2X - obj1X);
		float dy = (obj2Y - obj1Y);
		float dz = (obj2Z - obj1Z);
		return (dx * dx + dy * dy + dz * dz) < range * range;
	}
	
	public static boolean isIn3dRange(String pos, float obj2X, float obj2Y, float obj2Z, double range) {
		if (Strings.isNullOrEmpty(pos))
			return false;
		String[] xyz = pos.split(",");
		float dx = (Float.parseFloat(xyz[0]) - obj2X);
		float dy = (Float.parseFloat(xyz[1]) - obj2Y);
		float dz = (Float.parseFloat(xyz[2]) - obj2Z);
		return (dx * dx + dy * dy + dz * dz) < range * range;
	}
	
	public static boolean isIn3dRange(String pos1, String pos2, double range) {
		String[] xyz1 = pos1.split(",");
		String[] xyz2 = pos2.split(",");
		float dx = Float.parseFloat(xyz1[0]) - Float.parseFloat(xyz2[0]);
		float dy = Float.parseFloat(xyz1[1]) - Float.parseFloat(xyz2[1]);
		float dz = Float.parseFloat(xyz1[2]) - Float.parseFloat(xyz2[2]);
		return (dx * dx + dy * dy + dz * dz) < range * range;
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
	
	public static boolean isCloseEnough(float x1, float y1, float z1, float x2, float y2, float z2, int pow, int precision) {
		float mult = (float) Math.pow(10, pow);
		int dx = Math.abs((int) (x1 * mult) - (int) (x2 * mult));
		int dy = Math.abs((int) (y1 * mult) - (int) (y2 * mult));
		int dz = Math.abs((int) (z1 * mult) - (int) (z2 * mult));
		if (dx <= precision && dy <= precision && dz <= precision)
			return true;
		return false;
	}
	
	public static boolean isCloseEnough(String pos, float x2, float y2, float z2, int pow, int precision) {
		if (Strings.isNullOrEmpty(pos))
			return false;
			
		String[] xyz = pos.split(",");
		float mult = (float) Math.pow(10, pow);
		
		int dx = Math.abs((int) (Float.parseFloat(xyz[0]) * mult) - (int) (x2 * mult));
		int dy = Math.abs((int) (Float.parseFloat(xyz[1]) * mult) - (int) (y2 * mult));
		int dz = Math.abs((int) (Float.parseFloat(xyz[2]) * mult) - (int) (z2 * mult));
		if (dx <= precision && dy <= precision && dz <= precision)
			return true;
		return false;
	}
	
	public static boolean isCloseEnough(float x1, float y1, float z1, float x2, float y2, float z2, double radius) {
		int pow = String.valueOf((int) radius).length() - 1;
		int precision = (int) Math.ceil(radius);
		float mult = (float) Math.pow(10, pow);
		int dx = Math.abs((int) (x1 * mult) - (int) (x2 * mult));
		int dy = Math.abs((int) (y1 * mult) - (int) (y2 * mult));
		int dz = Math.abs((int) (z1 * mult) - (int) (z2 * mult));
		if (dx <= precision && dy <= precision && dz <= precision)
			return true;
		return false;
	}
	
	public static boolean isCloseEnough(String pos, float x2, float y2, float z2, double radius) {
		if (Strings.isNullOrEmpty(pos))
			return false;
			
		String[] xyz = pos.split(",");
		int pow = String.valueOf((int) radius).length() - 1;
		int precision = (int) Math.ceil(radius);
		float mult = (float) Math.pow(10, pow);
		
		int dx = Math.abs((int) (Float.parseFloat(xyz[0]) * mult) - (int) (x2 * mult));
		int dy = Math.abs((int) (Float.parseFloat(xyz[1]) * mult) - (int) (y2 * mult));
		int dz = Math.abs((int) (Float.parseFloat(xyz[2]) * mult) - (int) (z2 * mult));
		if (dx <= precision && dy <= precision && dz <= precision)
			return true;
		return false;
	}
}
