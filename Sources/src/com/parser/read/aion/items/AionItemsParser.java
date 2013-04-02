package com.parser.read.aion.items;

import java.util.List;

import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.items.ClientItems;
import com.parser.read.AbstractDirectoryParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsParser extends AbstractDirectoryParser<ClientItem> {

	public AionItemsParser() {
			super(AionReadingConfig.VERSION, AionReadingConfig.ITEMS_BINDINGS, AionReadingConfig.ITEMS, AionReadingConfig.ITEMS_PREFIX);
	}
	
	public AionItemsParser(String version, String pack, String folder, String prefix) {
			super(version, pack, folder, prefix);
	}

	@Override
	protected List<ClientItem> castFrom(Object topNode) {
		return ((ClientItems) topNode).getClientItem();
	}
	
	@Override
	protected String mapFileName(String fileName) {
		return fileName.replaceAll("client_items_", "").replaceAll(".xml", "");
	}
}
