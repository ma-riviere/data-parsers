package com.parser.write;

import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;

import com.parser.commons.utils.Logger;
import com.parser.commons.utils.Util;

import com.parser.commons.aion.AionDataHub;

public abstract class DataProcessor extends Writer implements IProcessor {

	protected Logger log = new Logger().getInstance();
	protected List<MarshalOrder> orders = new ArrayList<MarshalOrder>();
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
	
	//TODO: move declaration to IProecssor to make compulsory
	protected void addOrder(String file, String extension, String bindings, Object template, FastMap<Object, String> comments) {
		orders.add(new MarshalOrder(file, extension, bindings, template, comments));
	}
	
	protected void addOrder(String file, String bindings, Object template, FastMap<Object, String> comments) {
		orders.add(new MarshalOrder(file, ".xml", bindings, template, comments));
	}
	
	protected void addOrder(String file, String bindings, Object template) {
		orders.add(new MarshalOrder(file, ".xml", bindings, template, null));
	}
	
	protected void addComment(Object toMarshall, String linkedComment) {
		comments.put(toMarshall, " " + linkedComment + " ");
	}
}
