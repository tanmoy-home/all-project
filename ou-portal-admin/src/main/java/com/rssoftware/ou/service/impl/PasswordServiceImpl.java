package com.rssoftware.ou.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.ChangePasswordEntity;
import com.rssoftware.ou.repository.PasswordRepository;
import com.rssoftware.ou.service.PasswordService;


@Service
public class PasswordServiceImpl implements PasswordService {
	@Autowired
	PasswordRepository passwordRepository;

	@Override
	public boolean findAllPasswordByUser(String userName, String userPwd, int cnt) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Iterable<ChangePasswordEntity> allPwd = passwordRepository.findAll();
		int i = 0;
		 for(ChangePasswordEntity ch : allPwd){
			 if(userName.equals(ch.getUserName())){
				 i++;
				 if(i==cnt){
					 break;
				 }
				if(passwordEncoder.matches(userPwd, ch.getPassword())){
					return false;
				}
			 }
		 }
		 return true;
	}
	
	@Override
	public Iterable<ChangePasswordEntity> findByUsernameOrderByResetOnDesc(String username) {
		return passwordRepository.findFirstByUserNameOrderByResetOnDesc(username);
	}
}
