package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.Certificate;
import com.rssoftware.ou.database.entity.tenant.CertificatePK;
import com.rssoftware.ou.tenant.dao.CertificateDao;

@Repository
public class CertificateDaoImpl extends GenericDynamicDaoImpl<Certificate, CertificatePK> implements CertificateDao {

	public CertificateDaoImpl() {
		super(Certificate.class);
	}

}
