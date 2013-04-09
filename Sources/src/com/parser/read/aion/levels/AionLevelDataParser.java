package com.parser.read.aion.levels;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import com.parser.input.aion.level_data.LevelData;
import com.parser.input.aion.level_data.LevelInfo;

import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionLevelDataParser extends AbstractDirectoryParser<LevelData, LevelInfo> {

	public AionLevelDataParser() {
		super(AionReadingConfig.LEVEL_DATA_BINDINGS, AionReadingConfig.MISSION0, "leveldata");
	}
	
	public LevelData parseFileRoot(String name) {
		File toMarshall = null;
		
		if (name.contains("@")) {
			String[] names = name.split("@");
			Path path = Paths.get(directory + prefix + names[0] + extension);
			try {toMarshall = path.toFile();} catch (UnsupportedOperationException uoe) {System.out.println("\n[MAIN] [ERROR] Could not find file : " + names[0] + "/" + prefix + names[1] + extension);}
		}
		else {
			Path path = Paths.get(directory + prefix + name + extension);
			try {toMarshall = path.toFile();} catch (UnsupportedOperationException uoe) {System.out.println("\n[MAIN] [ERROR] Could not find file : " + prefix + name + extension);}
		}
		
		if (toMarshall == null)
			return null;
		
		return super.parseFileRoot(toMarshall);
	}
	
	@Override
	protected List<LevelInfo> cast(Object topNode) {
		List<LevelInfo> levelInfo = new ArrayList<LevelInfo>();
		levelInfo.add(((LevelData) topNode).getLevelInfo());
		return levelInfo;
	}
}
