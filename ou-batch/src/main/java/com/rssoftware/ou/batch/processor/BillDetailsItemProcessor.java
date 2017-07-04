package com.rssoftware.ou.batch.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.BillDetails;

public class BillDetailsItemProcessor implements ItemProcessor<BillDetails, BillDetails>{

	@Override
	public BillDetails process(BillDetails item) throws Exception {
		if(item == null)
			return null;
		
		if(item.isBadData())
		{
			System.out.println("Filter out bad data ................................");
			return null;
		}
		
		System.out.println(item.getConcatAI());
		
		item.setAdditionalAmounts((JsonToMap(item.getConcatAA())));
		item.setAdditionalInfo(JsonToMap(item.getConcatAI()));
		
		//update jsonString
		String concatAA = "["+item.getConcatAA().replace(",", "},{")+"]"; 
		String concatAI = "["+item.getConcatAI().replace(",", "},{")+"]"; 
		
		item.setConcatAA(concatAA);
		item.setConcatAI(concatAI);
		
		return item;
	}

	@SuppressWarnings("rawtypes")
	private Map JsonToMap(String jsonString) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		// convert JSON string to Map
		//String jsonString2 = "["+jsonString.replace(",", "},{")+"]";
		map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>(){});
		return map;
	}
}
