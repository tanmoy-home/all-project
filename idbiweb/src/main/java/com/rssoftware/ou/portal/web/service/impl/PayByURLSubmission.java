package com.rssoftware.ou.portal.web.service.impl;

import java.util.Map;

import org.bbps.schema.BillFetchResponse;
import org.springframework.ui.Model;

import com.rssoftware.ou.portal.web.dto.BillInfoDTO;



public class PayByURLSubmission extends AbstractPaymentStrategy {

	@Override
	public Object pay(BillInfoDTO billInfoDTO) {		
		return null;
	}

	@Override
	public String populateModel(Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTxnRefId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPaymentSuccess() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String parseRequestObjectToJSONString(Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseResponseObjectToJSONString(Object response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object digestResponseParams(Map<String, String> responseParams) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
