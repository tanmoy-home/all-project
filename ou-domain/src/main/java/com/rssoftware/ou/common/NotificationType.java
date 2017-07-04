package com.rssoftware.ou.common;

public enum NotificationType {
	SMS("sms"), EMAIL("e-mail");
	
	private String expandedForm;
	private NotificationType(String expandedForm) {
		this.expandedForm = expandedForm;
	}
	
	public String getExpandedForm() {
		return expandedForm;
	}
	
	public static NotificationType getFromName(String name){
		if (name == null){
			return null;
		}
		
		return NotificationType.valueOf(name);
	}

	public static NotificationType getFromExpandedForm(String expandedForm){
		if (expandedForm == null){
			return null;
		}
		
		for (NotificationType nType:NotificationType.values()){
			if (nType.getExpandedForm().equals(expandedForm)){
				return nType;
			}
		}
		
		return null;
	}
}
