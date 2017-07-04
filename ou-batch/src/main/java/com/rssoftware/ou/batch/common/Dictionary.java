package com.rssoftware.ou.batch.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.batch.field.FieldMapping;

public class Dictionary implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5085148344018590125L;
	private String targetBeanClass;
	private List<FieldMapping> fieldMappings;
	
	private FileType fileType;	
	private String billerId;
	private String delimiter;
	private String rootElement;
	private String customerParams;
	private String customerName;
	private String actualAmount;
	private String dueDate;
	private String billDate;
	private String billNumber;
	private String billPeriod;
	private String otherAmounts;
	private String additionalInfo;
	
	private final static Logger log = LoggerFactory.getLogger(Dictionary.class);
	
	public String getTargetBeanClass() {
		return targetBeanClass;
	}
	public void setTargetBeanClass(String targetBeanClass) {
		this.targetBeanClass = targetBeanClass;
	}
	public List<FieldMapping> getFieldMappings() {
		return fieldMappings;
	}
	public void setFieldMappings(List<FieldMapping> fieldMappings) {
		this.fieldMappings = fieldMappings;
	}


	private Map<String,String[]> fieldDefinition;
	
	public Map<String, String[]> getFieldDefinition() {
		return fieldDefinition;
	}
	public void setFieldDefinition(Map<String, String[]> fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getRootElement() {
		return rootElement;
	}
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public FileType getFileType() {
		return fileType;
	}
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
	public String getCustomerParams() {
		return customerParams;
	}
	public void setCustomerParams(String customerParams) {
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
	public String getOtherAmounts() {
		return otherAmounts;
	}
	public void setOtherAmounts(String otherAmounts) {
		this.otherAmounts = otherAmounts;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			    log.error(e.getMessage(), e);
	            log.info("In Excp : " + e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());
		}
		
		return jsonInString;
	}
	
}
