package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "admin_tbl_role_Privilege")
public class RolePrivilegeEntity implements Serializable{

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1369279224740734441L;

	@Id
	//@SequenceGenerator(name="privilege_seq_gen",sequenceName="admin_tbl_role_Privilege_seq")        
    //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="privilege_seq_gen")
	private Long id;
	
	@Column(name = "role_id")
	private Long roleid;
	
	@Column(name = "role_prvlg_id")
	private Long rolePrvlgId;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_on")
	private Timestamp createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getRolePrvlgId() {
		return rolePrvlgId;
	}

	public void setRolePrvlgId(Long rolePrvlgId) {
		this.rolePrvlgId = rolePrvlgId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

}