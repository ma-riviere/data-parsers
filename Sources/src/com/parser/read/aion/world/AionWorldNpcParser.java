package com.parser.read.aion.world;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import com.parser.input.aion.world_data.Clientzones;
import com.parser.input.aion.world_data.NpcInfos;

import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionWorldNpcParser extends AbstractDirectoryParser<Clientzones, NpcInfos> {

	public AionWorldNpcParser() {
		super(AionReadingConfig.WORLD_DATA_BINDINGS, AionReadingConfig.WORLD_DATA, AionReadingConfig.WORLD_DATA_PREFIX);
	}
	
	public List<NpcInfos> parseFile(String name) {
		File toMarshall = null;
		
		if (name.contains("@")) {
			String[] names = name.split("@");
			Path path = Paths.get(directory + prefix + names[0] + extension);
			try {toMarshall = path.toFile();} catch (UnsupportedOperationException uoe) {System.out.println("\n[MAIN][ERROR] Could not find file : " + names[0] + "/" + prefix + names[1] + extension);}
		}
		else {
			Path path = Paths.get(directory + prefix + name + extension);
			try {toMarshall = path.toFile();} catch (UnsupportedOperationException uoe) {System.out.println("\n[MAIN][ERROR] Could not find file : " + prefix + name + extension);}
		}
		
		if (toMarshall == null)
			return null;
		
		return super.parseFile(toMarshall);
	}

	@Override
	protected List<NpcInfos> cast(Object topNode) {
		List<NpcInfos> list = new ArrayList<NpcInfos>();
		list.add(((Clientzones) topNode).getNpcInfos());
		return list;
	}
}
