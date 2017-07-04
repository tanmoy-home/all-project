package com.rssoftware.ou.batch.field;

import java.io.Serializable;

import com.rssoftware.ou.batch.common.DataType;

public class FieldMapping implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5854892595833073773L;
	private String fieldQualifier;
	private int fieldSequence; // starts with 1
	private int startPosition; // starts with 1
	private int endPosition;
	private String fieldFormat;
	private DataType dataType;

	public String getFieldQualifier() {
		return fieldQualifier;
	}

	public void setFieldQualifier(String fieldQualifier) {
		this.fieldQualifier = fieldQualifier;
	}

	public int getFieldSequence() {
		return fieldSequence;
	}

	public void setFieldSequence(int fieldSequence) {
		this.fieldSequence = fieldSequence;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}


	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}
	
	
}
