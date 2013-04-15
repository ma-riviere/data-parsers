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
	
	// Used by ClienItemsWriter (TODO: Remove)
	public AionItemsParser(String folder, String prefix, String bindings) {
		super(folder, prefix, bindings);
	}
	
	public List<ClientItem> parse() {
		List<ClientItem> elements = new ArrayList<ClientItem>();
		for (ClientItems roots : parseDir().values())
			elements.addAll(roots.getClientItem());
		return elements;
	}
}
