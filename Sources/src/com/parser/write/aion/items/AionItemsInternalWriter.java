package com.parser.write.aion.items;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.parser.input.aion.items.ClientItem;

import com.parser.read.aion.items.AionItemsParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.item_name.Item;
import com.parser.output.aion.item_name.Items;

/**
 * Writing a custom Item NAME/ID XML (Internal use)
 */
public class AionItemsInternalWriter extends AbstractWriter {

	Items finalTemplates = new Items();
	Collection<Item> templateList = finalTemplates.getItem();
	List<ClientItem> clientItems;
	
	@Override
	public void parse() {clientItems = new AionItemsParser().parse();}

	@Override
	public void transform() {
		for (ClientItem ci : clientItems) {
			Item it = new Item();
			it.setId((Integer) ci.getId());
			it.setName(ci.getName() != null ? ci.getName().toUpperCase() : "");
			templateList.add(it);
		}
	}

	@Override
	public void marshall() {
		addOrder(AionWritingConfig.ITEMS_NAME_ID, AionWritingConfig.ITEMS_NAME_ID_BINDINGS, finalTemplates);
		FileMarshaller.marshallFile(orders);
		System.out.println("[ITEMS] Item Name/ID pairs count: " + templateList.size());
	}
}