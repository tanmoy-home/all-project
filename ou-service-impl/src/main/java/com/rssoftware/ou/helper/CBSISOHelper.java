package com.rssoftware.ou.helper;

import generated.CustAccountDetails;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;


//import org.apache.commons.lang3.StringUtils;
//import org.npci.upi.schema.PayConstant;
//import org.npci.upi.schema.PayerConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


//import com.rssoftware.upiint.common.GatewayConfiguration;
import com.rssoftware.ou.common.utils.CbsIsoStanGenerator;
//import com.rssoftware.upiint.model.imps.IMPSRequest;
import com.rssoftware.ou.common.utils.CommonUtils;
//import com.rssoftware.upiint.model.PSPAcProviderDetail;
//import com.rssoftware.upiint.model.TransactionChannel;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;
import com.rssoftware.ou.model.cbs.TxnStatus;
import com.rssoftware.ou.model.cbs.TxnType;
import com.rssoftware.ou.schema.cbs.CBSISOConstants;



public class CBSISOHelper {
	
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	public static final String SUCCESS = "S";
	public static final String FAILED = "F";
	private static final String PARAM_DELIM = "&";
	//private static String IDBI_WS_URL_UAT;
	//private static final String IDBI_WS_URL_PROD="http://10.144.16.123:5555/UPIWS/service/upi/"; //PROD
	//private static final String CARD_VALIDATION_URL_UAT = "http://10.144.16.32:5206/cardsahayakI_test/abhaycard.jsp?HtFlag=S&HtCardNumber={CARD_NO}&HtClientcode={CUST_ID}&HtCardValidity={CARD_VALIDITY}";
	//private static final String CARD_VALIDATION_URL_PROD = "http://10.144.16.207:5206/cardsahayakI/abhaycard.jsp?HtFlag=S&HtCardNumber={CARD_NO}&HtClientcode={CUST_ID}&HtCardValidity={CARD_VALIDITY}";
	private static final String IDBI_WS_ACC_LIST="acctdetails?";
	private static final String IDBI_WS_ATM_VAL="atmcardval?";
	private static final String IDBI_WS_AADHAR="adharenabledacctno?";
	private static final String IDBI_WS_MOB="mobNo=";
	private static final String IDBI_WS_ACCT_NO="acctNo=";
	private static final String AADHAR_NO = "adharNo=";
	private static final String IDBI_WS_DEBITNO="debitNo=";
	private static final String IDBI_WS_EXP_DATE="expDate=";
	public static final Map<String, String> RrnTxnIdMap = new ConcurrentHashMap<String, String>();
	public static final Map<String, Object> prevTxnMapdr = new ConcurrentHashMap<String, Object>();
	public static final Map<String, Object> prevTxnMapcr = new ConcurrentHashMap<String, Object>();
	
	public static final List<String> SCHEME_CODES = new ArrayList<String>(Arrays.asList("RCNRE", "RSNRE", "RSNWE", "RSPNE", "RSNPP", "RSSGE", "RSRPI", "RSMNV", "RSERI", "RSSNE", "USNRE", "RNEPS", "RSYNE", "USNRO", "CCNRO", "RCNRO"));
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static final Logger log = LoggerFactory.getLogger(CBSISOHelper.class);
	
	private ConfigurableEnvironment environment;
	
	private static String IDBI_WS_URL_UAT=getPropretyFiles("IDBI_URL");
	private static String IDBI_WS_URL_PROD=getPropretyFiles("IDBI_URL_PROD");
	private static String CARD_VALIDATION_URL_UAT=getPropretyFiles("CARD_VALIDATION_URL");
	private static String CARD_VALIDATION_URL_PROD=getPropretyFiles("CARD_VALIDATION_PROD");
	
	private static String formatMobile(String mobile){
		if(mobile.length()==10){
			return "91"+mobile;
		}
		return mobile;
	}
	
	public static CustAccountDetails listAcct(String mobile, String acctNum) {
		
		
		
		String url = (System.getProperty("env", "UAT").equals("PROD") ? IDBI_WS_URL_PROD : IDBI_WS_URL_UAT)
				+ IDBI_WS_ACC_LIST
				+ (mobile != null ? IDBI_WS_MOB + formatMobile(mobile) : "")
				+ (acctNum != null ? (mobile != null ?PARAM_DELIM:"") + IDBI_WS_ACCT_NO + acctNum
						: "");
		ResponseEntity<CustAccountDetails> responseEntity = restTemplate
				.getForEntity(url, CustAccountDetails.class);
		if (log.isDebugEnabled()) log.debug("Response listAcct: " + responseEntity.getBody());
		return responseEntity.getBody();
	}
	
