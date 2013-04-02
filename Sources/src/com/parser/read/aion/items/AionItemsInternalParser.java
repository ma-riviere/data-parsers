package com.parser.read.aion.items;

import java.util.List;

import com.parser.input.aion.p_items.Item;
import com.parser.input.aion.p_items.Items;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsInternalParser extends AbstractFileParser<Item> {

	public AionItemsInternalParser() {
			super(AionReadingConfig.VERSION, AionReadingConfig.ITEMS_INTERNAL_BINDINGS, AionReadingConfig.ITEMS_INTERNAL);
	}

	@Override
	protected List<Item> castFrom(Object topNode) {
		return ((Items) topNode).getItem();
	}
}
