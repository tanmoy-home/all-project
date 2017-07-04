package com.rssoftware.ou.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonInclude;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WeeklyReportReq implements Serializable {
	
	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String selectedDate;

	private static final long serialVersionUID = -3731979045008402895L;

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

}