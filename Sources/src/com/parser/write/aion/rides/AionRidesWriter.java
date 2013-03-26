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

	Rides finalTemplates = new Rides();
	Collection<RideInfo> templateList = finalTemplates.getRideInfo();
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
					System.out.println("Sprint speed > 0 but can not sprint; id=" + rideData.getId());
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
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.RIDE, AionWritingConfig.RIDE_PACK);
		System.out.println("[RIDES] Rides count: " + templateList.size());
	}

}
