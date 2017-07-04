package com.rssoftware.ou.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String refId;

	public CustomUserDetails(String username, String password, String refId,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.setRefId(refId);
		// TODO Auto-generated constructor stub
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

}
