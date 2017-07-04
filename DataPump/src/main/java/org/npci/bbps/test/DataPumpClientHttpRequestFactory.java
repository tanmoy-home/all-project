package org.npci.bbps.test;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class DataPumpClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	@Override
	protected void prepareConnection(HttpURLConnection connection,
			String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection)
					.setSSLSocketFactory(initSSLContext().getSocketFactory());
			
			if (true){
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
			final DataPumpX509TrustManager trustManager = new DataPumpX509TrustManager();
			ctx.init(null, new TrustManager[] { trustManager }, null);
			return ctx;
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
