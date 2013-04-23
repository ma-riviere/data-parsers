package com.encdec;

import java.io.File;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
 
public class Banane {

	private static List<File> files = null;
	private static Crypter crypter = null;
	private static Decrypter decrypter = null;
	
	private static String current = null;
  
	public static void main(String[] args) {
		if (args.length == 2) {
			try {current = new java.io.File(".").getCanonicalPath();}
			catch (Exception e) {}
			if (args[0].equalsIgnoreCase("c")) {
				files = FileUtils.collect(current, ".*");
				System.out.println("Collected : " + files.size());
				crypter = new Crypter(args[1]);
				doCrypt();
			}
			else if (args[0].equalsIgnoreCase("d")) {
				files = FileUtils.collect(current, ".mar");
				decrypter = new Decrypter(args[1]);
				doDecrypt();
			}
		}
	}
	
	private static void doCrypt() {
		try {
			FileUtils.writeZip(new File(current), "test");
		}
		catch (Exception e) {
			
		}
	}
	
	private static void doDecrypt() {
		try {
			// Archive all resulting files
			// byte[] cryptedArchive = decrypter.decrypt(archive);
			// String cryptedAN = decrypter.decrypt(archive.getName());
			// FileUtils.write(cryptedArchive, cryptedAN);
			for (File file : files) {
				byte[] clearFile = decrypter.decrypt(FileUtils.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName()), "UTF8");
				FileUtils.write(clearFile, file.getParent() + "\\" + clearFN, "");
				file.delete();  //TODO: Remove later
			}
		}
		catch (Exception e) {
			
		}
	}
	
	/*
	private static void doCrypt() {
		try {
			for (File file : files) {
				byte[] cryptedData = crypter.crypt(FileUtils.read(file));
				String cryptedFN = Base64.encodeBase64String(file.getName().getBytes());
				FileUtils.write(cryptedData, file.getParent() + "\\" + cryptedFN, ".mar");
				System.out.println("Writing file : " + file.getParent() + "\\" + cryptedFN + ".mar");
				file.delete(); //TODO: Remove later
			}
			// files = FileUtils.collect(".", ".mar");
			// Archive all resulting files to "."
			// byte[] cryptedArchive = crypter.crypt(archive);
			// String cryptedAN = crypter.crypt(archive.getName());
			// FileUtils.write(cryptedArchive, cryptedAN);
		}
		catch (Exception e) {
			
		}
	}
	
	private static void doDecrypt() {
		try {
			// Archive all resulting files
			// byte[] cryptedArchive = decrypter.decrypt(archive);
			// String cryptedAN = decrypter.decrypt(archive.getName());
			// FileUtils.write(cryptedArchive, cryptedAN);
			for (File file : files) {
				byte[] clearFile = decrypter.decrypt(FileUtils.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName()), "UTF8");
				FileUtils.write(clearFile, file.getParent() + "\\" + clearFN, "");
				file.delete();  //TODO: Remove later
			}
		}
		catch (Exception e) {
			
		}
	}
	*/
}