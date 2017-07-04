package com.rssoftware.ou.batch.writer;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.jxpath.JXPathContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.item.ItemWriter;

import com.rssoftware.ou.batch.common.Dictionary;
import com.rssoftware.ou.batch.common.FileType;
import com.rssoftware.ou.batch.field.FieldMapping;
import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.model.tenant.BillPaymentReportDataView;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public class BillPaymentReportWriter implements ItemWriter<BillPaymentReportDataView>{
	
	private Dictionary dictionary;
	private String outputFile;

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}



	@Override
	public void write(List<? extends BillPaymentReportDataView> items) throws Exception {
//		List<FieldMapping>  fieldMappings = dictionary.getFieldMappings();
		FileWriter fw = new FileWriter(outputFile,true);
		
		if (items != null) {

			StringBuffer stringBuffer = new StringBuffer();
			
			// Adding header
			fw.write("Transaction Reference Id,Transaction Type,Agent Id,Bill Date,Bill Amount,Transaction Date,Customer OU Id,"
					+ "Biller OU Id,Biller Category,Split Payment Mode,Customer Mobile Number,Biller Fee,Biller Fee Tax,Reversal,Decline,Split Pay");
			fw.write(System.lineSeparator());
			
			for (BillPaymentReportDataView data : items) {
				
				List<String> list = new ArrayList<String>();
				TransactionDataView txnDataView = data.getTransactionDataView();
				RawDataView rawDataView = data.getRawDataView();
				
				//t.TXN_REF_ID,t.TXN_TYPE,t.AGENT_ID,t.BILL_DATE,t.BILL_AMOUNT
				list.add(txnDataView.getTxnRefId());
				list.add(txnDataView.getTransactionType());
				list.add(txnDataView.getAgentID());
				list.add(txnDataView.getBillDate());
				list.add(String.valueOf(txnDataView.getBillAmount()));
				
				// r.TXN_DATE,r.CUSTOMER_OU_ID,r.BILLER_OU_ID,r.BILLER_CATEGORY,r.SPLIT_PAYMENT_MODE,r.CUSTOMER_MOBILE_NUMBER,r.BILLER_FEE,r.BILLER_FEE_TAX,r.REVERSAL,
				// r.DECLINE,r.SPLIT_PAY
				list.add(String.valueOf(rawDataView.getTxnDate()));
				list.add(rawDataView.getCustomerOuId());
				list.add(rawDataView.getBillerOuId());
				list.add(rawDataView.getBillerCategory());
				list.add(rawDataView.getSplitPaymentMode());
				list.add(rawDataView.getCustomerMobileNumber());
				list.add(String.valueOf(rawDataView.getBillerFee()));
				list.add(String.valueOf(rawDataView.getBillerFeeTax()));
				list.add(String.valueOf(rawDataView.getReversal()));
				list.add(String.valueOf(rawDataView.getDecline()));
				list.add(String.valueOf(rawDataView.getSplitPay()));
				
				if (stringBuffer.length() > 0) {
					stringBuffer.delete(0, stringBuffer.length());
				}
				
//				stringBuffer.append(listToCSV(list, ','));
				fw.write(listToCSV(list, ','));
				fw.write(System.lineSeparator());
			}
			
//			fw.write(stringBuffer.toString());
//			fw.write(System.lineSeparator());
			
		}
		fw.close();
		

	}
	
	private String listToCSV(List<String> listOfStrings, char separator) {
	    StringBuilder sb = new StringBuilder();

	    // all but last
	    for(int i = 0; i < listOfStrings.size() - 1 ; i++) {
	        sb.append(listOfStrings.get(i));
	        sb.append(separator);
	    }

	    // last string, no separator
	    if(listOfStrings.size() > 0){
	        sb.append(listOfStrings.get(listOfStrings.size()-1));
	    }

	    return sb.toString();
	}	
	
}
