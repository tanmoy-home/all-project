package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"ouId",
		"transactions"
})
@XmlRootElement(name = "RAWFile")
public class RAWFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1896632033489118005L;
	private List<RAWFileTransaction> transactions;
	@XmlAttribute(name = "ouId")
	private String ouId;
	public String getOuId() {
		return ouId;
	}
	public void setOuId(String ouId) {
		this.ouId = ouId;
	}
	public List<RAWFileTransaction> getTransactions() {
		if (transactions == null){
			transactions = new ArrayList<RAWFileTransaction>();
		}
		return transactions;
	}
}