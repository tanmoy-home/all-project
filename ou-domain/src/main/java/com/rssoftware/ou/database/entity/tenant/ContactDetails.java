package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the AGENT_INST_DETAILS database table.
 * 
 */
@Entity
@Table(name = "CONTACT_DETAILS")
@NamedQuery(name = "ContactDetails.findAll", query = "SELECT a FROM ContactDetails a")
public class ContactDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LINKED_ENTITY_ID")
	private String linkedEntityId;

	@Column(name = "LINKED_ENTITY_TYPE")
	private String linkedEntityType;

	@Column(name = "CONTACT_TYPE")
	private String contactType;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "PHONE_NO")
	private String phoneNo;

	@Column(name = "MOBILE_NO")
	private String mobileNo;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CRTN_USER_ID")
	private String crtnUserId;

	@Column(name = "ENTITY_STATUS")
	private String entityStatus;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;

	public String getLinkedEntityId() {
		return linkedEntityId;
	}

	public void setLinkedEntityId(String linkedEntityId) {
		this.linkedEntityId = linkedEntityId;
	}

	public String getLinkedEntityType() {
		return linkedEntityType;
	}

	public void setLinkedEntityType(String linkedEntityType) {
		this.linkedEntityType = linkedEntityType;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCrtnUserId() {
		return crtnUserId;
	}

	public void setCrtnUserId(String crtnUserId) {
		this.crtnUserId = crtnUserId;
	}

	public String getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtUserId() {
		return updtUserId;
	}

	public void setUpdtUserId(String updtUserId) {
		this.updtUserId = updtUserId;
	}
}