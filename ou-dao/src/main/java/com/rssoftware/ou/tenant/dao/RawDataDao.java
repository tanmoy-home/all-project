package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.RawData;
import com.rssoftware.ou.database.entity.tenant.RawDataPK;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

public interface RawDataDao extends GenericDao<RawData, RawDataPK> {

	List<Object> fetchData() throws IsoMsgException;
}
