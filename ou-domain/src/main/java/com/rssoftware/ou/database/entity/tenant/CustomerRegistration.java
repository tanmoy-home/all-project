package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Bean;

@Entity
@Table(name="CUSTOMER_REGISTRATION")
public class CustomerRegistration  implements Serializable{
	
	@Id
	@Column(name="cust_id")
	private String custID;
	
	@Column(name="bbpou_id")
	private String bbpouID;
	
	@Column(name="cust_name")
	private String custName;
	
	@Column(name="cust_gender")
	private String custGender;
	
	@Column(name="cust_dob")
	private String custDob;
	
	@Column(name="cust_mob")
	private String custMobile;
	
	@Column(name="cust_email")
	private String custEmail;
	
	@Column(name="cust_addr_1")
	private String custAddrLine1;
	
	@Column(name="cust_addr_2")
	private String custAddrLine2;
	
	@Column(name="cust_addr_3")
	private String custAddrLine3;
	
	@Column(name="cust_city")
	private String custCity;
	
	@Column(name="cust_state")
	private String custState;
	
	@Column(name="cust_pin")
	private String custPin;
	
	@Column(name="cust_aadhaar")
	private String custAadhaar;
	
	@Column(name="cust_pan")
	private String custPan;
	
	@Column(name="cust_pass")
	private String custPassport;
	
	@Column(name="effctv_from")
	private String effectiveFrom;
	
	@Column(name="effctv_to")
	private String effectiveTo;
	
	
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getBbpouID() {
		return bbpouID;
	}
	public void setBbpouID(String bbpouID) {
		this.bbpouID = bbpouID;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustGender() {
		return custGender;
	}
	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}
	public String getCustDob() {
		return custDob;
	}
	public void setCustDob(String custDob) {
		this.custDob = custDob;
	}
	public String getCustMobile() {
		return custMobile;
	}
	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustAddrLine1() {
		return custAddrLine1;
	}
	public void setCustAddrLine1(String custAddrLine1) {
		this.custAddrLine1 = custAddrLine1;
	}
	public String getCustAddrLine2() {
		return custAddrLine2;
	}
	public void setCustAddrLine2(String custAddrLine2) {
		this.custAddrLine2 = custAddrLine2;
	}
	public String getCustAddrLine3() {
		return custAddrLine3;
	}
	public void setCustAddrLine3(String custAddrLine3) {
		this.custAddrLine3 = custAddrLine3;
	}
	public String getCustCity() {
		return custCity;
	}
	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}
	public String getCustState() {
		return custState;
	}
	public void setCustState(String custState) {
		this.custState = custState;
	}
	public String getCustPin() {
		return custPin;
	}
	public void setCustPin(String custPin) {
		this.custPin = custPin;
	}
	public String getCustAadhaar() {
		return custAadhaar;
	}
	public void setCustAadhaar(String custAadhaar) {
		this.custAadhaar = custAadhaar;
	}
	public String getCustPan() {
		return custPan;
	}
	public void setCustPan(String custPan) {
		this.custPan = custPan;
	}
	public String getCustPassport() {
		return custPassport;
	}
	public void setCustPassport(String custPassport) {
		this.custPassport = custPassport;
	}
	public String getEffectiveFrom() {
		return effectiveFrom;
	}
	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	public String getEffectiveTo() {
		return effectiveTo;
	}
	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	@Override
	public String toString() {
		return "CustomerRegistration [custID=" + custID + ", bbpouID=" + bbpouID + ", custName=" + custName
				+ ", custGender=" + custGender + ", custDob=" + custDob + ", custMobile=" + custMobile + ", custEmail="
				+ custEmail + ", custAddrLine1=" + custAddrLine1 + ", custAddrLine2=" + custAddrLine2
				+ ", custAddrLine3=" + custAddrLine3 + ", custCity=" + custCity + ", custState=" + custState
				+ ", custPin=" + custPin + ", custAadhaar=" + custAadhaar + ", custPan=" + custPan + ", custPassport="
				+ custPassport + ", effectiveFrom=" + effectiveFrom + ", effectiveTo=" + effectiveTo + "]";
	}

	public CustomerRegistration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerRegistration(String custID, String bbpouID, String custName, String custGender, String custDob,
			String custMobile, String custEmail, String custAddrLine1, String custAddrLine2, String custAddrLine3,
			String custCity, String custState, String custPin, String custAadhaar, String custPan, String custPassport,
			String effectiveFrom, String effectiveTo) {
		super();
		this.custID = custID;
		this.bbpouID = bbpouID;
		this.custName = custName;
		this.custGender = custGender;
		this.custDob = custDob;
		this.custMobile = custMobile;
		this.custEmail = custEmail;
		this.custAddrLine1 = custAddrLine1;
		this.custAddrLine2 = custAddrLine2;
		this.custAddrLine3 = custAddrLine3;
		this.custCity = custCity;
		this.custState = custState;
		this.custPin = custPin;
		this.custAadhaar = custAadhaar;
		this.custPan = custPan;
		this.custPassport = custPassport;
		this.effectiveFrom = effectiveFrom;
		this.effectiveTo = effectiveTo;
	}
	
	
}
