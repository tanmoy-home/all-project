package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.ComplaintDisposition;
import com.rssoftware.ou.model.tenant.ComplaintDispositionView;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;
import com.rssoftware.ou.tenant.dao.ComplaintDispositionDao;
import com.rssoftware.ou.tenant.service.ComplaintDispositionService;

import in.co.rssoftware.bbps.schema.DispositionDetail;
import in.co.rssoftware.bbps.schema.DispositionList;
import in.co.rssoftware.bbps.schema.ServiceReasonDetail;
import in.co.rssoftware.bbps.schema.ServiceReasonList;

@Service
public class ComplaintDispositionServiceImpl implements ComplaintDispositionService{

	@Autowired
	ComplaintDispositionDao complaintDispositionDao;
	
	@Override
	public List<ComplaintDisposition> getDispositionsList() {
		List<ComplaintDisposition> dspList=complaintDispositionDao.getAll();
		return dspList;
	}
	
	
	@Override
	public List<ComplaintDispositionView> fetchALLDisposition() {
		List<ComplaintDisposition> dspList=complaintDispositionDao.getAll();
		List<ComplaintDispositionView> dspviewlist=mapToView(dspList);
		return dspviewlist;
	}
	
	
	private List<ComplaintDispositionView> mapToView(List<ComplaintDisposition> dspList) {
		List<ComplaintDispositionView> dspviewlist=new ArrayList<ComplaintDispositionView>();
		for(ComplaintDisposition dsp:dspList){
			ComplaintDispositionView dpsView=new ComplaintDispositionView();
			dpsView.setDispositionId(dsp.getDispositionId());
			dpsView.setDispositionName(dsp.getDispositionName());
			dspviewlist.add(dpsView);
		}
		return dspviewlist;
	}
	
	public static  DispositionList getDispositionJaxb(List<ComplaintDisposition> dis) {
		DispositionList dispositionList = null;
		if (dis != null) {
			dispositionList = new DispositionList();

			for (ComplaintDisposition aid : dis) {
				dispositionList.getDispositions().add(mapToJaxb(aid));
			}

		}
		
		return dispositionList;
	}
	
public static DispositionDetail mapToJaxb(ComplaintDisposition dis) {
		
	DispositionDetail detail= null;
		if(dis!=null)
		{
			detail=new DispositionDetail();
			detail.setId(dis.getDispositionId());
			detail.setName(dis.getDispositionName());
			
		
		}
		return detail;
	}

}
