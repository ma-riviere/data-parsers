package com.parser.write.aion.world;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.List;
import javolution.util.FastMap;

import static ch.lambdaj.Lambda.*;

import com.parser.commons.aion.bindings.SourceSphere;
import com.parser.commons.aion.properties.WorldProperties;
import com.parser.commons.utils.maths.MathUtil;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;

import com.parser.output.aion.source_sphere.*;

public class AionSourceSphereWriter extends AbstractWriter {

	Spheres spheres = new Spheres();
	Collection<Sphere> sList = spheres.getSphere();
	
	FastMap<Integer, List<SourceSphere>> clientSpheres = new FastMap<Integer, List<SourceSphere>>();
		
	@Override
	public void parse() {
		clientSpheres = aion.getClientSpheres();
		aion.getWorldMaps();
		aion.loadNpcNameIdMap();		
	}

	@Override
	public void transform() {
		for (Integer mapId : clientSpheres.keySet()) {
			for (SourceSphere css : clientSpheres.get(mapId)) {
				Sphere s = new Sphere();
				s.setNpcId(aion.getNpcIdByName(css.getName()));
				s.setType(css.getType().toUpperCase());
				s.setWorldId(mapId);
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
