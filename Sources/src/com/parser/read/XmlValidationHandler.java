package com.parser.read;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class XmlValidationHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
		if (event.getSeverity() == ValidationEvent.ERROR) {
			System.out.println(event.getMessage() + "at line:" + event.getLocator().getLineNumber());
		}
		else if (event.getSeverity() == ValidationEvent.FATAL_ERROR) {
			ValidationEventLocator locator = event.getLocator();
			String message = event.getMessage();
			int line = locator.getLineNumber();
			int column = locator.getColumnNumber();
			System.out.println("Error at [line=" + line + ", column=" + column + "]: " + message);
			throw new Error(event.getLinkedException());
		}
		return true;
	}

}
