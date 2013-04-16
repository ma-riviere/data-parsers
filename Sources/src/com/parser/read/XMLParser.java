package com.parser.read;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.parser.commons.utils.Util;

public abstract class XMLParser<R> extends FilesCollector {

	private String bindings;

	public XMLParser(String stringPath, String prefix, String bindings) {
		super(stringPath, prefix, ".xml");
		this.bindings = bindings;
	}
	
	public XMLParser(String bindings) {
		this(null, null, bindings);
	}
	
	boolean noDisplay = false;
	
	public R parseFile(String s) {
		File file = new File(s);
		noDisplay = false;
		return parseFile(file);
	}

	public R parseFile(File file) {
		R root = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(bindings);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setEventHandler(new XMLValidationHandler());
			Object collection = unmarshaller.unmarshal(file);
			if (!noDisplay)
				System.out.println("\n[MAIN][INFO] Parsing file " + file.getName());
			root = castRoot(collection);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return root;
	}
	
	public FastMap<String[], R> parseDir() {
		FastMap<String[], R> rootMap = new FastMap<String[], R>();
		
		List<File> files = collect();
		System.out.println("\n[MAIN][INFO] Parsing directory " + stringPath.substring(stringPath.lastIndexOf("/")) + " with " + files.size() + " files !");
		Util.printProgressBarHeader(files.size());
		noDisplay = true;
		
		for (File file : files) {
			R root = parseFile(file);
			if (root != null)
				rootMap.put(loadPath(file), root);
			Util.printCurrentProgress();
		}
		Util.printEndProgress();

		return rootMap;
	}
	
	public R castRoot(Object topNode) {
		return ((R) topNode);
	}
}