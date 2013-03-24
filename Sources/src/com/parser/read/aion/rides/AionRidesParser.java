package com.parser.read.aion.rides;

import java.util.List;

import com.parser.input.aion.rides.ClientRide;
import com.parser.input.aion.rides.ClientRides;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionRidesParser extends AbstractFileParser<ClientRide> {

	public AionRidesParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.RIDE_PACK, AionReadingConfig.RIDE);
	}

	@Override
	protected List<ClientRide> castFrom(Object topNode) {
		return ((ClientRides) topNode).getClientRideData();
	}

}
