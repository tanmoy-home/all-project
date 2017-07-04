package org.npci.bbps.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/*
 * Simple re-implementation of RestTemplace class to automatically sign and validate the XML Signature
 */
public final class DataPumpRestTemplate {
	private final RestTemplate restTemplate;

	private static int MAX_RETRY_COUNT = 3;
//	private static int MAX_RETRY_COUNT = 0;

	/*
	 * making the constructor private so that the object cannot be created
	 * directly.
	 */
	private DataPumpRestTemplate() {
		restTemplate = new RestTemplate(clientHttpRequestFactory());
		ClientHttpRequestInterceptor ri = new DataPumpRestTemplateInterceptor(); 
		List<ClientHttpRequestInterceptor> ris = new ArrayList<>(1); 
		ris.add(ri);
		restTemplate.setInterceptors(ris);
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		// BufferingClientHttpRequestFactory factory = new
		// BufferingClientHttpRequestFactory(new
		// SimpleClientHttpRequestFactory());
		// HttpComponentsClientHttpRequestFactory factory = new
		// HttpComponentsClientHttpRequestFactory();
		DataPumpClientHttpRequestFactory factory = new DataPumpClientHttpRequestFactory();
		factory.setReadTimeout(100000);
		factory.setConnectTimeout(1000);
		factory.setBufferRequestBody(false);
		return factory;
	}

	/*
	 * @return the object of RestTemplate which contains the Interceptors and
	 * RequestFactory so that the request and response XML can be validated
	 * automatically
	 */
	public static DataPumpRestTemplate createObject() {
		// return the RestTemplate object
		return new DataPumpRestTemplate();
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
