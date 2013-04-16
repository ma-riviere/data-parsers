package com.parser.write;

public class MarshallerData {

	public String bindings;
	public String file;
	public Object template;
	
	public MarshallerData(String file, String bindings, Object template) {
		this.file = file;
		this.bindings = bindings;
		this.template = template;
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getFile() {return file;}
	public void setFile(String value) {this.file = value;}
	
}