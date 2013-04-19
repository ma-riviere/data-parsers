package com.parser.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javanet.staxutils.IndentingXMLStreamWriter;

import com.parser.commons.utils.xml.XMLCommentsWritter;

public class FileMarshaller {
	
	public static void marshallFile(List<MarshallerData> orders) {
		try {
			for (MarshallerData order : orders) {
				JAXBContext jaxbContext = JAXBContext.newInstance(order.getBindings());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				System.out.println("[MARSHALLER] Writting file : " + order.getFileName());
				
				createDir(order.getPath());
				FileWriter fw = new FileWriter(new File(order.getPath()));
				XMLStreamWriter writer = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(fw));
				
				if (order.getCommentsMap() != null)
					marshaller.setListener(new XMLCommentsWritter(writer, order.getCommentsMap()));
				
				marshaller.marshal(order.getTemplate(), writer);
				
				writer.flush();	
				writer.close();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createDir(String dir) {
		String finalDir = "";
		File toCreate = null;
		String[] paths = dir.split("/");
		if (paths.length > 1) {
			for (int i = 0; i <= paths.length - 2; i++) {
				finalDir += paths[i] + "/";
				toCreate = new File(finalDir);
				if (toCreate != null)
					toCreate.mkdir();
			}
		}
	}
}