package com.parser.common.aion.bindings;

import java.util.ArrayList;
import java.util.List;

import com.parser.common.math.Point3D;

public class WayPoint {

	public String name;
	public int stepsCount;
	public List<Point3D> steps;
	
	public WayPoint() {}
	
	public WayPoint(String name, int stepsCount, List<Point3D> steps) {
		this.name = name;
		this.stepsCount = stepsCount;
		this.steps = steps;
	}
	
	public String getName() {return name;}
	public void setName(String value) {this.name = value;}
	
	public int getStepsCount() {return stepsCount;}
	public void setStepsCount(int value) {this.stepsCount = value;}
	
	public List<Point3D> getSteps() {
		 if (steps == null) {
            steps = new ArrayList<Point3D>();
        }
        return this.steps;
	}
	public void setName(List<Point3D> value) {this.steps = value;}
}
