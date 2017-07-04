package com.rssoftware.ou.common.rest;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;


public class EmailSender {
	
	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);
	

	public static void sendMail(String recipients, String fromEmail, String[] ccRecipients,
			String[] bccRecipients, String subject, String content, File attachment) {
		try {
			logger.info("Inside sendMail method......");			
			JavaMailSender javaMailSender=	BeanLocator.getBean(CommonConstants.BEAN_MAIL_SENDER_PREFIX + TransactionContext.getTenantId());// BeanLocator.getBean(JavaMailSender.class);
			logger.info("JavaMailSender object found ......" + javaMailSender);
			MimeMessage message = javaMailSender.createMimeMessage();

			MimeMessageHelper helper=null;
			try {
				helper = new MimeMessageHelper(message, true);
			} catch (javax.mail.MessagingException e) {
				e.printStackTrace();
			}
			
			
			if(fromEmail != null)
				helper.setFrom(fromEmail);

			if (recipients != null )
				helper.setTo(recipients);
			if (null != bccRecipients && bccRecipients.length > 0)
				helper.setBcc(bccRecipients);
			if (null != ccRecipients && ccRecipients.length > 0)
				helper.setCc(ccRecipients);
			helper.setSubject(subject);
			helper.setText(content, true);
			if (null != attachment){
				helper.addAttachment(attachment.getName(), attachment);
			}

			javaMailSender.send(message);
			logger.info("Mail sent successfully to " + recipients);
		} catch (MailException e) {
			logger.info("MailException occur sending email:" + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (javax.mail.MessagingException e) {
			logger.info("MessagingException occur sending email:" + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("Exception occur sending email:" + e.getMessage());
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
}