package com.rssoftware.ou.tenant.service;

public interface EmailService {

	public void createEmail(String body, String email, String fromEmail, String amount, String billerName, String date,
			String txnRefId, String consumerNo, String paymentChannel);

}
