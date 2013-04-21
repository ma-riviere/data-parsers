package com.parser.write.aion.rides;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.on;

import com.parser.input.aion.rides.ClientRide;
import com.parser.read.aion.rides.AionRidesParser;

import com.parser.write.DataProcessor;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.rides.*;

public class AionRidesWriter extends DataProcessor {

	Rides rides = new Rides();
	Collection<RideInfo> rideList = rides.getRideInfo();
	Collection<ClientRide> clientRideList;
	
	@Override
	public void collect() {
		clientRideList = sort(aion.getRides().values(), on(ClientRide.class).getId());
	}

	@Override
	public void transform() {
		for (ClientRide ride : clientRideList) {
			RideInfo info = new RideInfo();
			info.setId((int)ride.getId());
			info.setType(ride.getRideType());
			info.setMoveSpeed(ride.getMoveSpeed());
			if (ride.getFlySpeed() != 0)
				info.setFlySpeed(ride.getFlySpeed());
			if (ride.getSprintSpeed() != 0) {
				if (ride.getCanSprint() == 0)
					log.info("[RIDE] " + ride.getId() + " has sprint speed > 0 but can't sprint");
				else
					info.setSprintSpeed(ride.getSprintSpeed());
			}
			info.setStartFp(ride.getStartFp());
			info.setCostFp(ride.getCostFp());
			if (ride.getBoundRadius() != null) {
				Bounds radius = new Bounds();
				radius.setFront(ride.getBoundRadius().getFront());
				radius.setSide(ride.getBoundRadius().getSide());
				radius.setUpper(ride.getBoundRadius().getUpper());
				radius.setAltitude(ride.getAltitude());
				info.setBounds(radius);
			}
			rideList.add(info);
		}
	}

	@Override
	public void create() {
		addOrder(AionWritingConfig.RIDE, AionWritingConfig.RIDE_BINDINGS, rides, null);
		addOrder(AionWritingConfig.RIDE, ".dat", AionWritingConfig.RIDE_BINDINGS, rides, null);
		log.info("\n[RIDES] Rides count: ", rideList.size());
	}
}
