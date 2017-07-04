package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.SMSDetailsView;
import com.rssoftware.ou.model.tenant.SmsConfigView;
import com.rssoftware.ou.model.tenant.SmsMessageView;

public interface SmsService 
{
	/*@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void createSms(String mobile,String amount,String billerName,String date, String txnRefId);*/
	
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String createSms(SMSDetailsView smsDetailsView);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String sendSms(String mobNumber,String message ,String tenantId, String sendType, SmsMessageView.SMSType type,String baseUrl ) throws ValidationException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public SmsConfigView getSmsConfig(String tenantId,String sendType)throws ValidationException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	String saveSms(SMSDetailsView smsDetailsView);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	String getTxnSuccessTemplateByMsgType(String msgType);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	String getNotificationTemplateByMsgTypeAndEventType(String msgType, String eventType);
}
