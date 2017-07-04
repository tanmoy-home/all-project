package com.rssoftware.ou.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType.Tag;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.PymntInfType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.SpltPayType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.domain.CustomUserDetails;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeBreakup;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.SmsMessageView;
public class CommonUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
	private static final String SPACE_REPLACE_PATTERN = "_^_";
	private static final String SPACE = " ";
	private static final String PARENT_BILLER_ID_POSTFIX = "_^P";
	private static String HOSTNAME = null;
	private static org.joda.time.format.DateTimeFormatter formatterHHmmss = DateTimeFormat.forPattern("HHmmss");
	private static org.joda.time.format.DateTimeFormatter formatterMMdd = DateTimeFormat.forPattern("MMdd");
	private static org.joda.time.format.DateTimeFormatter formatterUTC = ISODateTimeFormat.dateTimeNoMillis();
	private static org.joda.time.format.DateTimeFormatter formatteryyyyMMdd = DateTimeFormat.forPattern("yyyyMMdd");
	private static org.joda.time.format.DateTimeFormatter formatteryyyy_MM_dd = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static org.joda.time.format.DateTimeFormatter formatteryyyy_MM_ddHH_mm_ss = DateTimeFormat
			.forPattern("yyyy-MM-dd HH:mm:ss");
	private static org.joda.time.format.DateTimeFormatter formatterMMddHHmmss = DateTimeFormat.forPattern("MMddHHmmss");
	private static org.joda.time.format.DateTimeFormatter formatteryyyy = DateTimeFormat.forPattern("yyyy");
	private static org.joda.time.format.DateTimeFormatter formatterDDDHH = DateTimeFormat.forPattern("DDDHH");
	public static org.joda.time.format.DateTimeFormatter formaterdd_MM_YYYY_HH_MM_A = DateTimeFormat.forPattern("dd-MM-YYYY hh:mm aa");
	private static org.joda.time.format.DateTimeFormatter formatterdd_MMM_yyyy_HH_mm_ss_Z = DateTimeFormat
			.forPattern("dd/MMM/yyyy:HH:mm:ss Z");
	private static org.joda.time.format.DateTimeFormatter formatterdd_MM_yyyy_HH_mm_ss_Z = DateTimeFormat
			.forPattern("dd-MM-yyyy HH:mm:ss Z");
	
	private static org.joda.time.format.DateTimeFormatter formatteryyyy_MM_dd_T_HH_mm_ss_Z = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static String FORMATTED_IP = getFormattedIP();
	private static int MAX_VALUE = 60000;
	private static AtomicInteger COUNTER = new AtomicInteger(0);
	public static final String RECORD_CREATOR_ID_IS_BATCH = "BATCH";
	private static org.joda.time.format.DateTimeFormatter formatterYMDHHmmss = DateTimeFormat.forPattern("YYYYMMddHHmmss");
	private static org.joda.time.format.DateTimeFormatter formatterYMDHHmm = DateTimeFormat.forPattern("YYYYMMddHHmm");
	private static org.joda.time.format.DateTimeFormatter formatterYMD = DateTimeFormat.forPattern("YYYYMMdd");
	public static org.joda.time.format.DateTimeFormatter formaterdd_MM_YYYY = DateTimeFormat.forPattern("dd-MM-YYYY");
