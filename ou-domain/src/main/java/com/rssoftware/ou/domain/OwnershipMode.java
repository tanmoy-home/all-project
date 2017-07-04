package com.rssoftware.ou.domain;

import java.io.Serializable;

public enum OwnershipMode implements Serializable{
	
	PSU,
	Government,
	Private;
	
	public static  OwnershipMode getExpOwnershipMode(String ownershipMode) {
		if(ownershipMode != null && ownershipMode.length() >0){
			
			for (OwnershipMode pm : OwnershipMode.values()) {
				if(ownershipMode.equals(pm.name())){
					return pm;
				}
			}
		
		}
		return null;
	}	
}
