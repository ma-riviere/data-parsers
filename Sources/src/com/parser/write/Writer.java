package com.parser.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer.Property;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.SaxonApiException;

import com.parser.commons.utils.xml.XMLCommentsWriter;

public abstract class Writer {

	private Marshaller marshaller;
	private FileOutputStream output;
	private static final int BUFFER_SIZE = 1024 * 1024 * 50;
	
	public void write(List<MarshalOrder> orders) {
		try {
			for (MarshalOrder order : orders) {
				init(order);
				System.out.println("[WRITER] Writting " + order.getExtension() + " file : " + order.getFileName());
				
				if (order.getExtension().equalsIgnoreCase(".xml"))
					writeXML(order);
				else
					writeBinary(order);
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (JAXBException je) {
			je.printStackTrace();
		} catch (XMLStreamException xse) {
			xse.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SaxonApiException sae) {
			sae.printStackTrace();
		}
	}

	private void writeXML(MarshalOrder order) throws IOException, JAXBException, XMLStreamException, SaxonApiException {		
		Processor p = new Processor(false);
		Serializer s = p.newSerializer();
		
		s.setOutputProperty(Property.METHOD, "xml");
		s.setOutputProperty(Property.INDENT, "yes");
		s.setOutputProperty(Property.ENCODING, "UTF-8");
		s.setOutputProperty(Property.STANDALONE, "yes");
		s.setOutputStream(output);
		
		XMLStreamWriter writer = s.getXMLStreamWriter();
		
		if (order.getCommentsMap() != null && !order.getCommentsMap().isEmpty())
			marshaller.setListener(new XMLCommentsWriter(writer, order.getCommentsMap()));
		
		marshaller.marshal(order.getTemplate(), writer);
		
		writer.flush();
		writer.close();
		output.close();
	}
	
	private void writeBinary(MarshalOrder order) throws IOException, JAXBException, ParserConfigurationException {		
		FileChannel out = output.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		marshaller.marshal(order.getTemplate(), doc);
		
		writeBufferData(doc, buffer);

		buffer.flip();		
		out.write(buffer);
		out.close();
		output.close();
	}
	
	private void writeBufferData(Node node, ByteBuffer buffer) {
		System.out.println("[INFO][MAIN] Analysing node : " + node.getNodeName());
		if (node.hasAttributes()) {writeNodeAttributes(buffer, node);}
		if (node.hasChildNodes()) {writeNodeChilds(buffer, node);}
		else {buffer.put("viria".getBytes());}
		System.out.println("[INFO][MAIN] Finished analysing node : " + node.getNodeName());
	}
	
	private void writeNodeAttributes(ByteBuffer buffer, Node node) {
		NamedNodeMap attributes = node.getAttributes();
		short attributesCount = (short) attributes.getLength();
		if (attributesCount > 0) {
			for (short j = 0; j < attributesCount; j++) {
				System.out.println("[INFO][ATTR] WRITING VALUE of : " + attributes.item(j).getNodeValue() + " for ATTRIBUTE " + attributes.item(j).getNodeName());
				addToBuffer(buffer, attributes.item(j).getNodeValue());
			}
		}
	}
	
	private void writeNodeChilds(ByteBuffer buffer, Node node) {
		NodeList childs = node.getChildNodes();
		short childsCount = (short) childs.getLength();
		System.out.println("[INFO][NODE] Node : " + node.getNodeName() + " has " + childsCount  + " childs !");
		if (childsCount > 0) {
			buffer.putShort(childsCount);
			System.out.println("[INFO][NODE] WRITING child COUNT of : " + childsCount);
			for (short i=0; i< childsCount; i++) {
				Node child = childs.item(i);
				writeBufferData(child, buffer);
			}
		}
	}
	
	private void addToBuffer(ByteBuffer buffer, Object source) {
		if (source instanceof Collection<?>)
			buffer.putInt(((Collection) source).size());
		else if (source instanceof String)
				buffer.put(source.toString().getBytes());
		else if (source instanceof Character)
			buffer.putChar((Character) source);
		else if (source instanceof Integer)
			buffer.putInt((Integer) source);
		else if (source instanceof Long)
			buffer.putLong((Long) source);
		else if (source instanceof Short)
			buffer.putShort((Short) source);
		else if (source instanceof Float)
			buffer.putFloat((Float) source);
		else if (source instanceof Double)
			buffer.putDouble((Double) source);
		else if (source instanceof Boolean)
			buffer.put((Boolean) source ? "true".getBytes() : "false".getBytes());
	}
	
	private void init(MarshalOrder order) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(order.getBindings());
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			createDir(order.getPath() + order.getExtension());
			output = new FileOutputStream(order.getPath() + order.getExtension(), false);
		
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createDir(String dir) {
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
