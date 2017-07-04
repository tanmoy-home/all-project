package com.rssoftware.ou.pg.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.pg.PGIntegrationService;
import com.rssoftware.ou.tenant.dao.impl.PgIntegrationFieldDaoImpl;


public class PG_IDBI implements PGIntegrationService {
	
	private final static Logger logger = LoggerFactory.getLogger(PG_IDBI.class);
	
	private Map<String, String> localVar = new HashMap<String, String>();
	
	@Override
	public List<PGParam> getPGFields(List<PGParam> reqVal) throws IsoMsgException {
		
		String orderId = getParamValue(reqVal, CommonConstants.TXN_REF_ID);
		localVar.put("orderId", orderId);
		String amount = getParamValue(reqVal, CommonConstants.PAYMENT_AMOUNT);
		localVar.put("AMT", amount);
		
		List<PGParam> pgFields = new ArrayList<PGParam>();
		try {
			PgIntegrationFieldDaoImpl pgIntegrationFieldDao = new PgIntegrationFieldDaoImpl();
			
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
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());		
	        //throw new Exception(e);
		}
		return pgFields;
	}

	
	@Override
	public List<PGParam> validatePGResponse(List<PGParam> reqVal) throws IsoMsgException {
		// TODO Auto-generated method stub
		return reqVal;
	}

	
	private String substituteParamValues(String val)  {
		String paramValue = null;
		if(val.length() > 4 && val.substring(0,4).equals("[L${"))  {
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


	

	

}
