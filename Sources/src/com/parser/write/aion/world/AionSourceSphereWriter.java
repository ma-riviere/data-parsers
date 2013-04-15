package com.parser.write.aion.world;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parser.commons.aion.AionDataCenter;
import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.utils.maths.MathUtil;

import com.parser.read.aion.world.AionSourceSphereParser;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.source_sphere.*;

public class AionSourceSphereWriter extends AbstractWriter {

	Spheres spheres = new Spheres();
	Collection<Sphere> sList = spheres.getSphere();
	
	List<SourceSphere> clientSpheres = new ArrayList<SourceSphere>();
		
	@Override
	public void parse() {
		clientSpheres = new AionSourceSphereParser().parse();
		new AionDataCenter().getInstance().getWorldMaps();
		new AionDataCenter().getInstance().loadNpcNameIdMap();		
	}

	@Override
	public void transform() {
		for (SourceSphere css : clientSpheres) {
			Sphere s = new Sphere();
			s.setNpcId(getNpcId(css.getName()));
			s.setType(css.getType().toUpperCase());
			s.setWorldId(getWorldId(css.getMap()));
			s.setLayer(css.getLayer());
			if (!Strings.isNullOrEmpty(css.getWpName()))
				s.setWayPoint(css.getWpName().toUpperCase());
			s.setVersion(css.getVersion());
			s.setCountry(css.getCountry());
			s.setPool(css.getClusterNum());
			
			Pos pos = new Pos();
			pos.setX(css.getX());
			pos.setY(css.getY());
			pos.setZ(css.getZ());
			pos.setR((float) css.getRadius());
			s.setPos(pos);
			
			if (s.getNpcId() != 0)
				sList.add(s);
		}
		spheres = order(spheres);
	}
	
	@Override
	public void marshall() {
		addOrder(AionWritingConfig.SPHERE_BINDINGS, AionWritingConfig.SPHERE, spheres);
		FileMarhshaller.marshallFile(orders);
		System.out.println("\n[SPHERE] source_sphere count: " + sList.size());
	}
	
	private int getWorldId(String s) {return new AionDataCenter().getInstance().getWorldId(s);}
	private int getNpcId(String s) {return (s != null) ? new AionDataCenter().getInstance().getNpcIdByName(s) : 0;}
	
	private Spheres order(Spheres spheres) {
		List<Sphere> list = new ArrayList<Sphere>(spheres.getSphere());
		Collections.sort(list, new Comparator<Sphere>() {	
			public int compare(Sphere o1, Sphere o2) {return o1.getWorldId().compareTo(o2.getWorldId());}
		});
		
		Collection<Sphere> coll = list;
		spheres.getSphere().clear();
		spheres.getSphere().addAll(coll);
		
		return spheres;
	}
}
