package com.parser.read.aion.items;

import java.util.List;

import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.items.ClientItems;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsParser extends AbstractDirectoryParser<ClientItems, ClientItem> {

	public AionItemsParser() {
		super(AionReadingConfig.ITEMS_BINDINGS, AionReadingConfig.ITEMS, AionReadingConfig.ITEMS_PREFIX);
	}
	
	public AionItemsParser(String bindings, String folder, String prefix) {
		super(bindings, folder, prefix);
	}

	@Override
	protected List<ClientItem> cast(Object topNode) {
		return ((ClientItems) topNode).getClientItem();
	}
}
