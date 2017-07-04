package com.rssoftware.ou.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.database.entity.tenant.admin.ChangePasswordEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.repository.PasswordRepository;
import com.rssoftware.ou.repository.RolePrivMappingUserRepository;
import com.rssoftware.ou.repository.UserRepository;
import com.rssoftware.ou.service.UserService;



@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RolePrivMappingUserRepository rolePrivMappingUserRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;
		
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public UserEntity findById(Long id) {
		return userRepository.findOne(id);
	}
	
	public Iterable<UserEntity> findByUpdatedBy(String userName) {
		return userRepository.findByUpdatedBy(userName);
	}
	
	public Iterable<UserEntity> findByRolePriviledge(Long roleId) {
		return  rolePrivMappingUserRepository.findRolesByPrivledge(roleId);
	}
	
	public Iterable<UserEntity> findUsersByRolePriviledge(String userName) {
		return  rolePrivMappingUserRepository.findUsersByRolePriviledge(userName);
	}

	public UserEntity findByUserName(String userName) {
		UserEntity user = userRepository.findByUsername(userName);
		return user;
	}

	public void saveUser(UserEntity user) {	
		userRepository.save(user);
	}
	
	public void changePassword(UserEntity user) {
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		userRepository.save(user);
		ChangePasswordEntity chPwd = new ChangePasswordEntity();
		chPwd.setUserName(user.getUsername());
		chPwd.setPassword(user.getPassword());
		chPwd.setResetOn(new Timestamp(new Date().getTime()));
		passwordRepository.save(chPwd);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate update explicitly.
	 * Just fetch the entity from db and update it with proper values within transaction.
	 * It will be updated in db once transaction ends. 
	 */
	public void updateUser(UserEntity userEntity) {
		UserEntity entity = userRepository.findOne(userEntity.getId());
		if(entity!=null){
			entity.setUsername((userEntity.getUsername()));
			if(!userEntity.getPassword().equals(entity.getPassword())){
				entity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			}
			entity.setFirstName(userEntity.getFirstName());
			entity.setLastName(userEntity.getLastName());
			entity.setContactNumber(userEntity.getContactNumber());
			entity.setEmail(userEntity.getEmail());
		}
	}

	
	public void deleteUserByName(String userName) {
		
		userRepository.delete(userRepository.findByUsername(userName));
	}

	public Iterable<UserEntity> findAllUsers() {
		return userRepository.findAll();
	}

	public boolean isUserNameUnique(Long id, String userName) {
		UserEntity user = findByUserName(userName);
		return ( user == null || ((id != null) && (user.getId() == id)));
	}
	
}
