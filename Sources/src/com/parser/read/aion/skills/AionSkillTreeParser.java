package com.parser.read.aion.skills;

import java.util.List;

import com.parser.input.aion.skill_learn.ClientSkillTree;
import com.parser.input.aion.skill_learn.ClientSkillTrees;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionSkillTreeParser extends AbstractFileParser<ClientSkillTree> {

	public AionSkillTreeParser() {
		super(AionReadingConfig.VERSION, AionReadingConfig.SKILL_TREE_BINDINGS, AionReadingConfig.SKILL_TREE);
	}

	@Override
	protected List<ClientSkillTree> castFrom(Object topNode) {
		return ((ClientSkillTrees) topNode).getClientSkillLearn();
	}

}
