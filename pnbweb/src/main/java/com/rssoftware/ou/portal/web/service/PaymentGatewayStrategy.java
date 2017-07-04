package com.rssoftware.ou.portal.web.service;

import org.springframework.ui.Model;

import com.rssoftware.ou.portal.web.dto.BillInfoDTO;

public interface PaymentGatewayStrategy {
//	public boolean pay();
	public Object pay(BillInfoDTO billInfoDTO);
	public String populateModel(Model model);
}
