package com.rssoftware.ou.domain;

import java.io.Serializable;

public enum CoverageMode implements Serializable{
	
	National,
	State,
	District;
	
	public static  CoverageMode getExpCoverageMode(String coverageMode) {
		if(coverageMode != null && coverageMode.length() >0){
			
			for (CoverageMode pm : CoverageMode.values()) {
				if(coverageMode.equals(pm.name())){
					return pm;
				}
			}
		
		}
		return null;
	}	
}
