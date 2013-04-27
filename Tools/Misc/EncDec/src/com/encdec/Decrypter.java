package com.encdec;

import java.io.File;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Decrypter {

	private String pass = null;
	
	public Decrypter(String pass) {
		this.pass = pass;
	}

	public byte[] decrypt(byte[] cryptedData) {
		try {
			byte[] passInBytes = Arrays.copyOf(DigestUtils.sha1(pass), 16);
			SecretKeySpec clef = new SecretKeySpec(passInBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, clef);
			return cipher.doFinal(cryptedData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors du decryptage des donnees : " + e);
			return null;
		}
	}
	
	public boolean decryptZip(File archive) {
		try {
			ZipFile zipFile = new ZipFile(archive);
			if (zipFile.isEncrypted()) {zipFile.setPassword(pass);}
			zipFile.extractAll("./");
			return true;
		}
		catch (ZipException e) {
			System.out.println("Erreur lors de l'extraction des donnees : ");
			e.printStackTrace();
			return false;
		}
	}
}