package com.parser.read;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public abstract class AbstractDirectoryParser<T> implements ClientDirectoryParser<T> {

	Map<String, List<T>> filesData = new HashMap<String, List<T>>();
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
			throw new IllegalArgumentException("Not a directory" + dir.getPath());
		}
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("." + extension) && pathname.getName().startsWith(prefix))
					return true;
				return false;
			}
		});
		System.out.println("Found " + files.length + " files");
		filesData.clear();
		for (File file : files) {
			List<T> dataList;
			try {
				JAXBContext jc = JAXBContext.newInstance(pack);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setEventHandler(new XmlValidationHandler());

				// System.out.println("Unmarshalling " + file.getName());
				Object collection = unmarshaller.unmarshal(file);
				String mappedName = mapFileName(file.getName());
				dataList = castFrom(collection);
				System.out.println("Size of " + file.getName() + " : " + dataList.size());
				filesData.put(mappedName, dataList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return filesData;
	}

	protected abstract List<T> castFrom(Object topNode);

	protected abstract String mapFileName(String fileName);

}
