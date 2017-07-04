package com.rs.bbpou.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rs.bbpou.service.HtmlResponseService;



@RestController
public class HtmlResponseController {

	@Autowired
	private  HtmlResponseService htmSer;

	@RequestMapping(value="/htmlRes/{idl}",  method = RequestMethod.GET,produces=MediaType.TEXT_HTML_VALUE)
	
	String handleHtmlResponse(Model map,@PathVariable("idl") Integer id) {
	//	map.addAttribute("editform",new EditForm() );
		String response= htmSer.getHtmlResponse(id);
		
		return response;
		
	}
}
