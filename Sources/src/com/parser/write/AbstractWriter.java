package com.parser.write;

import java.util.ArrayList;
import java.util.List;

import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

import com.parser.commons.aion.AionDataHub;

public abstract class AbstractWriter implements Writer {

	protected Logger log = new Logger().getInstance();
	protected List<MarshallerData> orders = new ArrayList<MarshallerData>();
	
	// Data Centers
	protected AionDataHub aion = new AionDataHub().getInstance();
	// protected TeraDataHub tera = new TeraDataHub().getInstance();
	// protected ArcheageDataHub arch = new ArcheageDataHub().getInstance();

	@Override
	public void start() {
		Util.printSection("Reading input data");
		parse();
		Util.printSection("Computing output data");
		transform();
		Util.printSection("Analyzing output content");
		finalise();
		Util.printSection("Writting output files");
		marshall();
		orders.clear();
		Util.printSection("FINISHED");
	}

	protected void finalise() {}
	
	protected void addOrder(String file, String bindings, Object template) {
		orders.add(new MarshallerData(file, bindings, template));
	}

}
