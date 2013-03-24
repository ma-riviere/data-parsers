package com.parser.read;

import java.util.List;
import java.util.Map;

public interface ClientDirectoryParser<T> {

	Map<String, List<T>> parse();
}
