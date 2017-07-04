package org.npci.bbps.test;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/*
 * re-implementation of ClientHttpRequestInterceptor to intercept request and response to validate the XML Signature
 * 
 *  @author RSDPP
 */
public class BBPSBatchRestTemplateInterceptor implements ClientHttpRequestInterceptor {

	private static String CONTENT_LENGTH_HEADER = "Content-Length";

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		int contentLength = body.length;
		if (request.getHeaders().containsKey(CONTENT_LENGTH_HEADER)){
			request.getHeaders().remove(CONTENT_LENGTH_HEADER);
		}
		request.getHeaders().add(CONTENT_LENGTH_HEADER, String.valueOf(contentLength));
		
		//System.out.println("********************Cheking body**************  :"+ new String(body));
		ClientHttpResponse response = execution.execute(request, body);

		return response;
	}
}