//	private static String getFormattedIP() {
//		byte[] ips = getCurrentIp().getAddress();
//		StringBuilder str = new StringBuilder(12); // 3*4
//		for (byte ipFrag : ips) {
//			int temp = ipFrag;
//			if (temp < 0) {
//				temp = temp + 256; // byte is -128 to 127, but IP is 0 to 255,
//									// so adding 256 (2^8)
//			}
//
//			str.append(padLeft(String.valueOf(temp), 3));
//		}
//		return str.toString();
//	}
	private static String getFormattedIP() {
		return getIP();
	}
	private static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s).replace(' ', '0');
	}
	public static String getDefaultTrustStore() {
		return System.getProperty("default.trust.store", "/etc/ssl/certs/java/cacerts");
	}
	public static final boolean isHeartbeatEnabled = true;
	public static String getFormattedTimestamp(Date date) {
		return formatterUTC.print(new DateTime(date));
	}
	public static String getFormattedCurrentTimestamp() {
		return getFormattedTimestamp(new Date());
	}
	public static Timestamp currentTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	public static String getFormattedDateyyyyMMdd(Date dt) {
		return formatteryyyyMMdd.print(new DateTime(dt));
	}
	
	public static Date parseDateyyyyMMdd(String dtStr) {
		if (dtStr == null) {
			return null;
		}
		try {
			return formatteryyyyMMdd.parseDateTime(dtStr).toDate();
		} catch (Exception e) {
			LOGGER.error( e.getMessage(), e);
	        LOGGER.info("In Excp : " + e.getMessage());	
	          return null;
		  }
	}
	public static String getFormattedDateyyyy_MM_dd(Date dt) {
		return formatteryyyy_MM_dd.print(new DateTime(dt));
	}
	public static String getDateFromddMMyy(Date dt) {
		return new SimpleDateFormat("dd-MM-yy").format(dt);
	}
	public static String getFormattedDateyyyy_MM_ddHH_mm_ss(Date dt) {
		return formatteryyyy_MM_ddHH_mm_ss.print(new DateTime(dt));
	}
	public static String getFormattedDateMMddHHmmss(Calendar dt) {
		return formatterMMddHHmmss.print(new DateTime(dt));
	}
	public static String getFormattedDateyyyy(Calendar dt) {
		return formatteryyyy.print(new DateTime(dt));
	}
	public static String getFormattedDateDDDHH(Calendar dt) {
		return formatterDDDHH.print(new DateTime(dt));
	}
	public static String getFormattedDatedd_MMM_yyyy_HH_mm_ss_Z(Calendar dt) {
		return formatterdd_MMM_yyyy_HH_mm_ss_Z.print(new DateTime(dt));
	}
	
	public static String formatterdd_MM_yyyy_HH_mm_ss_Z(Calendar dt) {
		return formatterdd_MM_yyyy_HH_mm_ss_Z.print(new DateTime(dt));
	}
	public static Date parseDateyyyy_MM_dd(String dtStr) {
		try {
			return formatteryyyy_MM_dd.parseDateTime(dtStr).toDate();
		} catch (Exception e) {
			LOGGER.error( e.getMessage(), e);
	        LOGGER.info("In Excp : " + e.getMessage());	
	          return null;
		  }
	}
	public static String getFormattedDateHHmmss(Calendar cal) {
		return formatterHHmmss.print(new DateTime(cal));
	}
	public static String getFormattedDateMMdd(Calendar cal) {
		return formatterMMdd.print(new DateTime(cal));
	}
	
	public static String getFormaterdd_MM_YYYY_HH_MM_A(String  datetime) {
		
		if(datetime.contains("."))
		{
			datetime=datetime.substring(0,datetime.indexOf('.'));
		}
		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt=null;
		try {
			 dt = mdyFormat.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formaterdd_MM_YYYY_HH_MM_A.print(new DateTime(dt.getTime()));
	}
	
public static String getFormaterdd_MM_YYYY(String  datetime) {
		
		if(datetime.length()>10)
		{
			datetime=datetime.substring(0,10);
		}
		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt=null;
		try {
			 dt = mdyFormat.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formaterdd_MM_YYYY.print(new DateTime(dt.getTime()));
	}
	
	public static Date parseDateUTC(String str) {
		try {
			return formatterUTC.parseDateTime(str).toDate();
		} catch (Exception e) {
			LOGGER.error( e.getMessage(), e);
	        LOGGER.info("In Excp : " + e.getMessage());	
	          return null;
		   }
	}
	public static Calendar parseCalendarUTC(String str) {
		try {
			return formatterUTC.parseDateTime(str).toGregorianCalendar();
		} catch (Exception e) {
			LOGGER.error( e.getMessage(), e);
	        LOGGER.info("In Excp : " + e.getMessage());	
	         return null;
		  }
	}
	public static String getFormattedEffectiveDates(String inputFormat, String outFormat, String strDate) {
		String newFormatDate = null;
		/*
		 * Example: SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
		 * SimpleDateFormat dFormatFinal = new SimpleDateFormat("dd-MMM-yyyy");
		 * SimpleDateFormat dFormatFinal = new SimpleDateFormat("dd/MM/yyyy");
		 */
		Date date = null;
		SimpleDateFormat dFormat = new SimpleDateFormat(inputFormat);
		SimpleDateFormat dFormatFinal = new SimpleDateFormat(outFormat);
		try {
			date = dFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOGGER.error( e.getMessage(), e);
	        LOGGER.info("In Excp : " + e.getMessage());	
	       }
		newFormatDate = dFormatFinal.format(date);
		return newFormatDate;
	}
	public static String getLoggedInUser() {
		if (null != SecurityContextHolder.getContext()
				&& null != SecurityContextHolder.getContext().getAuthentication()) {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}
		return "SYSTEM";
	}
	public static String getProxyHost() {
		return System.getProperty("proxy.host");
	}
	public static String getProxyPort() {
		return System.getProperty("proxy.port");
	}
	public static boolean hasValue(String input) {
		if (input != null && !"".equals(input.trim())) {
			return true;
		}
		return false;
	}
	public static String getHostname() {
		if (HOSTNAME != null) {
			return HOSTNAME;
		} else {
			try {
				HOSTNAME = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				HOSTNAME = getIP();
			}
		}
		return HOSTNAME;
	}
//	public static InetAddress getCurrentIp() {
//		try {
//			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//			while (networkInterfaces.hasMoreElements()) {
//				NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
//				Enumeration<InetAddress> nias = ni.getInetAddresses();
//				while (nias.hasMoreElements()) {
//					InetAddress ia = (InetAddress) nias.nextElement();
//					if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
//						return ia;
//					}
//				}
//			}
//		} catch (SocketException e) {
//		}
//		return null;
//	}
//	
	public static InetAddress getCurrentIp() {
		try {
			
			 InetAddress inet = InetAddress.getLocalHost();
			 String ip = inet.getHostAddress();
			 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
//	public static String getIP() {
//		byte[] ips = getCurrentIp().getAddress();
//		StringBuilder str = new StringBuilder(12); // 3*4
//		for (byte ipFrag : ips) {
//			int temp = ipFrag;
//			if (temp < 0) {
//				temp = temp + 256; // byte is -128 to 127, but IP is 0 to 255,
//									// so adding 256 (2^8)
//			}
//
//			str.append(String.valueOf(temp));
//			str.append('.');
//		}
//		return str.substring(0, str.length() - 1);
//	}
	
	public static String getIP() {
		String ip = null;
		try {
			
			 InetAddress inet = InetAddress.getLocalHost();
			 ip = inet.getHostAddress();
			 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LOGGER.error("exception in getting ip",e);
		}
		return ip;
		
	}
	public static String getPort() {
		return System.getProperty("server.port");
	}
	public static String getSiteCode() {
		return System.getProperty("server.sitecode");
	}
	public static String encode(String data) {
		System.out.println(data);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
		String hashedPassword = passwordEncoder.encode(data);
		System.out.println(hashedPassword);
		return hashedPassword;
	}
	public static String getGMTFormatTs() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(CommonUtils.currentTimestamp());
		cal.add(Calendar.DAY_OF_MONTH, 0);
		DateTime dtTime = new DateTime(new java.sql.Timestamp(cal.getTimeInMillis()));
		String TxnDateTs = dtTime.toLocalDateTime().toDateTime(DateTimeZone.getDefault()).toString();
		return TxnDateTs;
	}
	public static boolean isSplitPay(BillPaymentRequest billPaymentRequest) {
		if (billPaymentRequest != null && billPaymentRequest.getPaymentMethod() != null
				&& billPaymentRequest.getPaymentMethod().getSplitPay() != null) {
			if (SpltPayType.YES == billPaymentRequest.getPaymentMethod().getSplitPay()) {
				return true;
			}
		}
		return false;
	}
	public static boolean isQuickPay(BillPaymentRequest billPaymentRequest) {
		if (billPaymentRequest != null && billPaymentRequest.getPaymentMethod() != null
				&& billPaymentRequest.getPaymentMethod().getQuickPay() != null) {
			if (QckPayType.YES == billPaymentRequest.getPaymentMethod().getQuickPay()) {
				return true;
			}
		}
		return false;
	}
	public static String getDDMMYYYDateWithSlash(String dateString) {
		StringBuilder actualDate = new StringBuilder("");
		if (dateString != null && dateString.length() == 8) {
			StringBuilder dateStr = new StringBuilder(dateString);
			String yr = dateStr.substring(0, 4);
			String month = dateStr.substring(4, 6);
			String day = dateStr.substring(6);
			actualDate.append(day + "/" + month + "/" + yr);
		}
		return actualDate.toString();
	}
	public static Set<String> getParamsFromString(String input) {
		Set<String> params = new HashSet<String>();
		if (input != null) {
			Integer currentStartPosition = null;
			char[] charArray = input.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (currentStartPosition == null && isParamStart(i, charArray)) {
					currentStartPosition = i;
				}
				if (currentStartPosition != null && isParamEnd(i, charArray)) {
					params.add(input.substring(currentStartPosition,
							i + CommonConstants.HTML_PARAM_POSTFIX_CHAR_ARRAY.length));
					currentStartPosition = null;
				}
			}
		}
		return params;
	}
	private static boolean isParamStart(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_PARAM_PREFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_PARAM_PREFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	private static boolean isParamEnd(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_PARAM_POSTFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_PARAM_POSTFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	public static Set<String> getTemplatesFromString(String input) {
		Set<String> params = new HashSet<String>();
		if (input != null) {
			Integer currentStartPosition = null;
			char[] charArray = input.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (currentStartPosition == null && isTemplateStart(i, charArray)) {
					currentStartPosition = i;
				}
				if (currentStartPosition != null && isTemplateEnd(i, charArray)) {
					params.add(input.substring(currentStartPosition,
							i + CommonConstants.HTML_TEMPLATE_POSTFIX_CHAR_ARRAY.length));
					currentStartPosition = null;
				}
			}
		}
		return params;
	}
	private static boolean isTemplateStart(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_TEMPLATE_PREFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_TEMPLATE_PREFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	private static boolean isTemplateEnd(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_TEMPLATE_POSTFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_TEMPLATE_POSTFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	public static String extractParamName(String input) {
		if (input != null && input.startsWith(CommonConstants.HTML_PARAM_PREFIX)
				&& input.endsWith(CommonConstants.HTML_PARAM_POSTFIX)) {
			return input.substring(CommonConstants.HTML_PARAM_PREFIX.length(),
					input.length() - CommonConstants.HTML_PARAM_POSTFIX.length());
		}
		return input;
	}
	public static String extractTemplateName(String input) {
		if (input != null && input.startsWith(CommonConstants.HTML_TEMPLATE_PREFIX)
				&& input.endsWith(CommonConstants.HTML_TEMPLATE_POSTFIX)) {
			return input.substring(CommonConstants.HTML_TEMPLATE_PREFIX.length(),
					input.length() - CommonConstants.HTML_TEMPLATE_POSTFIX.length());
		}
		return input;
	}
	private static Set<String> getJavasFromString(String input) {
		Set<String> params = new HashSet<String>();
		if (input != null) {
			Integer currentStartPosition = null;
			char[] charArray = input.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (currentStartPosition == null && isJavaStart(i, charArray)) {
					currentStartPosition = i;
				}
				if (currentStartPosition != null && isJavaEnd(i, charArray)) {
					params.add(input.substring(currentStartPosition,
							i + CommonConstants.HTML_JAVA_POSTFIX_CHAR_ARRAY.length));
					currentStartPosition = null;
				}
			}
		}
		return params;
	}
	private static boolean isJavaStart(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_JAVA_PREFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_JAVA_PREFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	private static boolean isJavaEnd(int position, char[] input) {
		for (int i = 0; i < CommonConstants.HTML_JAVA_POSTFIX_CHAR_ARRAY.length; i++) {
			if (input.length <= (position + i)) {
				// end encountered before complete match
				return false;
			} else if (input[position + i] != CommonConstants.HTML_JAVA_POSTFIX_CHAR_ARRAY[i]) {
				// value mismatch
				return false;
			}
		}
		// came here, so match found
		return true;
	}
	private static String extractJavaName(String input) {
		if (input != null && input.startsWith(CommonConstants.HTML_JAVA_PREFIX)
				&& input.endsWith(CommonConstants.HTML_JAVA_POSTFIX)) {
			return input.substring(CommonConstants.HTML_JAVA_PREFIX.length(),
					input.length() - CommonConstants.HTML_JAVA_POSTFIX.length());
		}
		return input;
	}
	private static String executeJava(String javaInput) {
		// BeanLocator.getBean(ParamService.class);
		try {
			String className = javaInput.substring(0, javaInput.lastIndexOf('.'));
			String methodName = javaInput.substring(javaInput.lastIndexOf('.') + 1);
			Class<?> c = Class.forName(className);
			// extract if there is something between ( and )
			String argument = null;
			if (javaInput.contains("(") && javaInput.contains(")")) {
				argument = javaInput.substring(javaInput.indexOf('(') + 1, javaInput.lastIndexOf(')'));
				methodName = methodName.substring(0, methodName.lastIndexOf('('));
			}
			if (hasValue(argument)) {
				Method m = c.getMethod(methodName, String.class);
				Object ret = m.invoke(null, argument); // object is null because
														// this is expected to
														// be a static method
				if (ret != null) {
					return ret.toString();
				}
			} else {
				Method m = c.getMethod(methodName);
				Object ret = m.invoke(null); // object is null because this is
												// expected to be a static
												// method
				if (ret != null) {
					return ret.toString();
				}
			}
		} catch (Exception e) {
			LOGGER.error( e.getMessage(), e);
	          LOGGER.info("In Excp : " + e.getMessage());	
	      }
		return null;
	}
	public static String extractResolvedParamValue(String input) {
		if (input == null) {
			return null;
		}
		for (String javaParam : CommonUtils.getJavasFromString(input)) {
			String java = CommonUtils.extractJavaName(javaParam);
			String paramValue = executeJava(java);
			input = org.springframework.util.StringUtils.replace(input, javaParam,
					paramValue != null ? paramValue : "");
		}
		return input;
	}
	public static String getMacAddress() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(ip);
			byte[] mac = ni.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
	public static String extractValueFromContext(String input) {
		return TransactionContext.getVariable(input);
	}
	public static PaymentChannel getPaymentChannel(BillPaymentRequest billPaymentRequest) {
		PaymentChannel pc = null;
		if (billPaymentRequest != null && billPaymentRequest.getAgent() != null
				&& billPaymentRequest.getAgent().getDevice() != null
				&& billPaymentRequest.getAgent().getDevice().getTags() != null) {
			for (Tag tag : billPaymentRequest.getAgent().getDevice().getTags()) {
				if (tag != null && tag.getName() == DeviceTagNameType.INITIATING_CHANNEL) {
					pc = PaymentChannel.getFromExpandedForm(tag.getValue());
				}
			}
		}
		return pc;
	}
	public static PaymentModeBreakup[] getPaymentModeBreakups(BillPaymentRequest billPaymentRequest) {
		PaymentModeBreakup[] breakup = null;
		try {
			if (billPaymentRequest != null && billPaymentRequest.getPaymentMethod() != null
					&& billPaymentRequest.getAmount() != null && billPaymentRequest.getAmount().getAmt() != null
					&& billPaymentRequest.getAmount().getAmt().getAmount() != null) {
				
				long totalAmt =(long)Double.parseDouble(billPaymentRequest.getAmount().getAmt().getAmount());// Long.parseLong(billPaymentRequest.getAmount().getAmt().getAmount());
				String paymentModeStr = billPaymentRequest.getPaymentMethod().getPaymentMode();
				boolean splitPay = isSplitPay(billPaymentRequest);
				if (splitPay) {
					String paymentMode[] = paymentModeStr.split("\\|");
					long splitPayAmt = Long.parseLong(billPaymentRequest.getAmount().getSplitPayAmount());
					breakup = new PaymentModeBreakup[2];
					breakup[0] = new PaymentModeBreakup();
					breakup[0].setAmount(totalAmt - splitPayAmt);
					breakup[0].setPaymentMode(PaymentMode.getFromExpandedForm(paymentMode[0]));
					breakup[1] = new PaymentModeBreakup();
					breakup[1].setAmount(splitPayAmt);
					breakup[1].setPaymentMode(PaymentMode.getFromExpandedForm(paymentMode[1]));
					return breakup;
				} else {
					breakup = new PaymentModeBreakup[1];
					breakup[0] = new PaymentModeBreakup();
					breakup[0].setAmount(totalAmt);
					breakup[0].setPaymentMode(PaymentMode.getFromExpandedForm(paymentModeStr));
				}
			}
		} catch (Exception e) {
			// do nothing, will return null
		}
		return breakup;
	}
	public static boolean isEffective(String effectiveFrom, String effectiveTo) {
		boolean isEffectiveEntity = Boolean.FALSE;
		String todaysDate = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		if (effectiveFrom != null && effectiveTo != null) {
			if (effectiveFrom.compareTo(todaysDate) <= 0 && todaysDate.compareTo(effectiveTo) <= 0) {
				isEffectiveEntity = Boolean.TRUE;
			}
		}
		if (effectiveFrom != null && effectiveTo == null) {
			if (effectiveFrom.compareTo(todaysDate) <= 0) {
				isEffectiveEntity = Boolean.TRUE;
			}
		}
		if (effectiveFrom == null && effectiveTo != null) {
			if (effectiveTo.compareTo(todaysDate) >= 0) {
				isEffectiveEntity = Boolean.TRUE;
			}
		}
		if (effectiveFrom == null && effectiveTo == null) {
			isEffectiveEntity = Boolean.TRUE;
		}
		return isEffectiveEntity;
	}
	// public static void main(String[] args){
	// executeJava("com.rssoftware.ou.common.utils.CommonUtils.extractValueFromContext(test)");
	// }
	public static String getPGIntermediateFormPage(List<PGParam> pgParams, String url) {
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
		// sb.append(BeanLocator.getBean(ParamService.class).retrieveStringParamByName(CommonConstants.PAYMENT_GATEWAY_URL));
		sb.append(url);
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
	public static String getPGIntermediateURLPage(List<PGParam> pgParams, String url) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<Html>\n");
		sb.append("<Head></Head>\n");
		sb.append("<body>\n");
		sb.append("<label>Please wait...</label>\n");
		sb.append("<script>\n");
		sb.append("(function() {\n");
		String url1 = "";
		url1 = url1.concat("window.location.replace(\"" + url + "?");
		if (pgParams != null) {
			for (PGParam pgParam : pgParams) {
				url1 = url1.concat(pgParam.getParamName() + "=" + pgParam.getParamValue() + "&");
			}
		}
		url1 = url1.concat("\");\n");
		sb.append(url1);
		sb.append("})();\n");
		sb.append("</script>\n");
		sb.append("</body>\n");
		sb.append("</Html>\n");
		return sb.toString();
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
	public static PmtMtdType getInternetBankingAsPaymentModeWithoutSplitAndQuickPay() {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		return pmtMtdType;
	}
	public static PmtMtdType getInternetBankingAsPaymentModeWithQuickPay() {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.YES);
		return pmtMtdType;
	}
	public static PmtMtdType getInternetBankingAsPaymentModeWithSplitPay() {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.YES);
		pmtMtdType.setQuickPay(QckPayType.NO);
		return pmtMtdType;
	}
	public static PymntInfType getPaymentInformationWithBlankIFSCAndAccount() {
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setValue("");
		pymntInfo.getTags().add(ptag);
		return pymntInfo;
	}
	public static PymntInfType getDefaultPaymentInstrumentAsIFSCAndAccount(String paymentInstrument) {
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setValue(paymentInstrument);
		pymntInfo.getTags().add(ptag);
		return pymntInfo;
	}
	public static String getCCFAsStringWithOutDecimalPoint(String ccf) {
		if (ccf != null) {
			if (ccf.indexOf(".") >= 0) {
				return String.valueOf((new BigDecimal(ccf).multiply(CommonConstants.HUNDRED)).intValue());
			} else {
				return ccf;
			}
		} else {
			return CommonConstants.EMPTY_STRING;
		}
	}
	private static int getRandomNo(Integer digitLength) {
		int defaultLegnth = 5;
		int generatedRandomNo;
		if (null == digitLength) {
			digitLength = defaultLegnth;
		}
		int lengthOfNo = legnthGenerator(digitLength);
		Random r = new Random(System.currentTimeMillis());
		generatedRandomNo = ((1 + r.nextInt(2)) * lengthOfNo + r.nextInt(lengthOfNo));
		return generatedRandomNo;
	}
	public static boolean compareBothAddress(String reg_addrs, String reg_state, String reg_city, String reg_pin,
			String com_addrs, String com_state, String com_city, String com_pin) {
		if (reg_addrs != null && reg_state != null && reg_city != null && reg_pin != null && com_addrs != null
				&& com_state != null && com_city != null && com_pin != null) {
			if ((reg_addrs.equals(com_addrs)) && (reg_state.equals(com_state)) && (reg_city.equals(com_city))
					&& (reg_pin.equals(com_pin))) {
				return true;
			}
		}
		return false;
	}
	private static int legnthGenerator(Integer length) {
		StringBuffer genLength = new StringBuffer("1");
		if (length > 1) {
			for (int i = 1; i < length; i++) {
				genLength.append("0");
			}
		}
		return Integer.valueOf(genLength.toString());
	}
	public static String generateBillerId(String name_p, String sub_bilr, boolean isParent) {
		String billerId = null;
		String name = "";
		String subbilr = "";
		if (name_p != null && sub_bilr != null) {
			name = name_p.replaceAll("[-+.^:,_ ]", "").toUpperCase();
			subbilr = sub_bilr.replaceAll("[-+.^:,_ ]", "").toUpperCase();
			// Random random = new Random();
		} else {
			name = name_p.replaceAll("[-+.^:,_ ]", "").toUpperCase();
			subbilr = sub_bilr;
		}
		if (!isParent) {
			if (name.length() > 4 || name.length() == 4) {
				billerId = name.substring(0, 4);
			} else {
				billerId = name;
				int i = name.length();
				while (i < 4) {
					billerId += "0";
					i++;
				}
			}
			if (subbilr == null) {
				billerId += "00000";
			} else {
				if (subbilr.length() > 5) {
					billerId += subbilr.substring(0, 5);
				} else {
					int i = subbilr.length();
					billerId += subbilr.substring(0, i);
					while (i < 5) {
						billerId += "0";
						i++;
					}
				}
			}
			int i = getRandomNo(5);
			billerId += String.valueOf(i);
		} else {
			if (name.length() > 4 || name.length() == 4) {
				billerId = name.substring(0, 4);
			} else {
				billerId = name;
				int i = name.length();
				while (i < 4) {
					billerId += "0";
					i++;
				}
			}
			if (subbilr == null) {
				billerId += "00000";
			} else {
				if (subbilr.length() > 5) {
					billerId += subbilr.substring(0, 5);
				} else {
					int i = subbilr.length();
					billerId += subbilr.substring(0, i);
					while (i < 5) {
						billerId += "0";
						i++;
					}
				}
			}
			int i = getRandomNo(5);
			billerId += String.valueOf(i);
		}
		if (billerId.length() == 14) {
			return billerId;
		}
		return null;
	}
	public static String validateEffectiveDates(String effectiveDate) {
		StringBuilder effectDt = new StringBuilder("");
		if (effectiveDate != null && !effectiveDate.equals("")) {
			String[] split = effectiveDate.split("/");
			for (int i = split.length - 1; i >= 0; i--) {
				effectDt.append(split[i]);
			}
		} else {
			effectDt.append(CommonUtils.getFormattedDateyyyyMMdd(new Date()));
		}
		return effectDt.toString();
	}
	public static Collection<String> getPaymentChannelsAsStrings(Collection<PaymentChannelLimit> sourceColl,
			Collection<String> destinationColl) {
		for (PaymentChannelLimit pcl : sourceColl) {
			if(pcl.getPaymentChannel()!=null)
			{
			destinationColl.add(pcl.getPaymentChannel().name());
			destinationColl.add(pcl.getPaymentChannel().getExpandedForm());
			}
		}
		return destinationColl;
	}
	
	public static String[] getSmsParamsAsStrings(SmsMessageView messageView) {
		Collection<String> destinationColl= new ArrayList<String>();
		for (String pcl : messageView.getMessage().keySet()) {
			destinationColl.add(pcl);
		}
		return destinationColl.toArray(new String[destinationColl.size()]);
	}
	public static Collection<String> getPaymentModesAsStrings(Collection<PaymentModeLimit> sourceColl,
			Collection<String> destinationColl) {
		for (PaymentModeLimit pml : sourceColl) {
			if(pml.getPaymentMode()!=null)
			{
			destinationColl.add(pml.getPaymentMode().name());
			destinationColl.add(pml.getPaymentMode().getExpandedForm());
			}
		}
		return destinationColl;
	}
	private static String getNextSequence() {
		long temp = COUNTER.getAndIncrement();
		if (temp < 0) {
			temp = temp + (Integer.MIN_VALUE * (-1L));
		}
		return String.valueOf(temp % MAX_VALUE);
	}
	public static String generateId() {
		// host + current time millis + sequence
		StringBuilder str = new StringBuilder(FORMATTED_IP);
		str.append(String.valueOf(System.currentTimeMillis()));
		str.append(padLeft(getNextSequence(), 5));
		return encode(str.toString());
	}
	public static CustomUserDetails getLoggedInUserDetails() {
		if (null != SecurityContextHolder.getContext()
				&& null != SecurityContextHolder.getContext().getAuthentication()) {
			return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		return null;
	}
	
	public static String getServerNameWithPort() {	
		return getIP() + CommonConstants.STRING_COLON + getPort();
	}
	
	public static String getFormattedDateYMDHHmmss(Calendar cal){
	    return formatterYMDHHmmss.print(new org.joda.time.DateTime(cal));
	}
	
	public static String getFormattedDateYMDHHmm(Calendar cal){
	    return formatterYMDHHmm.print(new org.joda.time.DateTime(cal));
	}
	
	public static String getFormattedDateYMD(Calendar cal){
	    return formatterYMD.print(new org.joda.time.DateTime(cal));
	}
	
	public static String lpad(String inputString, int totalLength, String padStr) {
		if (inputString == null || padStr == null)
			return null;
		int padlength = totalLength - inputString.length();
		String returnString = "";
		for (int i = 0; i < padlength; i++)
			returnString += padStr;
		returnString += inputString;
		return returnString;
	}
	
	public static String convertPaisaToRupeesAsString(String amountInPaisa){
		return (new BigDecimal(amountInPaisa).divide(CommonConstants.HUNDRED)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
		
	}
	public static CellStyle getArialCellStyle(Workbook wb) {
		CellStyle cs = null;
		// font size 10
				Font f = wb.createFont();
				f.setFontName("Arial");
				f.setFontHeightInPoints((short) 8);
				// Simple style
				cs = wb.createCellStyle();
				cs.setFont(f);
				return cs;
	}
	
	public static CellStyle getArialHeaderCellStyle(Workbook wb) {
	// Bold Fond
	Font bold = wb.createFont();
	bold.setFontName("Arial");
	bold.setColor(IndexedColors.BLACK.getIndex());
	bold.setFontHeightInPoints((short)10);
	bold.setBold(true);
	bold.setItalic(false);
	// Bold style
	CellStyle csBold = wb.createCellStyle();
	csBold.setFont(bold);
	csBold.setWrapText(true);
	csBold.setIndention((short)10);
	csBold.setAlignment(HorizontalAlignment.CENTER);
	return csBold;
	}
	
	public static File writeToOutputStream(Workbook wb, String location) {
		File file = null;
		try {
			// Write the Excel file
			OutputStream outputStream = null;
			file = new File(location);
			outputStream = new FileOutputStream(file);
			wb.write(outputStream);
			outputStream.close();
			wb.close();
		}
		catch(Exception e) {
			LOGGER.error("Error in writeToOutputStream:" , e);
		}
		return file;
	}
}
