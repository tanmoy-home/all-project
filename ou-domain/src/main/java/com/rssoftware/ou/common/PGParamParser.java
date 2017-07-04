package com.rssoftware.ou.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PGParamParser {
	private Map<String, String> pgParamMap = new HashMap<>();
	
	public PGParamParser(List<PGParam> pgParamList) {
		if (pgParamList != null){
			for (PGParam param:pgParamList){
				if (param != null && param.getParamName() != null){
					pgParamMap.put(param.getParamName(), param.getParamValue());
				}
			}
		}
	}
	
	public String get(String paramName){
		if (paramName == null){
			return null;
		}
		return pgParamMap.get(paramName);
	}
}
