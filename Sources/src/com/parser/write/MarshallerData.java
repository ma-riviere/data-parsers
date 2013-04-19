package com.parser.write;

import java.io.File;
import javolution.util.FastMap;

public class MarshallerData {

	public String bindings;
	public String path;
	public Object template;
	public FastMap<Object, String> comments = null;
	
	public MarshallerData(String path, String bindings, Object template, FastMap<Object, String> comments) {
		this.path = path;
		this.bindings = bindings;
		this.template = template;
		this.comments = comments;
	}
	
	public MarshallerData(String path, String bindings, Object template) {
		this(path, bindings, template, null);
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getPath() {return path;}
	public void setPath(String value) {this.path = value;}
	
	public FastMap<Object, String> getCommentsMap() {return comments;}
	
	public String getFileName() {return new File(path).getName();}
}