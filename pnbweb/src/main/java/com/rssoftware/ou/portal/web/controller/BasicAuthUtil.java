package com.rssoftware.ou.portal.web.controller;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.Ack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;

@Component
public class BasicAuthUtil {

	@Value("${BASIC_AUTH_CREDENTIAL}")
	private String BASIC_AUTH_CREDENTIAL;
		
	public static HttpEntity<?> getHttpEntity() {
		BasicAuthUtil basicAuthUtil = BeanLocator.getBean(BasicAuthUtil.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + basicAuthUtil.getBase64Creds());
		Ack ack = new Ack();
		HttpEntity<Ack> httpEntity = new HttpEntity<Ack>(ack, headers);
		return httpEntity;
	}

	public static HttpEntity<?> getHttpEntity(Object object) {
		BasicAuthUtil basicAuthUtil = BeanLocator.getBean(BasicAuthUtil.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + basicAuthUtil.getBase64Creds());
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(object, headers);
		return httpEntity;
	}
	
	private String getBase64Creds() {
		byte[] plainCredsBytes = BASIC_AUTH_CREDENTIAL.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		return new String(base64CredsBytes);
	}

}