	public static CustAccountDetails atmCardValidation(String debitNo,
			String expdate, String mobile) {
		expdate = expdate.replaceAll("/", "");
		String url = (System.getProperty("env", "UAT").equals("PROD") ? IDBI_WS_URL_PROD : IDBI_WS_URL_UAT)
				+ IDBI_WS_ATM_VAL
				+ (debitNo != null ? IDBI_WS_DEBITNO + debitNo : "")
				+ (expdate != null ? PARAM_DELIM + IDBI_WS_EXP_DATE + expdate
						: "")
				+ (mobile != null ? PARAM_DELIM + IDBI_WS_MOB + formatMobile(mobile) : "");
		ResponseEntity<CustAccountDetails> responseEntity = restTemplate
				.getForEntity(url, CustAccountDetails.class);
		return responseEntity.getBody();
	}
	
	public static String validateCard(String debitNo, String expdate, String custId) {
		expdate = expdate.replaceAll("/", "");
		String url = (System.getProperty("env", "UAT").equals("PROD") ? CARD_VALIDATION_URL_PROD : CARD_VALIDATION_URL_UAT)
					 .replace("{CARD_NO}", "")
					 .replace("{CUST_ID}", custId.trim())
					 .replace("{CARD_VALIDITY}", "");
		if (log.isDebugEnabled()) log.debug("Calling validateCard: " + url);
		String response = restTemplate.getForObject(url, String.class);
		String trimmedResponse = "";
		if (response != null) {
			trimmedResponse = response.trim();
		}
		if (log.isDebugEnabled()) log.debug("Response validateCard: " + response);
		return trimmedResponse;
	}

	public static CustAccountDetails aadharMapping(String aadharNo) {
		String url = (System.getProperty("env", "UAT").equals("PROD") ? IDBI_WS_URL_PROD : IDBI_WS_URL_UAT) + IDBI_WS_AADHAR
				+ (aadharNo != null ? AADHAR_NO + aadharNo : "");
		ResponseEntity<CustAccountDetails> responseEntity = restTemplate
				.getForEntity(url, CustAccountDetails.class);
		return responseEntity.getBody();
	}
	
