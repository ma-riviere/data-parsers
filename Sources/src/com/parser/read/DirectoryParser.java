package com.parser.read;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DirectoryParser<R, E> {

	Map<String, R> parseRoot();
	
	Map<String, List<E>> parse();
	
	R parseFileRoot(File file);
	
	List<E> parseFile(File file);
	
	R castRoot(Object topNode);
}
