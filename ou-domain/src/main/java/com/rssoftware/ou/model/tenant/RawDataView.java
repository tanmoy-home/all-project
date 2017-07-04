package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.rssoftware.ou.common.RequestType;

public class RawDataView {
	public enum ReconStatus {
		UNREAD, MATCHED, NO_MATCHES_FOUND, NON_MATCHING_FIELDS;
	}
	public enum OUType {
		BOU, COU;
	}
	private String msgId;
	private String refId;
	private String mti;
	private String txnReferenceId;
	private RequestType txnType;
	private Date txnDate;
	private String customerOuId;
	private String billerOuId;
	private String agentId;
	private String responseCode;
	private String txnCurrencyCode;
	private BigDecimal txnAmount;
	private BigDecimal customerConvenienceFee;
	private BigDecimal customerOuSwitchingFee;
	private BigDecimal billerFee;
	private Timestamp clearingTimestamp;
	private String paymentChannel;
	private String paymentMode;
	private BigDecimal customerOuCountMonth;
	private String billerId;
	private String billerCategory;
	private Boolean splitPay;
	private String splitPaymentMode;
	private BigDecimal splitPayTxnAmount;
	private String customerMobileNumber;
	private Boolean reversal;
	private Boolean decline;
	private Boolean casProcessed;
	private String settlementCycleId;
	private BigDecimal customerConvenienceFeeTax;
	private Long billerFeeTax;
	private Long customerOUSwitchingFeeTax;
	private Long serviceFee;
	private String serviceFeeDescription;
	private Long serviceFeeTax;
	private String tenantId;
	private BigDecimal billerOuSwitchingFee;
	private BigDecimal billerOuCountMonth;
	private Long billerOUSwitchingFeeTax;
	//private BigDecimal billerOuInterchangeFee;
	//private BigDecimal customerOuInterchangeFee;
	private String reconDescription;
	private ReconStatus reconStatus;
	private Timestamp reconTs;
	private OUType ouType;
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getBillerCategory() {
		return billerCategory;
	}

