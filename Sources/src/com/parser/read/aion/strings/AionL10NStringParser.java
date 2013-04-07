package com.parser.read.aion.strings;

import java.util.List;

import com.parser.input.aion.strings.ClientString;
import com.parser.input.aion.strings.ClientStrings;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionL10NStringParser extends AbstractDirectoryParser<ClientStrings, ClientString> {

	public AionL10NStringParser() {
		super(AionReadingConfig.STRINGS_BINDINGS, AionReadingConfig.STRINGS_L10N, AionReadingConfig.STRINGS_PREFIX);
	}

	@Override
	protected List<ClientString> cast(Object topNode) {
		return ((ClientStrings) topNode).getString();
	}
}
