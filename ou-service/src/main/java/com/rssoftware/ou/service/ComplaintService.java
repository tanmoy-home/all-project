package com.rssoftware.ou.service;

import java.util.ArrayList;
import java.util.List;

import in.co.rssoftware.bbps.schema.ComplainRequest;
import in.co.rssoftware.bbps.schema.ComplainStatusRequest;
import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.ComplaintSearchResponse;
import in.co.rssoftware.bbps.schema.ComplaintsSearchRequest;
import in.co.rssoftware.bbps.schema.TransactionSearchRequest;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

import org.bbps.schema.Ack;
import org.bbps.schema.TxnDetail;
import org.bbps.schema.TxnStatusComplainResponse;

import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.domain.Complaint;
import com.rssoftware.ou.model.tenant.ComplaintView;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public interface ComplaintService {
	public ArrayList<TxnDetail> fetchTxnForComplaints(String mobile,String startDate,String endDate,String txnRefId,String refId) ;
		public Ack processComplaintRequest(ComplaintView complaint) throws ValidationException;
		public ComplaintResponse sendComplaintRequest(ComplainRequest complainRequest) throws ValidationException;
		public void saveComplaintResponse(TxnStatusComplainResponse response) throws ValidationException;
		public ComplaintResponse sendComplaintStatusRequest(ComplainStatusRequest complainStatusRequest) throws ValidationException;
		public TxnSearchResponse sendTransactionSearchRequestType(TransactionSearchRequest transactionSearchRequest) throws ValidationException;
		public ComplaintResponse getComplaintResponse(String refId);
		public ComplaintRequest getComplaintRequest(String refId);
		public void updateResponseStatus(String refId, RequestStatus status);
		public void updateRequestStatus(String refId, RequestStatus status);
		public ComplaintSearchResponse fetchAllComplaints(ComplaintsSearchRequest request);
		public TxnSearchResponse getTransactionSearchResponse(String mobileNo,String startDate,String endDate,String txnId ,String refId);
		public ComplaintRaisedResponse getComplaintRaisedResponse(String refId);
		
		
		
}
