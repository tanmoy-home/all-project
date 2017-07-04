package com.rssoftware.ou.vo;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserAccessVo {

	private Long id;
	private String userName;
	private String contact_number;
	private boolean resetPasswordFlag;
	private String lastLoginTime;
	private Boolean isActive;
	private Set<String> roles;
	private Set<String> accessibleServices;
	private Set<String> accessibleUIElements;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public boolean isResetPasswordFlag() {
		return resetPasswordFlag;
	}

	public void setResetPasswordFlag(boolean resetPasswordFlag) {
		this.resetPasswordFlag = resetPasswordFlag;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Set<String> getAccessibleServices() {
		return accessibleServices;
	}

	public void setAccessibleServices(Set<String> accessibleServices) {
		this.accessibleServices = accessibleServices;
	}

	public Set<String> getAccessibleUIElements() {
		return accessibleUIElements;
	}

	public void setAccessibleUIElements(Set<String> accessibleUIElements) {
		this.accessibleUIElements = accessibleUIElements;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
