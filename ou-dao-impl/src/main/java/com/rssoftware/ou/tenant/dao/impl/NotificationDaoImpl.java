package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.NotificationEventType;
import com.rssoftware.ou.database.entity.tenant.NotificationTemplate;
import com.rssoftware.ou.tenant.dao.NotificationDao;

@Repository
public class NotificationDaoImpl extends GenericDynamicDaoImpl<NotificationTemplate, String>implements NotificationDao {

	public static final String GET_SMS_TEMPLATE_BY_EVENTTYPE = "Select n from NotificationTemplate n where n.communicationType=:communicationType and n.eventType=:eventType";
	public static final String GET_SMS_TEMPLATE_BY_EVENT_COMMUN_TYPE = "Select n from NotificationTemplate n where n.communicationType=:communicationType and n.eventType=:eventType";

	public NotificationDaoImpl() {
		super(NotificationTemplate.class);
	}

	@Override
	public void createSms(String mobile, String amount, String billerName, String date, String txnRefId) {
	}

	@Override	
	public NotificationTemplate getTxnSuccessTemplateByMsgType(String msgType) {
		NotificationTemplate template = (NotificationTemplate) getSessionFactory().getCurrentSession()
				.createQuery(GET_SMS_TEMPLATE_BY_EVENTTYPE).setString("communicationType", msgType)
				.setString("eventType", NotificationEventType.TRANSACTION_SUCCESS.name()).uniqueResult();

		return template;
	}
	
	@Override	
	public NotificationTemplate getNotificationTemplateByMsgTypeAndEventType(String msgType,String eventType) {
		NotificationTemplate template = (NotificationTemplate) getSessionFactory().getCurrentSession()
				.createQuery(GET_SMS_TEMPLATE_BY_EVENT_COMMUN_TYPE).setString("communicationType", msgType)
				.setString("eventType", eventType).uniqueResult();

		return template;
	}
}
