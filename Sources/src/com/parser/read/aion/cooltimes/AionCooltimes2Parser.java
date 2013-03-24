package com.parser.read.aion.cooltimes;

import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2S;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;


public class AionCooltimes2Parser extends AbstractFileParser<ClientInstanceCooltime2> {

	public AionCooltimes2Parser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.COOLTIMES_PACK, AionReadingConfig.COOLTIMES2);
	}

	@Override
	protected List<ClientInstanceCooltime2> castFrom(Object topNode) {
		return ((ClientInstanceCooltime2S) topNode).getClientInstanceCooltime2();
	}

}
