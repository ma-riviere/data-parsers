package com.parser.read.aion.strings;

import java.util.List;

import com.parser.input.aion.strings.ClientString;
import com.parser.input.aion.strings.ClientStrings;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionDataStringParser extends AbstractDirectoryParser<ClientString> {

	public AionDataStringParser() {
			super(AionReadingConfig.VERSION, AionReadingConfig.STRINGS_BINDINGS, AionReadingConfig.STRINGS_DATA, AionReadingConfig.STRINGS_PREFIX);
	}

	@Override
	protected List<ClientString> castFrom(Object topNode) {
		return ((ClientStrings) topNode).getString();
	}
	
	@Override
	protected String mapFileName(String fileName) {
		return fileName.replaceAll("client_world_", "").replaceAll(".xml", "");
	}
}
