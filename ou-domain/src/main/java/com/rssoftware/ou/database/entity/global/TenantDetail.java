package com.rssoftware.ou.database.entity.global;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TENANT_DETAILS database table.
 * 
 */
@Entity
@Table(name="TENANT_DETAILS")
@NamedQuery(name="TenantDetail.findAll", query="SELECT t FROM TenantDetail t")
public class TenantDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TENANT_ID")
	private String tenantId;

	private String address;

	@Column(name="CONTACT_PERSON_NAME")
	private String contactPersonName;

	@Column(name="CONTACT_PHONE_NO")
	private String contactPhoneNo;

	@Column(name="OU_NAME")
	private String ouName;

	public TenantDetail() {
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPersonName() {
		return this.contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPhoneNo() {
		return this.contactPhoneNo;
	}

	public void setContactPhoneNo(String contactPhoneNo) {
		this.contactPhoneNo = contactPhoneNo;
	}

	public String getOuName() {
		return this.ouName;
	}

	public void setOuName(String ouName) {
		this.ouName = ouName;
	}

}