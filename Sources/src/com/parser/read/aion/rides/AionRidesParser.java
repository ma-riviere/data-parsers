package com.parser.read.aion.rides;

import java.util.List;

import com.parser.input.aion.rides.ClientRide;
import com.parser.input.aion.rides.ClientRides;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionRidesParser extends XMLParser<ClientRides> {

	public AionRidesParser() {super(AionReadingConfig.RIDE_BINDINGS);}
	
	public List<ClientRide> parse() {
		return parseFile(AionReadingConfig.RIDE).getClientRideData();
	}
}
