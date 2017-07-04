package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="SETTLEMENT_FILE")
@NamedQuery(name="SettlementFile.findAll", query="SELECT f FROM SettlementFile f")
public class SettlementFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5604406496154512683L;
	public enum FileType {
		MNSB, FINRAW, PDF_REPORT, CSV_REPORT, XLS_REPORT;
	}
	
	@EmbeddedId
	private SettlementFilePK settlementFilePK;

	@Column(name="MNSB_FILE_NAME")
	private String  mnsbFilename ;
	
		
	@Column(name="MNSB_FILE")
	private byte[]  mnsbFile;

	@Column(name="PDF_FILE_NAME")
	private String pdfFilename;
	
	
	@Column(name="PDF_FILE")
	private byte[]  pdfFile;

	@Column(name="CSV_FILE_NAME")
	private String  csvFilename;
	
	
	@Column(name="CSV_FILE")
	private byte[]  csvFile;
	
	@Column(name="XLS_FILE_NAME")
	private String  xlsFilename;

	
	@Column(name="XLS_FILE")
	private byte[]  xlsFile;
	
	@Column(name="TXT_FILE_NAME")
	private String  txtFilename;

	
	@Column(name="TXT_FILE")
	private byte[]  txtFile;

	public SettlementFile() {
	}

	public SettlementFilePK getSettlementFilePK() {
		return settlementFilePK;
	}

	public void setSettlementFilePK(SettlementFilePK settlementFilePK) {
		this.settlementFilePK = settlementFilePK;
	}

	public String getMnsbFilename() {
		return mnsbFilename;
	}

	public void setMnsbFilename(String mnsbFilename) {
		this.mnsbFilename = mnsbFilename;
	}

	public byte[] getMnsbFile() {
		return mnsbFile;
	}

	public void setMnsbFile(byte[] mnsbFile) {
		this.mnsbFile = mnsbFile;
	}

	public String getPdfFilename() {
		return pdfFilename;
	}

	public void setPdfFilename(String pdfFilename) {
		this.pdfFilename = pdfFilename;
	}

	public byte[] getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(byte[] pdfFile) {
		this.pdfFile = pdfFile;
	}

	public String getCsvFilename() {
		return csvFilename;
	}

	public void setCsvFilename(String csvFilename) {
		this.csvFilename = csvFilename;
	}

	public byte[] getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(byte[] csvFile) {
		this.csvFile = csvFile;
	}

	public String getXlsFilename() {
		return xlsFilename;
	}

	public void setXlsFilename(String xlsFilename) {
		this.xlsFilename = xlsFilename;
	}

	public byte[] getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(byte[] xlsFile) {
		this.xlsFile = xlsFile;
	}

	public String getTxtFilename() {
		return txtFilename;
	}

	public void setTxtFilename(String txtFilename) {
		this.txtFilename = txtFilename;
	}

	public byte[] getTxtFile() {
		return txtFile;
	}

	public void setTxtFile(byte[] txtFile) {
		this.txtFile = txtFile;
	}

}