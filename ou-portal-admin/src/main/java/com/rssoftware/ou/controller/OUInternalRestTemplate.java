package com.rssoftware.ou.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class OUInternalRestTemplate {
	private final RestTemplate restTemplate;

	private static int MAX_RETRY_COUNT = 3;

	private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	} };

	private OUInternalRestTemplate() {
		restTemplate = new RestTemplate(clientHttpRequestFactory());
	}

	private SimpleClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
			@Override
			protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
				if (connection instanceof HttpsURLConnection) {
					((HttpsURLConnection) connection).setSSLSocketFactory(initSSLContext().getSocketFactory());
					((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					});
				}
				connection.setDoOutput(true);
				super.prepareConnection(connection, httpMethod);
			}

			private SSLContext initSSLContext() {
				SSLContext sc = null;
				try {
					sc = SSLContext.getInstance("SSL");
					sc.init(null, UNQUESTIONING_TRUST_MANAGER, null);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					e.printStackTrace();
				}

				return sc;
			}
		};
		factory.setReadTimeout(100000);
		factory.setConnectTimeout(1000);
		//factory.setBufferRequestBody(false);

		return factory;
	}

	/*
	 * @return the object of RestTemplate which contains the Interceptors and
	 * RequestFactory so that the request and response XML can be validated
	 * automatically
	 */
	public static OUInternalRestTemplate createInstance() {
		// return the RestTemplate object
		return new OUInternalRestTemplate();
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
			Object... uriVariables) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.postForEntity(url, request, responseType, uriVariables);
			} catch (ResourceAccessException e) {
				if (i <= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}

	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.postForEntity(url, request, responseType);
			} catch (ResourceAccessException e) {
				if (i <= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}
	}

	public <T> T postForObject(String url, Object request, Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.postForObject(url, request, responseType);
			} catch (ResourceAccessException e) {
				if (i <= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}

	}

	public <T> T getForObject(String url, Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.getForObject(url, responseType);
			} catch (ResourceAccessException e) {
				if (i <= MAX_RETRY_COUNT) {
					throw e;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}

	}
	

	
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method,HttpEntity<T> httpEntity, Class responseType){
		return(restTemplate.exchange(url, method, httpEntity, responseType));
	}
	
	
}
