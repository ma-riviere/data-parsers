package com.parser.write;

import java.io.File;

public class MarshallerData {

	public String bindings;
	public String path;
	public Object template;
	
	public MarshallerData(String path, String bindings, Object template) {
		this.path = path;
		this.bindings = bindings;
		this.template = template;
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getPath() {return path;}
	public void setPath(String value) {this.path = value;}
	
	public String getFileName() {return new File(path).getName();}	
}