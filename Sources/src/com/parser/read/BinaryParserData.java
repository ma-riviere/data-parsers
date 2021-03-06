package com.parser.read;

import java.nio.MappedByteBuffer;

public class BinaryParserData {

	private MappedByteBuffer buffer = null;
	private int size = 0;
	private String[] path;
	
	public BinaryParserData() {}
	
	public MappedByteBuffer getBuffer() {return buffer;}
	public void setBuffer(MappedByteBuffer buffer) {this.buffer = buffer;}
	
	public int getSize() {return size;}
	public void setSize(int size) {this.size = size;}
	
	public String getPath(int i) {return path[i];}
	public void setPath(String[] path) {this.path = path;}
}
