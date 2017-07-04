package com.rssoftware.ou.batch.writer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.SpltPayType;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.Recon;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.database.entity.tenant.RawData;
import com.rssoftware.ou.database.entity.tenant.RawDataPK;
import com.rssoftware.ou.database.entity.tenant.ReconDetails;
import com.rssoftware.ou.database.entity.tenant.ReconDetailsPK;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.model.tenant.Differences;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus;
import com.rssoftware.ou.tenant.dao.impl.BillerDaoImpl;
import com.rssoftware.ou.tenant.dao.impl.RawDataDaoImpl;
import com.rssoftware.ou.tenant.dao.impl.ReconDetailsDaoImpl;
import com.rssoftware.ou.tenant.dao.impl.TransactionDataDaoImpl;


public class CustomReconItemWriter implements ItemWriter<Recon>{
	private static Logger log = LoggerFactory.getLogger(CustomReconItemWriter.class);

	private String settlementId;
	private double reconMaxTry;
	/*private long matchedTxns;
	private long notInOuTxns;
	private long notInCuTxns;
	private long mismatchedTxns;
	private long pendingTxns;
	private long broughtForwardTxns;*/
	@Autowired
	ReconDetailsDaoImpl reconDetailsDao;
	@Autowired
	TransactionDataDaoImpl transactionDataDao;
	@Autowired
	RawDataDaoImpl rawDataDao;
	@Autowired
	BillerDaoImpl billerDao;
	
	private ExecutionContext executionContext;
	private static ObjectMapper objectMapper = new ObjectMapper();

	@BeforeStep
	public void beforeStep(StepExecution stepExecution)
	{
	    this.executionContext = stepExecution.getJobExecution().getExecutionContext();
	}
	private ReconDetails formReconDetailsFromTD(Recon item) {
		ReconDetails reconDetails = new ReconDetails();
		ReconDetailsPK reconDetailsPK = new ReconDetailsPK();
		reconDetailsPK.setRefId(item.getRefId());
		reconDetailsPK.setTxnType(item.getTxnType());
		reconDetailsPK.setReconId(settlementId);
		reconDetails.setId(reconDetailsPK);
		reconDetails.setTxnRefId(item.getTxnRefId());
		reconDetails.setAgentId(item.getAgentId());
		reconDetails.setBillerId(item.getBillerId());
		return reconDetails;
	}
	private ReconDetails formReconDetailsFromRD(Recon item) {
		ReconDetails reconDetails = new ReconDetails();
		ReconDetailsPK reconDetailsPK = new ReconDetailsPK();
		reconDetailsPK.setRefId(item.getRawRefId());
		reconDetailsPK.setTxnType(item.getRawTxnType());
		reconDetailsPK.setReconId(settlementId);
		reconDetails.setTxnRefId(item.getTxnReferenceId());
		reconDetails.setId(reconDetailsPK);
		reconDetails.setAgentId(item.getAgentId());
		reconDetails.setBillerId(item.getBillerId());
		return reconDetails;
	}
	
	private TransactionData formTransactionData(Recon item) {
		TransactionDataPK id = new TransactionDataPK();
		id.setRefId(item.getRefId());
		id.setTxnType(item.getTxnType());
		TransactionData transactionData = transactionDataDao.get(id);
		transactionData.setReconTs(new Timestamp(System.currentTimeMillis()));
		transactionData.setReconCycleNo(new BigDecimal(item.getReconCycleNo().intValue()+1));
		return transactionData;
	}
	
	private RawData formRawData(Recon item) {
		RawDataPK rawDataPK = new RawDataPK();
		rawDataPK.setRefId(item.getRawRefId());
		rawDataPK.setTxnType(item.getRawTxnType());
		RawData rawData = rawDataDao.get(rawDataPK);
		rawData.setReconTs(new Timestamp(System.currentTimeMillis()));
		return rawData;
	}
	private void process(Recon item) throws Exception {
		ReconDetails reconDetails = new ReconDetails();
		/*
		 * updating brought forward transactions for a recon cycle
		 */
		if (item.getRefId() != null && item.getReconStatus().equals(ReconStatus.PENDING.name())) {
			reconDetails = formReconDetailsFromTD(item);
			reconDetails.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.BROUGHT_FORWARD.name());
			reconDetailsDao.createOrUpdate(reconDetails);
			this.executionContext.putLong("broughtForwardTxns", this.executionContext.getLong("broughtForwardTxns", 0 ) + 1 );
			//broughtForwardTxns = this.executionContext.getLong("broughtForwardTxns");
		}
		
