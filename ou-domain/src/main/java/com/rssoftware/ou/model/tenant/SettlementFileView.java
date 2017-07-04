package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

import com.rssoftware.ou.database.entity.tenant.SettlementFile;

public class SettlementFileView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6518879133260604139L;
	
	private byte[] file;
	private SettlementFile.FileType fileType;
	private String tenantId;
	private String fileName;
	
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public SettlementFile.FileType getFileType() {
		return fileType;
	}
	public void setFileType(SettlementFile.FileType fileType) {
		this.fileType = fileType;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}