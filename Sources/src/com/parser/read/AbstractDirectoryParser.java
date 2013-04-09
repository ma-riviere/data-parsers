package com.parser.read;

import java.io.File;
import java.nio.file.*;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.parser.common.utils.Util;

public abstract class AbstractDirectoryParser<R, E> implements ClientDirectoryParser<R, E> {

	Map<String, File> files = new HashMap<String, File>();
	Map<String, R> rootDataMap = new HashMap<String, R>();
	Map<String, List<E>> dataMap = new HashMap<String, List<E>>();
	
	protected String bindings;
	protected String directory;
	protected String prefix;
	protected String extension;

	public AbstractDirectoryParser(String bindings, String directory, String prefix, String extension) {
		this.bindings = bindings;
		this.directory = directory;
		this.prefix = prefix;
		this.extension = extension;
	}
	
	public AbstractDirectoryParser(String bindings, String directory, String prefix) {
		this(bindings, directory, prefix, ".xml");
	}
	
	@Override
	public R parseFileRoot(File toMarshall) {
		
		R rootData = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(bindings);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setEventHandler(new XmlValidationHandler());
			Object collection = unmarshaller.unmarshal(toMarshall);
			rootData = castRoot(collection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootData;
	}
	
	@Override
	public List<E> parseFile(File toMarshall) {
		
		List<E> data = new ArrayList<E>();
		
		try {
			JAXBContext jc = JAXBContext.newInstance(bindings);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setEventHandler(new XmlValidationHandler());
			Object collection = unmarshaller.unmarshal(toMarshall);
			data = cast(collection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public Map<String, R> parseRoot() {

		Path path = Paths.get(directory);
		if (!Files.isDirectory(path)) {throw new IllegalArgumentException("\n[MAIN] [ERROR] " + path + " is not a Directory !");}
		
		files.clear();
		
		try {addTree(path, files);} catch (IOException io) {System.out.println(io);}
		System.out.println("\n[MAIN] [INFO] Parsing directory " + directory + " with " + files.keySet().size() + " files !");
		
		rootDataMap.clear();
		Util.printProgressBarHeader(files.keySet().size());
		
		for (String dirName : files.keySet()) {
			R rootData;
			File f = files.get(dirName);
			try {
				JAXBContext jc = JAXBContext.newInstance(bindings);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setEventHandler(new XmlValidationHandler());

				Object collection = unmarshaller.unmarshal(f);
				rootData = castRoot(collection);
				rootDataMap.put(dirName, rootData);
				Util.printCurrentProgress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.printEndProgress();

		return rootDataMap;
	}
	
	@Override
	public Map<String, List<E>> parse() {

		Path path = Paths.get(directory);
		if (!Files.isDirectory(path)) {throw new IllegalArgumentException("\n[MAIN] [ERROR] " + path + " is not a Directory !");}
		
		files.clear();
		
		try {addTree(path, files);} catch (IOException io) {System.out.println(io);}
		System.out.println("\n[MAIN] [INFO] Parsing directory " + directory + " with " + files.keySet().size() + " files !");
		
		dataMap.clear();
		Util.printProgressBarHeader(files.keySet().size());
		
		for (String dirName : files.keySet()) {
			List<E> data;
			File f = files.get(dirName);
			try {
				JAXBContext jc = JAXBContext.newInstance(bindings);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setEventHandler(new XmlValidationHandler());

				Object collection = unmarshaller.unmarshal(f);
				data = cast(collection);
				dataMap.put(dirName, data);
				Util.printCurrentProgress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.printEndProgress();

		return dataMap;
	}
	
	protected void addTree(Path directory, Map<String, File> files) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {
			for (Path dirOrFile : ds) {
				// Is a directory
				if (Files.isDirectory(dirOrFile))
					addTree(dirOrFile, files);
				// Is a File and matches criteria
				else if (dirOrFile.toFile().getName().endsWith(extension) && dirOrFile.toFile().getName().startsWith(prefix)) {
					String mappedName = directory.toFile().getName() + "@" + mapFileName(dirOrFile.toFile().getName());
					files.put(mappedName, dirOrFile.toFile());
				}
			}
		}
	}

	@Override
	public R castRoot(Object topNode) {
		return ((R)topNode);
	}
	
	protected abstract List<E> cast(Object topNode);

	protected String mapFileName(String fileName) {
		return fileName.replaceAll(prefix, "").replaceAll(extension, "");
	}

}