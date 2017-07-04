package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "OTP_DETAILS_MOBILE_REG")
public class OtpDetails implements Serializable {

	private final static long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqGen")
	@SequenceGenerator(name = "SeqGen", sequenceName = "OTP_DTLS_SEQ", allocationSize = 1)
	@Column(name = "OTP_ID")
	protected Long otpId;

	@Column(name = "MOBILE_NO")
	protected String mobileNo;

	@Column(name = "OTP_SEED")
	protected String otpSeed;

	@Column(name = "OTP_TS")
	protected String otpTs;

	@Column(name = "OTP_STATUS")
	protected String otpStatus;

	@Column(name = "VALID_UPTO")
	protected java.sql.Timestamp validUpto;

	@Column(name = "CREATION_TS")
	protected java.sql.Timestamp creationTs;

	@Column(name = "CREATED_BY")
	protected String createdBy;

	@Column(name = "UPDATE_TS")
	protected java.sql.Timestamp updateTs;

	@Column(name = "UPDATED_BY")
	protected String updatedBy;
	
	@Transient
	private String otpNo;
	
	

	public String getOtpNo() {
		return otpNo;
	}

	public void setOtpNo(String otpNo) {
		this.otpNo = otpNo;
	}

	/**
	 * Gets the value of the otpId property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public Long getOtpId() {
		return otpId;
	}

	/**
	 * Sets the value of the otpId property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setOtpId(Long value) {
		this.otpId = value;
	}

	/**
	 * Gets the value of the mobileNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * Sets the value of the mobileNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMobileNo(String value) {
		this.mobileNo = value;
	}

	/**
	 * Gets the value of the otpSeed property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOtpSeed() {
		return otpSeed;
	}

	/**
	 * Sets the value of the otpSeed property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOtpSeed(String value) {
		this.otpSeed = value;
	}


	/**
	 * Gets the value of the otpTs property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOtpTs() {
		return otpTs;
	}

	/**
	 * Sets the value of the otpTs property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOtpTs(String value) {
		this.otpTs = value;
	}

	/**
	 * Gets the value of the otpStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOtpStatus() {
		return otpStatus;
	}

	/**
	 * Sets the value of the otpStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOtpStatus(String value) {
		this.otpStatus = value;
	}

	/**
	 * Gets the value of the validUpto property.
	 * 
	 */
	public java.sql.Timestamp getValidUpto() {
		return validUpto;
	}

	/**
	 * Sets the value of the validUpto property.
	 * 
	 */
	public void setValidUpto(java.sql.Timestamp value) {
		this.validUpto = value;
	}

	/**
	 * Gets the value of the creationTs property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public java.sql.Timestamp getCreationTs() {
		return creationTs;
	}

	/**
	 * Sets the value of the creationTs property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCreationTs(java.sql.Timestamp value) {
		this.creationTs = value;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updateTs
	 */
	public java.sql.Timestamp getUpdateTs() {
		return updateTs;
	}

	/**
	 * @param updateTs
	 *            the updateTs to set
	 */
	public void setUpdateTs(java.sql.Timestamp updateTs) {
		this.updateTs = updateTs;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
