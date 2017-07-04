package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



@Entity
@Table(name = "BILL_MAPPING_CONFIG")
@NamedQuery(name = "BillMappingConfig.findAll", query = "SELECT b FROM BillMappingConfig b")
public class BillMappingConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4601282556742753849L;

	@Id
	@Column(name = "BILLER_ID")
	private String blrId;
	
	@Column(name = "END_POSITION")
	private int endPosition;
	
	@Column(name="START_POSITION")
	private int startPosition;
	
	@Column(name="SEQUENCE")
	private int sequenceNo;
	
	@Column(name="FIELD_QUALIFIER")
	private String fieldQualifier;
	
	@Column(name="FIELD_DATA_TYPE")
	private String fieldDataType;
	
	@Column(name="FIELD_FORMAT")
	private String fieldFormat;

	public String getBlrId() {
		return blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getFieldQualifier() {
		return fieldQualifier;
	}

	public void setFieldQualifier(String fieldQualifier) {
		this.fieldQualifier = fieldQualifier;
	}

	public String getFieldDataType() {
		return fieldDataType;
	}

	public void setFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
	}

	public String getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}
}
