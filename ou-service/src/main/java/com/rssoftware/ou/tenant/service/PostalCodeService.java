package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.PostalCodeView;

public interface PostalCodeService { 
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	PostalCodeView getPostalCodeList() throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> fetchAll();

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> retirivePinByStateList(Long stateId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> retirivePinByCityList(Long cityId);

}
