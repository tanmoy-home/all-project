package com.rssoftware.ou.batch.writer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.TxnReport;
import com.rssoftware.ou.tenant.dao.impl.TxnReportDaoImpl;

public class TxnReportDBItemWriter implements ItemWriter<TxnReport> {
	private static Logger log = LoggerFactory.getLogger(TxnReportDBItemWriter.class);

	@Autowired
	TxnReportDaoImpl txnReportDao;

	@Override
	public void write(List<? extends TxnReport> items) throws Exception {
		if (items != null) {
			for (TxnReport data : items) {
				com.rssoftware.ou.database.entity.tenant.TxnReport txnReport = new com.rssoftware.ou.database.entity.tenant.TxnReport();
				txnReport.setBbpouName(data.getBbpouName());
				txnReport.setNoOfAgentOutlets(data.getNoOfAgentOutlets());
				txnReport.setOnUsTxnCount(data.getOnUsTxnCount()!=null?data.getOnUsTxnCount():BigDecimal.ZERO);
				txnReport.setOffUsTxnCount(data.getOffUsTxnCount()!=null?data.getOffUsTxnCount():BigDecimal.ZERO);
				txnReport.setOnUsTxnTot(data.getOnUsTxnTot()!=null?data.getOnUsTxnTot():BigDecimal.ZERO);
				txnReport.setOffUsTxnTot(data.getOffUsTxnTot()!=null?data.getOffUsTxnTot():BigDecimal.ZERO);
				txnReport.setOnUsFailedTxnCount(data.getOnUsFailedTxnCount()!=null?data.getOnUsFailedTxnCount():BigDecimal.ZERO);
				txnReport.setOffUsFailedTxnCount(data.getOffUsFailedTxnCount()!=null?data.getOffUsFailedTxnCount():BigDecimal.ZERO);
				txnReport.setOnUsFailedTxnTot(data.getOnUsFailedTxnTot()!=null?data.getOnUsFailedTxnTot():BigDecimal.ZERO);
				txnReport.setOffUsFailedTxnTot(data.getOffUsFailedTxnTot()!=null?data.getOffUsFailedTxnTot():BigDecimal.ZERO);
				txnReport.setFailureReason(data.getFailureReason());
				txnReport.setCashPaymentCount(data.getCashPaymentCount()!=null?data.getCashPaymentCount():BigDecimal.ZERO);
				txnReport.setDCCCPaymentCount(data.getDCCCPaymentCount()!=null?data.getDCCCPaymentCount():BigDecimal.ZERO);
				txnReport.setNetBankingPaymentCount(data.getNetBankingPaymentCount()!=null?data.getNetBankingPaymentCount():BigDecimal.ZERO);
				txnReport.setIMPSPaymentCount(data.getIMPSPaymentCount()!=null?data.getIMPSPaymentCount():BigDecimal.ZERO);
				txnReport.setPPIsPaymentCount(data.getPPIsPaymentCount()!=null?data.getPPIsPaymentCount():BigDecimal.ZERO);
				txnReport.setOtherPaymentCount(data.getOtherPaymentCount()!=null?data.getOtherPaymentCount():BigDecimal.ZERO);
				txnReport.setCrtnTs(new Timestamp(System.currentTimeMillis()));
				txnReportDao.createOrUpdate(txnReport);
				log.debug("Inserted into txn_report table!");
			}
		}
	}

}