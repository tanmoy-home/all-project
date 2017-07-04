package org.npci.bbps.test;

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


public class DataPumpX509TrustManager implements X509TrustManager {
	//static String DEFAULT_TRUST_STORE = "/etc/ssl/certs/java/cacerts";
	//static String DEFAULT_TRUST_STORE = "/usr/lib/jdk1.8.0_71/jre/lib/security/cacerts";
	static String DEFAULT_TRUST_STORE = System.getProperty("default.trust.store", "/etc/ssl/certs/java/cacerts");
//	static String PATH_TO_WS=System.getProperty("path.to.ws");
	private static String PATH_TO_WS=System.getenv("OU_HOME");
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
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream is = new FileInputStream(new File(PATH_TO_WS+"/config/certificate/OU/oussl.crt"));
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
			e.printStackTrace();
		}
	}
	
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try{
			trustManager.checkServerTrusted(chain, authType);
		}
		catch (CertificateException ce){
			ce.printStackTrace();
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
