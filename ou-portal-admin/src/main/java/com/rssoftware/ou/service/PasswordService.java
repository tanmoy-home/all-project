package com.rssoftware.ou.service;

import com.rssoftware.ou.database.entity.tenant.admin.ChangePasswordEntity;

public interface PasswordService {

	public boolean findAllPasswordByUser(String userName, String userPwd, int cnt);
	
	public Iterable<ChangePasswordEntity> findByUsernameOrderByResetOnDesc(String username);
}
