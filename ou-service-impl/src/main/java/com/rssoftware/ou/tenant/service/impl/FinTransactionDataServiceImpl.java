package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.FinTransactionDetails;
import com.rssoftware.ou.tenant.dao.FinTransactionDataDao;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;

@Service
public class FinTransactionDataServiceImpl implements FinTransactionDataService {
	
	private final static Logger logger = LoggerFactory.getLogger(FinTransactionDataServiceImpl.class);

	@Autowired
	private FinTransactionDataDao finTransactionDataDao;

	@Autowired
	private IDGeneratorService idGenetarorService;

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public FinTransactionData get(String txnRefId) { 
		return finTransactionDataDao.get(txnRefId);

	}

	@Override
	public void createOrUpdate(FinTransactionData finData) {
		finTransactionDataDao.createOrUpdate(finData);

	}
	
	public String insert(FinTransactionDetails finTransactionDetails) throws ValidationException {
		try {
			String pk = finTransactionDataDao.create(mapFrom(finTransactionDetails));
			return pk;
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw ValidationException.getInstance(ValidationErrorReason.UNEXPECTED_ERROR);
		}
	}

	public String insert(FinTransactionDetails finTransactionDetails, String refId) throws ValidationException {
		try {
			String pk = finTransactionDataDao.create(mapFrom(finTransactionDetails, refId));
			return pk;
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw ValidationException.getInstance(ValidationErrorReason.UNEXPECTED_ERROR);
		}
	}

	private FinTransactionData mapFrom(FinTransactionDetails finTransactionDetails, String refId) throws IOException {
		FinTransactionData fintxnDetails = new FinTransactionData();
		fintxnDetails.setRefId(refId);
		fintxnDetails
				.setTxnRefId(idGenetarorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, refId.substring(0, 4)));
		fintxnDetails.setAuthCode(finTransactionDetails.getAuthCode());
		fintxnDetails.setClientRefId(finTransactionDetails.getClientTxnRefId());
		fintxnDetails.setCrtnTs(new Timestamp(System.currentTimeMillis()));
		fintxnDetails.setCurrentStatus(finTransactionDetails.getStatus());
		fintxnDetails.setSelectedPaymentOptions(finTransactionDetails.getPaymentAmountDetails());
		fintxnDetails.setResponseJson(finTransactionDetails.getResponseJson());
		fintxnDetails.setRequestJson(finTransactionDetails.getRequestJson());
		return fintxnDetails;
	}
	
	private FinTransactionData mapFrom(FinTransactionDetails finTransactionDetails) throws IOException {
		FinTransactionData fintxnDetails = new FinTransactionData();
		fintxnDetails.setRefId(finTransactionDetails.getRefId());
		fintxnDetails.setTxnRefId(finTransactionDetails.getTxnRefId());
		fintxnDetails.setAuthCode(finTransactionDetails.getAuthCode());
		fintxnDetails.setClientRefId(finTransactionDetails.getClientTxnRefId());
		fintxnDetails.setCrtnTs(new Timestamp(System.currentTimeMillis()));
		fintxnDetails.setCurrentStatus(finTransactionDetails.getStatus());
		fintxnDetails.setSelectedPaymentOptions(finTransactionDetails.getPaymentAmountDetails());
		fintxnDetails.setResponseJson(finTransactionDetails.getResponseJson());
		fintxnDetails.setRequestJson(finTransactionDetails.getRequestJson());
		fintxnDetails.setTotalAmount(BigDecimal.valueOf(Double.valueOf(finTransactionDetails.getTotalAmount())));
		return fintxnDetails;
	}
}
