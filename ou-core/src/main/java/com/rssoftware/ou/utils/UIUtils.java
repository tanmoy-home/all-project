package com.rssoftware.ou.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.CustomerDtlsType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.SpltPayType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.ClearingFeeObj;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.FeeCalculationHelper;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.ParamConfig.DataType;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.ServiceTaxConfService;
import com.rssoftware.ou.tenant.service.ServiceTaxService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

public class UIUtils {
	
	
	private final static Logger logger = LoggerFactory.getLogger(UIUtils.class);

	private static final String SELECT = "--Select--";
	private static final String SPACE_REPLACE_PATTERN = "_^_";
	private static final String SPACE = " ";
	private static final String PARENT_BILLER_ID_POSTFIX = "_^P";
	private static String get_url = getPropretyFiles("uiutils_url");
	
	private UIUtils() {
	}

	/**
	 * sendAllSMS method generates a text message to the desired Mobile no.
	 *
	 * @author Anupa Basumallik
	 * @version phase I
	 * @since 2016-07-12
	 */

	public static String sendAllSMS(String mobile, String message) {
		final String USER_AGENT = "Mozilla/5.0";
		int retry = 0;
		while (true) {
			try {

				if (!mobile.startsWith(CommonConstants.MOBILE_COUNTRY_CODE_INDIA)
						&& !mobile.startsWith("+" + CommonConstants.MOBILE_COUNTRY_CODE_INDIA)) {
					StringBuffer sb = new StringBuffer("91");
					sb.append(mobile);
					mobile = sb.toString();
				}
		//		String url = "http://172.18.61.1:9050/psms/servlet/psms.Eservice2";
		//		String get_url = getPropretyFiles(url);
				// String url =
				// "http://api2.myvaluefirst.com/psms/servlet/psms.Eservice2";

				URL obj = new URL(get_url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setConnectTimeout(1000);
				con.setReadTimeout(1000);

				String urlParameters = "data=<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE MESSAGE SYSTEM \"http://127.0.0.1:80/psms/dtd/messagev12.dtd\" ><MESSAGE VER=\"1.2\"><USER USERNAME=\"ratnakarotp2\" PASSWORD=\"ratnakar77\"/><SMS UDH=\"0\" CODING=\"1\" TEXT=\""
						+ message + "\" PROPERTY=\"0\" ID=\"1\"><ADDRESS FROM=\"RBLBNK\" TO=\"" + mobile
						+ "\" SEQ=\"1\" TAG=\"\" /></SMS></MESSAGE>&action=send";

				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				System.out.println("Sending 'POST' request to URL : " + get_url);
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
				System.out.println("Server Response: " + response.toString());
				return "Success";
			} catch (UnknownHostException e) {
				System.out.println("[SMS] Could not send SMS notification to customer");
				e.printStackTrace();
				if (++retry > CommonConstants.MAX_SMS_TRY) {
					break;

				}
			} catch (Exception e) {
				System.out.println("[SMS] Could not send SMS notification to customer");
				logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());

				break;

			}
			retry++;
		}
		return "Failure";

	}

