package com.rssoftware.ou.portal.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class TransactionListExcelView extends AbstractExcelView {
	Map<Object, Object> model;

	public TransactionListExcelView(Map<Object, Object> mapList) {
		this.model = mapList;

	}

	@Override
	protected void buildExcelDocument(Map<String, Object> models, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=TransactionList.xls");

		/*HSSFSheet excelSheet = workbook.createSheet("Transaction List");
		setExcelHeader(excelSheet);

		@SuppressWarnings("unchecked")
		List<TransactionReportDetails> transactionReportDetailsList = (List<TransactionReportDetails>) model
				.get("TransactionList");
		setExcelRows(excelSheet, transactionReportDetailsList);

		OutputStream outStream = null;

		try {
			outStream = response.getOutputStream();
			workbook.write(outStream);
			outStream.flush();
		} finally {
			outStream.close();
			response.flushBuffer();

		}*/

	}

	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("Transaction Id");
		excelHeader.createCell(1).setCellValue("Channel Code");
		excelHeader.createCell(2).setCellValue("Org Id");
		excelHeader.createCell(3).setCellValue("Transaction Type");
		excelHeader.createCell(4).setCellValue("Total Amount");
		excelHeader.createCell(5).setCellValue("Initiated By");
		excelHeader.createCell(6).setCellValue("Transaction start time");
		excelHeader.createCell(7).setCellValue("Transaction status");
	}

	/*public void setExcelRows(HSSFSheet excelSheet, List<TransactionReportDetails> transactionList) {
		int record = 1;
		if (transactionList == null)
			return;

		for (TransactionReportDetails transaction : transactionList) {
			HSSFRow excelRow = excelSheet.createRow(record++);
			if (transaction.getTransaction() != null && transaction.getTransaction().getTxnId() != null)
				excelRow.createCell(0).setCellValue(transaction.getTransaction().getTxnId());
			else
				excelRow.createCell(0).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getChannelCode() != null)
				excelRow.createCell(1).setCellValue(transaction.getTransaction().getChannelCode().name());
			else
				excelRow.createCell(1).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getOrgId() != null)
				excelRow.createCell(2).setCellValue(transaction.getTransaction().getOrgId());
			else
				excelRow.createCell(2).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getTxnType() != null)
				excelRow.createCell(3).setCellValue(transaction.getTransaction().getTxnType());
			else
				excelRow.createCell(3).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getTxnTotalAmt() != null)
				excelRow.createCell(4).setCellValue(transaction.getTransaction().getTxnTotalAmt().toString());
			else
				excelRow.createCell(4).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getInitiatedBy() != null)
				excelRow.createCell(5).setCellValue(transaction.getTransaction().getInitiatedBy());
			else
				excelRow.createCell(5).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getCrtnTs() != null)
				excelRow.createCell(6).setCellValue(transaction.getTransaction().getCrtnTs());
			else
				excelRow.createCell(6).setCellValue("");
			if (transaction.getTransaction() != null && transaction.getTransaction().getTxnStatus() != null)
				excelRow.createCell(7).setCellValue(transaction.getTransaction().getTxnStatus());
			else
				excelRow.createCell(7).setCellValue("");
		}
	}*/

}
