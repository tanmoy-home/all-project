package com.rssoftware.ou.cbsiso.service.impl;

import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.bus.Event;

import com.rssoftware.ou.cbsiso.service.CbsIsoService;
import com.rssoftware.ou.cbsiso.service.ISOMessageService;
import com.rssoftware.ou.common.ComponentNames;
import com.rssoftware.ou.common.ErrorCode;
//import com.rssoftware.upiint.common.EventBus;
import com.rssoftware.ou.common.MessageType;
import com.rssoftware.ou.common.ServiceRequestContext;
import com.rssoftware.ou.common.exception.ChecksumException;
import com.rssoftware.ou.common.exception.DuplicateRequestException;
import com.rssoftware.ou.common.exception.ValidationException;
//import com.rssoftware.upiint.common.message.ComponentMessage;
import com.rssoftware.ou.model.cbs.ProcessingMessage;
//import com.rssoftware.upiint.common.service.InMemorySchedulerService;
//import com.rssoftware.upiint.db.CBSGatewayLog;
//import com.rssoftware.upiint.gateway.service.CBSGatewayLogService;
//import com.rssoftware.upiint.gateway.service.KeyService;
//import com.rssoftware.upiint.gateway.service.PSPAcProviderService;
//import com.rssoftware.upiint.gateway.service.TransactionComponentService;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;
import com.rssoftware.ou.model.cbs.TxnStatus;
import com.rssoftware.ou.model.cbs.TxnType;
import com.rssoftware.ou.schema.cbs.CBSISOConstants;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.helper.CBSISOHelper;

public class CbsIsoServiceImpl implements CbsIsoService {
//	@Autowired
//	private TransactionComponentService updateService;

	@Autowired
	private ISOMessageService isoMessageService;

//	@Autowired
//	private PSPAcProviderService pspAcProviderService;

//	@Autowired
//	private KeyService keyService;

//	@Autowired
//	CBSGatewayLogService cbsGatewayLogService;
	
	private boolean isSimulated = false; // make this false

	protected final Logger log = LoggerFactory.getLogger(getClass());

//	@Autowired
//	InMemorySchedulerService inMemorySchedulerService;

//	@Autowired
//	private EventBus eventBus;