	public static Map<Integer, Object> populateMapForISO8583(CBSRequest request,Map<Integer, Object> origRequest) throws IOException{
		if (request == null)
			return null;
		
		Map<Integer, Object> isoMap;
		String stan = CommonUtils.lpad(Integer.toString(IMPSStanGenerator.generateStanForIMPS()), 6, CBSISOConstants.ISO_ACK_INST_ID_TRNSFR_VALUE);
		
		isoMap = new ConcurrentHashMap<Integer, Object>();
		String rrn = TransactionHelper.getRrn(Calendar.getInstance(), stan);
		
		
		isoMap.put(new Integer("126"), rrn);
		isoMap.put(new Integer(CBSISOConstants.ISO_PROCESSING_CODE), request.getProcessingCode()); // Processing
		
		isoMap.put(new Integer(CBSISOConstants.ISO_CONTROLLER_ID), CBSISOConstants.ISO_UPI_CNTLR_ID);//Delivery Channel Controller Id
		isoMap.put(new Integer("11"), CbsIsoStanGenerator.generateStanForCBS());
	    isoMap.put(new Integer("12"), CommonUtils.getFormattedDateYMDHHmmss(Calendar.getInstance()));
	    isoMap.put(new Integer("17"), CommonUtils.getFormattedDateYMD(Calendar.getInstance()));

	    isoMap.put(new Integer("34"), CBSISOConstants.ISO_CUST_ID);
	    isoMap.put(new Integer("32"), CBSISOConstants.ISO_ACK_INST_ID); //todo
	    //isoMap.put(new Integer("126"), "KSKB1180104000019327");
	    isoMap.put(new Integer(CBSISOConstants.ISO_CURRENCY_CODE), "INR");
	    //Field 125(narration) -- UPI + 17 spaces + custref + / + payer or payee VPA
	    if(TxnType.CREDIT.equals(request.getTxnType())|| TxnType.DEBIT.equals(request.getTxnType())||
	    		TxnType.CREDIT_REVERSAL.equals(request.getTxnType())|| TxnType.DEBIT_REVERSAL.equals(request.getTxnType()) ){
	    	isoMap.put(new Integer(CBSISOConstants.ISO_SOL_CODE), CBSISOConstants.ISO_UPI_CNTLR_ID+"                 " + request.getCustRefId()
	    		+"/"+request.getUserVpa());
	    }else{
	    	isoMap.put(new Integer(CBSISOConstants.ISO_SOL_CODE), CBSISOConstants.ISO_UPI_CNTLR_ID);
	    }
		if(CBSISOConstants.ISO_SOL_PRCSNG_CODE.equals(request.getProcessingCode())){
			RrnTxnIdMap.put(rrn, request.getTxnRefId());
			isoMap.put(new Integer(CBSISOConstants.ISO_DEBIT_ACC_NUM),processAccNum(CBSISOConstants.ISO_BANK_CODE, 
					request.getAccNo().substring(0,4),
					request.getAccNo(), 
					CBSISOConstants.ISO_DEBIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE), CBSISOConstants.FUNCTION_VALUE);//Function code
		}

		if(TxnType.CREDIT.equals(request.getTxnType()) ){
			RrnTxnIdMap.put(rrn, request.getTxnRefId());
			String[] split = request.getAccNo().split(" ");
			isoMap.put(new Integer(CBSISOConstants.ISO_CREDIT_ACC_NUM),
					processAccNum(split[0], split[1], split[2], CBSISOConstants.ISO_CREDIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_TXN_AMNT),
					convertToIsoAmount(Double.toString(request.getTxnAmount())));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE), CBSISOConstants.FUNCTION_VALUE);//Function code
		}
		else if(TxnType.DEBIT.equals(request.getTxnType()) ){
			RrnTxnIdMap.put(rrn, request.getTxnRefId());
			String[] split = request.getAccNo().split(" ");
			isoMap.put(new Integer(CBSISOConstants.ISO_DEBIT_ACC_NUM),
					processAccNum(split[0], split[1], split[2], CBSISOConstants.ISO_DEBIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_TXN_AMNT),
					convertToIsoAmount(Double.toString(request.getTxnAmount())));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE), CBSISOConstants.FUNCTION_VALUE);//Function code
		}
		else if(TxnType.CREDIT_REVERSAL.equals(request.getTxnType())){
			RrnTxnIdMap.put(rrn, request.getOriginalTxnId());
			String[] split = request.getAccNo().split(" ");
			isoMap.put(new Integer(CBSISOConstants.ISO_CREDIT_ACC_NUM),
					processAccNum(split[0], split[1], split[2], CBSISOConstants.ISO_CREDIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_TXN_AMNT),
					convertToIsoAmount(Double.toString(request.getTxnAmount())));
			isoMap.put(new Integer("11"), origRequest.get(new Integer("11")));
			isoMap.put(new Integer("56"), populateField56(origRequest, 
					isoMap.get(new Integer("12")).toString(),isoMap.get(new Integer("32")).toString()));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE), "400");//Function code
		}
		else if(TxnType.DEBIT_REVERSAL.equals(request.getTxnType())){
			RrnTxnIdMap.put(rrn, request.getOriginalTxnId());
			String[] split = request.getAccNo().split(" ");
			isoMap.put(new Integer(CBSISOConstants.ISO_DEBIT_ACC_NUM),
					processAccNum(split[0], split[1], split[2], CBSISOConstants.ISO_DEBIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_TXN_AMNT),
					convertToIsoAmount(Double.toString(request.getTxnAmount())));
			isoMap.put(new Integer("11"), origRequest.get(new Integer("11")));
			isoMap.put(new Integer("56"), populateField56(origRequest, 
					isoMap.get(new Integer("12")).toString(),isoMap.get(new Integer("32")).toString()));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE),"400");//Function code
		}
		else if (CBSISOConstants.ISO_BAL_ENQ_PRCSNG_CODE.equals(request.getProcessingCode())){
			RrnTxnIdMap.put(rrn, request.getTxnRefId());
			String[] split = request.getAccNo().split(" ");
			isoMap.put(new Integer(CBSISOConstants.ISO_DEBIT_ACC_NUM),
					processAccNum(split[0], split[1], split[2], CBSISOConstants.ISO_DEBIT_ACC_NUM));
			isoMap.put(new Integer(CBSISOConstants.ISO_FUNCTION_CODE), CBSISOConstants.FUNCTION_VALUE);//Function code
		}
		
		return isoMap;
		
	}
	
	private static String populateField56(Map<Integer,Object> isoMap,String field12,String field32){
		//return "1200"+isoMap.get(new Integer("11"))+field12+"06"+field32;
		return "1200"+isoMap.get(new Integer("11"))+isoMap.get(new Integer("12")) + "010";
	}
	
	private static String convertToIsoAmount(String origAmount){
		boolean containsDecimal = origAmount.contains(".");
		String finalAmt = "";
		if (containsDecimal){
			int locDecimal = origAmount.indexOf(".");
			String wholeNumber = origAmount.substring(0, locDecimal);
			String decimal = origAmount.substring(locDecimal+1);
			if (decimal.length()==1){
				decimal = decimal+CBSISOConstants.ISO_ACK_INST_ID_TRNSFR_VALUE;
			}else{
				decimal = origAmount.substring(locDecimal+1, locDecimal+3);
			}
			finalAmt = wholeNumber+decimal;
			
		}else{
			finalAmt = origAmount+"00";
		}
		String output12 = CommonUtils.lpad(finalAmt, 12, CBSISOConstants.ISO_ACK_INST_ID_TRNSFR_VALUE);
		return output12;
	}
	
	private static String convertFromIsoAmount(String origAmount){
		String decimal = origAmount.substring(origAmount.length()-2, origAmount.length());
		String wholeNumber = origAmount.substring(0, origAmount.length()-2);
		String finalAmt = wholeNumber+"."+decimal;
		return finalAmt;
	}
	
	public static CBSResponse populateResponseFromMessageMap(Map<Integer, ? extends Object> isoMap){
		String mti = isoMap.get(new Integer(CBSISOConstants.ISO_ACK_INST_ID_TRNSFR_VALUE)).toString(); 
		if ("0800".equalsIgnoreCase(mti)) mti = "0810";
		else if ("0200".equalsIgnoreCase(mti)) mti = "0210";
		else if ("0220".equalsIgnoreCase(mti)) mti = "0230";
		else if ("0400".equalsIgnoreCase(mti)) mti = "0410";
		else if ("0420".equalsIgnoreCase(mti)) mti = "0430";
		
		Map<Integer, ? extends Object> field120Map = new HashMap<Integer, String>();
		if (isoMap.get(new Integer("120")) !=null && isoMap.get(new Integer("120")) instanceof Map){
			field120Map = (Map<Integer, ? extends Object>)isoMap.get(new Integer("120"));
		}
		
		CBSResponse response = new CBSResponse();
		if("000".equals(isoMap.get(new Integer(CBSISOConstants.ISO_RESP_CODE)))
				&& CBSISOConstants.ISO_SOL_PRCSNG_CODE.equals(isoMap.get(new Integer(CBSISOConstants.ISO_PROCESSING_CODE)))){
			String solId = CBSISOHelper.processSolId((String)isoMap.get(new Integer(CBSISOConstants.ISO_SOL_CODE)));
			response.setSol(solId);
		}
		if(!"000".equals(isoMap.get(new Integer(CBSISOConstants.ISO_RESP_CODE)))){
			response.setErrorCode((String)isoMap.get(new Integer(CBSISOConstants.ISO_RESP_CODE)));
			response.setStatus(TxnStatus.FAILURE);
		}
		else{
			if(CBSISOConstants.ISO_BAL_ENQ_PRCSNG_CODE.equals(isoMap.get(new Integer(CBSISOConstants.ISO_PROCESSING_CODE)))
					|| CBSISOConstants.ISO_DEBIT_PRCSNG_CODE.equals(isoMap.get(new Integer(CBSISOConstants.ISO_PROCESSING_CODE)))
					|| CBSISOConstants.ISO_CREDIT_PRCSNG_CODE.equals(isoMap.get(new Integer(CBSISOConstants.ISO_PROCESSING_CODE)))){
				processAccBalance(response, (String)isoMap.get(new Integer(CBSISOConstants.ISO_ACC_BAL_CODE)));
			}
			response.setStatus(TxnStatus.SUCCESS);
		}
		response.setCbsRefId((String)isoMap.get(new Integer("11")));
//		String rrn = null==isoMap.get(new Integer(CBSISOConstants.ISO_RETRIEVAL_REF_DATA))?isoMap.get(new Integer(CBSISOConstants.ISO_TXN_REF))+"":isoMap.get(new Integer(CBSISOConstants.ISO_RETRIEVAL_REF_DATA)).toString();
		String rrn = isoMap.get(new Integer("126")).toString();//126
		response.setTxnRefId(RrnTxnIdMap.get(rrn));
		
		RrnTxnIdMap.remove(rrn);
		
		
		return response;
	}
	
