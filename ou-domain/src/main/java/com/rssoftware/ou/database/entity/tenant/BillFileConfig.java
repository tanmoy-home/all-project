package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "BILL_FILE_CONFIG")
@NamedQuery(name = "BillFileConfig.findAll", query = "SELECT b FROM BillFileConfig b")
public class BillFileConfig implements Serializable{

	/**
	 *  
	 **/
	private static final long serialVersionUID = 9014245130212859187L;
	
	@Id
	@Column(name = "BILLER_ID")
	private String blrId;
	
	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "DATE_FORMAT")
	private String dateFormat;
	
	@Column(name = "ROOT_ELEMENT")
	private String rootElement;
	
	@Column(name = "DELIMITER")
	private String delimiter;
	
	@Column(name = "TARGET_CLASS_NAME")
	private String targetClassName;

	public String getBlrId() {
		return blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getRootElement() {
		return rootElement;
	}

	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

}
