package com.rs.BBPOU;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TemplateController {
	
	
	@ResponseBody
	@RequestMapping(value="/photo2", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] testphoto(HttpServletRequest request) throws IOException {
		
		HttpSession session = request.getSession();       
        ServletContext sc = session.getServletContext();



	    InputStream in = sc.getResourceAsStream("/resources/images/RSSoftware_Logo.png");
	//    InputStream input = getClass().getResourceAsStream("ListStopWords.txt");
	    
	    return IOUtils.toByteArray(in);
	}
	
	
	@RequestMapping(value = "/getjsFile", produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getFile(HttpServletRequest request) throws IOException{
		
		HttpSession session = request.getSession();       
        ServletContext sc = session.getServletContext();

	    ResponseEntity respEntity = null;

	   // byte[] reportBytes = null;
	   
	      //  InputStream inputStream = new FileInputStream("/resources/js/Customed/OwnerInfo.js");
	    	InputStream in = sc.getResourceAsStream("/resources/js/Customed/sample.js");
	       // String type=result.toURL().openConnection().guessContentTypeFromName("OwnerInfo.js");

	        byte[]out=org.apache.commons.io.IOUtils.toByteArray(in);

	        HttpHeaders responseHeaders = new HttpHeaders();
	      //  responseHeaders.add("content-disposition", "attachment; filename=OwnerInfo.js" );
	        responseHeaders.add("Content-Type","application/javascript");

	        respEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
	    
	    
	    return respEntity;
	}
	
	
	@RequestMapping(value = "/getcssFile", produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getCssFile(HttpServletRequest request) throws IOException{
		
		HttpSession session = request.getSession();       
        ServletContext sc = session.getServletContext();

	    ResponseEntity respEntity = null;

	   // byte[] reportBytes = null;
	   
	      //  InputStream inputStream = new FileInputStream("/resources/js/Customed/OwnerInfo.js");
	    	InputStream in = sc.getResourceAsStream("/resources/css/bbpou.css");
	       // String type=result.toURL().openConnection().guessContentTypeFromName("OwnerInfo.js");

	        byte[]out=org.apache.commons.io.IOUtils.toByteArray(in);

	        HttpHeaders responseHeaders = new HttpHeaders();
	      //  responseHeaders.add("content-disposition", "attachment; filename=OwnerInfo.js" );
	        responseHeaders.add("Content-Type","text/css");

	        respEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
	    
	    
	    return respEntity;
	}

	

}
