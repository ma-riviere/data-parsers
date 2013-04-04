package com.parser.write;

import java.util.Map;

import com.parser.write.aion.AionWritingConfig;

public class MarshallerData {

	public static final String baseDir = AionWritingConfig.BASE_DIR;

	public String bindings;
	public String version;
	public Map<Object, String> outputMap = null;
	public Object template;
	public String destFile = baseDir;
	
	public MarshallerData(String bindings, String version, Map<Object, String> outputMap, Object template, String destFile) {
		this.bindings = bindings;
		this.version = version;
		this.outputMap = outputMap;
		this.template = template;
		this.destFile = baseDir + destFile;
	}
	
	public MarshallerData(String bindings, String version, Map<Object, String> outputMap) {
		this(bindings, version, outputMap, null, null);
	}
	
	public MarshallerData(String bindings, String version, Object template, String destFile) {
		this(bindings, version, null, template, destFile);
	}
	
	public MarshallerData() {
		this(null, null, null, null, null);
	}
	
	public String getBindings() {return bindings;}
	public void setBindings(String value) {this.bindings = value;}
	
	public String getVersion() {return version;}
	public void setVersion(String value) {this.version = value;}
	
	public Map<Object, String> getOutputMap() {return outputMap;}
	public void setOutputMap(Map<Object, String> value) {this.outputMap = value;}
	
	public Object getTemplate() {return template;}
	public void setTemplate(Object value) {this.template = value;}
	
	public String getDestFile() {return destFile;}
	public void setDestFile(String value) {this.destFile = baseDir + value;}
	
}