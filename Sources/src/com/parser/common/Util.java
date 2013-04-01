package com.parser.common;

public class Util {

	public static void printSection(String s) {
		String s1 = ""; String s2; String s3 = "";
		
		while (s1.length() < 77)
			s1 += "=";
		
		s2 = "[ " + s + " ]";
		while (s2.length() < 77)
			s2 = " " + s2 + " ";
		
		while (s3.length() < 77)
			s3 += "=";

		System.out.println("\n" + s1 + "\n" +  s2 + "\n" + s3);
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

	public static void printCurrentProgress() {
		System.out.print("+");
	}
	
	public static void printEndProgress() {
		System.out.print(" \n");
	}
	
}