package com.parser.read.aion.skills;

import java.util.List;

import com.parser.input.aion.skills.ClientSkill;
import com.parser.input.aion.skills.ClientSkills;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionSkillsParser extends AbstractFileParser<ClientSkill> {

	public AionSkillsParser() {
		super(AionReadingConfig.SKILLS_BINDINGS, AionReadingConfig.SKILLS);
	}

	@Override
	protected List<ClientSkill> castFrom(Object topNode) {
		return ((ClientSkills) topNode).getSkillBaseClient();
	}

}
