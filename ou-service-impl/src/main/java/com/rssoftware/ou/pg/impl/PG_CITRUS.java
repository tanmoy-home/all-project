package com.rssoftware.ou.pg.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.pg.PGIntegrationService;
import com.rssoftware.ou.tenant.dao.impl.PgIntegrationFieldDaoImpl;
import com.rssoftware.ou.tenant.service.impl.ParamServiceImpl;


public class PG_CITRUS implements PGIntegrationService {
	
	private final static Logger logger = LoggerFactory.getLogger(PG_CITRUS.class);

	private Map<String, String> localVar = new HashMap<String, String>();
	
	@Override
	public List<PGParam> getPGFields(List<PGParam> reqVal) throws IsoMsgException {

		ParamServiceImpl paramService  = new  ParamServiceImpl();
		PgIntegrationFieldDaoImpl pgIntegrationFieldDao = new PgIntegrationFieldDaoImpl();

		String vanityURL = paramService.retrieveStringParamByName("citrusVanityURL", true);
		String currency = paramService.retrieveStringParamByName("citrusCurrency", true);
		String secretKey = paramService.retrieveStringParamByName("citrusSecretKey", true);

		String amount = getParamValue(reqVal, CommonConstants.PAYMENT_AMOUNT);
		//localVar.put("orderAmount", amount);
		localVar.put("orderAmount", "10.00");
		String merchantTxnId = getParamValue(reqVal, CommonConstants.TXN_REF_ID);
		localVar.put("merchantTxnId", merchantTxnId);
//		String secData = vanityURL+amount+merchantTxnId+currency;
		String secData = vanityURL+"10.00"+merchantTxnId+currency;
		localVar.put("secSignature", generateSecuritySignature(secData, secretKey));

		List<PGParam> pgFields = new ArrayList<PGParam>();

		try {
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
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}

		return pgFields;
	}
	
	@Override
	public List<PGParam> validatePGResponse(List<PGParam> reqVal) throws IsoMsgException {
		return reqVal;
	}
	
	private String substituteParamValues(String val)  {
		String paramValue = null;
		if(val == null) {
			val="";
		}
		if(val.length() >= 4 && val.substring(0,4).equals("[L${")) {
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
			if(p.getParamName()==paramName){
				return p.getParamValue();
			}
		}
		return null;
	}
	
	private String generateSecuritySignature(String data, String secKey) {
		String securitySignature = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
		    mac.init(new SecretKeySpec(secKey.getBytes("UTF-8"), "HmacSHA1"));
		    byte[] hexBytes = new org.apache.commons.codec.binary.Hex().encode(mac.doFinal(data.getBytes("UTF-8")));
		    securitySignature = new String(hexBytes, "UTF-8");			
		} catch (UnsupportedEncodingException e) {
	    } catch (InvalidKeyException e) {
	    } catch (NoSuchAlgorithmException e) {
	    }
		return securitySignature;
	}

	

	

}
