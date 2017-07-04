package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CERTIFICATES database table.
 * 
 */
@Entity
@Table(name="CERTIFICATES")
@NamedQuery(name="Certificate.findAll", query="SELECT c FROM Certificate c")
public class Certificate implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CertificatePK id;

	@Column(name="CRT_CER_ALIAS")
	private String crtCerAlias;

	
	@Column(name="CRT_CER_FILE")
	private byte[] crtCerFile;

	@Column(name="CSR_CHALLENGE_PWD")
	private String csrChallengePwd;

	
	@Column(name="CSR_FILE")
	private byte[] csrFile;

	@Column(name="KEY_ALIAS")
	private String keyAlias;

	
	@Column(name="KEY_FILE")
	private byte[] keyFile;

	@Column(name="KEY_PWD")
	private String keyPwd;

	
	@Column(name="P12_FILE")
	private byte[] p12File;

	
	@Column(name="PEM_FILE")
	private byte[] pemFile;

	@Column(name="VALID_FROM")
	private String validFrom;

	@Column(name="VALID_UPTO")
	private String validUpto;

	public Certificate() {
	}

	public CertificatePK getId() {
		return this.id;
	}

	public void setId(CertificatePK id) {
		this.id = id;
	}

	public String getCrtCerAlias() {
		return this.crtCerAlias;
	}

	public void setCrtCerAlias(String crtCerAlias) {
		this.crtCerAlias = crtCerAlias;
	}

	public byte[] getCrtCerFile() {
		return this.crtCerFile;
	}

	public void setCrtCerFile(byte[] crtCerFile) {
		this.crtCerFile = crtCerFile;
	}

	public String getCsrChallengePwd() {
		return this.csrChallengePwd;
	}

	public void setCsrChallengePwd(String csrChallengePwd) {
		this.csrChallengePwd = csrChallengePwd;
	}

	public byte[] getCsrFile() {
		return this.csrFile;
	}

	public void setCsrFile(byte[] csrFile) {
		this.csrFile = csrFile;
	}

	public String getKeyAlias() {
		return this.keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public byte[] getKeyFile() {
		return this.keyFile;
	}

	public void setKeyFile(byte[] keyFile) {
		this.keyFile = keyFile;
	}

	public String getKeyPwd() {
		return this.keyPwd;
	}

	public void setKeyPwd(String keyPwd) {
		this.keyPwd = keyPwd;
	}

	public byte[] getP12File() {
		return this.p12File;
	}

	public void setP12File(byte[] p12File) {
		this.p12File = p12File;
	}

	public byte[] getPemFile() {
		return this.pemFile;
	}

	public void setPemFile(byte[] pemFile) {
		this.pemFile = pemFile;
	}

	public String getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidUpto() {
		return this.validUpto;
	}

	public void setValidUpto(String validUpto) {
		this.validUpto = validUpto;
	}

}