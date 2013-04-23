package com.encdec;

import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Crypter {

	private char[] pass = null;
	private static byte[] salt = {
        (byte)0xd8, (byte)0x43, (byte)0x41, (byte)0x8c,
        (byte)0x7e, (byte)0xa2, (byte)0xef, (byte)0x56
    };
	
	public Crypter(String pass) {
		this.pass = pass.toCharArray();
	}

	public byte[] crypt(byte[] clearData) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(pass, salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			return cipher.doFinal(clearData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors de l'encryptage des donnees : " + e);
			return null;
		}
	}
	
	/*
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
	*/
}