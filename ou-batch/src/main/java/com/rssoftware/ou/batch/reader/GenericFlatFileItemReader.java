package com.rssoftware.ou.batch.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.rssoftware.ou.batch.common.Dictionary;
import com.rssoftware.ou.batch.common.FileType;
import com.rssoftware.ou.batch.field.FieldMapping;
import com.rssoftware.ou.batch.to.BillDetails;

public class GenericFlatFileItemReader implements ItemReader<BillDetails>{
	
	private Dictionary dictionary;
	
	private String inputFile;
	
	private String billerId;
	
	private String validationReport;
	
	private BufferedReader fileReader;
	
	private final static Logger log = LoggerFactory.getLogger(GenericFlatFileItemReader.class);

	
	@Override
	public BillDetails read() throws Exception {
		BillDetails billDetails = null;
		String line =  null;
		FileWriter fw = new FileWriter(validationReport,true);
		try{
			
			
			if (dictionary.getFileType() == FileType.DELIMITED || dictionary.getFileType() == FileType.FIXED_WIDTH){
					if (fileReader == null){
						fileReader = new BufferedReader(new FileReader(inputFile));
					}
		
					line = fileReader.readLine();
					if (line == null || "".equals(line.trim())){
						fileReader.close();
					}
					else {
						
						Map<Integer, String> valueMap = new HashMap<>(dictionary.getFieldMappings().size());
						
						
							if (dictionary.getFileType() == FileType.DELIMITED){
								String[] tokens = line.split(dictionary.getDelimiter());
								initializeValueMap(valueMap, tokens);
							}
							else if (dictionary.getFileType() == FileType.FIXED_WIDTH){
								initializeValueMap(valueMap, line);
							}
						
							billDetails = mapToObject(valueMap);
						
						
					}
				}
			}catch(Exception e){
				
				fw.write(line);
				fw.write(System.lineSeparator());
				fw.write(e.getMessage());
				fw.write(System.lineSeparator());
				fw.write(System.lineSeparator());

				billDetails = new BillDetails();
				billDetails.setBadData(true);
			}

			fw.close();
			return billDetails;
	}

	private BillDetails mapToObject(Map<Integer, String> valueMap) throws IOException{
		/*Class<?> clazz = Class.forName(dictionary.getTargetBeanClass());
		Object obj = clazz.getConstructor().newInstance();
		JXPathContext context = JXPathContext.newContext(obj);*/
		BillDetails billDetails = new BillDetails();
		Map<String,String> errorMap = new HashMap<String,String>();
			JXPathContext context = JXPathContext.newContext(billDetails);
			
			int seq = 0;
			for (FieldMapping fieldMapping:dictionary.getFieldMappings()){
				String value = valueMap.get(seq);
				
				if (value != null && !"".equals(value.trim())){
					value = value.trim();
					
					Object fieldValue = null;
					
				try{
					switch (fieldMapping.getDataType()) {
					case STRING:
						fieldValue = value;
						break;
					case BIGDECIMAL:
						if (fieldMapping.getFieldFormat() != null && isInteger(fieldMapping.getFieldFormat().trim()))
							fieldValue = new BigDecimal(value).movePointLeft(Integer.parseInt(fieldMapping.getFieldFormat()));
						else
							fieldValue = new BigDecimal(value);
						break;
					case BIGINT:
						fieldValue = new BigInteger(value);
						break;
					case DATE:
						SimpleDateFormat sdf = new SimpleDateFormat();
						if (fieldMapping.getFieldFormat() != null) 
							sdf.applyPattern(fieldMapping.getFieldFormat()); 
						fieldValue = sdf.parse(value.trim());
						break;
					case INT:
						fieldValue = Integer.parseInt(value);
						break;
					case LONG:
						fieldValue = Long.parseLong(value);
						break;
					default:
						fieldValue = value;
						break;
					}
				}catch(Exception e){
					errorMap.put(value, e.getClass().getName());
				}
					context.setValue(fieldMapping.getFieldQualifier(), fieldValue);
				}
				seq++;
			}
			
			if(errorMap.size()>0)
				throw new IOException(mapToJson(errorMap));
			
				ObjectMapper mapper = new ObjectMapper();
				billDetails.setConcatAA(mapper.writeValueAsString(billDetails.getAdditionalAmounts()));
				billDetails.setConcatAI(mapper.writeValueAsString(billDetails.getAdditionalInfo()));			
				billDetails.setBillerId(this.billerId.replaceAll("\'", ""));
				billDetails.setBadData(false);
				
		return billDetails;
	}
	
	
	private String mapToJson(Map<String,String> errorMap)
	{
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		// convert map to JSON string
		try {
			json = mapper.writeValueAsString(errorMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());
		}
		return json;
	}
	
	// to be used in case of delimited
	private void initializeValueMap(Map<Integer, String> valueMap, String[] tokens){
		int i = 0; 
		for (FieldMapping fieldMapping:dictionary.getFieldMappings()){
			if (tokens.length >= fieldMapping.getFieldSequence()){
				valueMap.put(i, tokens[fieldMapping.getFieldSequence()-1]);
			}
			i++;
		}
		
	}
	
	// to be used in case of fixed width
	private void initializeValueMap(Map<Integer, String> valueMap, String line){
		int i = 0; 
		for (FieldMapping fieldMapping:dictionary.getFieldMappings()){
			if (line.length() >= fieldMapping.getStartPosition()){
				int start = fieldMapping.getStartPosition() - 1;
				int end = line.length();
				
				if (line.length() >= fieldMapping.getEndPosition()){
					end = fieldMapping.getEndPosition();
				}
				
				valueMap.put(i, line.substring(start, end));
			}
			i++;
		}
	}
	
	
	 private static boolean isInteger(String s) {
	     System.out.println("Decimal Place = "+s); 
		 boolean isValidInteger = false;
	      try
	      {
	         Integer.parseInt(s);
	 
	         // s is a valid integer
	 
	         isValidInteger = true;
	      }
	      catch (NumberFormatException ex)
	      {
	         // s is not an integer
	      }
	 
	      return isValidInteger;
	   }
	
	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getValidationReport() {
		return validationReport;
	}

	public void setValidationReport(String validationReport) {
		this.validationReport = validationReport;
	}
	
}
