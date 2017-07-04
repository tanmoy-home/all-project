package com.rssoftware.ou.common.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;

public class OURestTemplate {
	private final RestTemplate restTemplate;

	private static int MAX_RETRY_COUNT = 3;

	/*
	 * making the constructor private so that the object cannot be created
	 * directly.
	 */
	private OURestTemplate() {
		restTemplate = new RestTemplate(clientHttpRequestFactory());
		ClientHttpRequestInterceptor ri = new OURestTemplateInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<>(1);
		ris.add(ri);
		restTemplate.setInterceptors(ris);
	}

		OuClientHttpRequestFactory factory = new OuClientHttpRequestFactory();
	private ClientHttpRequestFactory clientHttpRequestFactory() {
		factory.setReadTimeout(100000);
		factory.setConnectTimeout(1000);
		factory.setBufferRequestBody(false);
		
		/*try {
			if (CommonUtils.hasValue(CommonUtils.getProxyHost()) && CommonUtils.hasValue(CommonUtils.getProxyPort())){
				Proxy proxy= new Proxy(Type.HTTP, new InetSocketAddress(CommonUtils.getProxyHost(), Integer.parseInt(CommonUtils.getProxyPort())));
				factory.setProxy(proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not set proxy");
		}*/
			
		// initialize trust manager
		new BbpsX509TrustManager();
		
		return factory;
	}

	/*
	 * @return the object of RestTemplate which contains the Interceptors and
	 * RequestFactory so that the request and response XML can be validated
	 * automatically
	 */
	public static OURestTemplate createInstance() {
		// return the RestTemplate object
		return new OURestTemplate();
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType, Object... uriVariables) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.postForEntity(url, request, responseType,
						uriVariables);
			} catch (ResourceAccessException e) {
				if (i >= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}

	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.postForEntity(url, request, responseType);
			} catch (ResourceAccessException e) {
				if (i >= MAX_RETRY_COUNT) {
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
				if (i >= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}

	}
}
