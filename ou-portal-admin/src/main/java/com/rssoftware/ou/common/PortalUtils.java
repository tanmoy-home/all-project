package com.rssoftware.ou.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.Ack;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.service.UserService;

public final class PortalUtils {
	
	private static SecureRandom random = new SecureRandom();
	
	public static String generateRandomId() {
		return new BigInteger(50, random).toString(32);
	}
	private String getPrincipalDtl(UserService userService) {
		String crediantial = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userService.findByUserName(((UserDetails) principal).getUsername());

		if (principal instanceof UserDetails) {
			crediantial = ((UserDetails) principal).getUsername() + ":" + userEntity.getPassword(); //((UserDetails) principal).getPassword();
		} else {
			crediantial = principal.toString();
		}
		return crediantial;
	}

	public static HttpEntity<?> getHttpEntity(UserService userService) {
		HttpHeaders headers = new HttpHeaders();
		PortalUtils portalUtils = new PortalUtils();
		String plainCreds = portalUtils.getPrincipalDtl(userService);
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		headers.add("Authorization", "Basic " + base64Creds);
		Ack ack = new Ack();
		HttpEntity<Ack> httpEntity = new HttpEntity<Ack>(ack, headers);

		return httpEntity;
	}
	

	
	public static  HttpEntity<?> getHttpHeader(Object object, UserService userService)
    {
            HttpEntity<Object> httpEntity = new HttpEntity<Object>(object, getHttpEntity(userService).getHeaders());
            return httpEntity;
    }
	
	public static void flushFile(HttpServletResponse response,String fileName,long contentLength, FileInputStream inputStream)
		      throws IOException {
		    response.setContentType("application/octet-stream");
		    response.setContentLength((int)contentLength);
		    String headerKey = "Content-Disposition";
		    String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		    response.setHeader(headerKey, headerValue);
		    OutputStream outStream = response.getOutputStream();
		    byte[] buffer = new byte[4096];
		    int bytesRead = -1;
		    while ((bytesRead = inputStream.read(buffer)) != -1) {
		      outStream.write(buffer, 0, bytesRead);
		    }
		    inputStream.close();
		    outStream.close();
		  }


}