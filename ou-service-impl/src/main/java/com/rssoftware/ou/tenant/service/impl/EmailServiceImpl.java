package com.rssoftware.ou.tenant.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.NotificationEventType;
import com.rssoftware.ou.common.NotificationType;
import com.rssoftware.ou.common.rest.EmailSender;
import com.rssoftware.ou.database.entity.tenant.NotificationTemplate;
import com.rssoftware.ou.tenant.dao.NotificationDao;
import com.rssoftware.ou.tenant.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

	private static String TXN_ID = "##TransactionReferanceNo##";
	private static String TXN_AMOUNT = "##Amount##";
	private static String DATE = "##TransactionDate##";
	private static String BENEFICIARY_NAME = "##BillerName##";
	private static String CONSUMER_NO = "##ConsumerNo##";
	private static String TRANSACTION_STATE = "##TransactionState##";
	private static String PAYMENT_CHANNEL = "##PaymentChannel##";


	@Override
	public void createEmail(String body, String email, String fromEmail, String amount, String billerName, String date,
			String txnRefId, String consumerNo, String paymentChannel) {

		logger.info("Inside createEmail method......");

		String notificationMsg = null;
		try {

			notificationMsg = body.replace(TXN_ID, txnRefId).replace(DATE, date).replace(BENEFICIARY_NAME, billerName)
					.replace(CONSUMER_NO, consumerNo)
					.replace(TRANSACTION_STATE, NotificationEventType.TRANSACTION_SUCCESS.name())
					.replace(TXN_AMOUNT, amount).replace(PAYMENT_CHANNEL, paymentChannel);

			EmailSender.sendMail(email, fromEmail, null, null, "Bill Payment Receipt", notificationMsg, null);
		} catch (Exception ex) {
			logger.info("Error in email sending:: " + ex.getMessage());
		}

	}
}