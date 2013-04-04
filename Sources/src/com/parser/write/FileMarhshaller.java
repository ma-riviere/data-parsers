package com.parser.write;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.parser.output.aion.mission.*;

public class FileMarhshaller {

	//TODO: Replace Me
	public static void marshallFile(Object templates, String file, String bindings) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(bindings);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(templates, new FileOutputStream(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void marshallFile(MarshallerData md) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(md.getBindings());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			if (!md.getOutputMap().keySet().isEmpty()) {
				for (Object template : md.getOutputMap().keySet()) {
					System.out.println("YIHAAA");
					marshaller.marshal(template, new FileOutputStream(md.getOutputMap().get(template)));
				}
			} else if (md.getTemplate() != null && md.getDestFile() != null) {
				marshaller.marshal(md.getTemplate(), new FileOutputStream(md.getDestFile()));
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
