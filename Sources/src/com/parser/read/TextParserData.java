package com.parser.read;

import java.util.List;

public class TextParserData {

	private List<String> lines = null;
	private String name;
	
	public TextParserData() {}
	
	public List<String> getLines() {return lines;}
	public void setLines(List<String> lines) {this.lines = lines;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
}
