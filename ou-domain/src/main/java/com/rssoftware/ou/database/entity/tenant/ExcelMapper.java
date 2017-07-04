package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BILLER_DETAILS database table.
 * 
 */
@Entity
@Table(name = "EXCEL_MAPPER")
@NamedQuery(name = "ExcelMapper.findAll", query = "SELECT e FROM ExcelMapper e")
public class ExcelMapper implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MAP_ID")
	private String mapId;

	@Column(name = "OU_NAME")
	private String mapToName;

	@Column(name = "CU_NAME")
	private String mappedFromName;

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getMapToName() {
		return mapToName;
	}

	public void setMapToName(String mapToName) {
		this.mapToName = mapToName;
	}

	public String getMappedFromName() {
		return mappedFromName;
	}

	public void setMappedFromName(String mappedFromName) {
		this.mappedFromName = mappedFromName;
	}
}