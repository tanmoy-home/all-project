package com.rssoftware.ou.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bbps.schema.Ack;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;

@RestController
@RequestMapping(value = "/APIService")
public class TestAPIController {

	@RequestMapping(value = "/postApi/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> postApi(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId) throws Exception {

		TransactionContext.putTenantId(tenantId);
		Ack ack = new Ack();
		ResponseEntity<Ack> responseEntity = new ResponseEntity<Ack>(ack, HttpStatus.OK);
		ack.setRspCd("POST response is ok");
		System.out.println("RESPONSE @@@@@@@@@@@@ POST");
		return responseEntity;
	}

	@RequestMapping(value = "/getApi/urn:tenantId:{tenantId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getApi(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId) throws Exception {

		TransactionContext.putTenantId(tenantId);
		Ack ack = new Ack();
		ack.setRspCd("GET response is ok");
		ResponseEntity<Ack> responseEntity = new ResponseEntity<Ack>(ack, HttpStatus.OK);
		System.out.println("RESPONSE @@@@@@@@@@@@ GET");
		return responseEntity;
	}

	
}
