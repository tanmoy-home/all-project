package com.rssoftware.ou.pg.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.pg.PGIntegrationService;
import com.rssoftware.ou.tenant.dao.impl.PgIntegrationFieldDaoImpl;
import com.rssoftware.ou.tenant.service.impl.ParamServiceImpl;


public class PG_SBI implements PGIntegrationService {
	
	private Map<String, String> localVar = new HashMap<String, String>();
	
	private String checkSum;
	
	@Override
	public List<PGParam> getPGFields(List<PGParam> reqVal) throws Exception {
		
		ParamServiceImpl paramService  = new  ParamServiceImpl();
//		PgIntegrationFieldDaoImpl pgIntegrationFieldDao = new PgIntegrationFieldDaoImpl();
		
		//String vanityURL = paramService.retrieveStringParamByName("citrusVanityURL", true);
		
		String RU = paramService.retrieveStringParamByName("RU", true);
//		String mrCode = paramService.retrieveStringParamByName("merchantCode", true);
		

		/*String orderId = getParamValue(reqVal, CommonConstants.REF_ID);
		localVar.put("orderId", orderId);*/
		
		/*String mrCode = getParamValue(reqVal, CommonConstants.MERCHANT_CODE);
		localVar.put("merchantCode", mrCode);*/
		String rfNo = getParamValue(reqVal, CommonConstants.TXN_REF_ID);    //need to discuss with REF_ID
		localVar.put("refNo", rfNo);
		String amount = getParamValue(reqVal, CommonConstants.PAYMENT_AMOUNT);
		localVar.put("orderAmount", amount);
//		localVar.put("orderAmount", "1");
			
		
		String dataToGenerateCksm = "ref_no=" + localVar.get("refNo") + "|amount=" + localVar.get("orderAmount") + "|returnurl="+ RU;
		String checksumvalue = SBIEncryptDecrypt.generateCksm(dataToGenerateCksm);
		
		String dataToEncrypt = dataToGenerateCksm+"|checkSum="+checksumvalue;
		
		//Read key file from db and save it to local temp
		encryptData(dataToEncrypt);
		
		//String finalUrl = encData + mrCode;
		
		
//		String merchantId = getParamValue(reqVal, CommonConstants.MERCHANT_ID);
//		localVar.put("merchantId", merchantId);
/*		List<PGParam> pgFields = new ArrayList<PGParam>();
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
		
		return pgFields;*/
		return getPGParams();
	}

