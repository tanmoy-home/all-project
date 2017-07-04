package com.rssoftware.ou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.PersistentLoginEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.repository.OrganizationRepository;
import com.rssoftware.ou.repository.PersistentLoginRepository;
import com.rssoftware.ou.service.PersistentLoginService;
@Service
public class PersistentLoginEntityImpl implements PersistentLoginService {
	
	@Autowired
	public PersistentLoginRepository persistentLoginRepository;

	@Override
	public void saveLoginEntity(
			PersistentLoginEntity persistentLoginEntity) {
		
		persistentLoginRepository.save(persistentLoginEntity);

	}

	@Override
	public PersistentLoginEntity findByUserId(String userId) {
		// TODO Auto-generated method stub
		PersistentLoginEntity loginvalue = persistentLoginRepository.findByUserid(userId);
		return loginvalue;
	}

}
