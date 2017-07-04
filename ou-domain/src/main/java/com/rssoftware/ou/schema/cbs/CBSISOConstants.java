package com.rssoftware.ou.schema.cbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rssoftware.ou.common.utils.CommonUtils;

public class CBSISOConstants { 
//	public static ImpsConnectionStatus connectionStatus;
	public static final String successEchoResponse = "00";
	
	private static final Map<String, String> ImpsErrCdMap = new HashMap<String, String>();
    static {
    	ImpsErrCdMap.put("M0","Verification successful but original credit transaction declined");
    	ImpsErrCdMap.put("M1","Invalid / Unverified beneficiary credentials");
    	ImpsErrCdMap.put("M2","Amount limit Exceeded");
    	ImpsErrCdMap.put("M3","Account blocked/Frozen");
    	ImpsErrCdMap.put("M4","NRE Account");
    	ImpsErrCdMap.put("M5","Account closed");
    	ImpsErrCdMap.put("M6","Limit exceeded for member bank");
    	ImpsErrCdMap.put("M7","Transaction not permitted for this account type");
    	ImpsErrCdMap.put("M8","Transaction not permitted for this account type");
    	ImpsErrCdMap.put("MM","Transaction not allowed as this is non-reloadable card");
    	ImpsErrCdMap.put("MP","Transaction not allowed as Bank not enabled yet for P2A functionality");
    	ImpsErrCdMap.put("MC","Functionality not yet available for merchant through the payee bank");
    	ImpsErrCdMap.put("MX","Transaction not allowed as Aadhaar Number belongs to Remitter Bank");
    	ImpsErrCdMap.put("MV","Transaction not allowed as Bank is not enabled for IMPS P2U");
    	ImpsErrCdMap.put("MU","Transaction not allowed as Aadhar number is not in NPCI database");
    	ImpsErrCdMap.put("04","Transaction not allowed as Amount is greater than 2 Lakhs");
    	ImpsErrCdMap.put("MN","Transaction not allowed as Beneficiary bank is not enable for Foreign Inward Remittance. To be Decline by NPCI");
    	ImpsErrCdMap.put("Mw","Transaction not allowed as beneficiary is PPI and DE 3 is 904300 and applicable for P2P Transaction only. To be decline by NPCI");
    	ImpsErrCdMap.put("MQ","Transaction not allowed as invalid payment reference");
    	ImpsErrCdMap.put("MR","Transaction not allowed as invalid amount");
    	ImpsErrCdMap.put("MS","Transaction not allowed as invalid remitter account number");
    	ImpsErrCdMap.put("MT","Transaction not allowed as general error");
    	ImpsErrCdMap.put("08","Beneficiary node offline");
    	ImpsErrCdMap.put("91","Issuing bank CBS or node offline");
    	ImpsErrCdMap.put("92","Invalid NBIN");
    	ImpsErrCdMap.put("12","Invalid transaction");
    	ImpsErrCdMap.put("20","Invalid response code");
    	ImpsErrCdMap.put("96","Unable to process");
    	ImpsErrCdMap.put("51","Insufficient balance in pool A/c");
    }
	

	
	public static final String isoTypeEngine = "ISOTYPE";
	public static final String isoDocEngine = "ISODOC";
//	public static Map<String, IMPSResponse> RrnTxnIdMap = new ConcurrentHashMap<String, IMPSResponse>();
	

	private static String parserModel;// Which one to use? ISOType or ISODoc
	public static String getParserModel() {
		CBSISOConstants.parserModel = CBSISOConstants.isoDocEngine;//TODO get from a config file
		return parserModel;
	}
	public static void setParserModel(String parserModel) {
		CBSISOConstants.parserModel = parserModel;
	}
	
	public static String getImpsErrorText(String impsErrorCode){
		return ImpsErrCdMap.get(impsErrorCode);
	}
	
	public static List<String> getImpsErrorList(String impsErrorCode){
		if (!CommonUtils.hasValue(impsErrorCode)) return null;
		
		String errorText = ImpsErrCdMap.get(impsErrorCode.toUpperCase());
		if (CommonUtils.hasValue(errorText)){
			List<String> extErrMsgs = new ArrayList<String>();
			extErrMsgs.add(impsErrorCode + "-" + errorText);
			return extErrMsgs;
		}else
			return null;
		
	}
	public static final String ISO_ACK_INST_ID_INQ_VALUE = "601794";
	public static final String ISO_DEFAULT_ECHO_DATA = "1234";
	public static final String ISO_ECHO_DATA = "59";
	public static final String ISO_DEFAULT_AUTH_MODE = "1233";
	public static final String ISO_AUTH_MODE = "38";
	public static final String ISO_RESP_CODE = "39";
	public static final String ISO_ACC_BAL_CODE = "48";
	public static final String ISO_ACK_INST_ID_TRNSFR_VALUE = "0";
	public static final String ISO_ACK_INST_ID_CODE = "32";
	public static final String ISO_DEBIT_ACC_NUM = "102";
	public static final String ISO_CREDIT_ACC_NUM = "103";
	public static final String ISO_DEFAULT_CURRENCY = "356";
	public static final String ISO_CURRENCY_CODE = "49";
	public static final String ISO_UPI_CNTLR_ID = "UPI";
	public static final String ISO_CONTROLLER_ID = "123";
	public static final String FUNCTION_VALUE = "200";
	public static final String ISO_FUNCTION_CODE = "24";
	public static final String ISO_PROCESSING_CODE = "3";
	public static final String ISO_TXN_REF = "127";
	public static final String ISO_RETRIEVAL_REF_DATA = "37";
	public static final String ISO_TXN_AMNT = "4";
	public static final String ISO_SOL_PRCSNG_CODE = "820000";
	public static final String ISO_BAL_ENQ_PRCSNG_CODE = "310000";
	public static final String ISO_DEBIT_PRCSNG_CODE = "480000";
	public static final String ISO_CREDIT_PRCSNG_CODE = "490000";
	public static final String ISO_SOL_CODE = "125";
	public static final String ISO_SOL_DFLT_VAL = "4444";
	public static final String ISO_BANK_CODE = "IBKL";
	public static final String ISO_CUST_ID = "upi@idbi";
	public static final String ISO_ACK_INST_ID = "0";
	public static final int ISO_REQUEST_TIMEOUT = 20000;
	public static final String ISO_TIMEOUT_ERR_CODE= "911";
}
