package com.encdec;

import java.io.File;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
 
public class Banane {

	private static List<File> collected = null;
	private static Crypter crypter = null;
	private static Decrypter decrypter = null;
	private static FileUtils manager = null;
  
	public static void main(String[] args) {
		if (args.length == 2) {
			initFileUtils();
			if (args[0].equalsIgnoreCase("c")) {
				collected = manager.collect(".*");
				crypter = new Crypter(args[1]);
				doCrypt();
			}
			else if (args[0].equalsIgnoreCase("d")) {
				collected = manager.collect(".mar");
				decrypter = new Decrypter(args[1]);
				doDecrypt();
			}
		}
	}
	
	private static void initFileUtils() {
		try {
			String targetDir = new File(".").getCanonicalPath();
			manager = new FileUtils(targetDir);
		}
		catch (Exception e) {
			System.out.println("Error during initialization of File Manager : " + e);
		}
	}
	
	private static void doCrypt() {
		try {
			manager.initZipWriting();
			for (File file : collected) {
				byte[] cryptedData = crypter.crypt(manager.read(file));
				String cryptedFN = Base64.encodeBase64String(file.getName().getBytes());
				String relativePath = manager.getRelativePath(file);
				manager.zip(cryptedData, relativePath + cryptedFN);
				file.delete();
			}
			try {
				File archive = manager.getArchive("");
				byte[] cryptedAR = crypter.crypt(manager.read(archive));
				manager.write(cryptedAR, archive.getName(), ".mar");
				archive.delete();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		catch (Exception e) {e.printStackTrace();}
		manager.endZipWriting();
	}
	
	private static void doDecrypt() {
		try {
		if (collected.isEmpty())
			return;
			for (File archive : collected) {
				byte[] clearData = decrypter.decrypt(manager.read(archive));
				String newZip = archive.getName().substring(0, archive.getName().indexOf(".mar"));
				manager.write(clearData, newZip, "");
				manager.unZip(new File(newZip));
				archive.delete();
			}
			collected = manager.collect(".mar");
			for (File file : collected) {
				byte[] clearFile = decrypter.decrypt(manager.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName()));
				manager.write(clearFile, manager.getRelativePath(file) + "\\" + clearFN, "");
				file.delete(); 
			}
		}
		catch (Exception e) {e.printStackTrace();}
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