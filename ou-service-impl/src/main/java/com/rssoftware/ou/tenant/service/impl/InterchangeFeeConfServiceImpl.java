package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.MTI;
import com.rssoftware.ou.database.entity.tenant.InterchangeFeeConf;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.tenant.dao.InterchangeFeeConfDao;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;

@Service
public class InterchangeFeeConfServiceImpl implements InterchangeFeeConfService {

	@Autowired 
	private InterchangeFeeConfDao interchangeFeeConfDao;
	
	public void setInterchangeFeeConfDao(InterchangeFeeConfDao interchangeFeeConfDao) {
		this.interchangeFeeConfDao = interchangeFeeConfDao;
	}

	@Override
	public List<InterchangeFeeConfView> fetchAll() {
		List<InterchangeFeeConfView> iFeeConfViewList = new ArrayList<>();
		for(InterchangeFeeConf iFeeConf : interchangeFeeConfDao.getAll()) {
			iFeeConfViewList.add(mapFrom(iFeeConf));
		}
		return iFeeConfViewList;
	}

	@Override
	public InterchangeFeeConfView fetch(String interchangeFeeConfId) {
		return mapFrom(interchangeFeeConfDao.get(interchangeFeeConfId));
	}

	@Override
	public void delete(InterchangeFeeConfView interchangeFeeConfView) {
		interchangeFeeConfDao.delete(mapTo(interchangeFeeConfView));
	}	
	
	@Override
	public int deleteAllInterchangeFeeConfByBillerId(String billerId) {
		return interchangeFeeConfDao.deleteAllInterchangeFeeConfByBillerId(billerId);
	}

	@Override
	public void save(InterchangeFeeConfView interchangeFeeConfView) {
		InterchangeFeeConf interchangeFeeConf = mapTo(interchangeFeeConfView);
		interchangeFeeConfDao.createOrUpdate(interchangeFeeConf);
	}	
	
	@Override
	public List<InterchangeFeeConfView> fetchAllInterchangeFeeConfByBillerId(String blrId) {
		List<InterchangeFeeConfView> iFeeConfViewList = new ArrayList<>();
		for(InterchangeFeeConf iFeeConf : interchangeFeeConfDao.fetchAllInterchangeFeeConfByBillerId(blrId)) {
			iFeeConfViewList.add(mapFrom(iFeeConf));
		}
		return iFeeConfViewList;
	}
	
	private InterchangeFeeConf mapTo(InterchangeFeeConfView interchangeFeeConfView) {
			InterchangeFeeConf interchangeFeeConf = new InterchangeFeeConf();
			
			interchangeFeeConf.setFeeCfgId(UUID.randomUUID().getLeastSignificantBits());
			interchangeFeeConf.setBlrId(interchangeFeeConfView.getBlrId());
			interchangeFeeConf.setMti(interchangeFeeConfView.getMti() != null ? interchangeFeeConfView.getMti().getExpandedForm() : null);
			interchangeFeeConf.setPaymentMode(interchangeFeeConfView.getPaymentMode() != null ? interchangeFeeConfView.getPaymentMode().getExpandedForm() : null);
			interchangeFeeConf.setPaymentChannel(interchangeFeeConfView.getPaymentChannel() != null ? interchangeFeeConfView.getPaymentChannel().getExpandedForm() : null);
			interchangeFeeConf.setResponseCode(interchangeFeeConfView.getResponseCode());
			interchangeFeeConf.setFees(StringUtils.join(interchangeFeeConfView.getFees(),','));
			interchangeFeeConf.setEffectiveFrom(interchangeFeeConfView.getEffectiveFrom());
			interchangeFeeConf.setEffectiveTo(interchangeFeeConfView.getEffectiveTo());
			return interchangeFeeConf;
	}
	
	private InterchangeFeeConfView mapFrom(InterchangeFeeConf interchangeFeeConf) {
		if (null == interchangeFeeConf) {
			return null;
		}
		
		InterchangeFeeConfView interchangeFeeConfView = new InterchangeFeeConfView();
		interchangeFeeConfView.setFeeCfgId(interchangeFeeConf.getFeeCfgId());
		interchangeFeeConfView.setBlrId(interchangeFeeConf.getBlrId());
		interchangeFeeConf.setMti(interchangeFeeConfView.getMti() != null ? interchangeFeeConfView.getMti().getExpandedForm() : null);
		interchangeFeeConf.setPaymentMode(interchangeFeeConfView.getPaymentMode() != null ? interchangeFeeConfView.getPaymentMode().getExpandedForm() : null);
		interchangeFeeConf.setPaymentChannel(interchangeFeeConfView.getPaymentChannel() != null ? interchangeFeeConfView.getPaymentChannel().getExpandedForm() : null);
		interchangeFeeConf.setResponseCode(interchangeFeeConfView.getResponseCode());
		interchangeFeeConfView.setFees(Arrays.asList(interchangeFeeConf.getFees().split(",")));
		interchangeFeeConfView.setEffectiveFrom(interchangeFeeConf.getEffectiveFrom());
		interchangeFeeConfView.setEffectiveTo(interchangeFeeConf.getEffectiveTo());
		return interchangeFeeConfView;	
	}
}