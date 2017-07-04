package com.rssoftware.ou.common.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.rssoftware.ou.batch.to.BillDetails;

public class OuClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	
	
	private final static Logger logger = LoggerFactory.getLogger(OuClientHttpRequestFactory.class);


	@Override
	protected void prepareConnection(HttpURLConnection connection,
			String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection)
					.setSSLSocketFactory(initSSLContext().getSocketFactory());
			
			if (System.getProperty("bypass.hostname.check") == null
					|| System.getProperty("bypass.hostname.check").equalsIgnoreCase("true")){
				((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}
				});
				
			}
		}
		super.prepareConnection(connection, httpMethod);
	}

	private SSLContext initSSLContext() {
		try {
			System.setProperty("https.protocols", "TLSv1.2");

			// Set ssl trust manager. Verify against our server thumbprint
			final SSLContext ctx = SSLContext.getInstance("TLSv1.2");
			final BbpsX509TrustManager trustManager = new BbpsX509TrustManager();
			ctx.init(null, new TrustManager[] { trustManager }, null);
			return ctx;
		} catch (final Exception ex) {

			logger.error( ex.getMessage(), ex);
            logger.info("In Excp : " + ex.getMessage());
			return null;
		}

	}
}
