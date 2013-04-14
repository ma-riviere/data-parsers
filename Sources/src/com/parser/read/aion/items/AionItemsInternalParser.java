package com.parser.read.aion.items;

import java.util.List;

import com.parser.output.aion.item_name.Item;
import com.parser.output.aion.item_name.Items;
import com.parser.read.AbstractFileParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsInternalParser extends AbstractFileParser<Item> {

	public AionItemsInternalParser() {
			super(AionReadingConfig.ITEMS_INTERNAL_BINDINGS, AionReadingConfig.ITEMS_INTERNAL);
	}

	@Override
	protected List<Item> castFrom(Object topNode) {
		return ((Items) topNode).getItem();
	}
}
