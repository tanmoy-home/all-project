package com.rssoftware.ou.batch.processor;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.BOUBlrRecon;
import com.rssoftware.ou.common.FinReconStatus;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.tenant.dao.impl.BillerOUtransactionDataDaoImpl;
import com.rssoftware.ou.tenant.dao.impl.TenantParamDaoImpl;

public class BOUBlrReconItemProcessor implements ItemProcessor<BOUBlrRecon, BOUBlrRecon> {

	@Autowired
	BillerOUtransactionDataDaoImpl billerOUtransactionDataDao;
	@Autowired
	TenantParamDaoImpl tenantParamDao;
	private String reversalStatusList = "RESPONSE_REVERSE,TIMEOUT,SEND_FAILED,SEND_FAILED_ACK,RESPONSE_DECLINE";

	@Override
	public BOUBlrRecon process(BOUBlrRecon item) throws Exception {
		
	    final List<String> revStatusList = Arrays.asList(reversalStatusList.split(","));

	    BOUBlrRecon processedItem = new BOUBlrRecon();
		processedItem.setTxnRefId(item.getTxnRefId());
		processedItem.setMobile(item.getMobile());
		processedItem.setTxnDate(item.getTxnDate());
		processedItem.setBillAmount(item.getBillAmount());

		if (item.getBlrRevStatus()!=null && revStatusList.contains(item.getBlrRevStatus()))
			processedItem.setStatus(FinReconStatus.RESPONSE_REVERSE.name());
		
		else if(item.getBlrRevStatus()==null && (RequestStatus.valueOf(item.getBlrStatus())==RequestStatus.SENT || RequestStatus.valueOf(item.getBlrStatus())==RequestStatus.QPAY_INITIATED))
			processedItem.setStatus(FinReconStatus.RESPONSE_NOT_RECEIVED.name());
		
		else if(item.getBlrRevStatus()==null && (RequestStatus.RESPONSE_SUCCESS==RequestStatus.valueOf(item.getBlrStatus())))
			processedItem.setStatus(FinReconStatus.RESPONSE_SUCCESS.name());
		
		else 
			processedItem.setStatus(FinReconStatus.RESPONSE_FAILURE.name());
		
		billerOUtransactionDataDao.updateBOUBlrReconStatus(processedItem.getTxnRefId());
	
		return processedItem;
}
}