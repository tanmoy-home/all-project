package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "SMS_DETAILS")
@NamedQuery(name = "SMSDetails.findAll", query = "SELECT a FROM SMSDetails a")
public class SMSDetails implements Serializable {
	private static final long serialVersionUID = 1L;

		
	@Id
	@Column(name = "RECORD_ID")
	private long recordId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "SMS_MESSAGE")
	private String smsMessage;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "MOBILE_NO")
	private String mobileNo;

	@Column(name = "CREATION_DATE")
	private Timestamp creationDate;

	@Column(name = "DELIVERY_DATE")
	private Timestamp deliveryDate;

	@Column(name = "STATUS")
	private char status;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_BY")
	private String updtBy;

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtBy() {
		return updtBy;
	}

	public void setUpdtBy(String updtBy) {
		this.updtBy = updtBy;
	}

}
