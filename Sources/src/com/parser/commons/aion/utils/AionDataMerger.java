package com.parser.commons.aion;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.input.aion.items.ClientItem;

public class AionDataMerger {

	public static final AionDataMerger getInstance() {
		return SingletonHolder.instance;
	}
	
	public AionDataMerger() {}

	public List<ClientItem> mergeItemData(Map<String, List<ClientItem>> primary, Map<String, List<ClientItem>> secondary) {
		List<ClientItem> results = new ArrayList<ClientItem>();
		List<Integer> pl = new ArrayList<Integer>();
		List<Integer> sl = new ArrayList<Integer>();
		List<Integer> colliding = new ArrayList<Integer>();
		int last = -1; int first = -1;
		
		if (primary.isEmpty()) {
			System.out.println("[MERGER] Primary data is empty ...");
			return null;
		} else {
			for (List<ClientItem> lci : primary.values()) {
				for (ClientItem ci : lci) {
					pl.add(ci.getId());
				}
			}
			Collections.sort(pl);
			last = pl.get(pl.size() - 1);
		}
		
		if (secondary.isEmpty())
			System.out.println("[MERGER] Secondary data is empty ...");
		else {
			for (List<ClientItem> lci : secondary.values()) {
				for (ClientItem ci : lci) {
					sl.add(ci.getId());
				}
			}
			Collections.sort(sl);
			first = sl.get(0);
		}
		
		if (first != -1 && last != -1) {
			// Get the colliding ID list
			colliding = getCollidingIds(pl, sl);
			System.out.println("[MERGER] Info : " + colliding.size() + " items colliding.");

			for (List<ClientItem> lci : primary.values()) {
				for (ClientItem ci : lci) {
					results.add(ci);
				}
			}
			int i = 0;
			for (List<ClientItem> lci : secondary.values()) {
				for (ClientItem ci : lci) {
					ci.setId(last +1 + i);
					results.add(ci);
					i++;
				}
			}
		}
	
		return results;
	}
	
	private List<Integer> getCollidingIds(List<Integer> pl, List<Integer> sl) {
		List<Integer> colliding = new ArrayList<Integer>();
		for (Integer i : pl) {
			for (Integer j : sl) {
				if (i == j)
					colliding.add(i);
			}
		}
		System.out.println("[MERGER] Number of IDs colliding : " + colliding.size());
		return colliding;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder 	{
		protected static final AionDataMerger instance = new AionDataMerger();
	}
}
