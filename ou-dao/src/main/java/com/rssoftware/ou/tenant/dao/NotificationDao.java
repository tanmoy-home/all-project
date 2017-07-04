package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.NotificationTemplate;


public interface NotificationDao extends GenericDao<NotificationTemplate, String>
{
	public void createSms(String mobile,String amount,String billerName,String date, String txnRefId);
	
	NotificationTemplate getTxnSuccessTemplateByMsgType(String msgType);
	public NotificationTemplate getNotificationTemplateByMsgTypeAndEventType(String msgType,String eventType);

}
