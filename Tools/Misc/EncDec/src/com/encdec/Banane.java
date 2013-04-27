package com.encdec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
 
public class Banane {

	private static List<File> collected = new ArrayList<File>();
	private static Crypter crypter = null;
	private static Decrypter decrypter = null;
	public static FileManager manager = null;
	
	public static String CUSTOM_EXT = ".mar";
	public static String CUSTOM_ARCHIVE_EXT = ".marz";
  
	public static void main(String[] args) {
		new Gui();
	}
	
	public static void init(String choice, String password) {
		initFileManager();
		if (choice.equalsIgnoreCase("c")) {
			crypter = new Crypter(password);
			doCrypt();
		}
		else if (choice.equalsIgnoreCase("d")) {
			decrypter = new Decrypter(password);
			doDecrypt();
		}
	}
	
	private static void initFileManager() {
		try {
			System.out.println("[DEBUG] Init !");
			String targetDir = new File(".").getCanonicalPath();
			manager = new FileManager(targetDir);
			collected.clear();
		}
		catch (Exception e) {
			System.out.println("Error during initialization of File Manager : " + e);
		}
	}
	
	private static void doCrypt() {
		try {
			FileUtils.deleteQuietly(manager.getArchive(CUSTOM_ARCHIVE_EXT));
			collected = manager.collect("");
			System.out.println("[DEBUG] Collected " + collected.size() + " files");
			for (File file : collected) {
				byte[] cryptedData = crypter.crypt(manager.read(file));
				String cryptedFN = Base64.encodeBase64String(file.getName().getBytes());
				String filePath = manager.generateFilePath(file, cryptedFN, CUSTOM_EXT);
				manager.write(cryptedData, filePath);
			}
			
			List<File> collected2 = manager.collect(CUSTOM_EXT);
			System.out.println("[DEBUG] Collected " + collected2.size() + " crypted files");

			if (crypter.crypt2zip(manager.getArchive(".zip"), collected2)) {
				manager.changeExtension(manager.getArchive(".zip"), CUSTOM_ARCHIVE_EXT);
				for (File toDelete2 : collected2) {toDelete2.delete();}
				collected = manager.collect("");
				for (File toDelete : collected) {
					if (!toDelete.getName().contains(manager.getArchive(CUSTOM_ARCHIVE_EXT).getName()))
						toDelete.delete();
				}
				manager.cleanEmptyDirs();
				System.out.println("ATTENTION : Prenez soin de retenir votre mot de passe !");
			}
			else {
				System.out.println("[DEBUG] Archive could not be generated, deleting crypted elements");
				collected2 = manager.collect(CUSTOM_EXT);
				for (File toDelete : collected2) toDelete.delete();
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	private static void doDecrypt() {
		try {
			collected = manager.collect(CUSTOM_ARCHIVE_EXT);
			System.out.println("[DEBUG] Collected " + collected.size() + " archive files");
			for (File archive : collected) {
				if (decrypter.decryptZip(archive))
					archive.delete();
				else {
					System.out.println("		!!! Mot de passe incorrect !!!");
					return;
				}
			}
			collected.clear();
			collected = manager.collect(CUSTOM_EXT);
			System.out.println("[DEBUG] Collected " + collected.size() + " crypted files");
			for (File file : collected) {
				byte[] clearData = decrypter.decrypt(manager.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName().replace(CUSTOM_EXT, "")));
				String filePath = manager.generateFilePath(file, clearFN, "");
				manager.write(clearData, filePath);
				file.delete();
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
}