package com.parser.read.aion.skills;

import java.util.List;

import com.parser.input.aion.skills.ClientSkill;
import com.parser.input.aion.skills.ClientSkills;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionSkillsParser extends XMLParser<ClientSkills> {

	public AionSkillsParser() {super(AionReadingConfig.SKILLS_BINDINGS);}
	
	public List<ClientSkill> parse() {
		ClientSkills root = parseFile(AionReadingConfig.SKILLS);
		return root.getSkillBaseClient();
	}
}
