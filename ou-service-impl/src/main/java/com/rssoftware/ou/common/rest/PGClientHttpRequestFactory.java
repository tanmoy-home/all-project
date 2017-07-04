package com.rssoftware.ou.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import sun.net.www.protocol.https.DefaultHostnameVerifier;

public class PGClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

	private static final Logger log = LoggerFactory.getLogger(PGClientHttpRequestFactory.class);
	static String certPath = null;
	@Override
	protected void prepareConnection(HttpURLConnection connection,
			String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection)
					.setSSLSocketFactory(initSSLContext().getSocketFactory());
			
			if (System.getProperty("bypass.hostname.check") != null
					&& System.getProperty("bypass.hostname.check").equalsIgnoreCase("true")){
				((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
					
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				
			}
		}
		super.prepareConnection(connection, httpMethod);
	}

	private SSLContext initSSLContext() {
		try {
			System.setProperty("https.protocols", "TLSv1");

			// Set ssl trust manager. Verify against our server thumbprint
			final SSLContext ctx = SSLContext.getInstance("TLSv1");
			final PGX509TrustManager trustManager = new PGX509TrustManager();
			ctx.init(null, new TrustManager[] { trustManager }, null);
			return ctx;
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public void setCertPath(String thecertPath){
		certPath = thecertPath;
	}
}
