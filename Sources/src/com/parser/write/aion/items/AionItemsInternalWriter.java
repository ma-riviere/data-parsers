package com.parser.write.aion.items;

import java.util.Collection;

import com.parser.input.aion.items.ClientItem;

import com.parser.write.AbstractWriter;
import com.parser.write.FileMarshaller;
import com.parser.write.aion.AionWritingConfig;

import com.parser.output.aion.item_name.Item;
import com.parser.output.aion.item_name.Items;

/**
 * Writing a custom Item NAME/ID XML
 */
public class AionItemsInternalWriter extends AbstractWriter {

	Items items2 = new Items();
	Collection<Item> itemList = items2.getItem();
	Collection<ClientItem> clientItems;
	
	@Override
	public void collect() {
		clientItems = aion.getItems().values();
	}

	@Override
	public void transform() {
		for (ClientItem ci : clientItems) {
			Item it = new Item();
			it.setId((Integer) ci.getId());
			it.setName(ci.getName() != null ? ci.getName().toUpperCase() : "NO_NAME");
			itemList.add(it);
		}
	}

	@Override
	public void create() {
		addOrder(AionWritingConfig.ITEMS_NAME_ID, AionWritingConfig.ITEMS_NAME_ID_BINDINGS, items2);
		FileMarshaller.marshallFile(orders);
		System.out.println("\n[ITEMS-2] Item Name/ID pairs count : " + itemList.size());
	}
}