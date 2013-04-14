package com.parser.read;

import java.util.List;

public interface FileParser<T> {

	List<T> parse();
}
