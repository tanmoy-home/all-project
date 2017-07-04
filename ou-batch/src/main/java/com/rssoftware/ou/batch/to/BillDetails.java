package com.rssoftware.ou.batch.to;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.utils.LogUtils;

@XmlRootElement(name = "BillDetails")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)

public class BillDetails implements Serializable{
	
	private final static Logger logger = LoggerFactory.getLogger(BillDetails.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2106312078100122851L;
	
	private boolean badData;
	private String billerId;
	private String customerParam1;
	private String customerParam2;
	private String customerParam3;
	private String customerParam4;
	private String customerParam5;
	private String customerName;
	private BigDecimal actualAmount;
	private String dueDate;
	private String billDate;
	private String billNumber;
	private String billPeriod;
	private Map additionalAmounts = new HashMap();
	private Map additionalInfo = new HashMap();
	
	private String concatCP;
	private String concatAA;
	private String concatAI;
	
	
	
	public boolean isBadData() {
		return badData;
	}


	public void setBadData(boolean badData) {
		this.badData = badData;
	}


	public String getBillerId() {
		return billerId;
	}


	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}


	public String getCustomerParam1() {
		return customerParam1;
	}


	public void setCustomerParam1(String customerParam1) {
		this.customerParam1 = customerParam1;
	}


	public String getCustomerParam2() {
		return customerParam2;
	}


	public void setCustomerParam2(String customerParam2) {
		this.customerParam2 = customerParam2;
	}


	public String getCustomerParam3() {
		return customerParam3;
	}


	public void setCustomerParam3(String customerParam3) {
		this.customerParam3 = customerParam3;
	}


	public String getCustomerParam4() {
		return customerParam4;
	}


	public void setCustomerParam4(String customerParam4) {
		this.customerParam4 = customerParam4;
	}


	public String getCustomerParam5() {
		return customerParam5;
	}


	public void setCustomerParam5(String customerParam5) {
		this.customerParam5 = customerParam5;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public BigDecimal getActualAmount() {
		return actualAmount;
	}


	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}


	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


	public String getBillDate() {
		return billDate;
	}


	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}


	public String getBillNumber() {
		return billNumber;
	}


	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}


	public String getBillPeriod() {
		return billPeriod;
	}


	public void setBillPeriod(String billPeriod) {
		this.billPeriod = billPeriod;
	}


	public Map getAdditionalAmounts() {
		return additionalAmounts;
	}


	public void setAdditionalAmounts(Map additionalAmounts) {
		this.additionalAmounts = additionalAmounts;
	}


	public Map getAdditionalInfo() {
		return additionalInfo;
	}
	


	public void setAdditionalInfo(Map additionalInfo) {
		this.additionalInfo = additionalInfo;
	}


	public String getConcatCP() {
		return concatCP;
	}


	public void setConcatCP(String concatCP) {
		this.concatCP = concatCP;
	}


	public String getConcatAA() {
		return concatAA;
	}


	public void setConcatAA(String concatAA) {
		this.concatAA = concatAA;
	}


	public String getConcatAI() {
		return concatAI;
	}


	public void setConcatAI(String concatAI) {
		this.concatAI = concatAI;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonGenerationException e) {
			 logger.error( e.getMessage(), e);
	         logger.info("In Excp : " + e.getMessage());			
		   } catch (JsonMappingException e) {
			  logger.error( e.getMessage(), e);
	          logger.info("In Excp : " + e.getMessage());		
	         } 
		      catch (IOException e) {
	        	 logger.error( e.getMessage(), e);
		         logger.info("In Excp : " + e.getMessage());			
			
		      }
		
		return jsonInString;
	}
}
