package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.sql.Timestamp;

import in.co.rssoftware.bbps.schema.BillerFetchByParameter;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.BillerFetchRequestResponse;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.BillerFetchRequestResponseDao;
import com.rssoftware.ou.tenant.service.BillerFetchRequestResponseService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;

@Service
public class BillerFetchRequestResponseServiceImpl implements BillerFetchRequestResponseService{

	@Autowired
	BillerFetchRequestResponseDao billerFetchRequestResponseDao;
	
	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	private IDGeneratorService idGeneratorService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void save(BillerFetchRequestResponse billerFetchRequestResponse) {
		billerFetchRequestResponseDao.createOrUpdate(billerFetchRequestResponse);	
	}

	@Override
	public BillerFetchRequestResponse fetchByRefId(String refId) {
		BillerFetchRequestResponse rec=billerFetchRequestResponseDao.get(refId);
		return rec;
	}
	
	
	@Override
	public BillerFetchRequestResponse saveFetchRequest(BillerFetchByParameter billerFetchByParameter,String ouNmae) {
		BillerFetchRequestResponse req=new BillerFetchRequestResponse();
		//String tenantId=TransactionContext.getTenantId();
		//TenantDetail td = tenantDetailService.fetchByTenantId(tenantId);
		/*
		 * head.setOrigInst(tenantDetailService.getOUName(TransactionContext.getTenantId()));
		head.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, fetchRequest.getOrigInst()));
		
		 */
		//req.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName()));
		//req.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, tenantDetailService.getOUName(TransactionContext.getTenantId())));
		req.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID,ouNmae));
		if (billerFetchByParameter.getHrColonMins()!=null)
			req.setScheduleTime(billerFetchByParameter.getHrColonMins());
		if (billerFetchByParameter.getMyBiller()!=null)
			req.setMyBiller(billerFetchByParameter.getMyBiller().name());
		if(billerFetchByParameter.getBillerIds()!=null){
			String billerIds=StringUtils.join(billerFetchByParameter.getBillerIds(), ',');
			req.setBillerId(billerIds);
		}	
		if(billerFetchByParameter.getDateRange()!=null)
			req.setDateRange(billerFetchByParameter.getDateRange());
		req.setRequestTime(new Timestamp(System.currentTimeMillis()));
		try {
			req.setRequestJson(objectMapper.writeValueAsBytes(billerFetchByParameter));
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		save(req);
		return(req);
	}


}
