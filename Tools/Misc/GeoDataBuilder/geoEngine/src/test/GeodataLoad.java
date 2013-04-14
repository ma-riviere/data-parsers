// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 17.09.2011 21:59:09
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GeodataLoad.java

package test;

import aionjHungary.geoEngine.GeoWorldLoader;
import aionjHungary.geoEngine.models.GeoMap;
import java.util.*;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class GeodataLoad
{
	public static void main(String[] args) {
		System.out.println("Stub main");
	}

    public void setUp() throws Exception
    {
        GeoWorldLoader.setGeoDir("d:\\workspace\\aionj\\GeodataExtract\\");
        world = new GeoMap("220010000", 3072);
        Map<String, Spatial> models = GeoWorldLoader.loadMeshs();
        GeoWorldLoader.loadWorld(0xd1d1610, models, world);
        System.out.println(world.getChildren().size());
        int all = 0;
        for(Iterator<Spatial> i$ = world.getChildren().iterator(); i$.hasNext();)
        {
            Spatial node = (Spatial)i$.next();
            all += ((Node)node).getChildren().size();
        }

        System.out.println((new StringBuilder()).append("all ").append(all).toString());
    }

    public void testZ()
    {
//        float x = 561F;
//        float y = 2788F;
        System.out.println((new StringBuilder()).append("Z: ").append(world.getZ(571F, 2787F, 299F)).toString());
    }

    public void testCanSee()
    {
        if(world.canSee(527F, 2801F, 309F, 523F, 2791F, 297F))
            System.out.println("See");
        else
            System.out.println("not See");
    }

    private GeoMap world;
}