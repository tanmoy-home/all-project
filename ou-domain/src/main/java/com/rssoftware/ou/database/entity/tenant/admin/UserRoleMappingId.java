package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "admin_tbl_user_role_mapping")
public class UserRoleMappingId implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1369279224740734441L;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role_id")
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleMappingId)) return false;
        UserRoleMappingId that = (UserRoleMappingId) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getRoleId(), that.getRoleId());
    }
	
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRoleId());
    }

}
