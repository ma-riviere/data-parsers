package com.parser.commons.utils;

public class Util {

	public static void printSection(String s) {
		String s1 = ""; String s2; String s3 = "";
		
		while (s1.length() < 78)
			s1 += "=";
		
		s2 = "[ " + s + " ]";
		while (s2.length() < 77)
			s2 = " " + s2 + " ";
		
		while (s3.length() < 78)
			s3 += "=";

		System.out.println("\n" + s1 + "\n" +  s2 + "\n" + s3);
	}
	
	public static void printSubSection(String s) {
	
		s = "[ " + s + " ]";
		
		while (s.length() < 79)
			s = "-" + s + "-";

		System.out.println(s);
	}
	
	public static void printProgressBarHeader(int size) {
		StringBuilder header = new StringBuilder("0% [");
		for (int i = 0; i < size; i++) {
			header.append("-");
		}
		header.append("] 100%");
		System.out.println(header);
		System.out.print("    ");
	}

	public static void printCurrentProgress() {System.out.print("+");}
	
	public static void printEndProgress() {System.out.print(" \n");}
	
	public static String getFileName(String s) {
		String[] names = s.split("@");
		String name = "";
		if (names.length != 2)
			System.out.println("[UTIL] Error while splitting parsed file name ...");
		else 
			name = names[1];
		
		return name;
	}
	
	public static String getDirName(String s) {
		String[] names = s.split("@");
		return names[0];
	}
	
}