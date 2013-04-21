package com.parser.write;

import java.io.File;
import javolution.util.FastMap;

public class MarshalOrder {

	public String path;
	public String extension;
	public String bindings;
	public Object template;
	public FastMap<Object, String> comments = new FastMap<Object, String>();
	
	public MarshalOrder(String path, String extension, String bindings, Object template, FastMap<Object, String> comments) {
		this.path = path;
		this.extension = extension;
		this.bindings = bindings;
		this.template = template;
		this.comments = comments;
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getPath() {return path;}
	public void setPath(String value) {this.path = value;}
	
	public FastMap<Object, String> getCommentsMap() {return comments;}
	
	public String getExtension() {return extension;}
	
	
	public String getFileName() {return new File(path + extension).getName();}
}