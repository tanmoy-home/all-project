package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.database.entity.tenant.InterchangeFee;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.tenant.dao.InterchangeFeeDao;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;

@Service
public class InterchangeFeeServiceImpl implements InterchangeFeeService {

	@Autowired 
	private InterchangeFeeDao interchangeFeeDao;
	
	public void setInterchangeFeeDao(InterchangeFeeDao interchangeFeeDao) {
		this.interchangeFeeDao = interchangeFeeDao;
	}
	
	@Override
	public List<InterchangeFeeView> fetchAll() {
		List<InterchangeFeeView> iFeeViewList = new ArrayList<>();
		for(InterchangeFee interchangeFee : interchangeFeeDao.getAll()) {
			iFeeViewList.add(mapFrom(interchangeFee));
		}
		return iFeeViewList;
	}

	@Override
	public InterchangeFeeView fetch(String interchangeFeeId) {
		return mapFrom(interchangeFeeDao.get(interchangeFeeId));
	}

	@Override
	public void delete(InterchangeFeeView serviceTaxConfView) {
		interchangeFeeDao.delete(mapTo(serviceTaxConfView));
	}
		
	@Override
	public int deleteAllInterchangeFeeByBillerId(String billerId) {
		return interchangeFeeDao.deleteAllInterchangeFeeByBillerId(billerId);
	}
	
	@Override
	public void save(InterchangeFeeView interchangeFeeView) {
		InterchangeFee interchangeFee = mapTo(interchangeFeeView);
		interchangeFeeDao.createOrUpdate(interchangeFee);
	}
	
	@Override
	public List<InterchangeFeeView> fetchAllInterchangeFeeByBillerId(String blrId) {
		List<InterchangeFeeView> iFeeViewList = new ArrayList<>();
		for(InterchangeFee interchangeFee : interchangeFeeDao.fetchAllInterchangeFeeByBillerId(blrId)) {
			iFeeViewList.add(mapFrom(interchangeFee));
		}
		return iFeeViewList;
	}
	
	@Override
	public InterchangeFeeView fetchByBillerIdFeeCode(String billerId,String feeCode) {
		return mapFrom(interchangeFeeDao.getbyBilleridFeeCode(billerId,feeCode));
	}
	
	
	private InterchangeFee mapTo(InterchangeFeeView interchangeFeeView) {
		InterchangeFee interchangeFee = new InterchangeFee();
		
		interchangeFee.setBillerId(interchangeFeeView.getBillerId());
		interchangeFee.setFeeCode(interchangeFeeView.getFeeCode());
		interchangeFee.setFeeDesc(interchangeFeeView.getFeeDesc());
		interchangeFee.setFeeDirection(interchangeFeeView.getFeeDirection().name());
		//interchangeFee.setFeeId(interchangeFeeView.getFeeId());
		interchangeFee.setTranAmtRangeMin(interchangeFeeView.getTranAmtRangeMin());
		interchangeFee.setTranAmtRangeMax(interchangeFeeView.getTranAmtRangeMax());
		interchangeFee.setFlatFee(interchangeFeeView.getFlatFee());
		interchangeFee.setPercentFee(interchangeFeeView.getPercentFee());
		interchangeFee.setEffctvFrom(interchangeFeeView.getEffctvFrom());
		interchangeFee.setEffctvTo(interchangeFeeView.getEffctvTo());
		if(interchangeFeeView.getFeeId()==0L){
			interchangeFee.setFeeId(UUID.randomUUID().getLeastSignificantBits());	
		}
		else{
			interchangeFee.setFeeId(interchangeFeeView.getFeeId());

		}
		
		
	return interchangeFee;
}
	
	private InterchangeFeeView mapFrom(InterchangeFee interchangeFee) {
		if (null == interchangeFee) {
			return null;
		}
		
		InterchangeFeeView interchangeFeeView = new InterchangeFeeView();
		interchangeFeeView.setBillerId(interchangeFee.getBillerId());
		interchangeFeeView.setFeeCode(interchangeFee.getFeeCode());
		interchangeFeeView.setFeeDesc(interchangeFee.getFeeDesc());
		interchangeFeeView.setFeeDirection(interchangeFee.getFeeDirection() != null ? InterchangeFeeDirectionType.valueOf(interchangeFee.getFeeDirection()) : null);
		interchangeFeeView.setFeeId(interchangeFee.getFeeId());
		interchangeFeeView.setTranAmtRangeMax(interchangeFee.getTranAmtRangeMax() != null ? interchangeFee.getTranAmtRangeMax() : Long.MAX_VALUE);
		interchangeFeeView.setTranAmtRangeMin(interchangeFee.getTranAmtRangeMin() != null ? interchangeFee.getTranAmtRangeMin() : Long.MIN_VALUE);
		interchangeFeeView.setFlatFee(interchangeFee.getFlatFee());
		interchangeFeeView.setPercentFee(interchangeFee.getPercentFee());
		interchangeFeeView.setEffctvFrom(interchangeFee.getEffctvFrom());
		interchangeFeeView.setEffctvTo(interchangeFee.getEffctvTo());
		return interchangeFeeView;
	
	}
}