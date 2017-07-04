package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.OtpStatus;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;

public interface OtpService {
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public OtpDetails generateOtp(OtpDetails otpDetailsDev);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public String generatePassword();
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public OtpStatus validateOtp(OtpDetails otpDetailsDev, String encryptedOtp);
	
}
