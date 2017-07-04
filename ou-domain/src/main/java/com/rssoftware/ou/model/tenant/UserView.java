package com.rssoftware.ou.model.tenant;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.rssoftware.ou.domain.ReferenceUser;

public class UserView {

/*	private String userId;
	
	private String username;
	private String password;
	private String resetPasswordFlag;
	private String newPassword;
	private String confirmNewPass;
	private String profile;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String userRefId;
	private ReferenceUser refUser;
	private String address;
	private String state;
	private String country;
	private String pincode;
	private String phone;
	private String pan;
	private String aadhar;*/
	
	private Long id;	
	private String username;	
	private String password;		
	private String firstName;
	private String lastName;
	private String email;	
	private String contactNumber;	
	private Boolean resetPasswordFlag;			
	private String userRefId;	
	private String address;	
	private String country;
	private String state;
	private Long pincode;
	private Long mobile;
	private String pan;
	private String aadhar;
	private String createdBy;
	private Timestamp createdOn;
	private String updatedBy;
	private Timestamp updatedOn;
	private Boolean isActive;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Boolean getResetPasswordFlag() {
		return resetPasswordFlag;
	}
	public void setResetPasswordFlag(Boolean resetPasswordFlag) {
		this.resetPasswordFlag = resetPasswordFlag;
	}
	public String getUserRefId() {
		return userRefId;
	}
	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getPincode() {
		return pincode;
	}
	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getAadhar() {
		return aadhar;
	}
	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsAccountLocked() {
		return isAccountLocked;
	}
	public void setIsAccountLocked(Boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}
	private Boolean isAccountLocked;
	

}