	@Override
	public byte[] mapToMessage(ProcessingMessage msg) {
		String METHOD_NAME = "mapToMessage";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		byte[] isoMsg = null;
		try {
			Map<Integer, Object> origRequest =  null;
			String mti = "";
			CBSRequest req = (CBSRequest) msg.getPayload();
			if(TxnType.BALANCE_ENQ.equals(req.getTxnType())){
				req.setProcessingCode(CBSISOConstants.ISO_BAL_ENQ_PRCSNG_CODE);
				mti = "1200";
			}
			if(TxnType.DEBIT.equals(req.getTxnType())){
				req.setProcessingCode(CBSISOConstants.ISO_DEBIT_PRCSNG_CODE);
				mti = "1200";
			}
			else if( TxnType.CREDIT.equals(req.getTxnType())){
				req.setProcessingCode(CBSISOConstants.ISO_CREDIT_PRCSNG_CODE);
				mti = "1200";
			} 
			else if( TxnType.CREDIT_REVERSAL.equals(req.getTxnType()) ){
//				CBSGatewayLog log = null;
//				log = cbsGatewayLogService.fetchCbsRequestGatewayLog(req.getOriginalTxnId(), TxnType.CREDIT.getValue());
//				origRequest =(Map)CBSISOHelper.convertStringToCBSObj(log.getRequestMsg());
				req.setProcessingCode(CBSISOConstants.ISO_CREDIT_PRCSNG_CODE);
				mti = "1420";
			} 
			else if( TxnType.DEBIT_REVERSAL.equals(req.getTxnType())){
//				CBSGatewayLog log = null;
//				log = cbsGatewayLogService.fetchCbsRequestGatewayLog(req.getOriginalTxnId(), TxnType.DEBIT.getValue());
//				origRequest =(Map)CBSISOHelper.convertStringToCBSObj(log.getRequestMsg());
				req.setProcessingCode(CBSISOConstants.ISO_DEBIT_PRCSNG_CODE);
				mti = "1420";
			}
			else if( CBSISOConstants.ISO_SOL_PRCSNG_CODE.equals(req.getProcessingCode())){
				mti = "1200";
			}
			//mti = "0200";
			
			Map<Integer, Object> isoMap = CBSISOHelper
					.populateMapForISO8583(req,origRequest);

//			if(TxnType.BALANCE_ENQ.equals(req.getTxnType())){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(),
//						TxnType.BALANCE_ENQ.getValue(),
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			}
//			if(TxnType.DEBIT.equals(req.getTxnType())){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(), TxnType.DEBIT.getValue(),
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			}
//			else if( TxnType.CREDIT.equals(req.getTxnType())){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(), TxnType.CREDIT.getValue(),
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			} 
//			else if( TxnType.CREDIT_REVERSAL.equals(req.getTxnType()) ){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(), TxnType.CREDIT_REVERSAL.getValue(),
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			} 
//			else if( TxnType.DEBIT_REVERSAL.equals(req.getTxnType())){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(), TxnType.DEBIT_REVERSAL.getValue(),
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			}
//			else if( CBSISOConstants.ISO_SOL_PRCSNG_CODE.equals(req.getProcessingCode())){
//				cbsGatewayLogService.createRequestLog(req.getRequestId(),req.getTxnRefId(), "SOL_PROCESSING",
//						CBSISOHelper.convertCBSObjToString(isoMap) );
//			}

			try {
				isoMsg = isoMessageService.packISOMsg(mti, isoMap);
				//isoMsg = DatatypeConverter.parseHexBinary("31323030b0b081014000800000000000060000283430303030303030303030303030303030303031303031303030303031303030303030303135323831353230303930373231313734353338323030393037323132303030313030355241564920494e52333849424b4c202020202020203030303431202020303431313033303030303030313235202020203430202049424b4c202020202020203030303431202020303431313034303030303236343937202020203030334257593035306674463720202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020");
				//msg.setSourceCd(ComponentNames.IMPSADAPTER);
				if (log.isDebugEnabled()) {
					log.debug("ISO Map: " + isoMap + "\nISO Message: " + DatatypeConverter.printHexBinary(isoMsg));
				}
				
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
		        log.info("In Excp : " + ex.getMessage());
				/*processFailure(msg, ErrorCode.IMPS_PROCESSING_FAILED_IN_UPI,
						null);*/
				return null;
			}

		} catch (Exception ex) {
			log.error(METHOD_NAME, ex);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}
		return isoMsg;
	}

	@Override
	public void mapFromMessage(byte[] msg) {

		String METHOD_NAME = "mapFromMessage";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		try {
			ServiceRequestContext.initialize();

			String hexString = DatatypeConverter.printHexBinary(msg);// TODO
																		// Kept
																		// just
																		// for
																		// debugging

			if (CommonUtils.hasValue(hexString) && hexString.length() <= 4) {
				log.error("IMPS mapFromMessage error, appears to be the socket connection is down");
				return;
			}

			Map<Integer, ? extends Object> isoMap = isoMessageService
					.unpackISOMsg(msg);
			
			log.debug("IMPS response hex message : " + hexString);
			log.debug("IMPS response map : " + isoMap.toString());

			CBSResponse response;
			try {
				response = CBSISOHelper
						.populateResponseFromMessageMap(isoMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response = new CBSResponse("", e.getMessage(), TxnStatus.FAILURE);
			}
			
			response.setIsoMap((Map<Integer, Object>)isoMap);
			
			/*ComponentMessage pmsg = new ComponentMessage();
			pmsg.setMetaApi(true);
			//pmsg.setSourceCd(ComponentNames.IMPSADAPTER);
			pmsg.setRequestMessageType(MessageType.CBS_RESPONSE);
			pmsg.setUpiTransactionId(response.getTxnRefId());
			pmsg.setPayload(response);
			eventBus.notify(ComponentNames.CBS_ADAPTER, Event.wrap(pmsg));*/
		} catch (Exception ex) {
			log.error(METHOD_NAME, ex);
			
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}

	}

	@Override
	public void processTimeout(ProcessingMessage msg) {

		String METHOD_NAME = "processTimeout";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		/*
		 * msg = new ProcessingMessage(msg); if (isSimulated) return; try { if
		 * (msg != null && msg.getTransactionId() != null && msg.getNao() !=
		 * null && msg.getNao().getType() != null){ Transaction txn =
		 * updateService.fetchTransaction(msg.getTransactionId()); if
		 * (msg.getNao().getType() == NextActionObjectType.PAYEE){
		 * 
		 * int payeeIndex = msg.getNao().getPayeeIndex(); if (payeeIndex >= 0 &&
		 * txn.getDbTransaction().getPayees() != null &&
		 * txn.getDbTransaction().getPayees().getPayees() != null &&
		 * txn.getDbTransaction().getPayees().getPayees().size() > payeeIndex &&
		 * txn.getDbTransaction().getPayees().getPayees().get(payeeIndex) !=
		 * null){ DBTransactionPayeeType payee =
		 * txn.getDbTransaction().getPayees().getPayees().get(payeeIndex);
		 * 
		 * //Check if max retries have been exceeded if
		 * (payee.getIMPSRequest()!=null &&
		 * payee.getIMPSRequest().getVerificationSequences()!=null &&
		 * (payee.getIMPSRequest().getResponseApprovalNumber()==null &&
		 * payee.getIMPSRequest().getResponseCode()==null)){ int retriesDone =
		 * payee.getIMPSRequest().getVerificationSequences().size(); if
		 * (retriesDone
		 * >=GatewayConfiguration.getInstance().getCreditAdviceRetryLimit()){
		 * 
		 * if (payee.getIMPSRequest()!=null &&
		 * CommonUtils.hasValue(payee.getIMPSRequest().getOriginalRRN())){
		 * IMPSConstants
		 * .RrnTxnIdMap.remove(payee.getIMPSRequest().getOriginalRRN()); }
		 * 
		 * txn =
		 * updateService.updateIMPSRequestPayeeError(msg.getTransactionId(),
		 * ErrorCode.TIMEOUT, null, null, msg.getNao().getPayeeIndex());
		 * msg.setPayload(txn);
		 * eventBus.notify(ComponentNames.TRANSACTIONPROCESSOR,
		 * Event.wrap(msg));
		 * 
		 * //Initiate a reversal on the debit transaction, if the debit was not
		 * pre-approved //Commented as suggested by AS //End of reversal
		 * generation for payer
		 * 
		 * }else{ mapToMessage(msg); } } }
		 * 
		 * } else if (msg.getNao().getType() == NextActionObjectType.PAYER){
		 * 
		 * DBTransactionPayerType payer = txn.getDbTransaction().getPayer();
		 * 
		 * //Check if max retries have been exceeded if
		 * (payer.getIMPSRequest()!=null &&
		 * payer.getIMPSRequest().getVerificationSequences()!=null &&
		 * (payer.getIMPSRequest().getResponseApprovalNumber()==null &&
		 * payer.getIMPSRequest().getResponseCode()==null)){ int retriesDone =
		 * payer.getIMPSRequest().getVerificationSequences().size(); if
		 * (retriesDone
		 * >=GatewayConfiguration.getInstance().getCreditAdviceRetryLimit()){ if
		 * (payer.getIMPSRequest()!=null &&
		 * CommonUtils.hasValue(payer.getIMPSRequest().getOriginalRRN())){
		 * IMPSConstants
		 * .RrnTxnIdMap.remove(payer.getIMPSRequest().getOriginalRRN()); }
		 * 
		 * txn =
		 * updateService.updateIMPSRequestPayerError(msg.getTransactionId(),
		 * ErrorCode.TIMEOUT, null, null); msg.setPayload(txn);
		 * eventBus.notify(ComponentNames.TRANSACTIONPROCESSOR,
		 * Event.wrap(msg));
		 * 
		 * //Initiate a reversal on the debit transaction if no response
		 * received //This code has been moved to TransactionStageUtils //End of
		 * reversal generation for payer }else{ mapToMessage(msg); } } }
		 * 
		 * } } catch (ChecksumException e) {
		 * log.error("Checksum Error while timeout", e); e.printStackTrace(); }
		 * catch (ValidationException e) { log.error(METHOD_NAME, e); } catch
		 * (DuplicateRequestException e) { log.error(METHOD_NAME, e); }
		 * catch(Exception ex){ log.error(METHOD_NAME, ex); throw ex; }finally{
		 * if (log.isDebugEnabled()){ log.debug("Leaving "+METHOD_NAME); } }
		 */
	}

	private void processFailure(ProcessingMessage pmsg, ErrorCode errorCode,
			String extErrCd) throws ChecksumException, ValidationException,
			DuplicateRequestException {

		pmsg = new ProcessingMessage(pmsg);
		if (isSimulated)
			return;

		/*
		 * List<String> extErrMsgs = null; if (CommonUtils.hasValue(extErrCd)){
		 * extErrMsgs = IMPSConstants.getImpsErrorList(extErrCd); } Transaction
		 * txn = null; if (pmsg.getNao().getType() ==
		 * NextActionObjectType.PAYEE){ if (!pmsg.getNao().isReversal()) txn =
		 * updateService.updateIMPSRequestPayeeError(pmsg.getTransactionId(),
		 * errorCode, extErrCd, extErrMsgs,pmsg.getNao().getPayeeIndex()); else
		 * txn =
		 * updateService.updateIMPSRequestPayeeReversalError(pmsg.getTransactionId
		 * (), errorCode, extErrCd, extErrMsgs,pmsg.getNao().getPayeeIndex()); }
		 * else if (pmsg.getNao().getType() == NextActionObjectType.PAYER){ if
		 * (!pmsg.getNao().isReversal()) txn =
		 * updateService.updateIMPSRequestPayerError(pmsg.getTransactionId(),
		 * errorCode, extErrCd, extErrMsgs); else txn =
		 * updateService.updateIMPSRequestPayerReversalError
		 * (pmsg.getTransactionId(), errorCode, extErrCd, extErrMsgs); }
		 * pmsg.setPayload(txn);
		 * eventBus.notify(ComponentNames.TRANSACTIONPROCESSOR,
		 * Event.wrap(pmsg));
		 */
	}

	/*
	 * private String processCredBlock(IMPSRequest request, String de2) throws
	 * CryptoException{ Cred cred = request.getCredential();
	 * 
	 * if (cred==null || cred.getData() == null || cred.getType() == null)
	 * return null;
	 * 
	 * Cred.Data data = cred.getData(); CredType credType = cred.getType();
	 * 
	 * if (data==null || !CommonUtils.hasValue(data.getValue())) return null;
	 * 
	 * KeyDetail decryptionPrivateKey =
	 * keyService.getKeyForCodeAndKI(GatewayConfiguration
	 * .getInstance().getUPIOrgId(),data.getKi()); if (decryptionPrivateKey ==
	 * null){ throw new CryptoException(); }
	 * 
	 * 
	 * SecurityObject secObj = null;
	 * 
	 * CryptoService cryptoService =
	 * CryptoServiceFactory.getInstance().getCryptoServiceImpl
	 * (CryptoServiceFactory.CURRENT_HSM_TYPE); String de2ForHsm =
	 * de2.substring(6, 18); if (CredType.PIN.equals(credType) &&
	 * CommonUtils.hasValue(de2) && de2.length()==19){ secObj =
	 * cryptoService.decryptEncryptUsing3DES
	 * (decryptionPrivateKey.getHsmPrivateLabel(), de2ForHsm, data.getValue());
	 * }else if (CredType.OTP.equals(credType)) //secObj =
	 * cryptoService.decryptEncryptUsingPublicKey
	 * (decryptionPrivateKey.getHsmPrivateLabel(), data.getValue(),
	 * key.getHsmPublicLavel()); secObj =
	 * cryptoService.decryptEncryptUsing3DES(decryptionPrivateKey
	 * .getHsmPrivateLabel(), de2ForHsm, data.getValue()); else return null;
	 * 
	 * try { if (secObj == null ||
	 * !request.getTxnId().equals(secObj.getTransactionId()) ||
	 * CommonUtils.compareAmounts(request.getPayerAmt(), secObj.getTranAmount())
	 * != 0){ throw new CryptoException(); } } catch (Exception e) { throw new
	 * CryptoException(); }
	 * 
	 * return secObj.getCredential(); }
	 */

}
