package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RESPONSE_CODES database table.
 * 
 */
@Entity
@Table(name="RESPONSE_CODES")
@NamedQuery(name="ResponseCode.findAll", query="SELECT r FROM ResponseCode r")
public class ResponseCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ResponseCodePK id;

	@Column(name="CODE_DESC")
	private String codeDesc;

	public ResponseCode() {
	}

	public ResponseCodePK getId() {
		return this.id;
	}

	public void setId(ResponseCodePK id) {
		this.id = id;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}


}