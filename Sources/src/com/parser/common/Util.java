package com.parser.common;

public class Util {

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
		System.out.print("\n");
	}
	
}