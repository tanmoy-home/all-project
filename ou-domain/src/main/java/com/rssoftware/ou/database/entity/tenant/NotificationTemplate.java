package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TEMPLATE database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_TEMPLATE")
//@NamedQuery(name="template.findAll", query="SELECT a FROM template a")
public class NotificationTemplate implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TEMP_ID")
	private String tempID;

	@Id
	@Column(name="MESSAGE")
	private String message;

	@Id
	@Column(name="EVENT_TYPE")
	private String eventType ;

	@Id
	@Column(name="COMMUNICATION_TYPE")
	private String communicationType;


	public String getTempID() {
		return tempID;
	}
	public void setTempID(String tempID) {
		this.tempID = tempID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}