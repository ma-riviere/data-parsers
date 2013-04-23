package com.parser.read;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.charset.Charset;
// import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

import com.parser.commons.utils.Util;

public class TextParser extends FilesCollector {

	// static Charset charset = StandardCharsets.UTF_8;
	private String endLine = null;
	
	public TextParser(String stringPath, String prefix, String extension, String endLine) {
		super(stringPath, prefix, extension);
		this.endLine = endLine;
	}
	
	public TextParser(String stringPath, String prefix, String extension) {
		super(stringPath, prefix, extension);
	}
	
	public TextParser(String endLine) {
		super(null, null, null);
		this.endLine = endLine;
	}
	
	public TextParser() {
		super(null, null, null);
	}
	
	public TextParserData parseFile(String s) {
		File file = new File(s);
		return parseFile(file);
	}
	
	public List<TextParserData> parseDir() {
		List<TextParserData> tpds = new ArrayList<TextParserData>();
		
		List<File> files = collect();
		Util.printProgressBarHeader(files.size());
		
		for (File file : collect()) {
			TextParserData tpd = parseFile(file);
			if (tpd.getLines() != null && !tpd.getLines().isEmpty())
				tpds.add(tpd);
			Util.printCurrentProgress();
		}
		Util.printEndProgress();
		
		return tpds;
	}
	
	public TextParserData parseFile(File file) {
		TextParserData tpd = new TextParserData();
		
		// Path path = Paths.get(file.getPath());
		List<String> lines = null;
		try {
			FileReader fr = new FileReader(file);
			Scanner scanner = new Scanner(fr);
			if (endLine != null)
				scanner.useDelimiter(endLine);
				
			while (scanner.hasNext()) {
				tpd.addLine(scanner.next());
				// System.out.println("[DEBUG] Reading line : " + scanner.next());
			}
			// lines = Files.readAllLines(path, charset);
			// tpd.setLines(lines);
			tpd.setPath(loadPath(file));
			fr.close();
		}
		catch (FileNotFoundException fnf) { 
			fnf.printStackTrace();
			System.out.println("[TEXT PARSER] Error  while reading file : " + file.getName());
		}
		catch (IOException e) { 
			e.printStackTrace();
			System.out.println("[TEXT PARSER] Error  while reading file : " + file.getName());
		}

		return tpd;
	}
}