	public static void sendMail(String[] recipients, String[] ccRecipients, String[] bccRecipients, String subject,
			String content, File attachment) {
		try {

			JavaMailSender javaMailSender = BeanLocator.getBean(JavaMailSender.class);
			MimeMessage message = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = null;
			try {
				helper = new MimeMessageHelper(message, true);
			} catch (javax.mail.MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (recipients[0] != null && recipients.length > 0)
				helper.setTo(recipients);
			if (null != bccRecipients[0] && bccRecipients.length > 0)
				helper.setBcc(bccRecipients);
			if (null != ccRecipients[0] && ccRecipients.length > 0)
				helper.setCc(ccRecipients);
			helper.setSubject(subject);
			helper.setText(content);
			if (null != attachment) {
				helper.addAttachment(attachment.getName(), attachment);
			}

			javaMailSender.send(message);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (javax.mail.MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String escapeSpaces(String input) {
		if (input == null) {
			return input;
		}
		return StringUtils.replace(input, SPACE, SPACE_REPLACE_PATTERN);
	}

	public static String unEscapeSpaces(String input) {
		if (input == null) {
			return input;
		}
		return StringUtils.replace(input, SPACE_REPLACE_PATTERN, SPACE);
	}

	public static String escapeBillerId(String input, BillerView bv) {
		if (input == null) {
			return input;
		}
		input = escapeSpaces(input);
		if (bv != null && bv.isParentBlr()) {
			input = input + PARENT_BILLER_ID_POSTFIX;
		}
		return input;
	}

	public static String unEscapeBillerId(String input) {
		if (input == null) {
			return input;
		}

		if (input.endsWith(PARENT_BILLER_ID_POSTFIX)) {
			input = StringUtils.replace(input, PARENT_BILLER_ID_POSTFIX, "");
		}
		return input;
	}

	private static void appendBillPaymentFormSegment(StringBuilder sb, String label, Object value) {
		if (value != null) {

			if (label.equalsIgnoreCase("CCF + Tax(s)")) {
				appendBillPaymentFormSegmentAbsolute(sb, label,
						"<input type='text' class='form-control' id='ccfT' value='" + value.toString()
								+ "' disabled='true'>");

			} else if (label.equalsIgnoreCase("Total Amount to be paid")) {

				appendBillPaymentFormSegmentAbsolute(sb, label,
						"<input type='text' class='form-control' id='totalAm' value='" + value.toString()
								+ "' disabled='true'><br/><br/><br/>");
			} else {
				appendBillPaymentFormSegmentAbsolute(sb, label, "<input type='text' class='form-control' id='"
						+ escapeSpaces(label) + "' value='" + value.toString() + "' disabled='true'>");
			}

		}
	}

	private static void appendBillPaymentFormSegmentAbsolute(StringBuilder sb, String label, String value) {
		if (value != null) {
			sb.append("<div class=\"col-sm-3\">");
			sb.append("<div class=\"form-group\">");
			sb.append("<label>");
			sb.append(label);
			sb.append("</label>");
			sb.append(value);
			sb.append("</div>");
			sb.append("</div>");
		}
	}

	public static String getBillFetchFail(ValidationException ve) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<label>");
		sb.append("Bill Fetch failed.");
		sb.append("</label>");
		sb.append("<br/>");
		sb.append("<label>");
		sb.append("Error Code:" + ve.getCode());
		sb.append(", Description:" + ve.getDescription());
		sb.append("</label>");
		return sb.toString();
	}

	public static String getpaymenttag(String billerPaymentMode, HttpServletRequest request) {

		StringBuilder sb = new StringBuilder("");
		ApplicationConfigService applicationConfigService = BeanLocator.getBean(ApplicationConfigService.class);

		// dummy
		Set<String> strings = applicationConfigService.getAgentDevMap().get(PaymentChannel.ATM.getExpandedForm());

		// Set<String> strings=
		// applicationConfigService.getAgentDevMap().get(billerPaymentMode);

		for (String tag : strings) {
			sb.append("<input type='text' class='form-control' id='" + escapeSpaces(tag) + "' value='" + tag + "'>");
		}
		return sb.toString();

	}

	public static String getpaymentInfo(String billerPaymentInfo, HttpServletRequest request) {

		StringBuilder sb = new StringBuilder("");
		ApplicationConfigService applicationConfigService = BeanLocator.getBean(ApplicationConfigService.class);

		// dummy
		String set = applicationConfigService.getPaymentInfoMap().get(PaymentMode.Cash.getExpandedForm());
		// String set =
		// applicationConfigService.getPaymentInfoMap().get(billerPaymentInfo);

		for (String retval : set.split("|")) {
			sb.append("<input type='text' class='form-control' id='" + escapeSpaces(retval) + "' value='" + retval
					+ "'>");
		}

		return sb.toString();

	}

	public static String getpaymentchannal(String billerPaymentMode, HttpServletRequest request) {

		StringBuilder sb = new StringBuilder("");
		ApplicationConfigService applicationConfigService = BeanLocator.getBean(ApplicationConfigService.class);

		// for dummy
		int i = 1;
		Set<String> map = applicationConfigService.getPaymentModeChannelMap().get(PaymentMode.Cash.getExpandedForm());
		// for original
		// Set<String> map =
		// applicationConfigService.getPaymentModeChannelMap().get(billerPaymentMode);
		for (String paymnt : map) {

			if (map.size() == i) {

				sb.append(paymnt);
			} else {
				sb.append(paymnt);
				sb.append("|");
				i++;
			}

		}

		return sb.toString();
	}

	public static String getBillPaymentForm(BillerService billerService, String billerId,
			BillFetchResponse billFetchResponse, String agent, HttpServletRequest request) throws IOException{
		String customerName = null;
		BigDecimal amount = null;
		String dueDate = null;
		BigDecimal custConvFee = null;
		String custConvDesc = null;
		String billDate = null;
		String billNumber = null;
		String billPeriod = null;
		BillerView billerView = billerService.getBillerById(billerId);
		customerName = billFetchResponse.getBillerResponse().getCustomerName();
		amount = (new BigDecimal(billFetchResponse.getBillerResponse().getAmount())).divide(new BigDecimal(100))
				.setScale(2, RoundingMode.HALF_UP);
		dueDate = billFetchResponse.getBillerResponse().getDueDate();
		custConvFee = billFetchResponse.getBillerResponse().getCustConvFee() == null ? null
				: (new BigDecimal(billFetchResponse.getBillerResponse().getCustConvFee())).divide(new BigDecimal(100))
						.setScale(2, RoundingMode.HALF_UP);
		custConvDesc = billFetchResponse.getBillerResponse().getCustConvDesc();
		billDate = billFetchResponse.getBillerResponse().getBillDate();
		billNumber = billFetchResponse.getBillerResponse().getBillNumber();
		billPeriod = billFetchResponse.getBillerResponse().getBillPeriod();

		StringBuilder sb = new StringBuilder("");
		sb.append("<form id='billPayForm' name='billPayForm' method='post'/>");
		sb.append("<input type='hidden' id='payBillerId' name='payBillerId' value='" + billerId + "'/>");
		sb.append("<input type='hidden' id='refId' name='refId' value='" + billFetchResponse.getHead().getRefId()
				+ "'/>");
		sb.append("<input type='hidden' id='totamount' name='totamount' value=''/>");
		sb.append("<input type='hidden' id='ccf' name='ccf' value=''/>");

		appendBillPaymentFormSegment(sb, "Customer Name", customerName);
		appendBillPaymentFormSegment(sb, "Bill Amount", amount.toPlainString());
		appendBillPaymentFormSegment(sb, "Due Date", dueDate);
		appendBillPaymentFormSegment(sb, "Customer Convenience Fee", custConvFee);
		appendBillPaymentFormSegment(sb, "Customer Convenience Description", custConvDesc);
		appendBillPaymentFormSegment(sb, "Bill Date", billDate);
		appendBillPaymentFormSegment(sb, "Bill Number", billNumber);
		appendBillPaymentFormSegment(sb, "Bill Period", billPeriod);

		if (billFetchResponse.getAdditionalInfo() != null) {
			for (AdditionalInfoType.Tag tag : billFetchResponse.getAdditionalInfo().getTags()) {
				if (tag != null && tag.getName() != null) {
					appendBillPaymentFormSegment(sb, tag.getName(), tag.getValue());
				}
			}
		}

		appendBillPaymentFormSegment(sb, "CCF + Tax(s)", "");
		appendBillPaymentFormSegment(sb, "Total Amount to be paid", "");

		appendBillPaymentFormSegmentAbsolute(sb, "Amount Options",
				getBillerAmountOptions(billerView, billFetchResponse, amount));

		if (null != agent && agent.equalsIgnoreCase("agentUI")) {

			appendBillPaymentFormSegmentAbsolute(sb, "Payment Modes", getPaymentModes(request));

			appendBillPaymentFormSegmentAbsolute(sb, "Payment Channels", getPaymentChannels(request));
		}

		sb.append("<div class='pull-right' id='btnBillPayArea'>");
		sb.append(
				"<button type='submit' id='billPayment' class='btn btn-primary' name='billPayment' onclick='return validateAndSubmitBillPayment()'>PAY</Button>");
		sb.append("</div>");

		sb.append("</form>");

		return sb.toString();
	}

	public static String getPaymentModes(HttpServletRequest request) {
		StringBuilder select = new StringBuilder("");
		select.append("<select name='billerPaymentModes' id='billerPaymentModes' 'style='width: 450px'>");
		select.append("<option value=\"\">");
		select.append(SELECT);
		select.append("</option>");

		AgentService agentService = BeanLocator.getBean(AgentService.class);

		// for getting the agentID from sesion;
		HttpSession httpSession = request.getSession(true);

		String agtID = CommonUtils.getLoggedInUserDetails().getRefId();
		AgentView agtView = agentService.getAgentById(agtID);
		List<PaymentModeLimit> paymentModeLimits = agtView.getAgentPaymentModes();
		for (PaymentModeLimit paymentModeLimit : paymentModeLimits) {
			PaymentMode paymentMode = paymentModeLimit.getPaymentMode();
			select.append("<option value=\"" + paymentMode.getExpandedForm() + "\">");
			select.append(paymentMode.getExpandedForm());
			select.append("</option>");

		}
		select.append("</select>");
		select.append("<div class=\"col-sm-3\">");
		select.append("<div class=\"form-group\">");
		select.append("<div id=\"paymentinformation\">");
		select.append("</div>");
		select.append("</div>");
		select.append("</div>");
		return select.toString();
	}

	public static String getPaymentChannels(HttpServletRequest request) {
		StringBuilder select = new StringBuilder("");
		select.append("<select name='billerPaymentChannels' id='billerPaymentChannels' style='width: 450px'>");
		select.append("<option value=\"\">");
		select.append(SELECT);
		select.append("</option>");

		/*
		 * AgentService agentService = BeanLocator.getBean(AgentService.class);
		 * ApplicationConfigService applicationConfigService =
		 * BeanLocator.getBean(ApplicationConfigService.class); // for getting
		 * the agentID from sesion; HttpSession httpSession =
		 * request.getSession(true);
		 * 
		 * String agtID = (String) httpSession.getAttribute(""); AgentView
		 * agtView = agentService.getAgentById(agtID); List<PaymentChannelLimit>
		 * paymentModeLimits = agtView.getAgentPaymentChannels(); for
		 * (PaymentChannelLimit paymentModeLimit : paymentModeLimits) {
		 * PaymentChannel paymentChannel = paymentModeLimit.getPaymentChannel();
		 * select.append("<option value=\"" + paymentChannel.getExpandedForm() +
		 * "\">"); select.append(paymentChannel.getExpandedForm());
		 * select.append("</option>");
		 * 
		 * }
		 */
		select.append("</select>");
		select.append("<div class=\"col-sm-3\">");
		select.append("<div class=\"form-group\">");
		select.append("<div id=\"paymenttag\">");
		select.append("</div>");
		select.append("</div>");
		select.append("</div>");

		return select.toString();
	}

	/**
	 * getBillerFetchForm method generates the bill fetch form request.
	 * 
	 * @author Nivedita
	 * @version 1.1
	 * @changes spliting the fetch bill section
	 * @since 2016-07-14
	 */
	public static String getBillerFetchForm(BillerService billerService, String billerId) throws IOException{
		BillerView bv = billerService.getBillerById(billerId);

		List<ParamConfig> custParams = bv.getBillerCustomerParams();

		StringBuilder sb = new StringBuilder("");
		StringBuilder sbValidation = new StringBuilder("");
		sb.append("<input type='hidden' id='billerId' name='billerId' value='" + billerId + "'/>");
		// sb.append("<input type='hidden' id='agent' name='agent'
		// value='agentUI'/>\n");

		if (custParams != null) {
			sbValidation.append('[');
			for (ParamConfig pc : custParams) {
				if (pc != null) {

					boolean isOptional = pc.getOptional() != null ? pc.getOptional() : false;

					sbValidation.append('[');
					sbValidation.append("'");
					sbValidation.append(escapeSpaces(pc.getParamName()));
					sbValidation.append("'");
					sbValidation.append(",");
					sbValidation.append("'");
					sbValidation.append(pc.getDataType() != null ? pc.getDataType().name() : DataType.ALPHANUMERIC);
					sbValidation.append("'");
					sbValidation.append(",");
					sbValidation.append("'");
					sbValidation.append(pc.getOptional() != null ? pc.getOptional() : false);
					sbValidation.append("'");
					sbValidation.append(']');
					sbValidation.append(',');

					sb.append("<div class='col-sm-3'>");
					sb.append("<div class='form-group'>");
					if (!isOptional) {
						sb.append("<div>");
						sb.append("<label>");
						sb.append(pc.getParamName());
						sb.append(" *");
						sb.append("</label>");
						sb.append("<br/>");

						sb.append("<input type='text' id='" + escapeSpaces(pc.getParamName()) + "' name='"
								+ escapeSpaces(pc.getParamName()) + "' class='form-control'/>");
						sb.append("</div>");
					} else {
						sb.append("<label>");
						sb.append(pc.getParamName());
						sb.append("</label>");
						sb.append("<br/>");

						sb.append("<input type='text' id='" + escapeSpaces(pc.getParamName()) + "' name='"
								+ escapeSpaces(pc.getParamName()) + "' class='form-control'/>");

					}
					sb.append("</div>");
					sb.append("</div>");
				}
			}
			if (sbValidation.length() > 0 && sbValidation.charAt(sbValidation.length() - 1) == ',') {
				sbValidation.setCharAt(sbValidation.length() - 1, ' ');
			}

			sbValidation.append(']');
		}

		if (bv.isBlrAcceptsAdhoc()) {
			sb.append("<input type='hidden' id='totamount' name='totamount' value=''/>");
			sb.append("<input type='hidden' id='ccf' name='ccf' value=''/>");
			appendBillPaymentFormSegment(sb, "CCF + Tax(s)", "");
			appendBillPaymentFormSegment(sb, "Total Amount to be paid", "");
			sb.append("<div class='col-sm-3'>");
			sb.append("<div class='form-group'>");
			sb.append("<label>Quick Pay Amount</label>");
			sb.append("<br/>");
			sb.append(
					"<input type='text' id='quickPayAmount' name='quickPayAmount' onChange='calculateCCFQuickPay();' class='form-control' />");
			sb.append("</div></div>");

		}
		// sb.append("</br>");
		// sb.append("</br>");

		sb.append("<div class='pull-right' id='btnBillFetchArea'>");

		if (bv.isBlrAcceptsAdhoc()) {
			sb.append(
					"<button id='quickPay' type='button' class='btn btn-primary' name='quickPay' onclick=\"validateAndQuickPay("
							+ sbValidation + ")\">Quick Pay</Button>&nbsp;");
		}

		sb.append(
				"<button type='button' id='billFetch' class='btn btn-primary' name='billFetch' onclick=\"validateAndFetchBill("
						+ sbValidation + ")\">Fetch Bill</Button>");
		sb.append("</div>");
		return sb.toString();
	}

	public static String getBillerCategoryDropdown(BillerService billerService, String[] paymentChannels,
			String[] paymentModes) throws IOException{
		String[] billerCategories = billerService.getBillerCategories(paymentChannels, paymentModes);

		StringBuilder sb = new StringBuilder("<select name='billerCategories' id='billerCategories'>");
		sb.append("<option value=\"\">");
		sb.append(SELECT);
		sb.append("</option>");

		for (String billerCategory : billerCategories) {
			sb.append("<option value=\"");
			sb.append(escapeSpaces(billerCategory));
			sb.append("\">");
			sb.append(billerCategory);
			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String getWelcomeUser(String userName) {
		StringBuilder sb = new StringBuilder("");

		sb.append("<p name='userName' id='userName' value=\"");
		sb.append(escapeSpaces(userName));
		sb.append("\">");
		sb.append("</p>");
		return sb.toString();
	}

	public static String getAgentDashboard(double blc, String amt, Long billFetchCnt, Long billPayCnt) {

		StringBuilder sb = new StringBuilder();
		/*
		 * sb.append("<tr><td><h3>Account Balance</h3></td>");
		 * sb.append("<td>&nbsp&nbsp:&nbsp&nbsp</td><td><h3>Rs."+blc+
		 * "<h3></td></tr>"); sb.append("<tr><td><h3>Amount Collected</h3></td>"
		 * ); sb.append("<td>&nbsp&nbsp:&nbsp&nbsp</td><td><h3>Rs."+amt+
		 * "<h3></td></tr>"); sb.append(
		 * "<tr><td><h3>No. of Bill(s) Fetched</h3></td>");
		 * sb.append("<td>&nbsp&nbsp:&nbsp&nbsp</td><td><h3>"+billFetchCnt+
		 * "<h3></td></tr>"); sb.append(
		 * "<tr><td><h3>No. of Bill(s) Paid</h3></td>");
		 * sb.append("<td>&nbsp&nbsp:&nbsp&nbsp</td><td><h3>"+billPayCnt+
		 * "<h3></td></tr>"); sb.append("</table>");
		 */

		/*
		 * *********************************************************************
		 * ********************
		 */

		// ACCOUNT BALANCE
		sb.append("<div class=\"col-md-3\">");
		sb.append("<div class=\"sm-st clearfix\">");
		sb.append("<span class=\"sm-st-icon st-red\"><i class=\"fa fa-rupee\"></i></span>");
		sb.append("<div class=\"sm-st-info\">");
		sb.append("<span>" + blc + "</span>");
		sb.append("<br>Account Balance");
		sb.append("</div></div></div>");

		// AMOUNT COLLECTED
		sb.append("<div class=\"col-md-3\">");
		sb.append("<div class=\"sm-st clearfix\">");
		sb.append("<span class=\"sm-st-icon st-green\"><i class=\"fa fa-rupee\"></i></span>");
		sb.append("<div class=\"sm-st-info\">");
		sb.append("<span>" + amt + "</span>");
		sb.append("<br>Amount Collected");
		sb.append("</div></div></div>");

		// BILL FETCHED
		sb.append("<div class=\"col-md-3\">");
		sb.append("<div class=\"sm-st clearfix\">");
		sb.append("<span class=\"sm-st-icon st-violet\"><i class=\"fa fa-check-square-o\"></i></span>");
		sb.append("<div class=\"sm-st-info\">");
		sb.append("<span>" + billFetchCnt + "</span>");
		sb.append("<br>No. of Bill(s) Fetched");
		sb.append("</div></div></div>");

		// BILL PAID
		sb.append("<div class=\"col-md-3\">");
		sb.append("<div class=\"sm-st clearfix\">");
		sb.append("<span class=\"sm-st-icon st-blue\"><i class=\"fa fa-check-square-o\"></i></span>");
		sb.append("<div class=\"sm-st-info\">");
		sb.append("<span>" + billPayCnt + "</span>");
		sb.append("<br>No. of Bill(s) Paid");
		sb.append("</div></div></div>");

		return sb.toString();
	}

	public static String getBillerListDropdown(BillerService billerService, String billerCategory,
			String[] paymentChannels, String[] paymentModes) throws IOException{
		BillerView[] billerViewList = billerService.getBillersByCategory(unEscapeSpaces(billerCategory),
				paymentChannels, paymentModes);

		StringBuilder sb = new StringBuilder("<select name='billerList' id='billerList'>");
		sb.append("<option value=\"\">");
		sb.append(SELECT);
		sb.append("</option>");

		for (BillerView bv : billerViewList) {
			sb.append("<option value=\"");
			sb.append(escapeBillerId(bv.getBlrId(), bv));
			sb.append("\">");
			sb.append(bv.getBlrName());
			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String getSubBillerListDropdown(BillerService billerService, String parentBillerId,
			String billerCategory, String[] paymentChannels, String[] paymentModes) throws IOException{
		BillerView[] billerViewList = billerService.getSubBillersByParentBiller(unEscapeBillerId(parentBillerId),
				unEscapeSpaces(billerCategory), paymentChannels, paymentModes);

		StringBuilder sb = new StringBuilder("<select name='subBillerList' id='subBillerList'>");
		sb.append("<option value=\"\">");
		sb.append(SELECT);
		sb.append("</option>");

		for (BillerView bv : billerViewList) {
			sb.append("<option value=\"");
			sb.append(escapeSpaces(bv.getBlrId()));
			sb.append("\">");
			sb.append(bv.getBlrName());
			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String getTenantIdFromSession(HttpSession session) {
		if (session != null) {
			return (String) session.getAttribute("interimTenantId");
		}
		return null;
	}

	public static void setTenantIdToSession(HttpSession session, String tenantId) {
		session.setAttribute("interimTenantId", tenantId);
	}

	public static String getBillerAmountOptions(BillerView biller, BillFetchResponse billFetch, BigDecimal amount) {
		StringBuilder select = new StringBuilder("");
		select.append(
				"<select name='billerAmountOptions' id='billerAmountOptions'  onChange='calculateCCF();'style='width: 450px'>");
		select.append("<option value=\"\">");
		select.append(SELECT);
		select.append("</option>");

		if (biller.getBillerResponseParams() != null
				&& !biller.getBillerResponseParams().getAmountOptions().isEmpty()) {
			Map<String, BigDecimal> incomingPaymentAmounts = new HashMap<String, BigDecimal>();

			if (!billFetch.getBillerResponse().getTags().isEmpty()) {
				for (BillerResponseType.Tag tag : billFetch.getBillerResponse().getTags()) {
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())) {
						BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2,
								RoundingMode.HALF_UP);
						incomingPaymentAmounts.put(tag.getName(), tagAmount);
					}
				}
			}

			for (BillerResponseParams.AmountOption ao : biller.getBillerResponseParams().getAmountOptions()) {
				if (ao != null) {
					boolean allAmtsPresent = true;
					for (String amountBreakup : ao.getAmountBreakups()) {
						if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
							continue;
						}
						if (incomingPaymentAmounts.get(amountBreakup) == null) {
							allAmtsPresent = false;
							break;
						}
					}

					if (allAmtsPresent) {
						BigDecimal total = BigDecimal.ZERO;
						StringBuilder b = new StringBuilder("");
						StringBuilder breakup = new StringBuilder("");
						for (String amountBreakup : ao.getAmountBreakups()) {
							if (breakup.length() > 0) {
								breakup.append("|");
							}
							breakup.append(amountBreakup);
							if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
								b.append("Bill Amount (" + amount.toPlainString() + ")+");
								total = total.add(amount);
							} else {
								b.append(amountBreakup + " ("
										+ incomingPaymentAmounts.get(amountBreakup).toPlainString() + ")+");
								total = total.add(incomingPaymentAmounts.get(amountBreakup));
							}
						}

						select.append("<option value=\"" + total.toPlainString() + "|" + breakup + "\">");
						select.append(b.substring(0, b.length() - 1) + "=" + total.toPlainString());
						select.append("</option>");

					}
				}
			}

		} else {
			select.append(
					"<option value=\"" + amount.toPlainString() + "|" + BillerResponseParams.BASE_BILL_AMOUNT + "\">");
			select.append("Bill Amount (" + amount.toPlainString() + ")");
			select.append("</option>");
		}

		select.append("</select>");

		return select.toString();
	}

	public static String getPGIntermediatePage(List<PGParam> pgParams) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<Html>\n");
		sb.append("<Head></Head>\n");
		sb.append("<script>\n");
		sb.append("window.onload = function(){\n");
		sb.append("document.forms['frmPaymentInfo'].submit();\n");
		sb.append("}\n");
		sb.append("</script>\n");
		sb.append("<body>\n");
		sb.append("<label>Please wait...</label>\n");
		sb.append("<form name=\"frmPaymentInfo\" id=\"frmPaymentInfo\" action=\"");
		sb.append(BeanLocator.getBean(ParamService.class)
				.retrieveStringParamByName(CommonConstants.PAYMENT_GATEWAY_URL_PARAM_KEY + "?txnAmount="));
		sb.append("\" method=\"post\">\n");
		if (pgParams != null) {
			for (PGParam pgParam : pgParams) {

				sb.append("<input type=hidden id=\"" + pgParam.getParamName() + "\" name=\"" + pgParam.getParamName()
						+ "\" value=\"" + pgParam.getParamValue() + "\"/>\n");

			}
		}
		sb.append("</form>\n");
		sb.append("</body>\n");
		sb.append("</Html>\n");
		// System.out.println("Final XMl "+sb.toString());
		return sb.toString();
	}

	@SafeVarargs
	public static List<PGParam> mapPGResponse(MultiValueMap<String, Object>... paramsArr) {
		Map<String, String> paramMap = new HashMap<String, String>();
		if (paramsArr != null) {
			for (MultiValueMap<String, Object> params : paramsArr) {
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) != null && !params.get(key).isEmpty() && params.get(key).get(0) != null) {
							paramMap.put(key, params.get(key).get(0).toString());
						}
					}
				}
			}
		}
		List<PGParam> list = new ArrayList<PGParam>();
		for (String name : paramMap.keySet()) {
			PGParam pgParam = new PGParam();
			pgParam.setParamName(name);
			pgParam.setParamValue(paramMap.get(name));
			list.add(pgParam);
		}
		return list;
	}

	public static String getReceiptPage(String refId, String txnRefId, BillPaymentResponse bpr, String errCode,
			String errMsg) throws IOException {

		StringBuilder sb = new StringBuilder("");
		StringBuilder sbemail = new StringBuilder("");
		String email_content;
		sb.append("<Html>\n");
		sb.append("<Head>");

		sb.append("<link href=\"/css/bbps-bootstrap.css\" rel=\"stylesheet\">\n");
		sb.append("<link href=\"/css/bbpou.css\" rel=\"stylesheet\">\n");
		sb.append("<link href=\"/css/bbpou-print.css\" rel=\"stylesheet\">\n");
		sb.append("<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\">\n");
		sb.append("<script src=\"/js/jquery-1.12.3.min.js\"></script>\n");
		// sb.append("<script src=\"/js/metisMenu.min.js\"></script>\n");
		sb.append("<script src=\"/js/bootstrap.min.js\"></script>\n");
		sb.append("<script src=\"/js/jspdf.js\"></script>\n");
		sb.append("<script src=\"/js/from_html.js\"></script>\n");
		sb.append("<script src=\"/js/standard_fonts_metrics.js\"></script>\n");
		sb.append("<script src=\"/js/split_text_to_size.js\"></script>\n");
		sb.append("<script src=\"/js/cell.js\"></script>\n");
		sb.append("<script src=\"/js/FileSaver.js\"></script>\n");
		sb.append("<script src=\"/js/downloadPdf.js\"></script>\n");

		sb.append("</Head>\n");
		sb.append("<script>\n");
		sb.append("window.onload = function(){\n");
		sb.append("document.forms['frmPaymentInfo'].submit();\n");
		sb.append("}\n");
		sb.append("</script>\n");
		sb.append("<body><br/>\n");

		sb.append("<div class=\"container-fluid\">\n");
		if (CommonUtils.hasValue(errCode)) {
			sb.append("<label >Transaction response has failed</label><br/>\n");

			sb.append("<label>Error Code:" + errCode + "</label><br/>\n");
			sb.append("<label>Error Message:" + errMsg + "</label><br/>\n");
			sb.append("<label>Reference ID:" + refId + "</label><br/>\n");
			sb.append("<label>Transaction Reference ID:" + txnRefId + "</label><br/>\n");

		} else {

			System.out.println("==============BillPaymentResponse" + bpr);

			String billdate = bpr.getBillerResponse().getBillDate();
			String customer = bpr.getBillerResponse().getCustomerName();
			String billnum = bpr.getBillerResponse().getBillNumber();
			String billperiod = bpr.getBillerResponse().getBillPeriod();
			String amount = bpr.getBillerResponse().getAmount();
			Date date_time = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");

			String date = sdf.format(date_time);

			// String date_time= .toString();
			TransactionDataService transDtService = BeanLocator.getBean(TransactionDataService.class);
			TransactionDataView transactionDataView = transDtService.getTransactionData(refId, RequestType.PAYMENT);
			TransactionDataView transactionDataViewfetch = transDtService.getTransactionData(refId, RequestType.FETCH);
			String mobilenum = null;
			List list = null;
			if (transactionDataViewfetch != null) {
				mobilenum = transactionDataViewfetch.getBillFetchRequest().getCustomer().getMobile();
				list = transactionDataViewfetch.getBillFetchRequest().getCustomer().getTags();
			} else {
				mobilenum = transactionDataView.getBillPaymentRequest().getCustomer().getMobile();
				list = transactionDataView.getBillPaymentRequest().getCustomer().getTags();
			}

			String billerId = transactionDataView.getBillPaymentRequest().getBillDetails().getBiller().getId();
			BillerService billerService = BeanLocator.getBean(BillerService.class);
			BillerView billerView = billerService.getBillerById(billerId);
			String billerName = billerView.getBlrName();

			// List list=
			// transactionDataViewfetch.getBillFetchRequest().getCustomer().getTags();
			String[] email = new String[1];
			String[] phone = new String[1];
			for (Object tagName : list) {
				CustomerDtlsType.Tag tag = (CustomerDtlsType.Tag) tagName;

				if (tag.getName().equalsIgnoreCase("EMAIL")) {
					email[0] = tag.getValue();
					break;
				}

			}

			/*
			 * Validation for empty mobile number field; if mobile number is
			 * present then the sendAllSMS will be evoked;
			 */

			if (mobilenum != null) {
				sendAllSMS(mobilenum,
						" Payment of INR " + amount + " for " + billerName + " as on " + date + " is successfull.");
			}

			if (email[0] != null) {

				sbemail.append("<Html>\n");
				sbemail.append("<Head>");
				sbemail.append("<link href=\"/css/bbps-bootstrap.css\" rel=\"stylesheet\">\n");
				sbemail.append("<link href=\"/css/bbpou.css\" rel=\"stylesheet\">\n");
				sbemail.append("<link href=\"/css/bbpou-print.css\" rel=\"stylesheet\">\n");
				sbemail.append("<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\">\n");
				sbemail.append("<script src=\"/js/jquery-1.12.3.min.js\"></script>\n");
				sbemail.append("<script src=\"/js/bootstrap.min.js\"></script>\n");
				sbemail.append("<script src=\"/js/bbpou.js\"></script>\n");
				sbemail.append("</Head>\n");
				sbemail.append("<script>\n");
				sbemail.append("window.onload = function(){\n");
				sbemail.append("document.forms['frmPaymentInfo'].submit();\n");
				sbemail.append("}\n");
				sbemail.append("</script>\n");
				sbemail.append("<body><br/>\n");

				sbemail.append("<label>Hello!  " + customer + " , <label><br>\n");
				sbemail.append("<label>Please find your Payment Receipt for:  " + billdate + "<label><br>\n");
				sbemail.append("<div class=\"row\">\n");
				sbemail.append("<div class=\"col-sm-12\">\n");
				sbemail.append("<div class=\"box\">\n");
				sbemail.append("<div class=\"box-body\">\n");

				sbemail.append("<h3><center><b>" + billerName + "</b></center></h3>\n");
				sbemail.append("<center>Bill Receipt</center>\n");
				sbemail.append("<div class=\"row\">\n");

				sbemail.append("<div class=\"col-sm-12\">\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Customer Name</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + customer + "</label>\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Number</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + billnum + "</label>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");

				sbemail.append("<div class=\"col-sm-12\">\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Date</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + billdate + "</label>\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Period</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + billperiod + "</label>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");

				sbemail.append("<div class=\"col-sm-12\">\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append(
						"<label class =\"control-label col-sm-2\" id =\"lblform\">Transaction Reference ID</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + txnRefId + "</label>\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append(
						"<label class =\"control-label col-sm-2\" id =\"lblform\">Transaction Date & Time</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + date + "</label>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");

				sbemail.append("<div class=\"col-sm-12\">\n");
				sbemail.append("<div class=\"form-group\">\n");
				sbemail.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Amount</label>\n");
				sbemail.append("<label class =\"control-label col-sm-3\">" + amount + "</label>\n");
				sbemail.append("<div class=\"form-group\">\n");
				// sbemail.append("<label class =\"control-label col-sm-2\" id
				// =\"lblform\">Bill Period</label>\n");
				// sbemail.append("<label class =\"control-label
				// col-sm-3\">"+billperiod+"</label>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");

				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");
				sbemail.append("</div>\n");

				sbemail.append("</form>\n");
				sbemail.append("</div>\n");
				sbemail.append("</body>\n");
				sbemail.append("</Html>\n");

				email_content = sbemail.toString();

				// return email_content;

				sendMail(email, new String[1], new String[1], "Bill Payment Receipt", email_content, null);

			}

			sb.append("<div id=\"non-printable\">\n");
			sb.append("<h2 class=\"page-header\">Your Transaction is Successful</h2><br/>\n");
			sb.append("</div>");

			sb.append("<div class=\"row\">\n");
			sb.append("<div class=\"col-sm-12\">\n");
			sb.append("<div class=\"box\">\n");
			sb.append("<div class=\"box-body\">\n");

			// sb.append("<div id=\"content\">\n");//open content div

			sb.append("<div class=\"row\">\n");

			sb.append("<div id=\"content\">\n");// open content div

			sb.append("<h3><center><b>" + billerName + "</b></center></h3>\n");
			sb.append("<center>Bill Payment Receipt</center><br>\n");

			sb.append("<div class=\"col-sm-12\">\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Customer Name :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + customer + "</label>\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Number :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + billnum + "</label>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");

			sb.append("<div class=\"col-sm-12\">\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Date :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + billdate + "</label>\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Bill Period :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + billperiod + "</label>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");

			sb.append("<div class=\"col-sm-12\">\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Transaction Reference ID :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + txnRefId + "</label>\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Transaction Date & Time :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">" + date + "</label>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");

			sb.append("<div class=\"col-sm-12\">\n");
			sb.append("<div class=\"form-group\">\n");
			sb.append("<label class =\"control-label col-sm-2\" id =\"lblform\">Amount :</label>\n");
			sb.append("<label class =\"control-label col-sm-3\">INR " + amount + "</label>\n");
			sb.append("<div class=\"form-group\">\n");

			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");

			sb.append("<div id=\"editor\">\n");// edit
			sb.append("</div>\n");
			sb.append("</div>\n");// close the content div

			sb.append("<div id=\"non-printable\">\n");
			sb.append("<div class=\"col-sm-12\">\n");
			sb.append(
					"<center><button type=\"button\" class=\"btn btn-primary\" onclick=\"window.print()\">Print</button>&nbsp;\n");
			sb.append("<button type=\"button\" class=\"btn btn-primary\" id=\"cmd\">Download</button>&nbsp;<br><br>\n");

			if (email[0] != null) {
				sb.append("<label>* Copy of the receipt is sent to your registered Mail ID<label></center>\n");
			}

			sb.append("</div>\n");
			sb.append("</div>\n");

			sb.append("</div>\n");

			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");
			sb.append("</div>\n");

			// sb.append("</div>\n");//close the content div

			// sb.append("<div id=\"editor\">\n");
			// sb.append("</div>\n");

		}

		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("</body>\n");
		sb.append("</Html>\n");
		return sb.toString();

	}

	public static String getErrorReceiptPage(String errCode, String errMsg) {

		StringBuilder sb = new StringBuilder("");
		sb.append("<Html>\n");
		sb.append("<Head>");

		sb.append("<link href=\"/css/bbps-bootstrap.css\" rel=\"stylesheet\">\n");
		sb.append("<link href=\"/css/bbpou.css\" rel=\"stylesheet\">\n");
		sb.append("<link href=\"/css/bbpou-print.css\" rel=\"stylesheet\">\n");
		sb.append("<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\">\n");
		sb.append("<script src=\"/js/jquery-1.12.3.min.js\"></script>\n");
		sb.append("<script src=\"/js/bootstrap.min.js\"></script>\n");
		sb.append("<script src=\"/js/jspdf.js\"></script>\n");
		sb.append("<script src=\"/js/from_html.js\"></script>\n");
		sb.append("<script src=\"/js/standard_fonts_metrics.js\"></script>\n");
		sb.append("<script src=\"/js/split_text_to_size.js\"></script>\n");
		sb.append("</Head>\n");
		sb.append("<body><br/>\n");

		sb.append("<div class=\"container-fluid\">\n");
		if (CommonUtils.hasValue(errCode)) {
			sb.append("<table border='1' align='center' style='margin-top: 100px'><tr><td>\n");

			sb.append("<table style='margin:10px'>\n");

			sb.append("<tr><td style='padding-bottom: 30px'><b>Transaction has failed!</b></td></tr>\n");

			sb.append(
					"<tr><td style='padding-bottom: 20px'>Error Code:" + errCode + "</td></tr><tr> </tr><tr> </tr>\n");
			sb.append("<tr><td style='padding-bottom: 20px'>Error Message:" + errMsg + "</td></tr>\n");
			if (errCode.equals(ValidationErrorReason.INSUFFICIENT_BALANCE.name()))
				sb.append(
						"<tr><td style='padding-bottom: 20px'><button type='submit' id='recharge' class='btn btn-primary' name='recharge' onclick=''>Recharge</Button></td></tr>\n");
			sb.append("</table></td></tr></table>\n");

		}
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("</body>\n");
		sb.append("</Html>\n");
		return sb.toString();

	}

	public static String getDummyPGPage(String txnRefId) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<Html>\n");
		sb.append("<Head></Head>\n");
		sb.append("<script>\n");
		sb.append("window.onload = function(){\n");
		sb.append("document.forms['frmPaymentInfo'].submit();\n");
		sb.append("}\n");
		sb.append("</script>\n");
		sb.append("<body>\n");
		sb.append("<label>Please wait...</label>\n");
		sb.append(
				"<form name=\"frmPaymentInfo\" id=\"frmPaymentInfo\" action=\"https://localhost:9000/resource/pg-response/OU13\" method=\"post\">\n");
		sb.append("<input type=hidden id=\"status\" name=\"status\" value=\"SUCCESS\"/>");
		sb.append("<input type=hidden id=\"auth_code\" name=\"auth_code\" value=\"" + System.currentTimeMillis()
				+ "\"/>");
		sb.append("<input type=hidden id=\"ac_num\" name=\"ac_num\" value=\"12345\"/>");
		sb.append("<input type=hidden id=\"ifsc\" name=\"ifsc\" value=\"HDFC0012345\"/>");
		sb.append("<input type=hidden id=\"transactionRefId\" name=\"transactionRefId\" value=\"" + txnRefId + "\"/>");
		sb.append("</form>\n");
		sb.append("</body>\n");
		sb.append("</Html>\n");
		return sb.toString();
	}

	public static String calculateCCF(String billerId, String ccfAmount, BillFetchRequest billfetchRequest) throws IOException{

		BillerService billerService = BeanLocator.getBean(BillerService.class);
		BillerView billerView = billerService.getBillerById(billerId);

		String billerCategory = billerView.getBlrCategoryName();

		// Integer amtccf = Integer.parseInt(ccfAmount);

		Integer amtccf = new BigDecimal(ccfAmount).multiply(new BigDecimal("100")).intValue();

		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		AmountType amount = new AmountType();
		AmtType amt = new AmtType();
		amt.setAmount(amtccf.toString());
		amount.setAmt(amt);
		billPaymentRequest.setAmount(amount);

		billPaymentRequest.setBillDetails(billfetchRequest.getBillDetails());

		billPaymentRequest.setAgent(billfetchRequest.getAgent());

		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		billPaymentRequest.setPaymentMethod(pmtMtdType);

		InterchangeFeeConfService interchangeFeeConfService = BeanLocator.getBean(InterchangeFeeConfService.class);
		InterchangeFeeService interchangeFeeService = BeanLocator.getBean(InterchangeFeeService.class);

		List<InterchangeFeeConfView> icFeeConfs = interchangeFeeConfService
				.fetchAllInterchangeFeeConfByBillerId(billerId);
		List<InterchangeFeeView> icFees = interchangeFeeService.fetchAllInterchangeFeeByBillerId(billerId);

		List<InterchangeFeeConfView> nonDefaultICConfs = new ArrayList<>(2);
		List<InterchangeFeeConfView> defaultICConfs = new ArrayList<>(2);

		for (InterchangeFeeConfView icConf : icFeeConfs) {
			if (icConf != null) {
				if (icConf.isDefault()) {
					defaultICConfs.add(icConf);
				} else {
					nonDefaultICConfs.add(icConf);
				}
			}
		}

		Map<String, List<InterchangeFeeView>> iFeeMap = new HashMap<String, List<InterchangeFeeView>>();
		for (InterchangeFeeView fee : icFees) {
			if (fee != null && fee.getFeeCode() != null) {
				if (iFeeMap.get(fee.getFeeCode()) == null) {
					iFeeMap.put(fee.getFeeCode(), new ArrayList<InterchangeFeeView>(2));
				}
				iFeeMap.get(fee.getFeeCode()).add(fee);
			}
		}
		ServiceTaxConfService serviceTaxConfService = BeanLocator.getBean(ServiceTaxConfService.class);
		ServiceTaxService serviceTaxService = BeanLocator.getBean(ServiceTaxService.class);
		List<ServiceTaxConfView> taxConfs = serviceTaxConfService.fetchAllActiveList();
		List<ServiceTaxView> taxes = serviceTaxService.fetchAllActiveList();

		Map<String, List<ServiceTaxView>> serviceTaxMap = new HashMap<>();

		if (taxes != null) {
			for (ServiceTaxView serviceTax : taxes) {
				if (serviceTax != null && serviceTax.getCode() != null) {
					if (serviceTaxMap.get(serviceTax.getCode()) == null) {
						serviceTaxMap.put(serviceTax.getCode(), new ArrayList<ServiceTaxView>());
					}

					serviceTaxMap.get(serviceTax.getCode()).add(serviceTax);
				}
			}
		}

		ResponseCodeService responseCodeService = BeanLocator.getBean(ResponseCodeService.class);
		if (null == responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false)) {
			responseCodeService.refresh();
		}

		ClearingFeeObj cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(billPaymentRequest,
				responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false), nonDefaultICConfs, iFeeMap);

		if (cfo.getBouInterchangeFees() != null && !cfo.getBouInterchangeFees().isEmpty()
				&& cfo.getCouInterchangeFees() != null && !cfo.getCouInterchangeFees().isEmpty()) {
			// nothing to do int fees are already calculated
		} else { // try with the default fees
			cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(billPaymentRequest,
					responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false), defaultICConfs, iFeeMap);
		}

		FeeCalculationHelper.calculateInterchangeFeeTaxes(cfo, billerCategory, InterchangeFeeDirectionType.C2B,
				taxConfs, serviceTaxMap);

		return cfo.getBOUTotalCCFPlusTax().divide(new BigDecimal("100")).toPlainString();
	}

	public static String createsearchTable(List<String> refList, List<String> billerlist, List<String> billNameList,
			List<String> billdateList, List<String> billamountList, List<String> requestTypeList) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<table class='table table-striped'>");
		sb.append("<thead> <tr> <th>BillerName</th><th>BillDate</th> <th>Billamount</th> </tr></thead>");
		sb.append("<tbody>");
		for (int i = 0; i < 5; i++) {
			if (i == billNameList.size()) {
				break;
			}
			sb.append("<tr>");

			// sb.append("<a
			// href='/agenthtml/'"+tenantId+"'+>"+billNameList.get(i)+"</a></p>");

			sb.append("<td><a href=\"javascript:anchorcall(\'" + refList.get(i) + "\',\'" + billerlist.get(i) + "\',\'"
					+ requestTypeList.get(i) + "\')\">" + billNameList.get(i) + "</a></td>");
			sb.append("<td>" + billdateList.get(i) + "</td>");
			sb.append("<td>" + billamountList.get(i) + "</td>");
			sb.append("</tr>");

		}
		sb.append("</tbody>");
		sb.append("</table>");

		return sb.toString();

	}

	public static String getSuccessMessage() {
		StringBuilder sb = new StringBuilder("");
		sb.append("<h2>Password has been changed successfully....</h2>");
		return sb.toString();
	}

	public static String getFailureMessage() {
		StringBuilder sb = new StringBuilder("");
		sb.append("<h2>Failed to reset password. Old password doesnot match.</h2>");
		return sb.toString();
	}
	
	
	public static String getPropretyFiles(String key){
        String value=null;

       try {
            Properties prop = new Properties();
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("uiutils.properties");
            prop.load(is);
                value=prop.getProperty(key);
            } catch (IOException e) {
                 //e.printStackTrace();
                 logger.error(e.getMessage());
             }
       return value;
      }
	
	
}