	public void setBillerCategory(String billerCategory) {
		this.billerCategory = billerCategory;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public BigDecimal getBillerOuCountMonth() {
		return billerOuCountMonth;
	}

	public void setBillerOuCountMonth(BigDecimal billerOuCountMonth) {
		this.billerOuCountMonth = billerOuCountMonth;
	}

	public String getBillerOuId() {
		return billerOuId;
	}

	public void setBillerOuId(String billerOuId) {
		this.billerOuId = billerOuId;
	}

	/*public BigDecimal getBillerOuInterchangeFee() {
		return billerOuInterchangeFee;
	}

	public void setBillerOuInterchangeFee(BigDecimal billerOuInterchangeFee) {
		this.billerOuInterchangeFee = billerOuInterchangeFee;
	}
*/
	public BigDecimal getBillerOuSwitchingFee() {
		return billerOuSwitchingFee;
	}

	public void setBillerOuSwitchingFee(BigDecimal billerOuSwitchingFee) {
		this.billerOuSwitchingFee = billerOuSwitchingFee;
	}

	public Boolean getCasProcessed() {
		return casProcessed;
	}

	public void setCasProcessed(Boolean casProcessed) {
		this.casProcessed = casProcessed;
	}

	public Timestamp getClearingTimestamp() {
		return clearingTimestamp;
	}

	public void setClearingTimestamp(Timestamp clearingTimestamp) {
		this.clearingTimestamp = clearingTimestamp;
	}

	public BigDecimal getCustomerConvenienceFee() {
		return customerConvenienceFee;
	}

	public void setCustomerConvenienceFee(BigDecimal customerConvenienceFee) {
		this.customerConvenienceFee = customerConvenienceFee;
	}

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public BigDecimal getCustomerOuCountMonth() {
		return customerOuCountMonth;
	}

	public void setCustomerOuCountMonth(BigDecimal customerOuCountMonth) {
		this.customerOuCountMonth = customerOuCountMonth;
	}

	public String getCustomerOuId() {
		return customerOuId;
	}

	public void setCustomerOuId(String customerOuId) {
		this.customerOuId = customerOuId;
	}

	/*public BigDecimal getCustomerOuInterchangeFee() {
		return customerOuInterchangeFee;
	}

	public void setCustomerOuInterchangeFee(BigDecimal customerOuInterchangeFee) {
		this.customerOuInterchangeFee = customerOuInterchangeFee;
	}*/

	public BigDecimal getCustomerOuSwitchingFee() {
		return customerOuSwitchingFee;
	}

	public void setCustomerOuSwitchingFee(BigDecimal customerOuSwitchingFee) {
		this.customerOuSwitchingFee = customerOuSwitchingFee;
	}

	public Boolean getDecline() {
		return decline;
	}

	public void setDecline(Boolean decline) {
		this.decline = decline;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getReconDescription() {
		return reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public ReconStatus getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(ReconStatus reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Timestamp getReconTs() {
		return reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Boolean getReversal() {
		return reversal;
	}

	public void setReversal(Boolean reversal) {
		this.reversal = reversal;
	}

	public String getSettlementCycleId() {
		return settlementCycleId;
	}

	public void setSettlementCycleId(String settlementCycleId) {
		this.settlementCycleId = settlementCycleId;
	}

	public Boolean getSplitPay() {
		return splitPay;
	}

	public void setSplitPay(Boolean splitPay) {
		this.splitPay = splitPay;
	}

	public BigDecimal getSplitPayTxnAmount() {
		return splitPayTxnAmount;
	}

	public void setSplitPayTxnAmount(BigDecimal splitPayTxnAmount) {
		this.splitPayTxnAmount = splitPayTxnAmount;
	}

	public String getSplitPaymentMode() {
		return splitPaymentMode;
	}

	public void setSplitPaymentMode(String splitPaymentMode) {
		this.splitPaymentMode = splitPaymentMode;
	}

	public BigDecimal getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnCurrencyCode() {
		return txnCurrencyCode;
	}

	public void setTxnCurrencyCode(String txnCurrencyCode) {
		this.txnCurrencyCode = txnCurrencyCode;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnReferenceId() {
		return txnReferenceId;
	}

	public void setTxnReferenceId(String txnReferenceId) {
		this.txnReferenceId = txnReferenceId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public RequestType getTxnType() {
		return txnType;
	}

	public void setTxnType(RequestType txnType) {
		this.txnType = txnType;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public static Differences compare(RawDataView ou, RawDataView cu) {
		Differences diff = null;
		if (ou.getTxnDate() != null && cu.getTxnDate() != null) {
			if(ou.getTxnDate().compareTo(cu.getTxnDate()) != 0) {
			if (diff == null) {
				diff = new Differences();
			}
			diff.setOUtxnDate(ou.getTxnDate());
			diff.setCUtxnDate(cu.getTxnDate());

			}
		}
		else {
			if (diff == null) {
				diff = new Differences();
			}
			diff.setOUtxnDate(ou.getTxnDate());
			diff.setCUtxnDate(cu.getTxnDate());
		}
		if (ou.getMsgId() == null) {
			if (cu.getMsgId() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUmsgId(ou.getMsgId());
				diff.setCUmsgId(cu.getMsgId());
			}
		} 
		else {
			if (!((ou.getMsgId()).equals(cu.getMsgId()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUmsgId(ou.getMsgId());
				diff.setCUmsgId(cu.getMsgId());
			}
		}
		if (ou.getTxnReferenceId() == null) {
			if (cu.getTxnReferenceId() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUtxnReferenceId(ou.getTxnReferenceId());
				diff.setCUtxnReferenceId(cu.getTxnReferenceId());

			}
		} 
		else {
			if (!((ou.getTxnReferenceId()).equals(cu.getTxnReferenceId()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUtxnReferenceId(ou.getTxnReferenceId());
				diff.setCUtxnReferenceId(cu.getTxnReferenceId());
			}
		}
		if (ou.getPaymentMode() == null) {
			if (cu.getPaymentMode() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUpaymentMode(ou.getPaymentMode());
				diff.setCUpaymentMode(cu.getPaymentMode());

			}
		} 
		else {
			if (!((ou.getPaymentMode()).equals(cu.getPaymentMode()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUpaymentMode(ou.getPaymentMode());
				diff.setCUpaymentMode(cu.getPaymentMode());
			}
		}
		if (ou.getAgentId() == null) {
			if (cu.getAgentId() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUagentId(ou.getAgentId());
				diff.setCUagentId(cu.getAgentId());

			}
		} 
		else {
			if (!((ou.getAgentId()).equals(cu.getAgentId()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUagentId(ou.getAgentId());
				diff.setCUagentId(cu.getAgentId());
			}
		}

		if (ou.getBillerId() == null) {
			if (cu.getBillerId() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUbillerId(ou.getBillerId());
				diff.setCUbillerId(cu.getBillerId());

			}
		} 
		else {
			if (!((ou.getBillerId()).equals(cu.getBillerId()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUbillerId(ou.getBillerId());
				diff.setCUbillerId(cu.getBillerId());
			}
		}
		
		if (ou.getBillerCategory()== null) {
			if (cu.getBillerCategory() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUbillerCategory(ou.getBillerCategory());
				diff.setCUbillerCategory(cu.getBillerCategory());

			}
		} 
		else {
			if (!((ou.getBillerCategory()).equals(cu.getBillerCategory()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUbillerCategory(ou.getBillerCategory());
				diff.setCUbillerCategory(cu.getBillerCategory());
			}
		}

		if (ou.getSplitPay()== null) {
			if (cu.getSplitPay() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUsplitPay(ou.getSplitPay());
				diff.setCUsplitPay(cu.getSplitPay());

			}
		} 
		else {
			if (!((ou.getSplitPay()).equals(cu.getSplitPay()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUsplitPay(ou.getSplitPay());
				diff.setCUsplitPay(cu.getSplitPay());
			}
		}
		if (ou.getReversal()== null) {
			if (cu.getReversal() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUreversal(ou.getReversal());
				diff.setCUreversal(cu.getReversal());

			}
		} 
		else {
			if (!((ou.getReversal()).equals(cu.getReversal()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUreversal(ou.getReversal());
				diff.setCUreversal(cu.getReversal());
			}
		}
		if (ou.getDecline()== null) {
			if (cu.getDecline() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUdecline(ou.getDecline());
				diff.setCUdecline(cu.getDecline());

			}
		} 
		else {
			if (!((ou.getDecline()).equals(cu.getDecline()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUdecline(ou.getDecline());
				diff.setCUdecline(cu.getDecline());
			}
		}
		if (ou.getCustomerMobileNumber() == null) {
			if (cu.getCustomerMobileNumber() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUcustomerMobileNumber(ou.getCustomerMobileNumber());
				diff.setCUcustomerMobileNumber(cu.getCustomerMobileNumber());

			}
		} 
		else {
			if (!((ou.getCustomerMobileNumber()).equals(cu.getCustomerMobileNumber()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUcustomerMobileNumber(ou.getCustomerMobileNumber());
				diff.setCUcustomerMobileNumber(cu.getCustomerMobileNumber());
			}
		}

		
		if (ou.getTxnCurrencyCode() == null) {
			if (cu.getTxnCurrencyCode() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUtxnCurrencyCode(ou.getTxnCurrencyCode());
				diff.setCUtxnCurrencyCode(cu.getTxnCurrencyCode());

			}
		} 
		else {
			if (!((ou.getTxnCurrencyCode()).equals(cu.getTxnCurrencyCode()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUtxnCurrencyCode(ou.getTxnCurrencyCode());
				diff.setCUtxnCurrencyCode(cu.getTxnCurrencyCode());
			}
		}
		if (ou.getCustomerConvenienceFee() != null && cu.getCustomerConvenienceFee() != null) {
			if(cu.getCustomerConvenienceFeeTax()!=null) {
				if(ou.getCustomerConvenienceFee().compareTo(new BigDecimal(Math.abs(cu.getCustomerConvenienceFee().doubleValue())).add(new BigDecimal(Math.abs(cu.getCustomerConvenienceFeeTax().doubleValue())))) != 0) {
					if (diff == null) {
						diff = new Differences();
					}
					diff.setOUcustomerConvenienceFee(ou.getCustomerConvenienceFee());
					diff.setCUcustomerConvenienceFee(cu.getCustomerConvenienceFee());
				}
			}
			else {
				if(ou.getCustomerConvenienceFee().compareTo(new BigDecimal(Math.abs(cu.getCustomerConvenienceFee().doubleValue()))) != 0) {
					if (diff == null) {
						diff = new Differences();
					}
					diff.setOUcustomerConvenienceFee(ou.getCustomerConvenienceFee());
					diff.setCUcustomerConvenienceFee(cu.getCustomerConvenienceFee());
				}

			}
		}
		else {
			if (diff == null) {
				diff = new Differences();
			}
			diff.setOUcustomerConvenienceFee(ou.getCustomerConvenienceFee());
			diff.setCUcustomerConvenienceFee(cu.getCustomerConvenienceFee());
		}
			
		if (ou.getTxnAmount() != null && cu.getTxnAmount() != null) {
				if(ou.getTxnAmount().compareTo(new BigDecimal(Math.abs(cu.getTxnAmount().doubleValue()))) != 0) {
					if (diff == null) {
						diff = new Differences();
					}
					diff.setOUtxnAmount(ou.getTxnAmount());
					diff.setCUtxnAmount(cu.getTxnAmount());

				}
		}
		else {
			if (diff == null) {
				diff = new Differences();
			}
			diff.setOUtxnAmount(ou.getTxnAmount());
			diff.setCUtxnAmount(cu.getTxnAmount());
		}
		
		if (ou.getResponseCode() == null) {
			if (cu.getResponseCode() != null) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUresponseCode(ou.getResponseCode());
				diff.setCUresponseCode(cu.getResponseCode());

			}
		} 
		else {
			if (!((ou.getResponseCode()).equals(cu.getResponseCode()))) {
				if (diff == null) {
					diff = new Differences();
				}
				diff.setOUresponseCode(ou.getResponseCode());
				diff.setCUresponseCode(cu.getResponseCode());
			}
		}
		return diff;
	}
	public Long getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Long serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getServiceFeeDescription() {
		return serviceFeeDescription;
	}

	public void setServiceFeeDescription(String serviceFeeDescription) {
		this.serviceFeeDescription = serviceFeeDescription;
	}

	public Long getServiceFeeTax() {
		return serviceFeeTax;
	}

	public void setServiceFeeTax(Long serviceFeeTax) {
		this.serviceFeeTax = serviceFeeTax;
	}

	public BigDecimal getBillerFee() {
		return billerFee;
	}

	public void setBillerFee(BigDecimal billerFee) {
		this.billerFee = billerFee;
	}

	public BigDecimal getCustomerConvenienceFeeTax() {
		return customerConvenienceFeeTax;
	}

	public void setCustomerConvenienceFeeTax(BigDecimal customerConvenienceFeeTax) {
		this.customerConvenienceFeeTax = customerConvenienceFeeTax;
	}

	public Long getBillerFeeTax() {
		return billerFeeTax;
	}

	public void setBillerFeeTax(Long billerFeeTax) {
		this.billerFeeTax = billerFeeTax;
	}

	public Long getCustomerOUSwitchingFeeTax() {
		return customerOUSwitchingFeeTax;
	}

	public void setCustomerOUSwitchingFeeTax(Long customerOUSwitchingFeeTax) {
		this.customerOUSwitchingFeeTax = customerOUSwitchingFeeTax;
	}

	public Long getBillerOUSwitchingFeeTax() {
		return billerOUSwitchingFeeTax;
	}

	public void setBillerOUSwitchingFeeTax(Long billerOUSwitchingFeeTax) {
		this.billerOUSwitchingFeeTax = billerOUSwitchingFeeTax;
	}

	public OUType getOuType() {
		return ouType;
	}

	public void setOuType(OUType ouType) {
		this.ouType = ouType;
	}
	
}