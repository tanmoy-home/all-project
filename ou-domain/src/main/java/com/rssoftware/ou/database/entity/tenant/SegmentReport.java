package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the COMPLAINT_REPORT database table.
 * 
 */
@Entity
@Table(name="SEGMENT_REPORT")
@NamedQuery(name="SegmentReport.findAll", query="SELECT s FROM SegmentReport s")
public class SegmentReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="segment_report_seq_gen",sequenceName="SEGMENT_REPORT_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="segment_report_seq_gen")
	@Column(name="REPORT_ID", unique=true, nullable=false)
	private Long reportId;

	@Column(name="OU_NAME")
	private String bbpouName;
	
	@Column(name="BLR_CATEGORY_NAME")
	private String blrCategory;
	
	@Column(name="BLR_NAME")
	private String blrName;
	
	@Column(name="ONUS_TXNCOUNT")
	private BigDecimal onUsCount;
	
	@Column(name="OFFUS_TXNCOUNT")
	private BigDecimal offUsCount;

	@Column(name="ONUS_TXNTOT")
	private BigDecimal onUsTot;
	
	@Column(name="OFFUS_TXNTOT")
	private BigDecimal offUsTot;
	
	@Column(name="CRTN_TS")
	private Timestamp crtnTs;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getBbpouName() {
		return bbpouName;
	}

	public void setBbpouName(String bbpouName) {
		this.bbpouName = bbpouName;
	}

	public String getBlrCategory() {
		return blrCategory;
	}

	public void setBlrCategory(String blrCategory) {
		this.blrCategory = blrCategory;
	}

	public String getBlrName() {
		return blrName;
	}

	public void setBlrName(String blrName) {
		this.blrName = blrName;
	}

	public BigDecimal getOnUsCount() {
		return onUsCount;
	}

	public void setOnUsCount(BigDecimal onUsCount) {
		this.onUsCount = onUsCount;
	}

	public BigDecimal getOffUsCount() {
		return offUsCount;
	}

	public void setOffUsCount(BigDecimal offUsCount) {
		this.offUsCount = offUsCount;
	}

	public BigDecimal getOnUsTot() {
		return onUsTot;
	}

	public void setOnUsTot(BigDecimal onUsTot) {
		this.onUsTot = onUsTot;
	}

	public BigDecimal getOffUsTot() {
		return offUsTot;
	}

	public void setOffUsTot(BigDecimal offUsTot) {
		this.offUsTot = offUsTot;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

}
