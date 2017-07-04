package com.rssoftware.ou.batch.processor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.FinRecon;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.FinReconStatus;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.PGParamParser;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.database.entity.tenant.TenantParam;
import com.rssoftware.ou.tenant.dao.impl.FinTransactionDataDaoImpl;
import com.rssoftware.ou.tenant.dao.impl.TenantParamDaoImpl;

public class FinReconItemProcessor implements ItemProcessor<FinRecon, FinRecon> {
	private static ObjectMapper mapper = new ObjectMapper();
	@Autowired
	FinTransactionDataDaoImpl finTransactionDataDao;
	@Autowired
	TenantParamDaoImpl tenantParamDao;
	private String reversalStatusList = "RESPONSE_REVERSE,SENT,TIMEOUT,SEND_FAILED,SEND_FAILED_ACK,RESPONSE_DECLINE";

	@Override
	public FinRecon process(FinRecon item) throws Exception {
		
	    final List<String> revStatusList = Arrays.asList(reversalStatusList.split(","));

		FinRecon processedItem = new FinRecon();
		processedItem.setTxnRefId(item.getTxnRefId());
		processedItem.setAuthCode(item.getAuthCode());
		
		if(item.getUpdtTs()!=null)
			processedItem.setTxnTime(item.getUpdtTs());
		else
			processedItem.setTxnTime(item.getCrtnTs());
		processedItem.setAmount(item.getTotalAmount());

		/*if(item.getRequestJson()!=null) {
		    List<PGParam> pgParamList = mapper.readValue(item.getRequestJson(), mapper.getTypeFactory().constructCollectionType(List.class, PGParam.class));
		 // Invoke PG Specific Class
			String PG_CLASS = tenantParamDao.get("PG_NAME").getParamValueChar();
			String className = "com.rssoftware.ou.pg.impl.PG_" + PG_CLASS.toUpperCase();
			String amountTag = null;
			Class<?> c;
			
				
					c = Class.forName(className);
					Object t = c.newInstance();
					Method m = c.getMethod("getAmountTag");
					Object ret = m.invoke(t);
					if (ret != null) {
						amountTag  = (String) ret;
					}
					
				
		    PGParamParser parser = new PGParamParser(pgParamList);
		    String amount = parser.get(amountTag);
		    		processedItem.setAmount(amount);
		}*/

	    
	    /*String s = new String(item.getRequestJson());
	      if(s.indexOf("amount_return")!=-1) {
	    	s=s.substring(s.indexOf("amount_return"), s.length());
	    	s=s.substring(s.indexOf(":"), s.length());
	    	s=s.substring(s.indexOf("\"")+1, s.length());
	    	s=s.substring(0, s.indexOf("\""));
	    }
			processedItem.setAmount(s);*/

		/*ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(s.getBytes()));
		try {
		   paramList = (List<PGParam>) ois.readObject();
		} finally {
		    ois.close();
		}
		if(!paramList.isEmpty()) {
		PGParamParser object = new PGParamParser(paramList);*/
		//processedItem.setAmount(object.get("Amount"));
		//TBD NULL CHK
		if (item.getTxnCurrentStatus()!=null && revStatusList.contains(item.getTxnCurrentStatus()))
			processedItem.setStatus(FinReconStatus.RESPONSE_REVERSE.name());
		else if (item.getTxnCurrentStatus()!=null && item.getFinCurrentStatus()!=null && RequestStatus.valueOf(item.getTxnCurrentStatus())==RequestStatus.QPAY_INITIATED && RequestStatus.valueOf(item.getFinCurrentStatus())==RequestStatus.RESPONSE_SUCCESS)
			processedItem.setStatus(FinReconStatus.RESPONSE_REVERSE.name());
		else if(item.getFinCurrentStatus()!=null && RequestStatus.valueOf(item.getFinCurrentStatus())==RequestStatus.SENT)
			processedItem.setStatus(FinReconStatus.RESPONSE_NOT_RECEIVED.name());
		
		else if(item.getFinCurrentStatus()!=null && (RequestStatus.RESPONSE_SUCCESS==RequestStatus.valueOf(item.getFinCurrentStatus())) && item.getTxnCurrentStatus()==null)
			processedItem.setStatus(FinReconStatus.RESPONSE_REVERSE.name());
		
		else if(item.getFinCurrentStatus()!=null && (RequestStatus.RESPONSE_SUCCESS==RequestStatus.valueOf(item.getFinCurrentStatus())) && item.getTxnCurrentStatus()!=null && RequestStatus.RESPONSE_SUCCESS==RequestStatus.valueOf(item.getTxnCurrentStatus()))
			processedItem.setStatus(FinReconStatus.RESPONSE_SUCCESS.name());
		
		else if(item.getFinCurrentStatus()!=null && (RequestStatus.REFUND_SUCCESS==RequestStatus.valueOf(item.getFinCurrentStatus())))
			processedItem.setStatus(FinReconStatus.REFUND_SUCCESS.name());

		else if(item.getFinCurrentStatus()!=null && (RequestStatus.REFUND_DECLINE==RequestStatus.valueOf(item.getFinCurrentStatus())))
				processedItem.setStatus(FinReconStatus.REFUND_DECLINE.name());
		
		else 
			processedItem.setStatus(FinReconStatus.RESPONSE_FAILURE.name());
		
		finTransactionDataDao.updateReconStatus(processedItem.getTxnRefId());
	
		return processedItem;
}
}