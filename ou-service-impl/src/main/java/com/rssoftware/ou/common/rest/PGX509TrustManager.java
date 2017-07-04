package com.rssoftware.ou.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PGX509TrustManager implements X509TrustManager {

	private static final Logger log = LoggerFactory.getLogger(PGX509TrustManager.class);
	static String DEFAULT_TRUST_STORE = "/etc/ssl/certs/java/cacerts";
	static X509TrustManager trustManager;
	
	
	static {
		try {
			KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream in = new FileInputStream(DEFAULT_TRUST_STORE);
			try { 
				ts.load(in, null); 
			}
			finally { 
				in.close(); 
			}
			//String certPath = System.getProperty("cert.path") == null? "/home/rsdpp/git/npci-upi/certificate/psp1/pspssl.crt" : System.getProperty("cert.path")+"/haproxy.crt";
			String certPath = System.getProperty("double.veri.cert", "/home/rsdpp/git/npci-upi/certificate/ssl/haproxy.crt");
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream is = new FileInputStream(new File(certPath));
			InputStream caInput = new BufferedInputStream(is);
			Certificate ca;
			try {
				ca = cf.generateCertificate(caInput);
				ts.setCertificateEntry(""+System.currentTimeMillis(), ca);
			} finally {
				try {
					caInput.close();
				} catch (IOException e) {}
				try {
					is.close();
				} catch (IOException e) {}
			}
			
			// initialize a new TMF with the ts we just loaded
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ts);			
			
			TrustManager tms[] = tmf.getTrustManagers();
			for (int i = 0; i < tms.length; i++) {
				if (tms[i] instanceof X509TrustManager) {
					trustManager = (X509TrustManager) tms[i];
					break;
				}
			}
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage() , e );
		}
	}
	
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try{
			trustManager.checkServerTrusted(chain, authType);
		}
		catch (CertificateException ce){
			log.error(ce.getMessage() + ce );
			throw ce;
		}
			
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		trustManager.checkClientTrusted(chain, authType);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return trustManager.getAcceptedIssuers();
	}
	
}
