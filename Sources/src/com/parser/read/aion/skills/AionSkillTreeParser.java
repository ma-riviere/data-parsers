package com.parser.read.aion.skills;

import java.util.List;

import com.parser.input.aion.skill_learn.ClientSkillTree;
import com.parser.input.aion.skill_learn.ClientSkillTrees;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionSkillTreeParser extends XMLParser<ClientSkillTrees> {

	public AionSkillTreeParser() {super(AionReadingConfig.SKILL_TREE_BINDINGS);}
	
	public List<ClientSkillTree> parse() {
		ClientSkillTrees root = parseFile(AionReadingConfig.SKILL_TREE);
		return root.getClientSkillLearn();
	}
}
