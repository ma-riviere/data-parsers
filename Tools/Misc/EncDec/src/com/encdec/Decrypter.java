package com.encdec;

import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Decrypter {

	private char[] pass = null;
	private static byte[] salt = {
        (byte)0xd8, (byte)0x43, (byte)0x41, (byte)0x8c,
        (byte)0x7e, (byte)0xa2, (byte)0xef, (byte)0x56
    };
	
	public Decrypter(String pass) {
		this.pass = pass.toCharArray();
	}

	public byte[] decrypt(byte[] cryptedData) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(pass, salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret);
			return cipher.doFinal(cryptedData);
		}
		catch (Exception e) {
			System.out.println("Erreur lors du decryptage des donnees : " + e);
			return null;
		}
	}
}