//	public static Map<Integer, Object> populateMapForAdminMsg(IMPSRequest request){
//		if (request==null) return null;
//		
//		Map<Integer, Object> isoMap;
//		isoMap = new HashMap<Integer, Object>();
//		//Changing to IST as per NPCI
//		isoMap.put(new Integer("7"), TransactionHelper.getDateTime(Calendar.getInstance(), TimeZone.getTimeZone("IST"), "MMddHHmmss"));//transmission datetime #7
//		String stan = CommonUtils.lpad(Integer.toString(IMPSStanGenerator.generateStanForIMPS()), 6, CBSISOConstants.ISO_ACK_INST_ID_TRNSFR_VALUE);
//		
//		isoMap.put(new Integer("11"), stan);
//		//isoMap.put(new Integer("12"), getDateTime(TimeZone.getTimeZone("IST"), "HHmmss"));//local transaction time #12
//		//DE 32 not required for Admin message, confirmed by IMPS team
//		/*if (request.getAcqrInstId()!=null)
//			isoMap.put(new Integer("32"), request.getAcqrInstId());//acquirer institution id #32
//		else
//			isoMap.put(new Integer("32"), GatewayConfiguration.getIMPSUPIOrgId());*/
//		//isoMap.put(new Integer("37"), getRrn(stan));//retrieval reference data #37
//
//		//additional data #48(NF)
//		//Confirmed by NPCI that this is not required for admin messages
//		//isoMap.put(new Integer("48"), "");//additional data #48
//		
//		if (request.isImpsSignOn()){
//			isoMap.put(new Integer("70"), "001");
//		}else if (request.isImpsSignOff()){
//			isoMap.put(new Integer("70"), "002");
//		}else if (request.isImpsCutOver()){
//			isoMap.put(new Integer("70"), "201");
//		}else if (request.isImpsEcho()){
//			isoMap.put(new Integer("70"), "301");
//		}
//		
//		return isoMap;
//		
//	}
	
