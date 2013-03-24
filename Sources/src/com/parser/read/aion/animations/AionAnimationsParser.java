package com.parser.read.aion.animations;

import java.util.List;

import com.parser.input.aion.animations.ClientAnimation;
import com.parser.input.aion.animations.ClientAnimations;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionAnimationsParser extends AbstractFileParser<ClientAnimation> {

	public AionAnimationsParser() {
			super(AionReadingConfig.VERSION, AionReadingConfig.ANIMATIONS_PACK, AionReadingConfig.ANIMATIONS);
	}

	@Override
	protected List<ClientAnimation> castFrom(Object topNode) {
		return ((ClientAnimations) topNode).getCustomAnimation();
	}
}
