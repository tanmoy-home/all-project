package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="CHANNEL_PARAMETERS")	
public class ChannelParameters implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="REF_ID")
	private String refId;

	@Column(name="BILLER_PAYMENT_MODE")
	private String biller_Payment_Mode;

	@Column(name="BILLER_PAYMENT_CHANNEL")
	private String biller_Payment_channel;
	
	@Column(name="IP")
	private String ip;
	
	@Column(name="OS")
	private String os;
	
	@Column(name="MAC")
	private String mac;
	
	@Column(name="APP")
	private String app;
	
	@Column(name="TERMINAL_ID")
	private String terminal_id;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="GEOCODE")
	private String geoCode;
	
	@Column(name="IFSC")
	private String ifsc;
	
	@Column(name="POSTAL_CODE")
	private String postal_code;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="ACCOUNTNO")
	private String accountNo;
	
	@Column(name="CARDNUM")
	private String cardNum;
	
	@Column(name="AUTHCODE")
	private String authcode;
	
	@Column(name="MOBILENO")
	private String modileNo;
	
	@Column(name="MMID")
	private String mmid;
	
	@Column(name="WALLETNAME")
	private String walletName;
	
	@Column(name="VPA")
	private String vpa;
	
	
	@Column(name="IMEI")
	private String imei;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getBiller_Payment_Mode() {
		return biller_Payment_Mode;
	}

	public void setBiller_Payment_Mode(String biller_Payment_Mode) {
		this.biller_Payment_Mode = biller_Payment_Mode;
	}

	public String getBiller_Payment_channel() {
		return biller_Payment_channel;
	}

	public void setBiller_Payment_channel(String biller_Payment_channel) {
		this.biller_Payment_channel = biller_Payment_channel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getModileNo() {
		return modileNo;
	}

	public void setModileNo(String modileNo) {
		this.modileNo = modileNo;
	}

	public String getMmid() {
		return mmid;
	}

	public void setMmid(String mmid) {
		this.mmid = mmid;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getVpa() {
		return vpa;
	}

	public void setVpa(String vpa) {
		this.vpa = vpa;
	}
	
	
	
	
	
	
	

}