	@Override
	public List<PGParam> validatePGResponse(List<PGParam> reqVal) throws Exception {
		List<PGParam> updatedList = new ArrayList<PGParam>();
		String checkSum= "";
		String paramValue = "";
		/*ParamServiceImpl paramService  = new  ParamServiceImpl();
		byte[] fileContent = paramService.retrieveByteArrayParamByName("SecretKey", true);
		
		FileOutputStream fos;
		System.out.println("#####################"+System.getProperty("java.io.tmpdir")+"##########################");
		String fileLoc = System.getProperty("java.io.tmpdir")+"\\sbiKeyFile.key";*/

		if(reqVal != null) {
			for(PGParam param :reqVal) {
				String decData = decryptData(param.getParamValue());
				//String decData = SBIEncryptDecrypt.decryptData(param.getParamValue(), fileLoc);
				if(decData != null && !decData.isEmpty()) {
					for(String strParam : decData.split("\\|")) {
						String[] keyVal = strParam.split("\\=");
						String key = keyVal[0];
						String val = keyVal[1];
						if(key.equalsIgnoreCase("checksum")) {
							checkSum = val;
						}
						else {
							paramValue = paramValue.concat(key).concat("=").concat(val).concat("|");
							PGParam newParam = new PGParam();
							newParam.setParamName(key);
							newParam.setParamValue(val);
							updatedList.add(newParam);
						}
					}
					
					paramValue = paramValue.substring(0, paramValue.length()-1);
					String checksumvalue_new = SBIEncryptDecrypt.generateCksm(paramValue);
					
					//updatedList = validateChecksumAndUpdateList(decData);
					
					if((checkSum.trim().equals(checksumvalue_new.trim()))) {
						if(sendPost(updatedList)) {
							return updatedList;
						}
					}
					/*if(!(checkSum.trim().equals(checksumvalue_new.trim()))) 
						return null;
					else {
						if(sendPost(updatedList)) {
							return updatedList;
						}
					}*/
					/*if(sendPost(updatedList)) {
						return updatedList;
					}*/
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
	
	private boolean sendPost(List<PGParam> param) throws Exception {

		ParamServiceImpl paramService = new ParamServiceImpl();

		String url = System.getProperty("double.veri.url");
			//"https://merchant.onlinesbi.com/thirdparties/doubleverification.htm"; // "https://www.onlinesbi.com/thirdparties/doubleverification.htm";

		HttpClient client = new DefaultHttpClient();

		// PostMethod postMethod = new PostMethod(url);
		HttpPost post = new HttpPost(url);
		String paramVal = "";
		for(PGParam p:param) {
			paramVal = paramVal.concat(p.getParamName()).concat("=").concat(p.getParamValue()).concat("|");
		}
		paramVal = paramVal.substring(0, paramVal.length()-1);

		String checksumvalue = SBIEncryptDecrypt.generateCksm(paramVal);

		String dataToEncrypt = paramVal + "|checkSum=" + checksumvalue;

		// Read key file from db and save it to local temp
		encryptData(dataToEncrypt);
		List<PGParam> params = getPGParams();

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		for(PGParam p : params) {
			urlParameters.add(new BasicNameValuePair(p.getParamName(), p.getParamValue()));			
		}
		// String finalURL = url + encData +"&Merchant_code=" + mrCode;
/*		urlParameters.add(new BasicNameValuePair("URL", "url"));
		urlParameters.add(new BasicNameValuePair("encdata", "encData"));
		urlParameters.add(new BasicNameValuePair("Merchant_code", "mrCode"));
*/		/*
		 * urlParameters.add(new BasicNameValuePair("mrRefNo", "680011"));
		 * urlParameters.add(new BasicNameValuePair("amnt", "1"));
		 * urlParameters.add(new BasicNameValuePair("status", "success"));
		 */

		System.out.println(urlParameters);

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result.toString());
		
		String decData = decryptData(result.toString());
		if(decData != null && !decData.isEmpty()) {
			List<PGParam> updatedList = new ArrayList<PGParam>();
			updatedList = validateWithOldChecksum(decData);
			if(updatedList != null && !updatedList.isEmpty()){
				return true;
			}
		}
		/*List<String> myList = new ArrayList<String>(Arrays.asList(s.split(",")));
		System.out.println(myList);*/

		return false;
		/*
		 * if (post.getStatusCode() == HttpStatus.SC_OK) { String resp =
		 * post.getResponseBodyAsString(); } else {
		 * //...postMethod.getStatusLine(); }
		 */

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
	
	private void encryptData(String dataToEncrypt) {
		ParamServiceImpl paramService  = new  ParamServiceImpl();
		byte[] fileContent = paramService.retrieveByteArrayParamByName("SecretKey", true);
		
		FileOutputStream fos;
		String fileLoc = System.getProperty("java.io.tmpdir")+"\\sbiKeyFile.key";
		
		try {
			fos = new FileOutputStream(fileLoc);
			fos.write(fileContent);
			fos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();			
		}
		String encData = SBIEncryptDecrypt.encryptData(dataToEncrypt, fileLoc);
		localVar.put("encData", encData);
	}
	
	private String decryptData(String dataToDecrypt){
		ParamServiceImpl paramService  = new  ParamServiceImpl();
		byte[] fileContent = paramService.retrieveByteArrayParamByName("SecretKey", true);
		
		FileOutputStream fos;
		String fileLoc = System.getProperty("java.io.tmpdir")+"\\sbiKeyFile.key";
		
		try {
			fos = new FileOutputStream(fileLoc);
			fos.write(fileContent);
			fos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();			
		}
		String decData = SBIEncryptDecrypt.decryptData(dataToDecrypt, fileLoc);
		return decData;
	}
	
	private List<PGParam> validateWithOldChecksum(String decData){
		List<PGParam> updatedList = new ArrayList<PGParam>();
		String checkSumNew= "";
		String paramValue = "";
		
			for(String strParam : decData.split("\\|")) {
				String[] keyVal = strParam.split("\\=");
				String key = keyVal[0];
				String val = keyVal[1];
				if(key.equalsIgnoreCase("checksum")) {
					checkSumNew = val;
				}
				else {
					paramValue = paramValue.concat(key).concat("=").concat(val).concat("|");
					PGParam newParam = new PGParam();
					newParam.setParamName(key);
					newParam.setParamValue(val);
					updatedList.add(newParam);
				}
			}
						
			if((checkSum.trim().equals(checkSumNew.trim()))) {
				return updatedList;
			}
		return null;
	}

	

	

}
 
