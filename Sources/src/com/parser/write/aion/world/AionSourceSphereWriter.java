package com.parser.write.aion.world;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.on;

import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.properties.WorldProperties;
import com.parser.commons.utils.maths.MathUtil;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;

import com.parser.output.aion.source_sphere.*;

public class AionSourceSphereWriter extends AbstractWriter {

	Spheres spheres = new Spheres();
	Collection<Sphere> sList = spheres.getSphere();
	
	List<SourceSphere> ssList;
		
	@Override
	public void parse() {
		ssList = new ArrayList<SourceSphere>(flatten(aion.getSpheres().values()));
		aion.getWorldMaps();
		aion.getNpcs();		
	}

	@Override
	public void transform() {
		for (SourceSphere css : ssList) {
			Sphere s = new Sphere();
			s.setNpcId(aion.getNpcs().get(css.getName()).getId());
			s.setType(css.getType().toUpperCase());
			s.setWorldId(aion.getWorldId(css.getMap()));
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
		sList = sort(sList, on(Sphere.class).getWorldId());
	}
	
	@Override
	public void marshall() {
		addOrder(WorldProperties.SPHERE_OUTPUT, WorldProperties.SPHERE_OUTPUT_BINDINGS, spheres);
		FileMarshaller.marshallFile(orders);
		System.out.println("\n[SPHERE] source_sphere count: " + sList.size());
	}
}
