package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PG_INTEGRATION_FIELDS database table.
 * 
 */
@Entity
@Table(name="PG_INTEGRATION_FIELDS")
@NamedQuery(name="PgIntegrationField.findAll", query="SELECT p FROM PgIntegrationField p")
public class PgIntegrationField implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PgIntegrationFieldPK id;

	@Column(name="FIELD_VALUE")
	private String fieldValue;

	public PgIntegrationField() {
	}

	public PgIntegrationFieldPK getId() {
		return this.id;
	}

	public void setId(PgIntegrationFieldPK id) {
		this.id = id;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}