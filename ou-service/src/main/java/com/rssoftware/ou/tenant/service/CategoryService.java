
package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.CategoryView;

public interface CategoryService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	CategoryView fetchByName(String cmsCategory) throws DataAccessException;
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	List<CategoryView> fetchAll() throws DataAccessException;
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	List<CategoryView> retiriveCategoryList() throws DataAccessException;
}
