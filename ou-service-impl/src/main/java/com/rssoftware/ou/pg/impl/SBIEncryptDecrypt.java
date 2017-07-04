package com.rssoftware.ou.pg.impl;

import Encryption.AESEncrypt;
import Encryption.ChecksumMD5;

public class SBIEncryptDecrypt {
	
	
	
//	public static void main(String[] args){
//		String sourceData = "Q3nmRwjZgACOAzg2UzLPK4k1urBZ/BeNRfcUvMZSlfzSrZoHKlE5X3B012kvWJzSspBAPJmWulQN\nOQhgo5SkwKhGzYZysuchFFxNCRi68wyg0N0I/q9Edey930k9nVfpQPi21Af/8L5UnarbzRyKpQLc\n6MwcSySlzL4x7cOmeiw=";
//		//String sourceData = "key1=value1|key2=value2|key3=value3";
//		//String encryptedData = encryptData(sourceData, "D:/Project/BBOU_DOC/BHARAT_BILLS.key");
//		//System.out.println(encryptedData);
//		sourceData = sourceData.replace("/n", "").replace("/r", "");
//		
//		//System.out.println(encryptedData);
//		//System.out.println("EncData 1 : " + encData1);
//		decryptData(sourceData, "D:/Project/BBOU_DOC/BHARAT_BILLS.key");
//	}
	
	public static String generateCksm(String data) {
		String checksumvalue = "";
		
		ChecksumMD5 checksum = new ChecksumMD5();
		try {
			checksumvalue = checksum.getValue(data);
			System.out.println("The checkSum value for inputValue is: "+checksumvalue);
		} catch (Exception e) {
			System.out.println("the exception is: "+e.getMessage());
		}
		return checksumvalue;
	}
	public static String encryptData(String data, String fileLoc) {
		String encdata = "";
		System.out.println("################################# Data For Encyption ########################################");
		System.out.println("#################################  data is: "+data);
		try{
			AESEncrypt encrypt = new Encryption.AESEncrypt();                 
			//encrypt.setSecretKey("D:\\Project\\BBOU_DOC\\SBISecretkey.key"); //need to change path
			encrypt.setSecretKey(fileLoc);
			encdata = encrypt.encryptFile(data);
			System.out.println("################################# The Encrypted data is: "+encdata);
			
		} catch (Exception e) {
			System.out.println("################################# the exception is: "+e.getMessage());
			e.printStackTrace();
		}
		return encdata;
	}
	
	public static String decryptData(String data, String fileLoc) {
		String decdata = "";
		System.out.println("################################# Data For Decryption ########################################");
		System.out.println("#################################  data is: "+data);
		try{
			AESEncrypt encrypt = new Encryption.AESEncrypt();                 
			//encrypt.setSecretKey("D:\\Project\\BBOU_DOC\\SBISecretkey.key"); //need to change path
			encrypt.setSecretKey(fileLoc);
			decdata = encrypt.decryptFile(data);
			System.out.println("################################# The Decrypted data is: "+decdata);
			
		} catch (Exception e) {
			System.out.println("################################# the exception is: "+e.getMessage());
			e.printStackTrace();
		}
		return decdata;
		
	}
}
