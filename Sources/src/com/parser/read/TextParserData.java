package com.parser.read;

import java.util.ArrayList;
import java.util.List;

public class TextParserData {

	private List<String> lines = null;
	private String[] path;
	
	public TextParserData() {}
	
	public List<String> getLines() {return lines;}
	public void setLines(List<String> lines) {this.lines = lines;}
	public void addLine(String line) {
		if (lines == null)
			lines = new ArrayList<String>();
		lines.add(line);
	}
	
	public String getPath(int i) {return path[i];}
	public void setPath(String[] path) {this.path = path;}
}
