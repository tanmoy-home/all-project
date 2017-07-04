package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SettlementFilePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3007207594571249476L;

	@Column(name="SETTLEMENT_CYCLE_ID")
	private String settlementCycleId;

	@Column(name="FILE_TYPE")
	private String fileType;
	
	public SettlementFilePK() {
	}

	public String getSettlementCycleId() {
		return settlementCycleId;
	}

	public void setSettlementCycleId(String settlementCycleId) {
		this.settlementCycleId = settlementCycleId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}