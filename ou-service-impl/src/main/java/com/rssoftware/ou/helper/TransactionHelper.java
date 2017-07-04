package com.rssoftware.ou.helper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.util.List;
import java.util.TimeZone;

import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

//import com.rssoftware.upiint.common.CommonConstant;
//import com.rssoftware.upiint.model.Transaction;
//import com.rssoftware.upiint.model.TransactionChannel;
//import com.rssoftware.upiint.utils.ConverterUtils;

public abstract class TransactionHelper {
//	public static String getOriginatingChannel(Transaction transaction){
//		DBTransaction dbTransaction = transaction.getDbTransaction();
//		if (dbTransaction == null) return null;
//		
//		DBTransactionPayerType dbPayer = dbTransaction.getPayer();
//		DBTransactionPayeeType dbPayee = dbTransaction.getPayees().getPayees().get(0);
//		
//		PayerType payer = ConverterUtils.convert(dbPayer, true);
//		PayeeType payee = ConverterUtils.convert(dbPayee);
//		TransactionChannel originatingChannel = null;
//		
//		if (PayConstant.PAY.equals(dbTransaction.getTxn().getType()) && payer!=null){
//			if (payer.getDevice()!=null && payer.getDevice().getTags()!=null){
//				String payerdeviceTagType=null;
//				List<DeviceType.Tag> tags = payer.getDevice().getTags();
//				for (DeviceType.Tag currentTag : tags) {
//					if (DeviceTagNameType.TYPE.equals(currentTag.getName()))
//						payerdeviceTagType = currentTag.getValue();
//				}
//				if (CommonConstant.DEVICE_TAG_TYPE_MOBILE.equalsIgnoreCase(payerdeviceTagType)){
//					originatingChannel = TransactionChannel.MOBILE;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_INTERNET.equalsIgnoreCase(payerdeviceTagType)){
//					originatingChannel = TransactionChannel.INTERNET;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_MICRO_ATM.equalsIgnoreCase(payerdeviceTagType)){
//					originatingChannel = TransactionChannel.ATM;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_MICRO_ATM.equalsIgnoreCase(payerdeviceTagType)){
//					originatingChannel = TransactionChannel.MICROATM;
//				}
//			}
//		}else if (PayConstant.COLLECT.equals(dbTransaction.getTxn().getType()) && payee!=null){
//			if (payee.getDevice()!=null && payee.getDevice().getTags()!=null){
//				String payeedeviceTagType=null;
//				List<DeviceType.Tag> tags = payee.getDevice().getTags();
//				for (DeviceType.Tag currentTag : tags) {
//					if (DeviceTagNameType.TYPE.equals(currentTag.getName()))
//						payeedeviceTagType = currentTag.getValue();
//				}
//				if (CommonConstant.DEVICE_TAG_TYPE_MOBILE.equalsIgnoreCase(payeedeviceTagType)){
//					originatingChannel = TransactionChannel.MOBILE;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_INTERNET.equalsIgnoreCase(payeedeviceTagType)){
//					originatingChannel = TransactionChannel.INTERNET;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_ATM.equalsIgnoreCase(payeedeviceTagType)){
//					originatingChannel = TransactionChannel.ATM;
//				}else if (CommonConstant.DEVICE_TAG_TYPE_MICRO_ATM.equalsIgnoreCase(payeedeviceTagType)){
//					originatingChannel = TransactionChannel.MICROATM;
//				}
//			}
//		}
//		if (originatingChannel!=null)
//			return originatingChannel.toString();
//		else 
//			return null;
//	}
	
	public static String getDateTime(Calendar cal, TimeZone tz, String dateFormat) throws IOException{
		Date now = cal.getTime();
		
		//System.out.println("Date " + now);
	    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
	    df.setTimeZone(tz);

	    try {
	        String date = df.format(now);
		    return date;
	    } catch (Exception e) {
	        throw new IOException("Failed to parse date: ");
	    }
	}
	
	
	public static String getRrn(Calendar cal, String stan) throws IOException{
		String y = getDateTime(cal, TimeZone.getDefault(), "yyyy").substring(3);
		String dddHh = getDateTime(cal, TimeZone.getDefault(), "DDDHH");
		return y+dddHh+stan;
	}

}
