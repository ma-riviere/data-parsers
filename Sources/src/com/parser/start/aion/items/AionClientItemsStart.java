package com.parser.start.aion.items;

import com.parser.write.aion.items.AionClientItemsWriter;

public class AionClientItemsStart {

	public static void main(String[] args) {
		AionClientItemsWriter writer = new AionClientItemsWriter();
		writer.start();
	}
}