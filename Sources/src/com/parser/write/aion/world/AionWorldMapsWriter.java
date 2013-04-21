package com.parser.write.aion.world;

import com.google.common.base.Strings;
import java.util.Collection;
import javolution.util.FastMap;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.on;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;
import com.parser.input.aion.level_data.LevelOption;
import com.parser.input.aion.world_maps.WorldMap;
import com.parser.input.aion.zone_maps.Zonemap;

import com.parser.commons.aion.properties.WorldProperties;

import com.parser.write.DataProcessor;

import com.parser.output.aion.world_maps.*;

public class AionWorldMapsWriter extends DataProcessor {

	WorldMaps worldMaps = new WorldMaps();
	Collection<Map> mapList = worldMaps.getMap();
	
	Collection<WorldMap> clientMaps;
	FastMap<String, LevelData> levelData;
	FastMap<String, Zonemap> zoneMaps;
	
	@Override
	public void collect() {
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
			
			if (data == null) {
				System.out.println("[MAPS] Null Level Data for map : " + cm.getValue());
				continue;
			}
			
			LevelInfo infos = data.getLevelInfo();
			LevelOption options = (data.getMissions() != null && data.getMissions().getMission() != null) ? data.getMissions().getMission().getLevelOption() : null;			
			Zonemap zoneMap = zoneMaps.get(cm.getValue());
			
			if (infos == null || options == null) {
				System.out.println("[MAPS] Null Level Options/Infos for map : " + cm.getValue());
				continue;
			}
			
			m.setId(cm.getId());
			
			m.setName(aion.getStringText("STR_ZONE_NAME_" + cm.getValue()));
			
			if (options.getFly().getFlyWholeLevel() != 0)
				m.setDeathLevel(options.getFly().getMaxHeight());
				
			m.setWaterLevel((int) Math.round(infos.getWaterLevel()));
			
			String worldType = getWorldType(zoneMap.getMapCategory());
			if (worldType != null)
				m.setWorldType(worldType);
			m.setWorldSize(infos.getHeightmapXSize() + infos.getHeightmapYSize());
			
			m.setFlags(getFlags(cm, options));
			
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
	public void create() {
		addOrder(WorldProperties.OUTPUT_WORLD_MAPS, WorldProperties.OUTPUT_WORLD_MAPS_BINDINGS, worldMaps);
		write(orders);
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
	
	private int getFlags(WorldMap cm, LevelOption options) {
		int flags = 0;
		if (!options.getBindArea().getIsPossible().equalsIgnoreCase("0") || !options.getBindArea().getIsPossible().equalsIgnoreCase("false")) //0
			flags |= 1;
		if (!options.getReCall().getIsPossible().equalsIgnoreCase("0") || !options.getReCall().getIsPossible().equalsIgnoreCase("false")) //1
			flags |= 2;
		if (!options.getGlide().getIsPossible().equalsIgnoreCase("0") || !options.getGlide().getIsPossible().equalsIgnoreCase("false")) //1
			flags |= 4;
		if (options.getFly().getFlyWholeLevel() > 0) //0
			flags |= 8;
		if (options.getRide().getRideWholeLevel() == 1) //1
			flags |= 16;
		if (options.getRide().getRideWholeLevel() == 2) //1
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
