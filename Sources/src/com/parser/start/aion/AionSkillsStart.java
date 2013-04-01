package com.parser.start.aion;

import com.parser.write.aion.skills.AionSkillsWriter;

public class AionSkillsStart {

	public static void main(String[] args) {
		AionSkillsWriter writer = new AionSkillsWriter(Boolean.parseBoolean(args[0]));
		writer.start();
	}
}