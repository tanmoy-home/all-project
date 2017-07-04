package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.BusinessCategory;

public interface CategoryDao extends GenericDao<BusinessCategory, String>{

	BusinessCategory fetchByName(String cmsCategory);

}
