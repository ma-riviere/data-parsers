package com.parser.write.aion.cooltimes;

import java.util.Collection;
import java.util.List;

import com.parser.input.aion.cooltimes.ClientInstanceCooltime;
import com.parser.input.aion.cooltimes.ClientInstanceCooltime2;
import com.parser.common.aion.AionDataCenter;
import com.parser.read.aion.cooltimes.AionCooltimesParser;
import com.parser.read.aion.cooltimes.AionCooltimes2Parser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;
import com.parser.output.aion.cooltimes.InstanceCooltime;
import com.parser.output.aion.cooltimes.InstanceCooltimes;

public class AionCooltimesWriter extends AbstractWriter {

	InstanceCooltimes finalTemplates = new InstanceCooltimes();
	Collection<InstanceCooltime> templateList = finalTemplates.getInstanceCooltime();
	
	List<ClientInstanceCooltime> clientInstanceCooltimeList;
	List<ClientInstanceCooltime2> clientInstanceCooltimeList2;
	
	@Override
	public void parse() {
		clientInstanceCooltimeList = new AionCooltimesParser().parse();
		clientInstanceCooltimeList2 = new AionCooltimes2Parser().parse();
	}

	@Override
	public void transform() {
		for (ClientInstanceCooltime cic : clientInstanceCooltimeList) {
			InstanceCooltime ict = new InstanceCooltime();
			int world_id = new AionDataCenter().getInstance().getWorldIdByName(cic.getName());
			
			if (world_id != 0) {
				ict.setRace(adjustRace(cic.getRace()));
				ict.setWorldId(world_id);
				ict.setId((int) cic.getId());
				ict.setEntCoolTime(getMatchingCIC2Value((int) cic.getId()));
				ict.setMaxMemberLight((int) cic.getMaxMemberLight());
				ict.setMaxMemberDark((int) cic.getMaxMemberDark());
				if (cic.getEnterMinLevelLight() != null) {ict.setEnterMinLevelLight(cic.getEnterMinLevelLight());}
				if (cic.getEnterMinLevelDark() != null) {ict.setEnterMinLevelDark(cic.getEnterMinLevelDark());}
				ict.setCanEnterMentor(adjustMentor(cic.getCanEnterMentor()));
			} else {
				System.out.println("No WorldID could be found for InstanceCooldown : " + cic.getId() + ", Ignored.");
			}			
			templateList.add(ict);
		}
	}

	@Override
	public void marshall() {
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.COOLTIMES, AionWritingConfig.COOLTIMES_BINDINGS);
		System.out.println("Instance Cooltimes count: " + templateList.size());
	}
	
	private int getMatchingCIC2Value(int id) {
		for (ClientInstanceCooltime2 cic2 : clientInstanceCooltimeList2) {
			if (cic2.getId() == id)
				return cic2.getValue();
		}
		System.out.println("[WARNING] Value 0 given by default as IC");
		return 0;
	}
	
	private String adjustRace(String race) {
		if (race.equals("both")) {
			return "PC_ALL";
		} else if (race.equals("light")) {
			return "ELYOS";
		} else
			return "ASMODIANS";
	}
	
	private boolean adjustMentor(String mentor) {
		if (mentor.equals("TRUE")) {
			return true;
		} else
			return false;
	}
}
