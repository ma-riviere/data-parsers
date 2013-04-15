package com.parser.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.List;

import com.parser.commons.utils.Util;

public class BinaryParser extends FilesCollector {

	private ByteOrder byteOrder;

	public BinaryParser(String stringPath, String prefix, String extension, ByteOrder byteOrder) {
		super(stringPath, prefix, extension);
		this.byteOrder = byteOrder;
	}
	
	public BinaryParser(String stringPath, String prefix, String extension) {
		this(stringPath, prefix, extension, ByteOrder.LITTLE_ENDIAN);
	}
	
	public BinaryParser(ByteOrder byteOrder) {
		this(null, null, null, byteOrder);
	}
	
	public BinaryParser() {
		this(null, null, null, ByteOrder.LITTLE_ENDIAN);
	}
	
	public BinaryParserData parseFile(String s) {
		File file = new File(s);
		return parseFile(file);
	}
	
	public List<BinaryParserData> parseDir() {
		List<BinaryParserData> bpds = new ArrayList<BinaryParserData>();
		
		List<File> files = collect();
		Util.printProgressBarHeader(files.size());
		
		for (File file : files) {
			BinaryParserData bpd = parseFile(file);
			if (bpd.getBuffer() != null)
				bpds.add(bpd);
			Util.printCurrentProgress();
		}
		Util.printEndProgress();
		
		return bpds;
	}
	
	public BinaryParserData parseFile(File file) {
		BinaryParserData bpd = new BinaryParserData();
		FileChannel channel = null;
		try {
			channel = new RandomAccessFile(file, "r").getChannel();
			int size = (int) channel.size();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
			buffer.order(byteOrder);
			
			bpd.setBuffer(buffer);
			bpd.setSize(size);
			bpd.setName(Util.getEncodedName(file.getName(), file.getParentFile().getName()));
		}
		catch (IOException e) {
			System.out.println("[BINARY PARSER] : IO Error while loading " + file.getName());
			return null;
		}
		finally{
			try {
				if (channel != null)
					channel.close();
			}
			catch (IOException e) {
				System.out.println("[BINARY PARSER] : IO Error while closing channel for " + file.getName() + " : " + e);
			}
		}
		return bpd;
	}
}
