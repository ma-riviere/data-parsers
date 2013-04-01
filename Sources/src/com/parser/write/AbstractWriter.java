package com.parser.write;

import com.parser.common.Util;

public abstract class AbstractWriter implements Writer {

	@Override
	public void start() {
		Util.printSection("Reading input data");
		parse();
		Util.printSection("Computing output data");
		transform();
		Util.printSection("Analyzing output content");
		analyze();
		Util.printSection("Writting output file");
		marshall();
		Util.printSection("FINISHED");
	}

	protected void analyze() {}

}
