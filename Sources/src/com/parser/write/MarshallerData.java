package com.parser.write;

public class MarshallerData {

	public String bindings;
	public String file;
	public Object template;
	
	public MarshallerData(String bindings, String file, Object template) {
		this.bindings = bindings;
		this.file = file;
		this.template = template;
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getFile() {return file;}
	public void setFile(String value) {this.file = value;}
	
}