package com.rssoftware.ou.tenant.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BillFileConfig;
import com.rssoftware.ou.tenant.dao.BillFileConfigDao;

@Repository
public class BillFileConfigDaoImpl extends GenericDynamicDaoImpl<BillFileConfig, String> implements BillFileConfigDao {
	private static final String FETCH_BILL_FILE_CONFIG = "SELECT b FROM BillFileConfig b  ";

	public BillFileConfigDaoImpl() {
		super(BillFileConfig.class);
	}

	@Override
	public BillFileConfig getBillFileConfigByBillerId(String billerId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(FETCH_BILL_FILE_CONFIG + "where b.blrId = :billerId");
		query.setString("billerId", billerId);

		return (BillFileConfig) query.uniqueResult();
	}
}