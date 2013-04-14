package com.parser.commons.utils.maths;

import java.io.Serializable;

/**
 * This class represents 3D point.<br>
 * It's valid for serializing and cloning.
 * 
 * @author SoulKeeper
 */
@SuppressWarnings("serial")
public class Point3D implements Cloneable, Serializable {

	private float x;
	private float y;
	private float z;

	public Point3D() {}

	public Point3D(Point3D point) {
		this(point.getX(), point.getY(), point.getZ());
	}

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {return x;}
	public void setX(float x) {this.x = x;}

	public float getY() {return y;}
	public void setY(float y) {this.y = y;}

	public float getZ() {return z;}
	public void setZ(float z) {this.z = z;}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Point3D))
			return false;

		Point3D point3D = (Point3D) o;

		return x == point3D.x && y == point3D.y && z == point3D.z;
	}

	@Override
	public int hashCode() {
		float result = x;
		result = 31 * result + y;
		result = 31 * result + z;
		return (int)(result*100);
	}

	@Override
	public Point3D clone() throws CloneNotSupportedException {
		return new Point3D(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Point3D");
		sb.append("{x=").append(x);
		sb.append(", y=").append(y);
		sb.append(", z=").append(z);
		sb.append('}');
		return sb.toString();
	}
}
