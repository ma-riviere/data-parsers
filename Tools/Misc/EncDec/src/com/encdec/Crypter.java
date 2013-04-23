package com.encdec;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {

	private static String algo = "Blowfish";
	private String pass = null;
	
	public Crypter(String pass) {
		this.pass = pass;
	}

	public byte[] crypt(byte[] clearData) {
		try {
			byte[] passInBytes = pass.getBytes("UTF-8"); 
			Key clef = new SecretKeySpec(passInBytes, algo); 
			Cipher cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, clef);
			return cipher.doFinal(clearData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors de l'encryptage des donnees : " + e);
			return null;
		}
	}
}