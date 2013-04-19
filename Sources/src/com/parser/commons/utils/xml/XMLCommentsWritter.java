package com.parser.commons.utils.xml;

import javolution.util.FastMap;

import javax.xml.bind.Marshaller.Listener;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLCommentsWritter extends Listener {

	private final XMLStreamWriter xmlStreamWriter;
	private final FastMap<Object, String> comments;

	public XMLCommentsWritter(XMLStreamWriter xmlStreamWriter, FastMap<Object, String> comments) {
		this.xmlStreamWriter = xmlStreamWriter;
		this.comments = comments;
	}

	@Override
	public void beforeMarshal(Object source) {
		if (comments.keySet().contains(source)) {
			try {
				xmlStreamWriter.writeComment(comments.get(source));
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
}
