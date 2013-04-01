package com.parser.write;

public abstract class AbstractWriter implements Writer {

	@Override
	public void start() {
		printSection("Reading input data");
		parse();
		printSection("Computing output data");
		transform();
		printSection("Analyzing output content");
		analyze();
		printSection("Writting output file");
		marshall();
		printSection("FINISHED");
	}

	protected void analyze() {}
	
	private void printSection(String s) {
		String s1 = ""; String s2; String s3 = "";
		
		while (s1.length() < 77)
			s1 += "=";
		
		s2 = "[ " + s + " ]";
		while (s2.length() < 77)
			s2 = " " + s2 + " ";
		
		while (s3.length() < 77)
			s3 += "=";

		System.out.println("\n" + s1 + "\n" +  s2 + "\n" + s3);
	}

}
