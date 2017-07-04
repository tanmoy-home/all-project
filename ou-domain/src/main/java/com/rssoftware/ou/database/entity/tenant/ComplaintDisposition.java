package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="COMPLAINT_DISPOSITION")
@NamedQuery(name="ComplaintDisposition.findAll", query="SELECT dsp FROM ComplaintDisposition dsp")
public class ComplaintDisposition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1990351495985279321L;
	
	@Id
	@Column(name="DISPOSITION_ID")
	private String dispositionId;

	@Column(name="DISPOSITION_NAME")
	private String dispositionName;

	public String getDispositionId() {
		return dispositionId;
	}

	public void setDispositionId(String dispositionId) {
		this.dispositionId = dispositionId;
	}

	public String getDispositionName() {
		return dispositionName;
	}

	public void setDispositionName(String dispositionName) {
		this.dispositionName = dispositionName;
	}

	@Override
	public String toString() {
		return "ComplaintDisposition [dispositionId=" + dispositionId
				+ ", dispositionName=" + dispositionName + "]";
	}
	

}
