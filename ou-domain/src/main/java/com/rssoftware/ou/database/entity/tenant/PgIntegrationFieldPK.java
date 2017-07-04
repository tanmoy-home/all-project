package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PG_INTEGRATION_FIELDS database table.
 * 
 */
@Embeddable
public class PgIntegrationFieldPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FIELD_NAME")
	private String fieldName;

	@Column(name="FIELD_TYPE")
	private String fieldType;

	public PgIntegrationFieldPK() {
	}
	public String getFieldName() {
		return this.fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return this.fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PgIntegrationFieldPK)) {
			return false;
		}
		PgIntegrationFieldPK castOther = (PgIntegrationFieldPK)other;
		return 
			this.fieldName.equals(castOther.fieldName)
			&& this.fieldType.equals(castOther.fieldType);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fieldName.hashCode();
		hash = hash * prime + this.fieldType.hashCode();
		
		return hash;
	}
}