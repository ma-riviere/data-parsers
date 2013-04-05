package com.parser.read;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ClientDirectoryParser<T> {

	Map<String, List<T>> parse();
	
	List<T> parseFile(File file);
}
