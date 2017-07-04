package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE_MASTER")
@NamedQuery(name = "PortalUserRole.findAll", query = "SELECT a from PortalUserRole a")
public class PortalUserRole implements Serializable {

	private static final long serialVersionUID = 4729538136256092812L;

	@Id
	@Column(name = "USERROLE_ID", nullable = false, length = 20)
	private String userRoleId;

	@Column(name = "ROLE_NAME", nullable = false, length = 20)
	private String roleName;

	@Column(name = "CREATED_BY", nullable = false, length = 20)
	private String createdBy;

	@Column(name = "MODIFIED_BY", length = 20)
	private String modifiedBy;

	@Column(name = "ROLE_STATUS", nullable = false, length = 20)
	private String role_status;

	@Column(name = "CREATION_DATE_TIME", nullable = false)
	private Date creationDateTime;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getRole_status() {
		return role_status;
	}

	public void setRole_status(String role_status) {
		this.role_status = role_status;
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