//	public static IMPSRequest populateDummyTransaction(){
//
//		///////
//		IMPSRequest impsRequest = new IMPSRequest();
//		
//		impsRequest.setTxnId("0002");
//		impsRequest.setPayeeMobileNumber("9989796959");
//		//dummyTransaction.setPayeeAcMobileMobnum("9830910717");
//		impsRequest.setPayerMobileNumber("9830910718");
//		//dummyTransaction.setPayerAcMobileMobnum("9830910718");
//		impsRequest.setPayeeMmid("9034123");
//		//dummyTransaction.setPayeeAcMobileMmid("1234567");
//		impsRequest.setPayerMmid("8901234");
//		//dummyTransaction.setPayeeAcMobileMmid("8901234");
//		impsRequest.setPayeeAccountNumber("119677660150");
//		//dummyTransaction.setPayeeAcAccountAcnum("0123456789");
//		impsRequest.setPayerAccountNumber("5678901234");
//		//dummyTransaction.setPayeeAcAccountAcnum("5678901234");
//		impsRequest.setPayeeIfsc("BNPA0009009");
//		//dummyTransaction.setPayeeAcAccountIfsc("ICIC1234567");
//		impsRequest.setPayerIfsc("HDFC1234567");
//		//dummyTransaction.setPayeeAcAccountIfsc("HDFC1234567");
//		impsRequest.setPayeeEntityType(PayerConstant.PERSON);
//		//dummyTransaction.setPayeeType("PERSON");
//		impsRequest.setPayerEntityType(PayerConstant.PERSON);
//		//dummyTransaction.setPayerType("PERSON");
//		impsRequest.setPayeeName("RaviKumar");
//		//dummyTransaction.setPayeeName("Payee 123456");
//		impsRequest.setPayerName("JayKumar");
//		//dummyTransaction.setPayeeName("Payer 123456");
//		impsRequest.setPayeeAmt("101.03");
//		//dummyTransaction.setPayeeAmt("101.03");
//		impsRequest.setPayeeCurr(CBSISOConstants.ISO_DEFAULT_CURRENCY);
//		//dummyTransaction.setPayeeCurr("356");
//		impsRequest.setPayerAmt("101.03");
//		//dummyTransaction.setPayerAmt("101.03");
//		impsRequest.setPayerCurr(CBSISOConstants.ISO_DEFAULT_CURRENCY);
//		//dummyTransaction.setPayerCurr("356");
//		impsRequest.setTransactionType(PayConstant.PAY);
//		//dummyTransaction.setReqPayTxnType("PAY");
//		Calendar cal = Calendar.getInstance();
//		impsRequest.setOriginalTimeStamp(cal);
//		impsRequest.setField12OrigTxn(CommonUtils.getDateTime(cal, TimeZone.getTimeZone("IST"), "HHmmss"));// TODO need new DB field
//		impsRequest.setField13OrigTxn(CommonUtils.getDateTime(cal, TimeZone.getTimeZone("IST"), "MMdd"));// TODO need new DB field
//		impsRequest.setAcqrInstId("109999");
//		impsRequest.setNotes("RS Software");
//		impsRequest.setChannel(TransactionChannel.MOBILE);
//		
//		////////
//		return impsRequest;
//	}
	
	private static void processAccBalance(CBSResponse cbsResponse,String bal){
		bal = bal.trim();
		String currencyCode = bal.substring(bal.length()-3);
		int fract = Currency.getInstance(currencyCode).getDefaultFractionDigits();
		StringTokenizer tokenizer = new StringTokenizer(bal.substring(0, bal.length()-3),
				"+-", true) ;
		for(int i=1;i<=2;i++)
			tokenizer.nextToken();
		String sign = tokenizer.nextToken();
		String balance = tokenizer.nextToken();
		while(balance.indexOf('0')==0){
			balance = balance.substring(1);
		}
		
		if (balance==null || "".equals(balance))
			balance = "000";
		
		if(fract > 0){
			balance = balance.substring(0, balance.length()-fract) + "." + balance.substring(balance.length()-fract);
		}
		cbsResponse.setAccBalance(Double.parseDouble(sign + balance));
	}
	
	public static String processAccNum(String bankId,String solId,String accNum,String code){
		StringBuilder sb = new StringBuilder();
		if(CBSISOConstants.ISO_CREDIT_ACC_NUM.equals(code)){
			sb.append("  ");
		}
		sb.append(bankId);
		for(int i=bankId.length();i<11;i++){
			sb.append(" ");
		}
		for(int i=0;i<5-solId.trim().length();i++){
			solId = "0" + solId.trim();
		}
		
		sb.append(solId);
		for(int i=solId.length();i<8;i++){
			sb.append(" ");
		}
		sb.append(accNum);
		for(int i=accNum.length();i<19;i++){
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public static String processSolId(String solId){
		int index = 9+80+80+8+5+3+1+5+80+80+80+6;
		return solId.substring(index,index+8).trim();
	}
	
//	public static String getRemitterBankOrgId(IMPSRequest request){
//		if (PayConstant.COLLECT.equals(request.getTransactionType())){
//			PSPAcProviderDetail remitterBank = null;
//			if (CommonUtils.hasValue(request.getPayerIfsc())){
//				remitterBank = GatewayConfiguration.getInstance().getPSPByIFSC(request.getPayerIfsc().trim());
//			}else if (CommonUtils.hasValue(request.getPayerMmid())){
//				String fwdNbin = request.getPayerMmid().substring(0, 4);
//				remitterBank = GatewayConfiguration.getInstance().getPSPByNbin(fwdNbin);
//			}
//			if (remitterBank!=null){
//				return remitterBank.getOrgId();
//			}
//		}
//		return null;
//	}

	public static String convertCBSObjToString(Object cbsObj){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(cbsObj);
		encoder.close();
		return new String(baos.toByteArray());
	}
	
	public static Object convertStringToCBSObj(String cbsObj){
		ByteArrayInputStream baos = new ByteArrayInputStream(cbsObj.getBytes());
		XMLDecoder xmlDec = new XMLDecoder(baos);
		Object cbsReq = xmlDec.readObject();
		xmlDec.close();
		return cbsReq;
	}
	
	public static String getPropretyFiles(String key){
		String value=null;
        
        try {
        	Properties prop = new Properties();
	    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    	InputStream is = classloader.getResourceAsStream("getURL.properties");
	    	prop.load(is);
			value=prop.getProperty(key);
		 } catch (IOException e) {
			 //e.printStackTrace();
			 log.error(e.getMessage());
		 }
        return value;
	}
	
}
