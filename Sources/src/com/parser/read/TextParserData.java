package com.parser.read;

import java.util.List;

public class TextParserData {

	private List<String> lines = null;
	private String[] path;
	
	public TextParserData() {}
	
	public List<String> getLines() {return lines;}
	public void setLines(List<String> lines) {this.lines = lines;}
	
	public String getPath(int i) {return path[i];}
	public void setPath(String[] path) {this.path = path;}
}
