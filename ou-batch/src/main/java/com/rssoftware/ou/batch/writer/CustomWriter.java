package com.rssoftware.ou.batch.writer;

import java.io.FileWriter;
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

public class CustomWriter implements ItemWriter<BillDetails>{
	
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
	public void write(List<? extends BillDetails> items) throws Exception {
		List<FieldMapping>  fieldMappings = dictionary.getFieldMappings();
		FileWriter fw = new FileWriter(outputFile,true);
		if(items!=null){
			for(BillDetails data: items)
			{
				
				JXPathContext context = JXPathContext.newContext(data);

				Map map = new TreeMap();
				for(FieldMapping fieldMapping: fieldMappings){	
					String[] fieldValueWithPosition = new String[3]; 
					fieldValueWithPosition[0] = context.getValue(fieldMapping.getFieldQualifier()).toString();
					fieldValueWithPosition[1] = fieldMapping.getStartPosition()+"";
					fieldValueWithPosition[2] = fieldMapping.getEndPosition()+"";
					int fieldSequence = fieldMapping.getFieldSequence();
					map.put(fieldSequence,fieldValueWithPosition);
				}
				
				StringBuffer stringBuffer = new StringBuffer();
				for(int i=1;i<=map.size();i++)
				{
					String fieldValue = ((String[])map.get(Integer.valueOf(i)))[0];
					
					if(dictionary.getFileType().equals(FileType.FIXED_WIDTH))
					{
						int startPosition = Integer.parseInt(((String[])map.get(Integer.valueOf(i)))[1]);
						int endPosition = Integer.parseInt(((String[])map.get(Integer.valueOf(i)))[2]);
						String valueWithPos = afterPositioning(fieldValue,startPosition,endPosition);
						stringBuffer.append(valueWithPos);
					}
					else
					{
						stringBuffer.append(fieldValue);
						if(i<map.size())
							stringBuffer.append(dictionary.getDelimiter());
					}
				}
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
				
				fw.write(stringBuffer.toString());
				fw.write(System.lineSeparator());

			}
			
			
		}
		fw.close();
		

	}

	private String afterPositioning(String fieldValue, int startPosition, int endPosition) {
		StringBuffer stringBuffer = new StringBuffer();
		int length = endPosition-startPosition+1;
		if(fieldValue.length()<=length)
		{
			for(int i=0;i<=length-fieldValue.length();i++) stringBuffer.append(".");
			stringBuffer.append(fieldValue);	
		}
		
		return stringBuffer.toString();
	}
	
	
}
