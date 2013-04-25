package com.encdec;

import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class Crypter {

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
			System.out.println("Erreur lors de l'encryptage des donnees : " + e);
			return null;
		}
	}
}