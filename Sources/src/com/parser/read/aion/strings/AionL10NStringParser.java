package com.parser.read.aion.strings;

import java.util.ArrayList;
import java.util.List;

import com.parser.input.aion.strings.ClientString;
import com.parser.input.aion.strings.ClientStrings;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionL10NStringParser extends XMLParser<ClientStrings> {

	public AionL10NStringParser() {
		super(AionReadingConfig.STRINGS_L10N, AionReadingConfig.STRINGS_PREFIX, AionReadingConfig.STRINGS_BINDINGS);
	}

	public List<ClientString> parse() {
		List<ClientString> elements = new ArrayList<ClientString>();
		for (ClientStrings roots : parseDir().values())
			elements.addAll(roots.getString());
		return elements;
	}
}
