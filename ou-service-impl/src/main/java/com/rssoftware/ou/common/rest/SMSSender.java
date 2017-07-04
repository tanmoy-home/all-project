package com.rssoftware.ou.common.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.model.tenant.SMSDetailsView;
import com.rssoftware.ou.tenant.service.ParamService;

public class SMSSender {

	private static Logger log = Logger.getLogger(SMSSender.class);

	public static String sendAllSMS(SMSDetailsView smsDetailsView) {
		final String USER_AGENT = "Mozilla/5.0";
		int retry = 0;
		while (true) {
			try {

				if (!smsDetailsView.getMobileNo().startsWith(CommonConstants.MOBILE_COUNTRY_CODE_INDIA)
						&& !smsDetailsView.getMobileNo().startsWith("+" + CommonConstants.MOBILE_COUNTRY_CODE_INDIA)) {
					StringBuffer sb = new StringBuffer("91");
					sb.append(smsDetailsView.getMobileNo());
					smsDetailsView.setMobileNo(sb.toString());
				}

				String url = BeanLocator.getBean(ParamService.class).retrieveStringParamByName(CommonConstants.SMS_SERVICE_URL_PARAM_KEY);

				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setConnectTimeout(1000);
				con.setReadTimeout(1000);

				String urlParameters = "data=<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE MESSAGE SYSTEM \"http://127.0.0.1:80/psms/dtd/messagev12.dtd\" ><MESSAGE VER=\"1.2\"><USER USERNAME=\"ratnakarotp2\" PASSWORD=\"ratnakar77\"/><SMS UDH=\"0\" CODING=\"1\" TEXT=\""
						+ smsDetailsView.getSmsMessage() + "\" PROPERTY=\"0\" ID=\"1\"><ADDRESS FROM=\"RBLBNK\" TO=\"" + smsDetailsView.getMobileNo()
						+ "\" SEQ=\"1\" TAG=\"\" /></SMS></MESSAGE>&action=send";

				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				System.out.println("Sending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);

				int responseCode = con.getResponseCode();
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				log.debug("Server Response: " + response.toString());
				return "Success";
			} catch (UnknownHostException e) {
				log.warn("[SMS] Could not send SMS notification to customer");
				e.printStackTrace();
				if (++retry > CommonConstants.MAX_SMS_TRY) {
					break;

				}
			} catch (Exception e) {
				log.warn("[SMS] Could not send SMS notification to customer");
				e.printStackTrace();

				break;

			}
			retry++;
		}
		return "Failure";

	}

	

}