		/*
		 * updating recon_status for transaction_data not present in
		 * raw_data
		 */
		if (item.getRawRefId() == null) {
			
			if(item.getReconCycleNo()!=null && ((item.getReconCycleNo().doubleValue()-reconMaxTry)>=0)) {
				reconDetails = formReconDetailsFromTD(item);
				reconDetails.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NOT_IN_CU.name());
				reconDetailsDao.createOrUpdate(reconDetails);
				this.executionContext.putLong("notInCuTxns", this.executionContext.getLong("notInCuTxns", 0 ) + 1 );
				//notInCuTxns = this.executionContext.getLong("notInCuTxns");

				
				TransactionData transactionData = formTransactionData(item);
				transactionData.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.NO_MATCHES_FOUND.name());
				transactionDataDao.createOrUpdate(transactionData);
			}
			else {
				reconDetails = formReconDetailsFromTD(item);
				reconDetails.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.PENDING.name());
				reconDetailsDao.createOrUpdate(reconDetails);
				this.executionContext.putLong("pendingTxns", this.executionContext.getLong("pendingTxns", 0 ) + 1 );
				//pendingTxns = this.executionContext.getLong("pendingTxns");

				TransactionData transactionData = formTransactionData(item);
				transactionData.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.PENDING.name());
				transactionDataDao.createOrUpdate(transactionData);
			}
		}
		/*
		 * updating recon_status for raw_data not present in
		 * transaction_data
		 */
		else if (item.getRefId() == null) {
			reconDetails = formReconDetailsFromRD(item);
			reconDetails.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NOT_IN_OU.name());
			reconDetailsDao.createOrUpdate(reconDetails);
			this.executionContext.putLong("notInOuTxns", this.executionContext.getLong("notInOuTxns", 0 ) + 1 );
			//notInOuTxns = this.executionContext.getLong("notInOuTxns");
			//log.debug("JobCount: "+notInOuTxns);
			
			RawData rawdata = formRawData(item);
			rawdata.setReconStatus(ReconStatus.NO_MATCHES_FOUND.name());
			rawDataDao.createOrUpdate(rawdata);
		}
		/*
		 * updating recon_status for raw_data present in
		 * transaction_data
		 */
		else {
			
			Differences diff = RawDataView.compare(formRDOU(item), formRDCU(item));
			
			// update recon_status where non-matching fields exist
			if (diff != null) {
				this.executionContext.putLong("mismatchedTxns", this.executionContext.getLong("mismatchedTxns", 0 ) + 1 );
				//mismatchedTxns = this.executionContext.getLong("mismatchedTxns");
				RawData rawdata = formRawData(item);
				rawdata.setReconStatus(ReconStatus.NON_MATCHING_FIELDS.name());
				rawdata.setReconDescription(diff.toString());
				rawDataDao.createOrUpdate(rawdata);
				
				TransactionData transactionData = formTransactionData(item);
				transactionData.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.NON_MATCHING_FIELDS.name());
				transactionData.setReconDescription(diff.toString());
				transactionDataDao.createOrUpdate(transactionData);

				
				reconDetails = formReconDetailsFromRD(item);
				reconDetails.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NON_MATCHING_FIELDS.name());
				reconDetails.setReconDescription(diff.toString());
				reconDetailsDao.createOrUpdate(reconDetails);
			}
			
			// update recon_status where completely matches
			else {
				this.executionContext.putLong("matchedTxns", this.executionContext.getLong("matchedTxns", 0 ) + 1 );
				//matchedTxns = this.executionContext.getLong("matchedTxns");

				RawData rawdata = formRawData(item);
				rawdata.setReconStatus(ReconStatus.MATCHED.name());
				rawDataDao.createOrUpdate(rawdata);
				
				TransactionData transactionData = formTransactionData(item);
				transactionData.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.MATCHED.name());
				transactionDataDao.createOrUpdate(transactionData);
			}

		}
	}	

	private RawDataView formRDCU(Recon item) {
		if (item == null) {
			return null;
		}
		RawDataView rdv = new RawDataView();
		rdv.setTxnType(RequestType.valueOf(item.getRawTxnType()));
		rdv.setMsgId(item.getRawMsgId());
		rdv.setAgentId(item.getAgentId());
		rdv.setBillerCategory(item.getBillerCategory());
		rdv.setBillerId(item.getBillerId());
		rdv.setBillerOuCountMonth(item.getBillerOUCountMonth());
		rdv.setBillerOuId(item.getBillerOUId());
		//rdv.setBillerOuInterchangeFee(item.getBillerOUInterchangeFee());
		rdv.setBillerOuSwitchingFee(item.getBillerOUSwitchingFee());
		rdv.setCasProcessed(item.getCasProcessed());
		rdv.setCustomerConvenienceFee(item.getCustomerConvenienceFee());
		rdv.setCustomerMobileNumber(item.getCustomerMobileNUmber());
		rdv.setCustomerOuCountMonth(item.getCustomerOUCountMonth());
		rdv.setCustomerOuId(item.getCustomerOUId());
		//rdv.setCustomerOuInterchangeFee(item.getCustomerOUInterchangeFee());
		rdv.setCustomerOuSwitchingFee(item.getCustomerOUSwitchingFee());
		rdv.setDecline(item.getDecline());
		rdv.setMti(item.getMti());
		rdv.setPaymentChannel(item.getPaymentChannel());
		rdv.setPaymentMode(item.getPaymentMode());
		rdv.setResponseCode(item.getResponseCode());
		rdv.setReversal(item.getReversal());
		rdv.setSettlementCycleId(item.getSettlementCycleId());
		rdv.setSplitPay(item.getSplitPay());
		rdv.setSplitPayTxnAmount(item.getSplitPayTxnAmount());
		rdv.setSplitPaymentMode(item.getSplitPaymentMode());
		rdv.setTxnAmount(item.getTxnAmount());
		rdv.setTxnCurrencyCode(item.getTxnCurrencyCode());
		rdv.setTxnDate(item.getTxnDate());
		rdv.setTxnReferenceId(item.getTxnReferenceId());
		rdv.setRefId(item.getRawRefId());
		rdv.setBillerFee(new BigDecimal(item.getBillerFee()));
		rdv.setCustomerConvenienceFeeTax(item.getCustomerConvenienceFeeTax());
		rdv.setBillerFeeTax(item.getBillerFeeTax());
		rdv.setCustomerOUSwitchingFeeTax(item.getCustomerOUSwitchingFeeTax());
		rdv.setBillerOUSwitchingFeeTax(item.getBillerOUSwitchingFeeTax());
		return rdv;		
	}
	
	//Converting a TransactionData object to RawDataView object
	private RawDataView formRDOU(Recon item) {
		
			if (item == null) {
				return null;
			}
			RawDataView rdv = new RawDataView();
			rdv.setMsgId(item.getMsgId());
			rdv.setRefId(item.getRefId());
			rdv.setTxnReferenceId(item.getTxnRefId());
			if(item.getTxnType()!=null) {
				rdv.setTxnType(RequestType.valueOf(item.getTxnType()));
				rdv.setMti(RequestType.valueOf(item.getTxnType()).name());
			}
			rdv.setReversal(item.getCurrentStatus().equals(RequestStatus.RESPONSE_REVERSE.name()));
			rdv.setDecline(item.getCurrentStatus().equals(RequestStatus.RESPONSE_DECLINE.name()));

				if (item.getRequestJson() != null) {
					BillPaymentRequest billPaymentRequest = null;
					try {
						billPaymentRequest = objectMapper.readValue(item.getRequestJson(), BillPaymentRequest.class);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
					Date date = null;
					try {
						date = new Date(formatter.parse(billPaymentRequest.getTxn().getTs()).getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					rdv.setTxnDate(date);
					rdv.setCustomerOuId(billPaymentRequest.getHead().getOrigInst());
					rdv.setBillerOuId(billPaymentRequest.getHead().getOrigInst());
					rdv.setAgentId(billPaymentRequest.getAgent().getId());
					rdv.setTxnCurrencyCode(billPaymentRequest.getAmount().getAmt().getCurrency());
					rdv.setBillerId(billPaymentRequest.getBillDetails().getBiller().getId());
					rdv.setBillerCategory(billerDao.get(rdv.getBillerId()).getBlrCategoryName());
					rdv.setCustomerMobileNumber(billPaymentRequest.getCustomer().getMobile());
					rdv.setPaymentMode(billPaymentRequest.getPaymentMethod().getPaymentMode());
					rdv.setSplitPay(billPaymentRequest.getPaymentMethod().getSplitPay()==SpltPayType.YES?true:false);

				}
				if (item.getResponseJson() != null) {
					BillPaymentResponse billPaymentResponse = null;
					try {
						billPaymentResponse = objectMapper .readValue(item.getResponseJson(), BillPaymentResponse.class);
					} catch (IOException e) {
						e.printStackTrace();
					}
					rdv.setResponseCode(billPaymentResponse.getReason().getResponseCode());
					rdv.setCustomerConvenienceFee(new BigDecimal(billPaymentResponse.getBillerResponse().getCustConvFee()));
					rdv.setTxnAmount(new BigDecimal(billPaymentResponse.getBillerResponse().getAmount()));
				}
			
			/*
			 * could not find the mapping in transaction_data for these fields in raw_data
			 */
			// rdv.setBillerOuCountMonth(rd.getBillerOuCountMonth());
			// rdv.setBillerOuSwitchingFee(rd.getBillerOuSwitchingFee());
			// rdv.setCasProcessed(rd.getCasProcessed());
			// rdv.setClearingTimestamp(rd.getClearingTimestamp());
			// rdv.setCustomerOuCountMonth(rd.getCustomerOuCountMonth());
			// rdv.setCustomerOuSwitchingFee(rd.getCustomerOuSwitchingFee());
			// rdv.setPaymentChannel(rd.getPaymentChannel());
			// rdv.setSettlementCycleId(rd.getSettlementCycleId());
			// rdv.setSplitPayTxnAmount(rd.getSplitPayTxnAmount());
			// rdv.setSplitPaymentChannel(rd.getSplitPaymentChannel());
			//customerConvenienceFeeTax
			//billerFeeTax
			//customerOUSwitchingFeeTax
			return rdv;
	}
		
	public String getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(String settlementId) {
		this.settlementId = settlementId;
	}
	
	public double getReconMaxTry() {
		return reconMaxTry;
	}
	public void setReconMaxTry(double reconMaxTry) {
		this.reconMaxTry = reconMaxTry;
	}
	@Override
	public void write(List<? extends Recon> items) throws Exception {
		if(items!=null){
				for(Recon data: items)
				{
					process(data);

				}
		}
	}
	
}