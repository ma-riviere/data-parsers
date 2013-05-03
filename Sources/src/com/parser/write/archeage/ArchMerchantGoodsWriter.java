package com.parser.write.archeage;

import com.parser.write.DataProcessor;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.on;

// import com.parser.commons.archeage.properties.ArchProperties; //TODO
// import com.parser.output.archeage.merchants.*; //TODO

public class ArchMerchantGoodsWriter extends DataProcessor {

	// Merchants merchants = new Merchants();
	// Collection<Merchant> mList = Merchants.getMerchant();
	// ResultSet mSet;
		
	@Override
	public void collect() {
		// mSet = arch.getMerchants(ArchProperties.HOST, ArchProperties.USER, ArchProperties.PASS, "SELECT * FROM merchant_goods;"); //TODO
	}

	@Override
	public void transform() {
		// while (mSet.next()) {
			// Merchant mer = new Merchant();
			// mer.setId();
		// }
		// mList = sort(mList, on(Merchant.class).getId());
	}
	
	@Override
	public void create() {
		// addOrder(WorldProperties.OUTPUT_SPHERE, WorldProperties.SPHERE_OUTPUT_BINDINGS, spheres);
		// System.out.println("\n[SPHERE] source_sphere count: " + sList.size());
	}
}
