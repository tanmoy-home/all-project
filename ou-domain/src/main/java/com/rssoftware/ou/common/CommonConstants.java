 package com.rssoftware.ou.common;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface CommonConstants {
	
	static final Logger log = LoggerFactory.getLogger(CommonConstants.class);

	public static int DB_RETRY_COUNT = 3;
	public static int DB_RETRY_DELAY = 2000;
	public static String INR_CURENCY = "356";

	public static int LENGTH_REF_ID = 35;
	public static int LENGTH_MSG_ID = 35;
	public static int LENGTH_TXN_REF_ID = 12;
	public static int DISHTV_CUSTOM_PARAM_LEN = 15;

	public static String EXPRESSION_IS_PAYMENT_SUCCESS = "EXPRESSION_IS_PAYMENT_SUCCESS";
	public static String EXPRESSION_GET_PAYMENT_INSTRUMENT = "EXPRESSION_GET_PAYMENT_INSTRUMENT";
	public static String EXPRESSION_GET_AUTH_CODE = "EXPRESSION_GET_AUTH_CODE";
	public static String EXPRESSION_GET_TRAN_REF_ID = "EXPRESSION_GET_TRAN_REF_ID";
	public static String EXPRESSION_GET_TRAN_AMT = "EXPRESSION_GET_TRAN_AMT";
	public static String EXPRESSION_GET_TRAN_ID = "EXPRESSION_GET_TRAN_ID";
	public static String EXPRESSION_GET_ORDER_ID = "EXPRESSION_GET_ORDER_ID";
	public static String EXPRESSION_GET_STATUS = "EXPRESSION_GET_STATUS";
	public static String EXPRESSION_GET_CCF = "EXPRESSION_GET_CCF";
	public static String EXPRESSION_GET_REMITTANCE_ACCOUNT = "EXPRESSION_GET_REMITTANCE_ACCOUNT";

	// public static String nbURI = "nbURI";

	// public static String SIGNATURE_NODE_PATTERN = "unexpected element
	// (uri:\"http://www.w3.org/2000/09/xmldsig#\", local:\"Signature\"";
	public static String SIGNATURE_NODE_PATTERN = "local:\"Signature\"";

	public static String HTML_PARAM_PREFIX = "[${";
	public static String HTML_PARAM_POSTFIX = "}]";

	public static char[] HTML_PARAM_PREFIX_CHAR_ARRAY = HTML_PARAM_PREFIX.toCharArray();
	public static char[] HTML_PARAM_POSTFIX_CHAR_ARRAY = HTML_PARAM_POSTFIX.toCharArray();

	public static String HTML_TEMPLATE_PREFIX = "[T${";
	public static String HTML_TEMPLATE_POSTFIX = "}T]";

	public static char[] HTML_TEMPLATE_PREFIX_CHAR_ARRAY = HTML_TEMPLATE_PREFIX.toCharArray();
	public static char[] HTML_TEMPLATE_POSTFIX_CHAR_ARRAY = HTML_TEMPLATE_POSTFIX.toCharArray();

	public static String HTML_JAVA_PREFIX = "[J${";
	public static String HTML_JAVA_POSTFIX = "}J]";

	public static char[] HTML_JAVA_PREFIX_CHAR_ARRAY = HTML_JAVA_PREFIX.toCharArray();
	public static char[] HTML_JAVA_POSTFIX_CHAR_ARRAY = HTML_JAVA_POSTFIX.toCharArray();

	public static String CU_DOMAIN = "CU_DOMAIN";
	public static String PAYMENT_GATEWAY_URL_PARAM_KEY = "PAYMENT_GATEWAY_URL";

	public static String DEFAULT_AGENT = "DEFAULT_AGENT";
	public static String DEFAULT_PAYMENT_MODE = "DEFAULT_PAYMENT_MODE";
	public static String DEFAULT_PAYMENT_CHANNEL = "DEFAULT_PAYMENT_CHANNEL";
	public static String DEFAULT_MAC_ADDRESS = "DEFAULT_MAC_ADDRESS";

	public static String TXN_REF_ID = "txnRefId";
	public static String REF_ID = "refId";
	public static String PAYMENT_AMOUNT = "paymentAmount";
	public static String QUICK_PAY = "quickPay";
	public static String ORDER_ID = "orderId";
	public static String MERCHANT_ID = "merchantId";
	public static String CUSTOMER_CONVIENCE_FEE = "ccf";
	public static String ACCOUNT_NO = "ac_num";
	public static String AUTH_CODE = "auth_code";
	public static String STATUS = "status";
	public static String REQ_ID = "reqId";


	public static final int POLLING_WAIT_TIME_MILLIS = 3000;
	public static final int MAX_POLLING_WAIT_TIME_MILLIS = 30000;
	public static final long LONG_MAX_POLLING_WAIT_TIME_MILLIS = 30000L;

	// Response Code
	public static final String RESP_SUCCESS_MSG = "Successful";
	public static final String RESP_FAILURE_MSG = "Failure";
	public static final String BBPS_SYSTEM_NAME = "BBCU";

	// BBCU Endpoint URL
	public static final String BILL_FETCH_REQUEST_URl = "/bbps/BillFetchRequest/1.0/urn:referenceId:";
	public static final String BILL_FETCH_RESPONSE_URl = "/bbps/BillFetchResponse/1.0/urn:referenceId:";
	public static final String DIAGNOSTIC_REQUEST_URL = "/bbps/ReqHbt/1.0/urn:referenceId:";
	public static final String BILL_PAYMENT_REQUEST_URl = "/bbps/BillPaymentRequest/1.0/urn:referenceId:";
	public static final String BILL_PAYMENT_RESPONSE_URl = "/bbps/BillPaymentResponse/1.0/urn:referenceId:";
	public static final String BILLER_FETCH_REQUEST_URl = "/bbps/BillerFetchRequest/1.0/urn:referenceId:";

	// TimeOUT TIme
	public static final long REQUEST_TIME_OUT = 300000;
	public static final String REQUEST_TIME_OUT_PARAM_DB = "requestTimeOut";

	// Retry Count
	public static final String RETRY_COUNT = "RETRY_COUNT";
	// Reversal Retry Count
	public static final String REVERSAL_RETRY_COUNT = "REVERSAL_RETRY_COUNT";

	public static final String PAYMENT_INFO_REMARKS = "Remarks";
	public static final String PAYMENT_INFO_IFSC_AC = "IFSC|AccountNo";
	public static final String PAYMENT_INFO_CARD_AUTHCODE = "CardNum|AuthCode";
	public static final String PAYMENT_INFO_MMID_MOBNUM = "MMID|MobileNo";
	public static final String PAYMENT_INFO_WALLET_MOBNUM = "WalletName|MobileNo";
	public static final String PAYMENT_INFO_VPA = "VPA";

	public static final String PAYMENT_CHANNEL_TAG_TERMINAL_ID = "TERMINAL_ID";
	public static final String PAYMENT_CHANNEL_TAG_MOBILE = "MOBILE";
	public static final String PAYMENT_CHANNEL_TAG_GEOCODE = "GEOCODE";
	public static final String PAYMENT_CHANNEL_TAG_POSTAL_CODE = "POSTAL_CODE";
	public static final String PAYMENT_CHANNEL_TAG_OS = "OS";
	public static final String PAYMENT_CHANNEL_TAG_APP = "APP";
	public static final String PAYMENT_CHANNEL_TAG_IP = "IP";
	public static final String PAYMENT_CHANNEL_TAG_MAC = "MAC";
	public static final String PAYMENT_CHANNEL_TAG_IFSC = "IFSC";
	public static final String PLATFORM_DATASOURCE_JNDI = "jdbc/platformDB";

	/* SMS integration constants */
	public static final String MOBILE_COUNTRY_CODE_INDIA = "91";
	public static final int MAX_SMS_TRY = 3;
	public static final String SMS_MAX_RETRY_ATTEMPT = "SMS_MAX_RETRY_ATTEMPT";
	public static final String SMS_SERVICE_URL = "SMS_SERVICE_URL";

	// Settlement Consumer Event
	public static final String SETTLEMENT_FILE_EVENT = "sFileEvent";
	public static final String SETTLEMENT_COMMON_FILE_LOCATION_KEY = "fileLocation";

	// Reconciliation Consumer Event
	public static final String RECONCILIATION_FILE_EVENT = "reconciliationEvent";

	// Common Constatns for Batch and RestTemplate Custom method for Batch
	public static final String BATCH_FILE_UPLOAD_PARAMETER_NAME = "file";
	
	//Batch related Event
	public static final String OU_BATCH_REQ_EVENT = "batchRequest";
	public static final String OU_BATCH_FILE_EVENT = "batchFile";
	public static final String BOU_BLR_RECON_EVENT = "bouBlrRecon";

	// CAS Codes
	public static String CURRENCY_SYMBOL = "356"; // numeric currency code
	public static int FEE_FRACTION_PLACES = 6;
	public static String PG_SUBMISSION_TYPE_PARAM_KEY = "PG_SUBMISSION_TYPE";
	public static String PG_NAME = "PG_NAME";

	// Fetch/Payment Customer Email
	public static final String CUSTOMER_EMAIL_FIELD = "EMAIL";

	// SMS Sending Mode
	public static final String SMS_NOTIFICATION_TYPE = "SMS_NOTIFICATION_TYPE";
	public static final String SMS_SERVICE_URL_PARAM_KEY = "SMS_SERVICE_URL";

	// Application Config Table Groups
	public static final String PAYMENT_MODE_Vs_CHANNEL = "Payment_Mode_Vs_Channel";
	public static final String PAYMENT_MODE_Vs_INFORMATION = "Payment_Mode_Vs_Information";
	public static final String CHANNEL_Vs_TAG = "Channel_Vs_Tag";

	public static String SEPERATOR_TILDE_SIGN = "~";
	public static String SEPERATOR_COMMA_SEPARATOR = ",";
	public static final String APPLICATION_CONFIG_COVERAGE = "COVERAGE";
	public static final String APPLICATION_CONFIG_OWNERSHIP = "OWNERSHIP";

	public static final String EMPTY_STRING = "";
	public static final String APPLICATION_CONFIG_GROUP_SMTP = "SMTP";
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	public static final String BEAN_MAIL_SENDER_PREFIX = "JavaMailSender_";
	public static final String  QUICK_PAY_AMOUNT_TYPE_TAG = "QUICKPAY";
	public static final String  ZERO_VALUE_AS_STRING = "0";
	public static final String COMPLAINT_REQ_URL = "/CMS/TxnStatusComplainRequest";
	public static final String COMPLAINT_RSP_URL = "/CMS/TxnStatusComplainResponse";

	public static final String AGENT_ID="agent_id";
	public static final String PASS_KEY="pass_key";
	public static final String IMEI_CODE="imei_code";
	public static final String PAYMENT_MODE="payment_mode";
	public static final String BILL_PAY_STATUS="billPayStatus";
	public static final String BILL_PAY_STATUS_BOOLEAN="billPayStatusBoolean";
	public static final String RBL_REF_NO="rblRefNo";
	public static final String PAYMENT_INSTRUMENT="PAYMENT_INSTRUMENT";
	public static final String BBPS_REF_ID="bbpsRefId";
	public static String PAYMENT_GATEWAY_REFUND_URL="PAYMENT_GATEWAY_REFUND_URL";

	public static final String EXCEPTION_MSG = "Some sytsem exception occured, please contact administrator ...";
	public static final String STRING_SLASH = "/";
	public final String SPACE = " ";
	public static final String OU_REQUEST_EVENT = "ouRequest";
	public static final String OU_BILLER_SIMULATOR_EVENT = "billerSimulator";
	public static final String OU_BILL_FETCH_EVENT = "billFetch";
	
	public static final String SESSION_LOGGED_IN_USER = "logged_in_user";
	public static String SESSION_TENANT_ID = "tenantId";
	public static final String STRING_COLON = ":";
	public static final String CU_RESPONSE_EVENT = "cuResponse";
	public static final String NOTIFICATION_URL = "/APIService/response/urn:tenantId:";
	
	// Biller OU endpoing URL
	public static final String BILLER_BILL_SYNC_FETCH_REQUEST_URl = "/APIService/BillerOU/Biller_simulator/fetch_sync_online_bill/urn:tenantId:";
	public static final String BILLER_BILL_FETCH_REQUEST_URl = "/APIService/BillerOU/Biller_simulator/fetch_online_bill/urn:tenantId:";
	public static final String BILLER_BILL_PAY_REQUEST_URl = "/APIService/BillerOU/Biller_simulator/pay_online_bill/urn:tenantId:";
	public static final String BILLER_BILL_SYNC_PAY_REQUEST_URl = "/APIService/BillerOU/Biller_simulator/pay_sync_online_bill/urn:tenantId:";
	public static final String BILLER_REVERSAL_REQUEST_URl = "/APIService/BillerOU/Biller_simulator/reverse_payment/urn:tenantId:";
	
	// PG Integratin Required endpoing URL
//	public static final String OU_API_DOMAIN = "https://localhost:9090";	
	//SMS for IDBI constants
	public static String TEMPLATE_TXN_ID = "##TransactionReferanceNo##";
	public static String TEMPLATE_TXN_AMOUNT = "##Amount##";
	public static String TEMPLATE_DATE = "##TransactionDate##";
	public static String TEMPLATE_BENEFICIARY_NAME = "##BeneficiaryName##";
	public static String TEMPLATE_TRANSACTION_STATE = "##TransactionState##";
	public static String TEMPLATE_BILLER_NAME = "##BillerName##";
	public static String TEMPLATE_CONSUMER_NO = "##ConsumerNo##";
	public static String TEMPLATE_PAYMENT_CHANNEL = "##PaymentChannel##";
	
	//COMPLAINT SMS
	public static String TEMPLATE_COMP_ID ="##COMP_ID##";
	public static String TEMPLATE_COMP_SVC_TYPE ="##SVC_TYPE##";
	public static String TEMPLATE_TXN_REF_ID ="####TXN_REF_ID####";
	
	//OTP SMS
	public static String TEMPLATE_OTP ="##OTP##";
	public static String TRAN_OTP="COMP_TRAN";
	public static String SVC_OTP="COMP_SVC";
	public static String OTP_EXPIRE_TIME_IN_SEC="OTP_EXPIRE_TIME_IN_SEC";
	//SMS Constants
	public static final String SMS_BY_URL="BY_URL";
	public static final String SMS_TO_DB="TO_DB";
	public static final String SMS_SENT="SENT";
	public static final String SMS_NOT_SENT="NOT_SENT";
	
	public static final String STRING_INDIA_ISD_CODE = "+91";
	public static final String STRING_SYSTEM = "SYSTEM";
	public static String FROM_EMAIL = "FROM_EMAIL";
	public static String QUICK_PAY_BILL_NUMBER = "QUICK_PAY_BILL_NUMBER";

	//DishTv parameters
	public static final String BILLER_DISH_TV_ALIAS = "Dish TV";
	//DishTv FROM RS LAN
	//public static final String urlValidate = "http://demo.itzcash.com/payment/servlet/VCDetailsServlet";
	//public static final String urlPayment = "http://demo.itzcash.com/payment/servlet/DishTVTransactionServlet";
	//DishTv from ITZCASH UAT
	public static final String urlValidate = "http://staging.itzcash.com/payment/servlet/VCDetailsServlet";
	public static final String urlPayment = "http://staging.itzcash.com/payment/servlet/DishTVTransactionServlet";
	
	//DishTv from ITZCASH PROD
	//public static final String urlValidate = "http://www.itzcash.com:8080/payment/servlet/VCDetailsServlet";
	//public static final String urlPayment = "http://www.itzcash.com:8080/payment/servlet/DishTVTransactionServlet";
	String   DEFAULT_USERNAME_PASSWORD="YWdlbnQxOiQyYSQwNCRWOS56di5LZUtHdjU1RWlYa0cuMkZ1dmV2UHBvaldIYnhJclpDMnVuc1o4V3ZmeW9Ka3dRMg==";
	String   IS_HTTP_ENABLED="IsInternalHttpEnabled";
	String   HTTP_URL_PREFIX="http://";
	String   HTTPS_URL_PREFIX="https://";

	
	//to customize failure ack sent to cu for bill fetch and payment
	public static final String ENABLE_BILL_FETCH_FAILURE_ACK_NAME = "ENABLE_BILL_FETCH_FAILURE_ACK";
	public static final String ENABLE_BILL_FETCH_FAILURE_ACK_VALUE = "1";
	public static final String ENABLE_BILL_PAYMENT_FAILURE_ACK_NAME = "ENABLE_BILL_PAYMENT_FAILURE_ACK";
	public static final String ENABLE_BILL_PAYMENT_FAILURE_ACK_VALUE = "1";

	public static String getPropretyFiles(String key){
		String value=null;
        
        try {
        	Properties prop = new Properties();
	    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    	InputStream is = classloader.getResourceAsStream("BOU_Process.properties");
	    	prop.load(is);
			value=prop.getProperty(key);
		 } catch (IOException e) {
			 //e.printStackTrace();
			 log.error(e.getMessage());
		 }
        return value;
	}

}
