package com.parser.write;

import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;

import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

import com.parser.commons.aion.AionDataHub;

public abstract class AbstractWriter implements Writer {

	protected Logger log = new Logger().getInstance();
	protected List<MarshallerData> orders = new ArrayList<MarshallerData>();
	protected FastMap<Object, String> comments = new FastMap<Object, String>();
	
	// Data Centers
	protected AionDataHub aion = new AionDataHub().getInstance();
	// protected TeraDataHub tera = new TeraDataHub().getInstance();
	// protected ArcheageDataHub arch = new ArcheageDataHub().getInstance();

	@Override
	public void start() {
		Util.printSection("Collecting input data");
		collect();
		Util.printSection("Computing output data");
		transform();
		Util.printSection("Analyzing output content");
		finalise();
		Util.printSection("Creating output files");
		create();
		orders.clear();
		comments.clear();
		Util.printSection("FINISHED");
	}

	protected void finalise() {}
	
	protected void addOrder(String file, String bindings, Object template, FastMap<Object, String> comments) {
		orders.add(new MarshallerData(file, bindings, template, comments));
	}
	
	protected void addOrder(String file, String bindings, Object template) {
		orders.add(new MarshallerData(file, bindings, template, null));
	}
	
	protected void addComment(Object toMarshall, String linkedComment) {
		comments.put(toMarshall, " " + linkedComment + " ");
	}
}
