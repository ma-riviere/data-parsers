package com.parser.write.aion.world;

import com.google.common.base.Strings;
import java.util.Collection;
import javolution.util.FastMap;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.on;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.world_maps.WorldMap;
import com.parser.input.aion.zone_maps.Zonemap;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;

import com.parser.output.aion.world_maps.*;

public class AionWorldMapsWriter extends AbstractWriter {

	WorldMaps worldMaps = new WorldMaps();
	Collection<Map> mapList = worldMaps.getMap();
	
	Collection<WorldMap> clientMaps;
	FastMap<String, LevelData> levelData;
	FastMap<String, Zonemap> zoneMaps;
	
	@Override
	public void parse() {
		clientMaps = aion.getWorldMaps().values();
		levelData = new FastMap(aion.getLevelData());
		zoneMaps = new FastMap(aion.getZoneMaps());
		aion.getStrings();
		aion.getCooltimes();
	}

	@Override
	public void transform() {
		for (WorldMap cm : clientMaps) {
			Map m = new Map();
			
			LevelData data = levelData.get(cm.getValue());
			Zonemap zoneMap = zoneMaps.get(cm.getValue());
			
			m.setId(cm.getId());
			
			m.setName(aion.getStringText("STR_ZONE_NAME_" + cm.getValue()));
			
			if (data.getMissions().getMission().getLevelOption().getFly().getFlyWholeLevel() != 0)
				m.setDeathLevel(data.getMissions().getMission().getLevelOption().getFly().getMaxHeight());
				
			m.setWaterLevel((int) Math.round(data.getLevelInfo().getWaterLevel()));
			
			String worldType = getWorldType(zoneMap.getMapCategory());
			if (worldType != null)
				m.setWorldType(worldType);
			m.setWorldSize(data.getLevelInfo().getHeightmapXSize() + data.getLevelInfo().getHeightmapYSize());
			
			m.setFlags(getFlags(cm, data));
			
			if (cm.getMaxUser() != null && cm.getMaxUser() != 0)
				m.setMaxUser(cm.getMaxUser());
			
			if (cm.getBeginnerTwinCount() != null && cm.getBeginnerTwinCount() != 0)
				m.setTwinCount(1); // [UPDATE] We don't need it right now
			
			if (aion.getCooltimes().get(cm.getValue().toUpperCase()) != null)
				m.setInstance("true");
			
			if (cm.getExceptBuff() != null && cm.getExceptBuff().equalsIgnoreCase("true"))
				m.setExceptBuff("true");
			
			if (cm.getPrison() != null && cm.getPrison().equalsIgnoreCase("true"))
				m.setPrison("true");
			
			AiInfo ai = new AiInfo();
			
			m.setAiInfo(ai);
			
			mapList.add(m);
		}
		mapList = sort(mapList, on(Map.class).getId());
	}
	
	@Override
	public void marshall() {
		addOrder(WorldProperties.OUTPUT_WORLD_MAPS, WorldProperties.OUTPUT_WORLD_MAPS_BINDINGS, worldMaps);
		FileMarshaller.marshallFile(orders);
		System.out.println("\n[MAPS] WorldMaps count: " + mapList.size());
	}
	
	private String getWorldType(String category) {
		if (Strings.isNullOrEmpty(category))
			return null;
		else {
			switch(category.toUpperCase()) {
				case "LIGHT":
					return "ELYSEA";
				case "DARK":
					return "ASMODAE";
				case "DRAGON":
					return "BALAUREA";
				case "ABYSS":
					return "ABYSS";
				default:
					log.unique("Unknown map category", category, true);
					return null;
			}
		}
	}
	
	private int getFlags(WorldMap cm, LevelData data) {
		int flags = 0;
		if (!data.getMissions().getMission().getLevelOption().getBindArea().getIsPossible().equalsIgnoreCase("0") || !data.getMissions().getMission().getLevelOption().getBindArea().getIsPossible().equalsIgnoreCase("false")) //0
			flags |= 1;
		if (!data.getMissions().getMission().getLevelOption().getReCall().getIsPossible().equalsIgnoreCase("0") || !data.getMissions().getMission().getLevelOption().getReCall().getIsPossible().equalsIgnoreCase("false")) //1
			flags |= 2;
		if (!data.getMissions().getMission().getLevelOption().getGlide().getIsPossible().equalsIgnoreCase("0") || !data.getMissions().getMission().getLevelOption().getGlide().getIsPossible().equalsIgnoreCase("false")) //1
			flags |= 4;
		if (data.getMissions().getMission().getLevelOption().getFly().getFlyWholeLevel() > 0) //0
			flags |= 8;
		if (data.getMissions().getMission().getLevelOption().getRide().getRideWholeLevel() == 1) //1
			flags |= 16;
		if (data.getMissions().getMission().getLevelOption().getRide().getRideWholeLevel() == 2) //1
			flags |= 32;
		// if (PVP_ENABLED) // cm.getShowPvpState() > 0 ? //1
			// flags |= 64;
		// if (DUEL_SAME_RACE_ENABLED) //1
			// flags |= 128;
		// if (DUEL_OTHER_RACE_ENABLED)
			// flags |= 256;
		return flags;
	}
}
