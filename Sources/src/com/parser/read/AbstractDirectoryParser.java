package com.parser.read;

import java.io.File;
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

		File dir = new File("../../Data/" + version + "/client/" + directory);
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("\n[MAIN] [ERROR] " + dir.getPath() + " is not a Directory !");
		}
		
		files.clear();
		addTree(dir, files);
		System.out.println("\n[MAIN] [INFO] Parsing directory " + directory + " with " + files.length + " files !");
		
		filesData.clear();
		Util.printProgressBarHeader(files.length);
		
		for (File file : files.values()) {
			List<T> dataList;
			try {
				JAXBContext jc = JAXBContext.newInstance(pack);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setEventHandler(new XmlValidationHandler());

				// System.out.println("Unmarshalling " + file.getName());
				Object collection = unmarshaller.unmarshal(file);
				String mappedName = files.getKey(file) + "@" + mapFileName(file.getName());
				dataList = castFrom(collection);
				// System.out.println("Size of " + file.getName() + " : " + dataList.size());
				filesData.put(mappedName, dataList);
				Util.printCurrentProgress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.printEndProgress();

		return filesData;
	}
	
	protected void addTree(File directory, Map<String, File> files) throws IOException {
		try (DirectoryStream<File> ds = Files.newDirectoryStream(directory)) {
			for (File dirOrFile : ds) {
				// Is a directory
				if (Files.isDirectory(dirOrFile))
					addTree(dirOrFile, files);
				// Is a File and matches criteria
				else if (dirOrFile.getName().endsWith("." + extension) && dirOrFile.getName().startsWith(prefix))
					files.put(directory.getName(), dirOrFile);
				// Is a file but does not match criteria
				else
					continue;
			}
		}
	}

	protected abstract List<T> castFrom(Object topNode);

	protected abstract String mapFileName(String fileName);

}
