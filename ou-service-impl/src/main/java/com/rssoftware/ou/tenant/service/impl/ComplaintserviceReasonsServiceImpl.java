package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.ComplaintserviceReasons;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;
import com.rssoftware.ou.tenant.dao.ComplaintserviceReasonsDao;
import com.rssoftware.ou.tenant.service.ComplaintserviceReasonsService;

import in.co.rssoftware.bbps.schema.ParticipationType;
import in.co.rssoftware.bbps.schema.ServiceReasonDetail;
import in.co.rssoftware.bbps.schema.ServiceReasonList;

@Service
public class ComplaintserviceReasonsServiceImpl implements ComplaintserviceReasonsService{

	@Autowired
	ComplaintserviceReasonsDao complaintserviceReasonsDao;
	
	@Override
	public List<ComplaintserviceReasonsView> fetchServiceReasonsByAgent() {
		List<ComplaintserviceReasons> crsList=complaintserviceReasonsDao.getServiceReasonsByType(ParticipationType.AGENT.name());
		List<ComplaintserviceReasonsView> csrListView=new ArrayList<ComplaintserviceReasonsView>();
		for(ComplaintserviceReasons csr:crsList){
			ComplaintserviceReasonsView csrView=mapToView(csr);
			csrListView.add(csrView);
		}
		return csrListView;
	}
	
	@Override
	public List<ComplaintserviceReasonsView> fetchServiceReasonsByBiller() {
		List<ComplaintserviceReasons> crsList=complaintserviceReasonsDao.getServiceReasonsByType(ParticipationType.BILLER.name());
		List<ComplaintserviceReasonsView> csrListView=new ArrayList<ComplaintserviceReasonsView>();
		for(ComplaintserviceReasons csr:crsList){
			ComplaintserviceReasonsView csrView=mapToView(csr);
			csrListView.add(csrView);
		}
		return csrListView;
	}
	
	@Override
	public List<ComplaintserviceReasonsView> fetchServiceReasonsBySystem() {
		List<ComplaintserviceReasons> crsList=complaintserviceReasonsDao.getServiceReasonsByType(ParticipationType.SYSTEM.name());
		List<ComplaintserviceReasonsView> csrListView=new ArrayList<ComplaintserviceReasonsView>();
		for(ComplaintserviceReasons csr:crsList){
			ComplaintserviceReasonsView csrView=mapToView(csr);
			csrListView.add(csrView);
		}
		return csrListView;
	}
	
	private ComplaintserviceReasonsView mapToView(ComplaintserviceReasons csr) {
		ComplaintserviceReasonsView csrView=new ComplaintserviceReasonsView();
		csrView.setReasonId(csr.getReasonId());
		csrView.setReasonType(csr.getReasonType());
		csrView.setReasonCode(csr.getReasonCode());
		csrView.setReasonName(csr.getReasonName());	
		return csrView;
	}
	
	public static  ServiceReasonList getServiceReasonJaxb(List<ComplaintserviceReasonsView> csr) {
		ServiceReasonList serviceReasonList = null;
		if (csr != null) {
			serviceReasonList = new ServiceReasonList();

			for (ComplaintserviceReasonsView aid : csr) {
				serviceReasonList.getServiceReasons().add(mapToJaxb(aid));
			}

		}
		
		return serviceReasonList;
	}
	
public static ServiceReasonDetail mapToJaxb(ComplaintserviceReasonsView csr) {
		
	ServiceReasonDetail reasonDetail= null;
		if(csr!=null)
		{
			reasonDetail=new ServiceReasonDetail();
			reasonDetail.setId(csr.getReasonId());
			reasonDetail.setName(csr.getReasonName());
			reasonDetail.setReasonCode(csr.getReasonCode());
			reasonDetail.setReasonType(csr.getReasonType());
		
		}
		return reasonDetail;
	}

}
