package com.parser.read;

import java.util.List;

public interface ClientFileParser<T> {

	List<T> parse();
}
