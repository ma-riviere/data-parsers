package com.parser.read.aion.items;

import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;

import com.parser.input.aion.items.ClientItem;
import com.parser.input.aion.items.ClientItems;

import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsParser extends XMLParser<ClientItems> {

	public AionItemsParser() {
		super(AionReadingConfig.ITEMS, AionReadingConfig.ITEMS_PREFIX, AionReadingConfig.ITEMS_BINDINGS);
	}
	
	public AionItemsParser(String bindings, String folder, String prefix) {
		super(folder, prefix, bindings);
	}
	
	public List<ClientItem> parse() {
		List<ClientItem> results = new ArrayList<ClientItem>();
		for (ClientItems items : parseDir().values())
			results.addAll(items.getClientItem());
		return results;
	}
}
