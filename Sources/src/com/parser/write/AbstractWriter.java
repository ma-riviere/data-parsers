package com.parser.write;

import java.util.ArrayList;
import java.util.List;

import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;
import com.parser.commons.aion.properties.AionProperties;

public abstract class AbstractWriter implements Writer {

	protected Logger log = new Logger().getInstance();
	
	protected List<MarshallerData> orders = new ArrayList<MarshallerData>();

	@Override
	public void start() {
		Util.printSection("Reading input data");
		parse();
		Util.printSection("Computing output data");
		transform();
		Util.printSection("Analyzing output content");
		finalise();
		Util.printSection("Writting output file");
		marshall();
		orders.clear();
		Util.printSection("FINISHED");
	}

	protected void finalise() {}
	
	protected void addAionOrder(String file, String bindings, Object template) {
		orders.add(new MarshallerData(AionProperties.OUTPUT_PATH + file, bindings, template));
	}

}
