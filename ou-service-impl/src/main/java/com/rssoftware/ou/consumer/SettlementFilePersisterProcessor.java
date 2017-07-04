package com.rssoftware.ou.consumer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.database.entity.tenant.RAWFile;
import com.rssoftware.ou.database.entity.tenant.RAWFileTransaction;
import com.rssoftware.ou.database.entity.tenant.SettlementFile;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.RawDataView.OUType;
import com.rssoftware.ou.model.tenant.SettlementFileView;
import com.rssoftware.ou.tenant.service.RawDataService;
import com.rssoftware.ou.tenant.service.SettlementFileService;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
public class SettlementFilePersisterProcessor implements Consumer<Event<SettlementFileView>>{

	
	private final static Logger logger = LoggerFactory.getLogger(SettlementFilePersisterProcessor.class);
	
	@Autowired
	private SettlementFileService settlementFileService;
	
	@Autowired
	private RawDataService rawDataService;
	
	@Autowired
	private EventBus eventBus; 
	
	@Override
	public void accept(Event<SettlementFileView> arg0) {
		SettlementFileView sFileView = arg0.getData();
		TransactionContext.putTenantId(sFileView.getTenantId());
		settlementFileService.insert(sFileView);
		
		if(SettlementFile.FileType.FINRAW == sFileView.getFileType()) {
			try {
				Unmarshaller unmarshaller = JAXBContext.newInstance(RAWFile.class).createUnmarshaller();
				RAWFile rawFile = (RAWFile)unmarshaller.unmarshal(new ByteArrayInputStream(arg0.getData().getFile()));
				rawFile.getTransactions().forEach( rawFileTransation -> {
					RawDataView rawDataView = mapFrom(rawFileTransation);
					
						try {
							rawDataService.insert(rawDataView);
						} catch (Exception e) {
							logger.error( e.getMessage(), e);
					        logger.info("In Excp : " + e.getMessage());
						}
						
					
				});
				
				RawDataView rawDataView = new RawDataView();
				rawDataView.setTenantId(sFileView.getTenantId());
				rawDataView.setSettlementCycleId(sFileView.getFileName());
				eventBus.notify(CommonConstants.RECONCILIATION_FILE_EVENT, Event.wrap(rawDataView));
			} 
				
			catch (JAXBException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());	
		       }
		}
	}
	
	private RawDataView mapFrom(RAWFileTransaction rawFileTransaction) {
		RawDataView rawData = new RawDataView();
		if((rawFileTransaction.getAgentId()==null && rawFileTransaction.getBillerOUCountMonth()!=null) || !rawFileTransaction.getMti().equals(RequestType.PAYMENT.name())) {
			rawData.setOuType(OUType.BOU);
		}
		else {
			rawData.setOuType(OUType.COU);
		}
		rawData.setAgentId(rawFileTransaction.getAgentId());
		rawData.setBillerCategory(rawFileTransaction.getBillerCategory());
		rawData.setBillerId(rawFileTransaction.getBillerId());
		rawData.setBillerOuCountMonth(
				rawFileTransaction.getBillerOUCountMonth() != null ? 
						new BigDecimal(rawFileTransaction.getBillerOUCountMonth().toString()) : BigDecimal.ZERO);
		rawData.setBillerOuId(rawFileTransaction.getBillerOUId());
		//rawData.setBillerOuInterchangeFee(rawFileTransaction.getBillerOUInterchangeFee());
		rawData.setBillerOuSwitchingFee(rawFileTransaction.getBillerOUSwitchingFee());
		rawData.setCasProcessed(
				rawFileTransaction.isCasProcessed() != null && rawFileTransaction.isCasProcessed().booleanValue() == true ?
						true : false);
		try {
			rawData.setClearingTimestamp(
					rawFileTransaction.getClearingTimestamp() != null ? 
							new Timestamp(
									new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(
											rawFileTransaction.getClearingTimestamp()).getTime()) : null);
		} catch (ParseException e1) {
			logger.error( e1.getMessage(), e1);
	         logger.info("In Excp : " + e1.getMessage());
	       }
		rawData.setCustomerConvenienceFee(
				rawFileTransaction.getCustomerConvenienceFee() !=  null ?
						new BigDecimal(rawFileTransaction.getCustomerConvenienceFee().toString()) : BigDecimal.ZERO);
		rawData.setCustomerMobileNumber(rawFileTransaction.getCustomerMobileNumber());
		rawData.setCustomerOuCountMonth(
				rawFileTransaction.getCustomerOUCountMonth() != null ? 
						new BigDecimal(rawFileTransaction.getCustomerOUCountMonth().toString()) : BigDecimal.ZERO);
		rawData.setCustomerOuId(rawFileTransaction.getCustomerOUId());
		//rawData.setCustomerOuInterchangeFee(rawFileTransaction.getCustomerOUInterchangeFee());
		rawData.setCustomerOuSwitchingFee(rawFileTransaction.getCustomerOUSwitchingFee());
		rawData.setDecline(
				rawFileTransaction.isDecline() != null && rawFileTransaction.isDecline().booleanValue() == true ?
						true : false);
		rawData.setMsgId(rawFileTransaction.getMsgId());
		rawData.setMti(rawFileTransaction.getMti());
		rawData.setPaymentChannel(rawFileTransaction.getPaymentChannel());
		rawData.setPaymentMode(rawFileTransaction.getPaymentMode());
		rawData.setResponseCode(rawFileTransaction.getResponseCode());
		rawData.setReversal(				
				rawFileTransaction.isReversal() != null && rawFileTransaction.isReversal().booleanValue() == true ?
						true : false);
		rawData.setSplitPay(
				rawFileTransaction.isSplitPay() != null && rawFileTransaction.isSplitPay().booleanValue() == true ?
						true : false);
		rawData.setSplitPayTxnAmount(
				rawFileTransaction.getSplitPayTxnAmount() != null ?
						new BigDecimal(rawFileTransaction.getSplitPayTxnAmount().toString()) : BigDecimal.ZERO);
		rawData.setSplitPaymentMode(rawFileTransaction.getSplitPaymentMode());
		rawData.setTxnAmount(
				rawFileTransaction.getTxnAmount() != null ?
						new BigDecimal(rawFileTransaction.getTxnAmount().toString()) : BigDecimal.ZERO);
		rawData.setTxnCurrencyCode(rawFileTransaction.getTxnCurrencyCode());
		try {
			rawData.setTxnDate(
					rawFileTransaction.getTxnDate() != null ?
							new SimpleDateFormat("yyyy-MM-dd").parse(rawFileTransaction.getTxnDate()) : null);
		} catch (ParseException e) {
			 logger.error( e.getMessage(), e);
	         logger.info("In Excp : " + e.getMessage());
		   }
		//Primary Key in Domain
		if(rawFileTransaction.getRefId()==null)
			rawData.setRefId(rawFileTransaction.getTxnReferenceId());
		else
			rawData.setRefId(rawFileTransaction.getRefId());
		
		rawData.setTxnType(
				rawFileTransaction.getTxnType() != null?
						RequestType.valueOf(rawFileTransaction.getTxnType()) :
							RequestType.PAYMENT);
		
		rawData.setReconStatus(RawDataView.ReconStatus.UNREAD);
		rawData.setSettlementCycleId(rawFileTransaction.getSettlementCycleId());
		rawData.setTxnReferenceId(rawFileTransaction.getTxnReferenceId());
		rawData.setServiceFee(rawFileTransaction.getServiceFee());
		rawData.setBillerFee(rawFileTransaction.getBillerFee());
		rawData.setCustomerConvenienceFeeTax(rawFileTransaction.getCustomerConvenienceFeeTax());
		rawData.setBillerFeeTax(rawFileTransaction.getBillerFeeTax());
		rawData.setCustomerOUSwitchingFeeTax(rawFileTransaction.getCustomerOUSwitchingFeeTax());
		rawData.setServiceFeeDescription(rawFileTransaction.getServiceFeeDescription());
		rawData.setServiceFeeTax(rawFileTransaction.getServiceFeeTax());
		rawData.setBillerOUSwitchingFeeTax(rawFileTransaction.getBillerOUSwitchingFeeTax());
		return rawData;
	}

	public SettlementFileService getSettlementFileService() {
		return settlementFileService;
	} 

	public void setSettlementFileService(SettlementFileService settlementFileService) {
		this.settlementFileService = settlementFileService;
	}
}
