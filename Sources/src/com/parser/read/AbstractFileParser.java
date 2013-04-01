package com.parser.read;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public abstract class AbstractFileParser<T> implements ClientFileParser<T> {

	protected List<T> dataList;

	protected String version;
	protected String pack;
	protected String file;

	public AbstractFileParser(String version, String pack, String file) {
		this.version = version;
		this.pack = pack;
		this.file = file;
	}

	@Override
	public List<T> parse() {
		try {
			JAXBContext jc = JAXBContext.newInstance(pack);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setEventHandler(new XmlValidationHandler());
			File dataFile = new File("../../Data/" + version + "/client/" + file);
			if(dataFile.exists()){
				System.out.println("\n[MAIN] [INFO] " + dataFile.getName()+" exists, reading it !");
				Object collection = unmarshaller.unmarshal(dataFile);
				dataList = castFrom(collection);
				// System.out.println("Size of " + file + " : " + dataList.size());
			}else{
				System.out.println("\n[MAIN][ERROR] " + dataFile + " could not be found !!");
				return Collections.emptyList();
			}
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	protected abstract List<T> castFrom(Object topNode);

}
