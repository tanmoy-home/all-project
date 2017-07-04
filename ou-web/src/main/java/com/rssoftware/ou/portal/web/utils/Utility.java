package com.rssoftware.ou.portal.web.utils;

import java.io.IOException;
import java.util.Properties;

import org.bbps.schema.Ack;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class Utility {
	
	public static HttpEntity<?> getHttpEntityForGet()
    {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + Constants.DEFAULT_USERNAME_PASSWORD);
            Ack ack = new Ack();
            HttpEntity<Ack> httpEntity = new HttpEntity<Ack>(ack, headers);
            return httpEntity;
    }
	 
	 public static  HttpEntity<?> getHttpEntityForPost(Object object)
    {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + Constants.DEFAULT_USERNAME_PASSWORD);
            HttpEntity<Object> httpEntity = new HttpEntity<Object>(object,headers);
            return httpEntity;
    }

	
}
