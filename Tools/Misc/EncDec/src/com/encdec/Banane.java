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
		new Gui();
	}
	
	public static void init(String choice, String password) {
		initFileUtils();
		if (choice.equalsIgnoreCase("c")) {
			collected = manager.collect("");
			crypter = new Crypter(password);
			doCrypt();
		}
		else if (choice.equalsIgnoreCase("d")) {
			collected = manager.collect(".marz");
			decrypter = new Decrypter(password);
			doDecrypt();
		}
	}
	
	private static void initFileUtils() {
		try {
			System.out.println("[DEBUG] Init !");
			String targetDir = new File(".").getCanonicalPath();
			manager = new FileUtils(targetDir);
			collected.clear();
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
				String filePath = manager.generateFilePath(file, cryptedFN, ".mar");
				manager.write(cryptedData, filePath);
				file.delete();
			}
			collected.clear();
			collected = manager.collect(".mar");
			System.out.println("[DEBUG] Collected " + collected.size() + " crypted files");
			crypter.crypt2zip(manager.getArchive(""), collected);
			manager.changeExtension(manager.getArchive(""), ".marz");
			
			for (File toDelete : collected)
				toDelete.delete();
			manager.cleanEmptyDirs();
		}
		catch (Exception e) {e.printStackTrace();}
		System.out.println("ATTENTION : Prenez soin de retenir votre mot de passe !");
	}
	
	private static void doDecrypt() {
		try {
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
			collected = manager.collect(".mar");
			System.out.println("[DEBUG] Collected " + collected.size() + " crypted files");
			for (File file : collected) {
				byte[] clearData = decrypter.decrypt(manager.read(file));
				String clearFN = new String(Base64.decodeBase64(file.getName().replace(".mar", "")));
				String filePath = manager.generateFilePath(file, clearFN, "");
				manager.write(clearData, filePath);
				file.delete();
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
}