package com.parser.commons.utils.xml;

import javolution.util.FastMap;
import com.google.common.base.Strings;

import javax.xml.bind.Marshaller.Listener;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLCommentsWriter extends Listener {

	private final XMLStreamWriter xmlStreamWriter;
	private final FastMap<Object, String> comments;

	public XMLCommentsWriter(XMLStreamWriter xmlStreamWriter, FastMap<Object, String> comments) {
		this.xmlStreamWriter = xmlStreamWriter;
		this.comments = comments;
	}

	@Override
	public void beforeMarshal(Object source) {
		//TODO: UseFastEquals (hashCode2, see WDGen)
		if (comments.keySet().contains(source) && !Strings.isNullOrEmpty(comments.get(source))) {
			try {
				xmlStreamWriter.writeComment(comments.get(source));
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
}
