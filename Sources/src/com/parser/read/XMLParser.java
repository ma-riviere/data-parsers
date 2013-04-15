package com.parser.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	
	public XMLParser() {
		this(null, null, null);
	}
	
	public R parseFile(String s) {
		File file = new File(s);
		return parseFile(file);
	}
	
	public R parseFile(File file) {
		R root = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(bindings);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setEventHandler(new XmlValidationHandler());
			Object collection = unmarshaller.unmarshal(file);
			root = cast(collection);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return root;
	}
	
	public FastMap<String, R> parseDir() {
		FastMap<String, R> rootMap = new FastMap<String, R>();
		
		List<File> files = collect();
		Util.printProgressBarHeader(files.size());
		
		for (File file : files) {
			R root = parseFile(file);
			if (root != null)
				rootMap.put(Util.getEncodedName(file.getName(), file.getParentFile().getName()), root);
			Util.printCurrentProgress();
		}
		Util.printEndProgress();

		return rootMap;
	}
	
	public R cast(Object topNode) {
		return ((R) topNode);
	}
}