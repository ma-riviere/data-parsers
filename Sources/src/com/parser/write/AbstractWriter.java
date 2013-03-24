package com.parser.write;

public abstract class AbstractWriter implements Writer {

	@Override
	public void build() {
		System.out.println("\n### Parsing client data ... ###\n");
		parse();
		System.out.println("\n### Transforming data ...###\n");
		transform();
		System.out.println("\n### Analyzing final content ... ###\n");
		analyze();
		System.out.println("\n### Marshalling file ... ###\n");
		marshall();
		System.out.println("\n### Finished successfully ! ###\n");
	}

	protected void analyze() {
		// TODO Auto-generated method stub
	}

}
