package com.parser.read;

import java.io.File;
import java.nio.file.*;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.parser.common.Util;

public abstract class AbstractDirectoryParser<T> implements ClientDirectoryParser<T> {

	Map<String, List<T>> filesData = new HashMap<String, List<T>>();
	Map<String, File> files = new HashMap<String, File>();
	
	protected String version;
	protected String pack;
	protected String directory;
	protected String prefix;
	protected String extension;

	public AbstractDirectoryParser(String version, String pack, String directory, String prefix, String extension) {
		this.version = version;
		this.pack = pack;
		this.directory = directory;
		this.prefix = prefix;
		this.extension = extension;
	}
	
	public AbstractDirectoryParser(String version, String pack, String directory, String prefix) {
		this(version, pack, directory, prefix, "xml");
	}
	
	public AbstractDirectoryParser(String version, String pack, String directory) {
		this(version, pack, directory, "", "xml");
	}

	@Override
	public Map<String, List<T>> parse() {

		Path path = Paths.get("../../Data/" + version + "/client/" + directory);
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("\n[MAIN] [ERROR] " + path + " is not a Directory !");
		}
		
		files.clear();
		
		try {addTree(path, files);} catch (IOException io) {System.out.println(io);}
		System.out.println("\n[MAIN] [INFO] Parsing directory " + directory + " with " + files.keySet().size() + " files !");
		
		filesData.clear();
		Util.printProgressBarHeader(files.keySet().size());
		
		for (String dirName : files.keySet()) {
			List<T> dataList;
			File file = files.get(dirName);
			try {
				JAXBContext jc = JAXBContext.newInstance(pack);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setEventHandler(new XmlValidationHandler());

				Object collection = unmarshaller.unmarshal(file);
				dataList = castFrom(collection);
				filesData.put(dirName, dataList);
				Util.printCurrentProgress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.printEndProgress();

		return filesData;
	}
	
	protected void addTree(Path directory, Map<String, File> files) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {
			for (Path dirOrFile : ds) {
				// Is a directory
				if (Files.isDirectory(dirOrFile))
					addTree(dirOrFile, files);
				// Is a File and matches criteria
				else if (dirOrFile.toFile().getName().endsWith("." + extension) && dirOrFile.toFile().getName().startsWith(prefix)) {
					String mappedName = directory.toFile().getName() + "@" + mapFileName(dirOrFile.toFile().getName());
					files.put(mappedName, dirOrFile.toFile());
				}
			}
		}
	}

	protected abstract List<T> castFrom(Object topNode);

	protected abstract String mapFileName(String fileName);

}
