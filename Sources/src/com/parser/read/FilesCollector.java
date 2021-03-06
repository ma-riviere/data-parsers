package com.parser.read;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javolution.util.FastList;
import org.apache.commons.io.FilenameUtils;

public abstract class FilesCollector {

	protected FastList<File> files = new FastList<File>();

	protected String stringPath;
	protected String prefix;
	protected String extension;

	public FilesCollector(String stringPath, String prefix, String extension) {
		this.stringPath = stringPath;
		this.prefix = prefix;
		this.extension = extension;
	}
	
	public FastList<File> collect() {		
		Path path = Paths.get(stringPath);
		try {explore(path, files);}
		catch (IOException io) {System.out.println(io);}
		return files;
	}
	
	protected void explore(Path directory, FastList<File> files) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {
			for (Path dirOrFile : ds) {
				if (Files.isDirectory(dirOrFile))
					explore(dirOrFile, files);
				else if (dirOrFile.toFile().getName().endsWith(extension) && dirOrFile.toFile().getName().startsWith(prefix))
					files.add(dirOrFile.toFile());
			}
		}
	}
	
	public String[] loadPath(File file) {
		String[] results = new String[5];
		results[0] = FilenameUtils.getBaseName(file.getName());
		for (int i = 1; i < 5; i++)  {
			if (file.getParentFile() != null) {
				file = file.getParentFile();
				results[i] = file.getName();
			}
		}
		return results;
	}
}
