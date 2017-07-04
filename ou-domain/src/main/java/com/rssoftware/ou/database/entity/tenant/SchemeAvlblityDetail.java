package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SCHEME_AVLBLITY_DETAILS")
@NamedQuery(name = "SchemeAvlblityDetail.findAll", query = "SELECT a FROM SchemeAvlblityDetail a")
public class SchemeAvlblityDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4277936439492204148L;

	@EmbeddedId	
	private SchemeAvlblityDetailPK id;

	public SchemeAvlblityDetailPK getId() {
		return id;
	}

	public void setId(SchemeAvlblityDetailPK id) {
		this.id = id;
	}
	
}
