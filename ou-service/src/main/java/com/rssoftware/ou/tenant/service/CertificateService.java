package com.rssoftware.ou.tenant.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.CertificateView;

public interface CertificateService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	void refresh() throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException;
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void save(CertificateView ce);
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void delete(CertificateView ce);
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	CertificateView[] retrieveCertificateList();
	void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException;
	Certificate getCUSignerCertificate();
	Key[] getOUSignerPrivatePublicKey();
	X509TrustManager getTrustManager();
	
}
