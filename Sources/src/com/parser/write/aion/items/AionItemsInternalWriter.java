package com.parser.write.aion.items;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.parser.input.aion.items.ClientItem;

import com.parser.read.aion.items.AionItemsParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.input.aion.p_items.Item;
import com.parser.input.aion.p_items.Items;

/**
 * Writing a custom Item NAME/ID XML (Internal use)
 */
public class AionItemsInternalWriter extends AbstractWriter {

	Items finalTemplates = new Items();
	Collection<Item> templateList = finalTemplates.getItem();
	Map<String, List<ClientItem>> clientItemData;
	
	@Override
	public void parse() {clientItemData = new AionItemsParser().parse();}

	@Override
	public void transform() {
		for (List<ClientItem> lci : clientItemData.values()) {
			for (ClientItem ci : lci) {
				Item it = new Item();
				it.setId((Integer) ci.getId());
				it.setName(ci.getName() != null ? ci.getName().toUpperCase() : "");
				templateList.add(it);
			}
		}
	}

	@Override
	public void marshall() {
		System.out.println("[ITEMS] Item Name/ID pairs count: " + templateList.size());
		FileMarhshaller.marshallFile(finalTemplates, AionWritingConfig.ITEMS_NAME_ID, AionWritingConfig.ITEMS_NAME_ID_PACK);
	}
}