package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.database.entity.tenant.ReconDetails;

public class ReconView {
	
	private final static Logger logger = LoggerFactory.getLogger(ReconView.class);

	private String reconId;
	private BigDecimal matchedCount;
	private BigDecimal notInOUCount;
	private BigDecimal notInCUCount;
	private BigDecimal mismatchedCount;
	private BigDecimal pendingCount;
	private BigDecimal broughtForwardCount;
	private Timestamp reconTs;
	private List<ReconDetails> notInOuList;
	private List<ReconDetails> notInCuList;
	private List<ReconDetails> mismatchedList;
	private List<ReconDetails> pendingList;
	private List<ReconDetails> broughtForwardList;


	public String getReconId() {
		return reconId;
	}

	public void setReconId(String reconId) {
		this.reconId = reconId;
	}

	public BigDecimal getMatchedCount() {
		return matchedCount;
	}

	public void setMatchedCount(BigDecimal matchedCount) {
		this.matchedCount = matchedCount;
	}

	public BigDecimal getNotInOUCount() {
		return notInOUCount;
	}

	public void setNotInOUCount(BigDecimal notInOUCount) {
		this.notInOUCount = notInOUCount;
	}

	public BigDecimal getNotInCUCount() {
		return notInCUCount;
	}

	public void setNotInCUCount(BigDecimal notInCUCount) {
		this.notInCUCount = notInCUCount;
	}

	public BigDecimal getMismatchedCount() {
		return mismatchedCount;
	}

	public void setMismatchedCount(BigDecimal mismatchedCount) {
		this.mismatchedCount = mismatchedCount;
	}

	public BigDecimal getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(BigDecimal pendingCount) {
		this.pendingCount = pendingCount;
	}

	public BigDecimal getBroughtForwardCount() {
		return broughtForwardCount;
	}

	public void setBroughtForwardCount(BigDecimal broughtForwardCount) {
		this.broughtForwardCount = broughtForwardCount;
	}

	public Timestamp getReconTs() {
		return reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public List<ReconDetails> getNotInOuList() {
		return notInOuList;
	}

	public void setNotInOuList(List<ReconDetails> notInOuList) {
		this.notInOuList = notInOuList;
	}

	public List<ReconDetails> getNotInCuList() {
		return notInCuList;
	}

	public void setNotInCuList(List<ReconDetails> notInCuList) {
		this.notInCuList = notInCuList;
	}

	public List<ReconDetails> getMismatchedList() {
		return mismatchedList;
	}

	public void setMismatchedList(List<ReconDetails> mismatchedList) {
		this.mismatchedList = mismatchedList;
	}

	public List<ReconDetails> getPendingList() {
		return pendingList;
	}

	public void setPendingList(List<ReconDetails> pendingList) {
		this.pendingList = pendingList;
	}

	public List<ReconDetails> getBroughtForwardList() {
		return broughtForwardList;
	}

	public void setBroughtForwardList(List<ReconDetails> broughtForwardList) {
		this.broughtForwardList = broughtForwardList;
	}

	/*@Override
	public String toString() {
		return "ReconView [reconId=" + reconId + ", matchedCount="
				+ matchedCount + ", notInOUCount=" + notInOUCount
				+ ", notInCUCount=" + notInCUCount + ", mismatchedCount="
				+ mismatchedCount + ", pendingCount=" + pendingCount
				+ ", broughtForwardCount=" + broughtForwardCount + ", reconTs="
				+ reconTs + ", notInOuList=" + notInOuList + ", notInCuList="
				+ notInCuList + ", mismatchedList=" + mismatchedList
				+ ", pendingList=" + pendingList + ", broughtForwardList="
				+ broughtForwardList + "]";
	}*/

	
	@Override
	public String toString() {
		DateFormat formatter = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
		Date date = null;
		try {
			date = new Date(formatter.parse(
					reconTs.toString()).getTime());
		} catch (ParseException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}
		return "{\"reconId\":\"" + reconId + "\", \"matchedCount\":\""
				+ matchedCount + "\", \"notInOUCount\":\"" + notInOUCount
				+ "\", \"notInCUCount\":\"" + notInCUCount + "\", \"mismatchedCount\":\""
				+ mismatchedCount + "\", \"pendingCount\":\"" + pendingCount
				+ "\", \"broughtForwardCount\":\"" + broughtForwardCount + "\", \"reconTs\":\""
				+ date + "\", \"notInOuList\":" + notInOuList + ", \"notInCuList\":"
				+ notInCuList + ", \"mismatchedList\":" + mismatchedList
				+ ", \"pendingList\":" + pendingList + ", \"broughtForwardList\":"
				+ broughtForwardList + "}";
	}
	
	
}
