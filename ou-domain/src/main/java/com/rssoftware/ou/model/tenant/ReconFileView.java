package com.rssoftware.ou.model.tenant;

import java.io.File;
import java.sql.Timestamp;

public class ReconFileView {

	private File file;
	private Timestamp date;
	private String fileName;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}