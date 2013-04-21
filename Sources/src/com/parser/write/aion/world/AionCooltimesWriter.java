package com.parser.write.aion.world;

import java.util.Collection;
import javolution.util.FastMap;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;

import com.parser.commons.aion.enums.Race;
import com.parser.commons.aion.properties.WorldProperties;

import com.parser.write.DataProcessor;

import com.parser.output.aion.cooltimes.InstanceCooltime;
import com.parser.output.aion.cooltimes.InstanceCooltimes;

public class AionCooltimesWriter extends DataProcessor {

	InstanceCooltimes cooltimes = new InstanceCooltimes();
	Collection<InstanceCooltime> cooltimeList = cooltimes.getInstanceCooltime();
	
	FastMap<ClientInstanceCooltime, ClientInstanceCooltime2> ccs;
	
	@Override
	public void collect() {
		ccs = new FastMap<ClientInstanceCooltime, ClientInstanceCooltime2>(aion.getCooltimes());
	}

	@Override
	public void transform() {
		for (ClientInstanceCooltime cic : ccs.keySet()) {
			InstanceCooltime ict = new InstanceCooltime();
			int mapId = aion.getWorldId(cic.getName());
			
			if (mapId != 0) {
				ict.setRace(Race.fromClient(cic.getRace()).toString());
				ict.setWorldId(mapId);
				ict.setId((int) cic.getId());
				ict.setEntCoolTime((ccs.get(cic) != null) ? ccs.get(cic).getValue() : 0);
				ict.setMaxMemberLight((int) cic.getMaxMemberLight());
				ict.setMaxMemberDark((int) cic.getMaxMemberDark());
				if (cic.getEnterMinLevelLight() != null) {ict.setEnterMinLevelLight(cic.getEnterMinLevelLight());}
				if (cic.getEnterMinLevelDark() != null) {ict.setEnterMinLevelDark(cic.getEnterMinLevelDark());}
				ict.setCanEnterMentor(cic.getCanEnterMentor().toLowerCase());
			}
			else
				System.out.println("[COOLTIMES] No WorldID could be found for Cooltime : " + cic.getId() + ", Ignored.");
			
			cooltimeList.add(ict);
		}
	}

	@Override
	public void create() {
		addOrder(WorldProperties.OUTPUT_COOLTIMES, WorldProperties.OUTPUT_COOLTIMES_BINDINGS, cooltimes);
		write(orders);
		System.out.println("\n[COOLTIMES] Cooltimes count : " + cooltimeList.size());
	}
}
