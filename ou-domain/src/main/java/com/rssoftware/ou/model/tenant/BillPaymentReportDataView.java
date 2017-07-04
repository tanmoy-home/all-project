package com.rssoftware.ou.model.tenant;

public class BillPaymentReportDataView {
	
	private TransactionDataView transactionDataView;
	private RawDataView rawDataView;
	
	public BillPaymentReportDataView() {
	}
	
	public BillPaymentReportDataView(TransactionDataView transactionDataView, RawDataView rawDataView) {
		super();
		this.transactionDataView = transactionDataView;
		this.rawDataView = rawDataView;
	}

	public TransactionDataView getTransactionDataView() {
		return transactionDataView;
	}
	
	public void setTransactionDataView(TransactionDataView transactionDataView) {
		this.transactionDataView = transactionDataView;
	}
	
	public RawDataView getRawDataView() {
		return rawDataView;
	}
	
	public void setRawDataView(RawDataView rawDataView) {
		this.rawDataView = rawDataView;
	}
}
