package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.tenant.dao.ComplaintResponseDao;
import com.rssoftware.ou.tenant.service.ComplaintResponseService;

@Service
public class ComplaintResponseServiceImpl implements ComplaintResponseService{

	@Autowired
	ComplaintResponseDao complaintResponseDao;
	
	
	@Override
	public void save(ComplaintResponse complaintResponse) {
		complaintResponseDao.createOrUpdate(complaintResponse);
		
	}


	@Override
	public ComplaintResponse getResponse(String refId) {
		ComplaintResponse resp=complaintResponseDao.getResponseByRefId(refId);
		return resp;
	}
	

}
