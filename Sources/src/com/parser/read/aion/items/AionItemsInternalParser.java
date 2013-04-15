package com.parser.read.aion.items;

import java.util.List;

import com.parser.output.aion.item_name.Item;
import com.parser.output.aion.item_name.Items;
import com.parser.read.XMLParser;
import com.parser.read.aion.AionReadingConfig;

public class AionItemsInternalParser extends XMLParser<Items> {

	public AionItemsInternalParser() {super(AionReadingConfig.ITEMS_INTERNAL_BINDINGS);} 
	
	public List<Item> parse() {
		Items root = parseFile(AionReadingConfig.ITEMS_INTERNAL);
		return root.getItem();
	}	
}
