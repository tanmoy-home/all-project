package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class CertificateView implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum CertType {SSL, SIGNER};
	
	private String orgId;
	private CertType certType;
	private String crtCerAlias;
	private byte[] crtCerFile;
	private String csrChallengePwd;
	private byte[] csrFile;
	private String keyAlias;
	private byte[] keyFile;
	private String keyPwd;
	private byte[] p12File;
	private byte[] pemFile;
	private String validFrom;
	private String validUpto;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public CertType getCertType() {
		return certType;
	}
	public void setCertType(CertType certType) {
		this.certType = certType;
	}
	public String getCrtCerAlias() {
		return crtCerAlias;
	}
	public void setCrtCerAlias(String crtCerAlias) {
		this.crtCerAlias = crtCerAlias;
	}
	public byte[] getCrtCerFile() {
		return crtCerFile;
	}
	public void setCrtCerFile(byte[] crtCerFile) {
		this.crtCerFile = crtCerFile;
	}
	public String getCsrChallengePwd() {
		return csrChallengePwd;
	}
	public void setCsrChallengePwd(String csrChallengePwd) {
		this.csrChallengePwd = csrChallengePwd;
	}
	public byte[] getCsrFile() {
		return csrFile;
	}
	public void setCsrFile(byte[] csrFile) {
		this.csrFile = csrFile;
	}
	public String getKeyAlias() {
		return keyAlias;
	}
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	public byte[] getKeyFile() {
		return keyFile;
	}
	public void setKeyFile(byte[] keyFile) {
		this.keyFile = keyFile;
	}
	public String getKeyPwd() {
		return keyPwd;
	}
	public void setKeyPwd(String keyPwd) {
		this.keyPwd = keyPwd;
	}
	public byte[] getP12File() {
		return p12File;
	}
	public void setP12File(byte[] p12File) {
		this.p12File = p12File;
	}
	public byte[] getPemFile() {
		return pemFile;
	}
	public void setPemFile(byte[] pemFile) {
		this.pemFile = pemFile;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidUpto() {
		return validUpto;
	}
	public void setValidUpto(String validUpto) {
		this.validUpto = validUpto;
	}



}