package com.rssoftware.ou.model.tenant;

//import java.io.IOException;
import java.io.Serializable;
import java.util.List;

//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;

public class BillView implements Comparable<BillView>, Serializable {

	private static final long serialVersionUID = -1348099929438906876L;

	private String billerId;
	private List<String> customerParams;
	private String customerName;
	private String actualAmount;
	private String dueDate;
	private String billDate;
	private String billNumber;
	private String billPeriod;
	private List<String> otherAmounts;
	private List<String> additionalInfo;

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public List<String> getCustomerParams() {
		return customerParams;
	}

	public void setCustomerParams(List<String> customerParams) {
		this.customerParams = customerParams;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
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

	public List<String> getOtherAmounts() {
		return otherAmounts;
	}

	public void setOtherAmounts(List<String> otherAmounts) {
		this.otherAmounts = otherAmounts;
	}

	public List<String> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(List<String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	
	@Override
	public int compareTo(BillView o) {
		return (billerId!=null?billerId:"").compareTo(o.billerId!=null?o.billerId:"");
	}

}
