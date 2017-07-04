package com.rssoftware.ou.portal.web.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.model.tenant.BillerView;

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
				sb.append("<option value='" + blr.getId() + "'  data-fetch-req='"+(blr.getFetchRequirement()==null?"OPTIONAL":blr.getFetchRequirement().toUpperCase())+"'  data-accept-adhoc='"+(!blr.isAcceptsAdhoc()?"0":"1")+"'  data-isparent='"+(!blr.isIsParent()?"0":"1")+"'\">" + blr.getName() + "</option>\n");
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

	public static String getTransactionSaerchPage(TxnSearchResponse txnSearchResponse,Map<String,String>bilerdetailList,String mob) {
		StringBuilder sb = new StringBuilder("");
        // for(int i=0;i<10;i++){
        // TxnDetail txn=txnlist.get(i);
		
        for (TxnDetailType txn : txnSearchResponse.getTxnDetails()) {
               sb.append("<tr>\n");
               String txnId = txn.getTxnReferenceId();
               sb.append("<th style=\"padding-left: 5px;width:1%;\"   scope=\"row\"><input onclick=\"enableView()\" type=\"radio\" name=\"txnlist\" value=\"" + txn.getTxnReferenceId()
                            + "\"></th>\n");
               sb.append("<td style=\"width:10%;\" id='"+txn.getTxnReferenceId()+"_txnId' > XXXXXXXX" + txn.getTxnReferenceId().substring(txnId.length()-4, txnId.length()) + "</td>\n");
//             sb.append("<td style=\"width:11%;display:none;\" id='"+txn.getTxnReferenceId()+"_billerId' >" + txn.getBillerId() + "</td>\n");
               String blrName=null;
               
                blrName=bilerdetailList.get(txn.getBillerId());
               if(blrName==null){
               blrName="Undefined";
               }
               String mobbutton="";
               if(mob!=null&&!mob.isEmpty())
               {
            	   mobbutton=" <input id='"+txnId+"_mob' type='hidden' value='"+mob+"'"+"/>";
               }
               sb.append("<td style=\"width:11%;\" id='"+txn.getTxnReferenceId()+"_billerId' >"+ mobbutton + blrName + "</td>\n");
//             sb.append("<td style=\"width:13%;\">" + "Undefined" + "</td>\n");
               // sb.append("<td>"+txn.getAmount()+"</td>\n");
               /*
               * billAmount =new
               * BigDecimal(amount).divide(CommonConstants.HUNDRED) .setScale(2,
               * RoundingMode.HALF_UP).toPlainString();
               */
               sb.append("<td style=\"width:13%;\" id='"+txn.getTxnReferenceId()+"_amount'>" + new BigDecimal(txn.getAmount())
                            .divide(CommonConstants.HUNDRED).setScale(2, RoundingMode.HALF_UP).toPlainString() + "</td>\n");
               sb.append("<td style=\"width:11%;\" id='"+txn.getTxnReferenceId()+"_status'>" + txn.getTxnStatus() + "</td>\n");
               sb.append("<td style=\"width:25%;\" id='"+txn.getTxnReferenceId()+"_dt' >" + txn.getTxnDate() + "</td>\n");
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
	
	public static String getBillerAmountOptions(BillerView biller, FetchResponse fetchresp) {

		StringBuilder select = new StringBuilder("");
		// select.append(
		// "<select name='billerAmountOptions' id='billerAmountOptions'
		// onChange='calculateCCF()'; style='width: 100%; padding: 5px 0px;'>");
		if (fetchresp != null && fetchresp.getErrorMessages().size() == 0) {
			BigDecimal amount = new BigDecimal(fetchresp.getBillerResponse().getAmount());
			select.append("<option value=\"\">");
			select.append("Please Select");
			select.append("</option>");

			if (biller.getBillerResponseParams() != null
					&& !biller.getBillerResponseParams().getAmountOptions().isEmpty()) {
				Map<String, BigDecimal> incomingPaymentAmounts = new HashMap<String, BigDecimal>();

				if (!fetchresp.getBillerResponse().getTags().isEmpty()) {
					for (in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag : fetchresp.getBillerResponse()
							.getTags()) {
						if (tag != null && CommonUtils.hasValue(tag.getName())
								&& CommonUtils.hasValue(tag.getValue())) {
							BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100))
									.setScale(2, RoundingMode.HALF_UP);
							incomingPaymentAmounts.put(tag.getName(), tagAmount);
						}
					}
				}

				for (BillerResponseParams.AmountOption ao : biller.getBillerResponseParams().getAmountOptions()) {
					if (ao != null) {
						boolean allAmtsPresent = true;
						for (String amountBreakup : ao.getAmountBreakups()) {
							if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
								continue;
							}
							if (incomingPaymentAmounts.get(amountBreakup) == null) {
								allAmtsPresent = false;
								break;
							}
						}

						if (allAmtsPresent) {
							BigDecimal total = BigDecimal.ZERO;
							StringBuilder b = new StringBuilder("");
							StringBuilder breakup = new StringBuilder("");
							for (String amountBreakup : ao.getAmountBreakups()) {
								if (breakup.length() > 0) {
									breakup.append("|");
								}
								breakup.append(amountBreakup);
								if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
									b.append("Bill Amount (" + amount.toPlainString() + ")+");
									total = total.add(amount);
								} else {
									b.append(amountBreakup + " ("
											+ incomingPaymentAmounts.get(amountBreakup).toPlainString() + ")+");
									total = total.add(incomingPaymentAmounts.get(amountBreakup));
								}
							}

							select.append("<option value=\"" + total.toPlainString() + "|" + breakup + "\">");
							select.append(b.substring(0, b.length() - 1) + "=" + total.toPlainString());
							select.append("</option>");

						}
					}
				}

			} else {
				select.append("<option value=\"" + amount.toPlainString() + "|" + BillerResponseParams.BASE_BILL_AMOUNT
						+ "\">");
				select.append("Bill Amount (" + amount.toPlainString() + ")");
				select.append("</option>");
			}

			// select.append("</select>");
		}
		return select.toString();
		
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
