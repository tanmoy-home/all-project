package com.rssoftware.ou.tenant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.TxnReportResp;
import com.rssoftware.ou.database.entity.tenant.TxnReport;
import com.rssoftware.ou.tenant.dao.TxnReportDao;
import com.rssoftware.ou.tenant.service.TxnReportService;

@Service
public class TxnReportServiceImpl implements TxnReportService {

	private final static Logger logger = LoggerFactory
			.getLogger(TxnReportServiceImpl.class);

	@Autowired
	TxnReportDao txnReportDao;

	@Override
	public TxnReportResp getTxnReport(String selectedDate) {
		TxnReport txnReport = txnReportDao.getTxnReport(selectedDate);
		return mapToJaxb(txnReport);
	}

	private TxnReportResp mapToJaxb(TxnReport txnReport) {
		TxnReportResp txnReportResp = new TxnReportResp();
		
		if(txnReport!=null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Txn Report Fetched Id: " +txnReport.getReportId());
			}
			txnReportResp.setBbpouName(txnReport.getBbpouName());
			txnReportResp.setNoOfAgentOutlets(txnReport.getNoOfAgentOutlets());
			txnReportResp.setOnUsTxnCount(txnReport.getOnUsTxnCount());
			txnReportResp.setOffUsTxnCount(txnReport.getOffUsTxnCount());
			txnReportResp.setOnUsTxnTot(txnReport.getOnUsTxnTot());
			txnReportResp.setOffUsTxnTot(txnReport.getOffUsTxnTot());
			txnReportResp.setOnUsFailedTxnCount(txnReport.getOnUsFailedTxnCount());
			txnReportResp.setOffUsFailedTxnCount(txnReport.getOffUsFailedTxnCount());
			txnReportResp.setOnUsFailedTxnTot(txnReport.getOnUsFailedTxnTot());
			txnReportResp.setOffUsFailedTxnTot(txnReport.getOffUsFailedTxnTot());
			txnReportResp.setFailureReason(txnReport.getFailureReason()==null?"":txnReport.getFailureReason());
			txnReportResp.setCashPaymentCount(txnReport.getCashPaymentCount());
			txnReportResp.setDCCCPaymentCount(txnReport.getDCCCPaymentCount());
			txnReportResp.setNetBankingPaymentCount(txnReport.getNetBankingPaymentCount());
			txnReportResp.setIMPSPaymentCount(txnReport.getIMPSPaymentCount());
			txnReportResp.setPPIsPaymentCount(txnReport.getPPIsPaymentCount());
			txnReportResp.setOtherPaymentCount(txnReport.getOtherPaymentCount());
		}
		
		return txnReportResp;	
	}

}
