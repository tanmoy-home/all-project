package com.rssoftware.ou.model.tenant;

public class ContactDetailsView {
	public enum LinkedEntityType {
		OU, BILLER, AGENT, AGENT_INSTITUTE
	};

	public enum ContactType {
		ADMIN_1, ADMIN_2, ADMIN_3, TECH_1, TECH_2, TECH_3, CMS_1, CMS_2, CMS_3, DMS_1, DMS_2, DMS_3, DEFAULT
	};

	private String linkedEntityID;
	private LinkedEntityType linkedEntityType;
	private ContactType contactType;
	private String firstName;
	private String lastName;
	private String designation;
	private String department;
	private String phoneNo;
	private String mobileNo;
	private String emailID;

	public String getLinkedEntityID() {
		return linkedEntityID;
	}

	public void setLinkedEntityID(String linkedEntityID) {
		this.linkedEntityID = linkedEntityID;
	}

	public LinkedEntityType getLinkedEntityType() {
		return linkedEntityType;
	}

	public void setLinkedEntityType(LinkedEntityType linkedEntityType) {
		this.linkedEntityType = linkedEntityType;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
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

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	@Override
	public String toString() {
		return "ContactDetails [linkedEntityID=" + linkedEntityID
				+ ", linkedEntityType=" + linkedEntityType + ", contactType="
				+ contactType + ", firstName=" + firstName + ", lastName="
				+ lastName + ", designation=" + designation + ", department="
				+ department + ", phoneNo=" + phoneNo + ", mobileNo="
				+ mobileNo + ", emailID=" + emailID + "]";
	}
}