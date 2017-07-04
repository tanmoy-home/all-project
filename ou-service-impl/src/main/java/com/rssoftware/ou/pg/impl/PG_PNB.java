package com.rssoftware.ou.pg.impl;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.pg.PGIntegrationService;
import com.rssoftware.ou.tenant.dao.impl.PgIntegrationFieldDaoImpl;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.impl.ParamServiceImpl;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PG_PNB implements PGIntegrationService {

	private Map<String, String> localVar = new HashMap<String, String>();
	
	@Override
	public List<PGParam> getPGFields(List<PGParam> reqVal) throws NoSuchAlgorithmException, IOException, Exception {
		
		ParamService paramService  = BeanLocator.getBean(ParamServiceImpl.class);//new  ParamServiceImpl();
		String RU = paramService.retrieveStringParamByName("RU");
		

		/*String orderId = getParamValue(reqVal, CommonConstants.REF_ID);
		localVar.put("orderId", orderId);*/
		
		/*String mrCode = getParamValue(reqVal, CommonConstants.MERCHANT_CODE);
		localVar.put("merchantCode", mrCode);*/
		String rfNo = getParamValue(reqVal, CommonConstants.TXN_REF_ID);    //need to discuss with REF_ID
		localVar.put("refNo", rfNo);
		String amount = getParamValue(reqVal, CommonConstants.PAYMENT_AMOUNT);
		localVar.put("orderAmount", amount);
//		localVar.put("orderAmount", "1");
		SimpleDateFormat ft = new SimpleDateFormat ("d/M/yyyy HH:mm:ss");
		String txnDate = ft.format(new Date());
		localVar.put("txnDate", txnDate);
		
		String dataToGenerateCksm = "cin=" + localVar.get("refNo") + "|merchantdate=" + txnDate + "|amt=" + localVar.get("orderAmount") + "|RU="+ RU;
		String checksumvalue = generateCksm(dataToGenerateCksm);
		
		String dataToEncrypt = dataToGenerateCksm+"|checksum="+checksumvalue;
		byte[] key;
		
		String encodekey = paramService.retrieveStringParamByName("SECRET_KEY");
		if(encodekey.trim() == null || encodekey.trim().isEmpty()) {
			//Read key file from db and save it to local temp
			key = paramService.retrieveByteArrayParamByName("SecretKey");
		}
		else {
			key = encodekey.getBytes("UTF-8");			
		}
		encrypt(dataToEncrypt, key);
		
		return getPGParams();
	}

	@Override
	public List<PGParam> validatePGResponse(List<PGParam> reqVal) throws NoSuchAlgorithmException, IOException, Exception {
		System.out.println("in validatePGResponse");
		List<PGParam> updatedList = new ArrayList<PGParam>();
		String checkSum= "";
		String paramValue = "";
		ParamService paramService  = BeanLocator.getBean(ParamServiceImpl.class);//new  ParamServiceImpl();
		//ParamServiceImpl paramService  = new  ParamServiceImpl();
		byte[] key;
		
		String encodekey = paramService.retrieveStringParamByName("SECRET_KEY");
		System.out.println("Encode Key:" + encodekey);
		
		if(encodekey.trim() == null || encodekey.trim().isEmpty()) {
			//Read key file from db and save it to local temp
			key = paramService.retrieveByteArrayParamByName("SecretKey");
		}
		else {
			key = encodekey.getBytes("UTF-8");			
		}
//		encrypt(dataToEncrypt, key);

		if(reqVal != null) {
			for(PGParam param :reqVal) {
				if(param.getParamName().equalsIgnoreCase("encdata")) {
						
					String decData = decrypt(param.getParamValue(), encodekey);
					if(decData != null && !decData.isEmpty()) {
						for(String strParam : decData.split("\\|")) {
							String[] keyVal = strParam.split("\\=");
							String keyofparams = keyVal[0];
							String valofparams = keyVal[1];
							if(keyofparams.equalsIgnoreCase("checksum")) {
								checkSum = valofparams;
							}
							else {
								paramValue = paramValue.concat(keyofparams).concat("=").concat(valofparams).concat("|");
								PGParam newParam = new PGParam();
								newParam.setParamName(keyofparams);
								newParam.setParamValue(valofparams);
								updatedList.add(newParam);
							}
						}
						
						paramValue = paramValue.substring(0, paramValue.length()-1);
						String checksumvalue_new = generateCksm(paramValue);
						
						if(!(checkSum.trim().equals(checksumvalue_new.trim()))) 
							return null;
						else {
							return updatedList;
						}
					}
					return null;
				}
			}
		}
		return null;
	}

		
	

	private String substituteParamValues(String val)  {
		String paramValue = null;
		if(val.substring(0,4).equals("[L${"))  {
			String param = val.substring(4, val.length()-3);
			paramValue = localVar.get(param);			
		}
		else
		{
			paramValue  = val;
		}
		return paramValue;
	}
	
	private String getParamValue(List<PGParam> paramList, String  paramName)  {
		for(PGParam p:paramList){
			if(p.getParamName().equalsIgnoreCase(paramName)){
				return p.getParamValue();
			}
		}
		return null;
	}
	
	private String generateCksm(String delimited_text) throws NoSuchAlgorithmException, IOException{
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(delimited_text.getBytes());
	    byte byteData[] = md.digest();
	    StringBuffer hexString = new StringBuffer();
		for (int i=0;i<byteData.length;i++) {
		String hex=Integer.toHexString(0xff & byteData[i]);
	    if(hex.length()==1) hexString.append('0');
	   	    hexString.append(hex);		
		}
	    return hexString.toString();
	}
		
	private void encrypt(String cksumData, byte[] key) throws Exception {
	        Cipher c = Cipher.getInstance("AES");
	        //byte key[] = encodekey.getBytes("UTF-8");
	        MessageDigest sha = MessageDigest.getInstance("SHA-1");
	        key = sha.digest(key);
	        key = Arrays.copyOf(key, 16);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
	        c.init(1, secretKeySpec);
	        byte encVal[] = c.doFinal(cksumData.getBytes());
	        String encryptedValue = (new BASE64Encoder()).encode(encVal);
	        localVar.put("encData", encryptedValue); 
	}
		
	private static String decrypt(String encryptedData, String encodekey) throws Exception {
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
	
	private List<PGParam> getPGParams() {
		
		List<PGParam> pgFields = new ArrayList<PGParam>();
		PgIntegrationFieldDaoImpl pgIntegrationFieldDao = new PgIntegrationFieldDaoImpl();
		
		try {
			//PgIntegrationFieldDaoImpl pgIntegrationFieldDao = new PgIntegrationFieldDaoImpl();
			
			List<PgIntegrationField> allFields = pgIntegrationFieldDao.getAll();

			
			if (allFields != null){
				for (PgIntegrationField field:allFields){
					if (PGIntegrationFieldType.REQ.name().equals(field.getId().getFieldType())){
						PGParam param = new PGParam();
						param.setParamName(field.getId().getFieldName());
						param.setParamValue(substituteParamValues(field.getFieldValue()));
						pgFields.add(param);
					}
				}
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
			//throw new Exception(e);
		}		
		return pgFields;
	}

	
	
}
