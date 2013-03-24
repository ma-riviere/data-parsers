package com.parser.read.aion.cooltimes;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltimes;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;


public class AionCooltimesParser extends AbstractFileParser<ClientInstanceCooltime> {

	public AionCooltimesParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.COOLTIMES_PACK, AionReadingConfig.COOLTIMES);
	}

	@Override
	protected List<ClientInstanceCooltime> castFrom(Object topNode) {
		return ((ClientInstanceCooltimes) topNode).getClientInstanceCooltime();
	}

}
