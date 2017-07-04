package com.rssoftware.ou.tenant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.model.tenant.UserView;
import com.rssoftware.ou.tenant.dao.UserDao;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.UserService;
import com.rssoftware.ou.validator.UserValidator;

@Service
public class UserServiceImpl implements UserService {

	private final static Logger	log	= LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserDao						userDao;

	/*
	 * @Autowired UserDetailsDao userDetailsDao;
	 */

	@Autowired
	private AgentService		agentService;

	@SuppressWarnings("unchecked")
	@Override
	public UserView getUserById(String userId) {
		UserEntity user = (UserEntity) userDao.get(userId);
		UserView userView = mapFrom(user);
		return userView;
	}

	@Override
	public UserView getUserByUserName(String userName) {
		UserEntity user = userDao.getUserByUserName(userName);
		UserView userView = mapFrom(user);
		return userView;
	}
	
	@Override
	public UserEntity getUsrDtlByUserName(String userName) {
		UserEntity user = userDao.getUserByUserName(userName);
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(UserView userView) {
		userDao.createOrUpdate(mapTo(userView));

	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveNewPassword(UserView userView) {
		UserEntity user = (UserEntity) userDao.get(userView.getId());
		user.setPassword(userView.getPassword());
		user.setResetPasswordFlag(false);
		userDao.createOrUpdate(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserView updateProfile(UserView userView) throws ValidationException {
		userView = UserValidator.validateRequest(userView);
		UserEntity user = (UserEntity) userDao.get(userView.getId());
		// UserDetail userDetail=user.getUserDetail();
		if (userView.getFirstName() != null)
			user.setFirstName(userView.getFirstName());
		if (userView.getLastName() != null)
			user.setLastName(userView.getLastName());
		if (userView.getAddress() != null)
			user.setAddress(userView.getAddress());
		if (userView.getCountry() != null)
			user.setCountry(userView.getCountry());
		if (userView.getEmail() != null)
			user.setEmail(userView.getEmail());
		if (userView.getMobile() != null)
			user.setMobile(userView.getMobile());
		if (userView.getContactNumber() != null)
			user.setContactNumber(userView.getContactNumber());
		if (userView.getAddress() != null)
			user.setAddress(userView.getAddress());
		if (userView.getCountry() != null)
			user.setCountry(userView.getCountry());
		if (userView.getState() != null)
			user.setState(userView.getState());
		if (userView.getPincode() != null)
			user.setPincode(userView.getPincode());
		if (userView.getAadhar() != null)
			user.setAadhar(userView.getAadhar());
		if (userView.getPan() != null)
			user.setPan(userView.getPan());

		// user.setUserDetail(userDetail);
		try {
			userDao.createOrUpdate(user);
			return userView;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			return null;
		}
	}

	@Override
	public void delete(String userId) {
		// TODO Auto-generated method stub

	}

	private UserView mapFrom(UserEntity user) {
		if (null == user) {
			return null;
		}
		UserView userView = new UserView();
		userView.setId(user.getId());
		userView.setUsername(user.getUsername());
		userView.setPassword(user.getPassword());
		userView.setFirstName(user.getFirstName());
		userView.setLastName(user.getLastName());
		userView.setEmail(user.getEmail());
		userView.setResetPasswordFlag(user.getResetPasswordFlag());
		userView.setContactNumber(user.getContactNumber());
		userView.setCreatedBy(user.getCreatedBy());
		userView.setUpdatedBy(user.getUpdatedBy());
		userView.setIsActive(user.getIsActive());
		userView.setIsAccountLocked(user.getIsAccountLocked());
		userView.setUserRefId(user.getUserRefId());
		userView.setAddress(user.getAddress());
		userView.setCountry(user.getCountry());
		userView.setState(user.getState());
		userView.setPincode(user.getPincode());
		userView.setMobile((user.getMobile()));
		userView.setPan(user.getPan());
		userView.setAadhar(user.getAadhar());
		return userView;
	}

	@SuppressWarnings("unchecked")
	private UserEntity mapTo(UserView userView) {
		if (userView == null)
			return null;

		UserEntity user = (UserEntity) userDao.get(userView.getId());
		if (user == null)
			user = new UserEntity();

		user.setId(userView.getId());
		user.setUsername(userView.getUsername());
		user.setPassword(userView.getPassword());
		user.setFirstName(userView.getFirstName());
		user.setLastName(userView.getLastName());
		user.setEmail(userView.getEmail());
		user.setResetPasswordFlag(userView.getResetPasswordFlag());
		user.setContactNumber(userView.getContactNumber());
		user.setCreatedBy(userView.getCreatedBy());
		user.setUpdatedBy(userView.getUpdatedBy());
		user.setIsActive(userView.getIsActive());
		user.setIsAccountLocked(userView.getIsAccountLocked());
		user.setUserRefId(userView.getUserRefId());
		user.setAddress(userView.getAddress());
		user.setCountry(userView.getCountry());
		user.setState(userView.getState());
		user.setPincode(userView.getPincode());
		user.setMobile((userView.getMobile()));
		user.setPan(userView.getPan());
		user.setAadhar(userView.getAadhar());

		// String flg=userView.getResetPasswordFlag();

		/*
		 * UserDetail userDetail=user.getUserDetail(); if(userDetail == null){
		 * userDetail=new UserDetail(); }
		 */

		// user.setUserDetail(userDetail);

		/*
		 * UserProfile userProfile=user.getUserProfile(); if(userProfile ==
		 * null){ userProfile=new UserProfile(); }
		 * userProfile.setProfileType(userView.getProfile());
		 * user.setUserProfile(userProfile);
		 */

		// userProfile.getUsers().add(user);

		return user;
	}

	private String encodePassword(String password) {
		return new BCryptPasswordEncoder(11).encode(password);
	}

}
