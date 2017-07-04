package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "admin_tbl_user_role_mapping")
public class UserRoleMappingEntity implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1369279224740734441L;

	@EmbeddedId
	UserRoleMappingId userRoleMappingId;
	
	
	public UserRoleMappingId getUserRoleMappingId() {
		return userRoleMappingId;
	}

	public void setUserRoleMappingId(UserRoleMappingId userRoleMappingId) {
		this.userRoleMappingId = userRoleMappingId;
	}

	@Column(name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;
	

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleMappingEntity)) return false;
        UserRoleMappingEntity that = (UserRoleMappingEntity) o;
        return Objects.equals(getUserRoleMappingId().getUserId(), that.getUserRoleMappingId().getUserId()) &&
                Objects.equals(getUserRoleMappingId().getRoleId(), that.getUserRoleMappingId().getRoleId());
    }
	
    @Override
    public int hashCode() {
        return Objects.hash(getUserRoleMappingId().getUserId(), getUserRoleMappingId().getRoleId());
    }

}
