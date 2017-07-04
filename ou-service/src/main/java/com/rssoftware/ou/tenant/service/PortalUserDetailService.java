
package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.PortalUserDetail;
import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.domain.UserDetail;

public interface PortalUserDetailService {
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public UserDetail getPortalUserDetails(String userId);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public void insert(PortalUserDetail portalUserDetail);

	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<PortalUserRole> getUserRollList(PortalUserRole portalUserRole);
*/
	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public void fetch(PortalUserRole portalRole);*/
	
}
