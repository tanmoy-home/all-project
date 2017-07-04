package com.rssoftware.ou.common.rest;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.tenant.service.CertificateService;

public class BbpsX509TrustManager implements X509TrustManager {
	private static CertificateService certificateService = null;

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if (certificateService == null){
			certificateService = BeanLocator.getBean(CertificateService.class);
		}
		certificateService.getTrustManager().checkClientTrusted(chain, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if (certificateService == null){
			certificateService = BeanLocator.getBean(CertificateService.class);
		}
		certificateService.checkServerTrusted(chain, authType);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		if (certificateService == null){
			certificateService = BeanLocator.getBean(CertificateService.class);
		}
		return certificateService.getTrustManager().getAcceptedIssuers();
	}

}
