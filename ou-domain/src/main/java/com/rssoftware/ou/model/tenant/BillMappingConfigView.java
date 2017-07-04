package com.rssoftware.ou.model.tenant;

public class BillMappingConfigView {
	private int endPosition;
	private int startPosition;
	private int sequenceNo;
	private String fieldQualifier;
	private String fieldDataType;
	private String fieldFormat;
	
	
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
