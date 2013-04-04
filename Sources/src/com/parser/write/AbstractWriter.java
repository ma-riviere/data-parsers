package com.parser.write;

import java.util.ArrayList;
import java.util.List;

import com.parser.common.*;

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
	
	protected void addOrder(String bindings, String file, Object template) {
		orders.add(new MarshallerData(bindings, file, template));
	}

}
