package com.parser.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class FileMarhshaller {
	
	public static void marshallFile(List<MarshallerData> orders) {
		try {
			for (MarshallerData order : orders) {
				JAXBContext jaxbContext = JAXBContext.newInstance(order.getBindings());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
				createDir(order.getFile());
				marshaller.marshal(order.getTemplate(), new FileOutputStream(order.getFile()));
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void createDir(String dir) {
		String finalDir = "";
		String[] paths = dir.split("/");
		if (paths.length > 1) {
			for (int i = 0; i <= paths.length - 2; i++) {
				finalDir += paths[i] + "/";
			}	
		File toCreate = new File(finalDir);
		toCreate.mkdir();
		}
	}
}
