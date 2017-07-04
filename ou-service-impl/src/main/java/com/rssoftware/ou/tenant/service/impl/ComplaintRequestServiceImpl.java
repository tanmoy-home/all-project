package com.rssoftware.ou.tenant.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.tenant.dao.ComplaintRequestDao;
import com.rssoftware.ou.tenant.service.ComplaintRequestService;

@Service
public class ComplaintRequestServiceImpl implements ComplaintRequestService{

	
	@Autowired
	ComplaintRequestDao complaintRequestDao;
	
	@Override
	public void save(ComplaintRequest complaintRequest) {
		complaintRequestDao.createOrUpdate(complaintRequest);
		
	}
	
	@Override
	public ComplaintRequest getRequest(String refId) {
		ComplaintRequest req=complaintRequestDao.getRequestByRefId(refId);
		return req;
	}
	
	@Override
	public List<ComplaintRequest> fetchALLByMobile(String mobile) {
		List<ComplaintRequest> complaintList=complaintRequestDao.getComplaintListByMobile(mobile);
		return complaintList;
	}
	
}
 