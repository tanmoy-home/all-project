package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CERTIFICATES database table.
 * 
 */
@Embeddable
public class CertificatePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="CERT_TYPE")
	private String certType;

	public CertificatePK() {
	}
	public String getOrgId() {
		return this.orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCertType() {
		return this.certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CertificatePK)) {
			return false;
		}
		CertificatePK castOther = (CertificatePK)other;
		return 
			this.orgId.equals(castOther.orgId)
			&& this.certType.equals(castOther.certType);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orgId.hashCode();
		hash = hash * prime + this.certType.hashCode();
		
		return hash;
	}
}