package com.parser.read.aion.animations;

import java.util.List;

import com.parser.input.aion.animations.ClientAnimation;
import com.parser.input.aion.animations.ClientAnimations;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionAnimationsParser extends XMLParser<ClientAnimations> {

	public AionAnimationsParser() {super(AionReadingConfig.ANIMATIONS_BINDINGS);}
	
	public List<ClientAnimation> parse() {
		return parseFile(AionReadingConfig.ANIMATIONS).getCustomAnimation();
	}
}
