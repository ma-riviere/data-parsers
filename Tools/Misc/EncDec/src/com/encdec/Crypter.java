package com.encdec;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Crypter extends Banane {

	private String pass = null;
	
	public Crypter(String pass) {
		this.pass = pass;
	}

	public byte[] crypt(byte[] clearData) {
		try {
			byte[] passInBytes = Arrays.copyOf(DigestUtils.sha1(pass), 16);
			SecretKeySpec clef = new SecretKeySpec(passInBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, clef);
			return cipher.doFinal(clearData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors de l'encryptage des donnees : ");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean crypt2zip(File archive, List<File> filesToAdd) {
		
		try {
			ArrayList<File> files = new ArrayList<File>(filesToAdd);
			ZipFile zipFile = new ZipFile(archive);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
			parameters.setPassword(pass);
			for (File file : files) {
				parameters.setRootFolderInZip(manager.getRelativePathWithoutFN(file));
				zipFile.addFile(file, parameters);
			}
		} catch (ZipException e) {
			System.out.println("Erreur lors de l'archivage des donnees : ");
			// e.printStackTrace();
			return false;
		}
		return true;
	}
}