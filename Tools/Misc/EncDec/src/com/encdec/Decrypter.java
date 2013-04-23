package com.encdec;

import java.security.Key; 
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec; 

public class Decrypter {

	private static String algo = "Blowfish";
	private String pass = null;
	
	public Decrypter(String pass) {
		this.pass = pass;
	}

	public byte[] decrypt(byte[] cryptedData) {
		try {
			byte[] passInBytes = pass.getBytes("UTF-8"); 
			Key clef = new SecretKeySpec(passInBytes, algo); 
			Cipher cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.DECRYPT_MODE, clef);
			return cipher.doFinal(cryptedData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors du decryptage des donnees : " + e);
			return null;
		}
	}
}