package com.encdec;

import java.io.File;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
 
public class Banane {

	private static List<File> collected = null;
	private static Crypter crypter = null;
	private static Decrypter decrypter = null;
	public static FileUtils manager = null;
  
	public static void main(String[] args) {
		if (args.length == 2) {
			initFileUtils();
			if (args[0].equalsIgnoreCase("c")) {
				collected = manager.collect("");
				crypter = new Crypter(args[1]);
				doCrypt();
			}
			else if (args[0].equalsIgnoreCase("d")) {
				collected = manager.collect(".zip"); //TODO :change to .marz
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
			System.out.println("[DEBUG] Collected " + collected.size() + " files");
			for (File file : collected) {
				byte[] cryptedData = crypter.crypt(manager.read(file));
				String cryptedFN = Base64.encodeBase64String(file.getName().getBytes());
				String relativePath = manager.getRelativePathWithoutFN(file) != "" ? manager.getRelativePathWithoutFN(file) + "\\" : "";
				manager.write(cryptedData, relativePath + cryptedFN, ".mar");
				file.delete();
			}
			collected.clear();
			collected = manager.collect(".mar");
			crypter.crypt2zip(manager.getArchive(""), collected);
			// manager.changeExtension(manager.getArchive(".zip"), ".marz");
			
			for (File toDelete : collected)
				toDelete.delete();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	private static void doDecrypt() {
		try {
			System.out.println("[DEBUG] Collected " + collected.size() + " archive files");
			for (File archive : collected) {
				if (decrypter.decryptZip(archive))
					archive.delete();
			}
			collected.clear();
			collected = manager.collect(".mar");
			System.out.println("[DEBUG] Collected " + collected.size() + " crypted files");
			for (File file : collected) {
				byte[] clearFile = decrypter.decrypt(manager.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName().replace(".mar", "")));
				String relativePath = manager.getRelativePathWithoutFN(file) != "" ? manager.getRelativePathWithoutFN(file) + "\\" : "";
				manager.write(clearFile, relativePath + clearFN, "");
				file.delete();
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
}