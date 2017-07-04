package com.rssoftware.ou.batch.to;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.TypeOfBatch;

public class BillDump {
	
	private final static Logger logger = LoggerFactory.getLogger(BillDump.class);

	
	private String billerOU;
	private String billerId;
	private String inputFile;
	private TypeOfBatch batchType;
	
	public String getBillerOU() {
		return billerOU;
	}
	public void setBillerOU(String billerOU) {
		this.billerOU = billerOU;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getInputFile() {
		return inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public TypeOfBatch getBatchType() {
		return batchType;
	}
	public void setBatchType(TypeOfBatch billPayment) {
		this.batchType = billPayment;
	}

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
		    } catch (IOException e) {
		    	logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
		       }
		
		return jsonInString;
	}	
}
