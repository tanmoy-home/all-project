package com.rssoftware.ou.portal.web.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bbps.schema.TxnDetail;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;

import in.co.rssoftware.bbps.schema.Agent;
import in.co.rssoftware.bbps.schema.AgentList;
import in.co.rssoftware.bbps.schema.BillerDetail;
import in.co.rssoftware.bbps.schema.BillerList;
import in.co.rssoftware.bbps.schema.BillerResponseType;
import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.DispositionDetail;
import in.co.rssoftware.bbps.schema.DispositionList;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.ServiceReasonDetail;
import in.co.rssoftware.bbps.schema.ServiceReasonList;
import in.co.rssoftware.bbps.schema.TxnDetailType;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

public class UIUtils {

	public static String getServiceReasonListDropdown(ServiceReasonList serviceReasonList) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<option value=\"\">Please Select</option>\n");
		if (serviceReasonList != null && serviceReasonList.getServiceReasons() != null) {
			for (ServiceReasonDetail reason : serviceReasonList.getServiceReasons()) {
				sb.append("<option value=\"" + reason.getName() + "\">" + reason.getName() + "</option>\n");
			}
		}
		return sb.toString();
	}

	public static String getAllAgentsDropdown(AgentList agntList) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<option value=\"\">Please Select</option>\n");
		if (agntList != null && agntList.getAgentLists() != null) {
			for (Agent agent : agntList.getAgentLists()) {
				sb.append("<option value=\"" + agent.getAgentId() + "\">" + agent.getAgentName() + "</option>\n");
			}
		}
		return sb.toString();
	}

	public static String getAllBillersDropdown(BillerList billerList) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<option value=\"\">Please Select</option>\n");
		if (billerList != null && billerList.getBillers() != null) {
			for (BillerDetail blr : billerList.getBillers()) {
				sb.append("<option value='" + blr.getId() + "'  data-isparent='"+(!blr.isIsParent()?"0":"1")+"' data-isadhoc='"+(!blr.isAcceptsAdhoc()?"0":"1")+"'   >" + blr.getName() + "</option>\n");
			}
		}
		return sb.toString();
	}

	public static String getTimeOut(ValidationException ve) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<label>");
		sb.append("<font color=\"red\" style=\"font-size: 18px;font-family: roboto;\">");
		sb.append("Failed to get response.");
		sb.append("</label>");
		sb.append("<br/>");
		sb.append("<label>");
		sb.append("Error Code:" + ve.getCode());
		sb.append(", Description:" + ve.getDescription());
		sb.append("</font><br>");
		sb.append("</label>");
		return sb.toString();
	}

	public static String getComplaintMessage(ComplaintRaisedResponse resp) {
		// <font color="green" style="font-size: 18px;font-family: roboto;"> No
		// transaction found for this Mobile no.</font>
		StringBuilder sb = new StringBuilder("");
		if (resp.getOpenComplaint() != null) {
			if (resp.getOpenComplaint().equals("Y")) {
				sb.append("<font color=\"red\" style=\"font-size: 18px;font-family: roboto;\">");
				sb.append("<center>");
				sb.append("Complaint already raised for this Transaction with Complaint ID: " + resp.getComplaintId());
				sb.append("</font></center><br><br>");
			} else {
				sb.append("<font color=\"green\" style=\"font-size: 18px;font-family: roboto;\">");
				sb.append("<center>");
				sb.append("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
				sb.append("</font></center><br><br>");
			}
		} else {
			sb.append("<font color=\"green\" style=\"font-size: 18px;font-family: roboto;\">");
			sb.append("<center>");
			sb.append("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
			sb.append("</font></center><br><br>");
		}
		return sb.toString();
	}

	public static String getTransactionSaerchPage(TxnSearchResponse txnSearchResponse) {

		StringBuilder sb = new StringBuilder("");
		// for(int i=0;i<10;i++){
		// TxnDetail txn=txnlist.get(i);

		for (TxnDetailType txn : txnSearchResponse.getTxnDetails()) {
			sb.append("<tr>\n");
			String txnId = txn.getTxnReferenceId();
			sb.append("<th style=\"padding-left: 5px;width:2%;\" scope=\"row\"><input type=\"radio\" name=\"txnlist\" value=\"" + txn.getTxnReferenceId()
					+ "\"></th>\n");
			sb.append("<td style=\"width:12%;\">" + txnId + "</td>\n");
			sb.append("<td style=\"width:12%;\">" + txn.getBillerId() + "</td>\n");
			// String blrName=null;
			// if(billerService.getBillerById(txn.getBillerId())!=null){
			// blrName=billerService.getBillerById(txn.getBillerId()).getBlrName();
			// }else{
			// blrName="Undefined";
			// }
			sb.append("<td style=\"width:13%;\">" + "Undefined" + "</td>\n");
			// sb.append("<td>"+txn.getAmount()+"</td>\n");
			/*
			 * billAmount =new
			 * BigDecimal(amount).divide(CommonConstants.HUNDRED) .setScale(2,
			 * RoundingMode.HALF_UP).toPlainString();
			 */
			sb.append("<td style=\"width:13%;\">" + new BigDecimal(txn.getAmount())
					.divide(CommonConstants.HUNDRED).setScale(2, RoundingMode.HALF_UP).toPlainString() + "</td>\n");
			sb.append("<td style=\"width:12%;\">" + txn.getTxnStatus() + "</td>\n");
			sb.append("<td style=\"width:25%;\">" + txn.getTxnDate() + "</td>\n");
			sb.append("</tr>\n");
		}

		// System.out.println(sb.toString());
		return sb.toString();

	}

	public static String getDispositionListDropdown(DispositionList displist) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<option value=\"\">Please Select</option>\n");
		if (displist != null && displist.getDispositions() != null) {
			for (DispositionDetail disp : displist.getDispositions()) {
				sb.append("<option value=\"" + disp.getName() + "\">" + disp.getName() + "</option>\n");
			}
		}

		return sb.toString();
	}

	public static String getComplaintStatus(ComplaintRaisedResponse resp) {
		StringBuilder sb = new StringBuilder("");
		if(resp!=null)
		{
		if (resp.getComplaintStatus() != null) {
			sb.append("<font color=\"green\" style=\"font-size: 18px;font-family: roboto;\">");
			sb.append("<center>");
			sb.append("Current Status: " + resp.getComplaintStatus());
			sb.append("</font></center><br>");
		}
		if (resp.getAssigned() != null) {
			sb.append("<font color=\"green\" style=\"font-size: 18px;font-family: roboto;\">");
			sb.append("<center>");
			sb.append("Assigned To: " + resp.getAssigned());
			sb.append("</font></center><br>");
		}
		if (resp.getResponseCode().equals("001")) {
			sb.append("<font color=\"red\" style=\"font-size: 18px;font-family: roboto;\">");
			sb.append("<center>");
			sb.append(resp.getResponseReason());
			sb.append("</font></center><br>");
		}
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String getBillerAmountOptions( FetchResponse fetchresp) {

		StringBuilder select = new StringBuilder("");
			select.append("<option value=\"\">");
			select.append("Please Select");
			select.append("</option>");
            for(BillerResponseType.Tag tag:fetchresp.getBillerResponse().getTags())
            {
            	select.append("<option value='"+tag.getValue()+"'>"+tag.getName()+"</option>");
            }
			
		return select.toString();
	}
}
