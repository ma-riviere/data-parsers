package com.parser.write.aion.rides;

import java.util.Collection;
import java.util.List;

import com.parser.input.aion.rides.ClientRide;
import com.parser.read.aion.rides.AionRidesParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;
import com.parser.output.aion.rides.Bounds;
import com.parser.output.aion.rides.RideInfo;
import com.parser.output.aion.rides.Rides;

public class AionRidesWriter extends AbstractWriter {

	Rides rides = new Rides();
	Collection<RideInfo> templateList = rides.getRideInfo();
	List<ClientRide> clientRideList;
	
	@Override
	public void parse() {
		clientRideList = new AionRidesParser().parse();
	}

	@Override
	public void transform() {
		for (ClientRide rideData : clientRideList) {
			RideInfo info = new RideInfo();
			info.setId((int)rideData.getId());
			info.setType(rideData.getRideType());
			info.setMoveSpeed(rideData.getMoveSpeed());
			if (rideData.getFlySpeed() != 0)
				info.setFlySpeed(rideData.getFlySpeed());
			if (rideData.getSprintSpeed() != 0) {
				if (rideData.getCanSprint() == 0)
					System.out.println("[RIDE] " + rideData.getId() + " has sprint speed > 0 but can't sprint");
				else
					info.setSprintSpeed(rideData.getSprintSpeed());
			}
			info.setStartFp(rideData.getStartFp());
			info.setCostFp(rideData.getCostFp());
			if (rideData.getBoundRadius() != null) {
				Bounds radius = new Bounds();
				radius.setFront(rideData.getBoundRadius().getFront());
				radius.setSide(rideData.getBoundRadius().getSide());
				radius.setUpper(rideData.getBoundRadius().getUpper());
				radius.setAltitude(rideData.getAltitude());
				info.setBounds(radius);
			}
			templateList.add(info);
		}
	}

	@Override
	public void marshall() {
		addAionOrder(AionWritingConfig.RIDE, AionWritingConfig.RIDE_BINDINGS, rides);
		FileMarhshaller.marshallFile(orders);
		System.out.println("\n[RIDES] Rides count: " + templateList.size());
	}

}
