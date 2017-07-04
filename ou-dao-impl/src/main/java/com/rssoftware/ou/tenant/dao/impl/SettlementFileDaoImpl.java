package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SettlementFile;
import com.rssoftware.ou.database.entity.tenant.SettlementFilePK;
import com.rssoftware.ou.tenant.dao.SettlementFileDao;

@Repository
public class SettlementFileDaoImpl extends GenericDynamicDaoImpl<SettlementFile, SettlementFilePK> implements SettlementFileDao {

	public SettlementFileDaoImpl() {
		super(SettlementFile.class);
	}
}