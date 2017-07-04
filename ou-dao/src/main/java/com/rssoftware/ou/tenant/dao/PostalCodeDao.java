package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.PostalCode;

public interface PostalCodeDao extends GenericDao<PostalCode, Long> {

	List<PostalCode> fetchAll();

	List<PostalCode> retirivePinByStateList(Long stateId);

	List<PostalCode> retirivePinByCityList(Long cityId);
	
}
