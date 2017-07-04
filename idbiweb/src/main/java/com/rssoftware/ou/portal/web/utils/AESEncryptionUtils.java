package com.rssoftware.ou.portal.web.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESEncryptionUtils {

	/*public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IOException {

		String plainText = "cin=don1458795625|name=vinay kumar gupta|address=22 delhi";
		String respURL = "uD%2BupsugI0N5ZK19Bkuj53GLLVszFUpgz3NTxxT2AzwDaBj4ehyQa6veZpRtlgbIyyp0XAZzLjMOQtCKYmweX9DiqA7GPebUtLzmw3NoqE75dcj%2BdaqWUbHQ4UKK1hgP%0D%0AvS90WxZCxFN9UsvXnV8v9A%3D%3D";
		String respText = "uD+upsugI0N5ZK19Bkuj53GLLVszFUpgz3NTxxT2AzwDaBj4ehyQa6veZpRtlgbIyyp0XAZzLjMOQtCKYmweX9DiqA7GPebUtLzmw3NoqE75dcj+daqWUbHQ4UKK1hgPvS90WxZCxFN9UsvXnV8v9A==";
		String keyText = "test@123!#";
		Key key;
		SecureRandom rand = new SecureRandom();
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(rand);
		generator.init(128);
		key = generator.generateKey();
		
		String result = java.net.URLDecoder.decode(respURL, "UTF-8");
		
		
		System.out.println("Original  Text === " + plainText);
		System.out.println("Response   URL === " + result);
		System.out.println("Encrypted Text === " + encrypt(plainText, keyText.getBytes()));
		System.out.println("Decrypted Text ==== " + decrypt(encrypt(plainText, keyText.getBytes()), keyText));
		System.out.println("Decrypted Response Text ==== " + decrypt(respURL, keyText));
	}*/
	
	public static String generateMD5Checksum(String originalMsg) {
	   	
		try {
			return AESEncryptionUtils.generateChecksum(originalMsg, "MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String generateChecksum(String originalMsg, String algorithmName) throws NoSuchAlgorithmException {
		// Algorithms: ["SHA1", "MD5"]

	    MessageDigest md = MessageDigest.getInstance(algorithmName);
	    byte[] dataBytes = originalMsg.getBytes();

	    //md.update(dataBytes, 0, nread);

	    byte[] mdbytes = md.digest(dataBytes);

	    //convert the byte to hex format
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < mdbytes.length; i++) {
	    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	    }

	    //System.out.println("Digest(in hex format):: " + sb.toString());
		
	    return sb.toString();
	}
	
	public static Key genRandumKey() throws NoSuchAlgorithmException {
		Key key;
		SecureRandom rand = new SecureRandom();
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(rand);
		generator.init(128);
		key = generator.generateKey();
		return key;
	}

	/*public static String encrypt(String plainText, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),
		// "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

		return (new Base64().encodeAsString(encryptedTextBytes));
	}

	public static String decrypt(String encryptedText, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		// SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),
		// "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

		return new String(decryptedTextBytes);
	}*/
	
	public static String encrypt(String cksumData, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		c.init(1, secretKeySpec);
		byte encVal[] = c.doFinal(cksumData.getBytes());
		String encryptedValue = (new BASE64Encoder()).encode(encVal);
		return (encryptedValue);
	}

	public static String decrypt(String encryptedData, String encodekey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, IOException {
		Cipher c = Cipher.getInstance("AES");
		byte key[] = encodekey.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		c.init(2, secretKeySpec);
		byte decordedValue[] = (new BASE64Decoder()).decodeBuffer(encryptedData);
		byte decValue[] = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

}
