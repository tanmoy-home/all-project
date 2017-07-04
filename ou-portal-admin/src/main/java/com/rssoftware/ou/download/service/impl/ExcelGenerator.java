package com.rssoftware.ou.download.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.ComplaintReportHeaderContent;
import com.rssoftware.ou.common.ComplaintReportResp;
import com.rssoftware.ou.common.SegmentReportHeaderContent;
import com.rssoftware.ou.common.SegmentReportResp;
import com.rssoftware.ou.common.SegmentReportRespList;
import com.rssoftware.ou.common.TxnReportHeaderContent;
import com.rssoftware.ou.common.TxnReportResp;
import com.rssoftware.ou.common.utils.CommonUtils;

public class ExcelGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelGenerator.class);
	
	public File generateTxnReportExcel(TxnReportResp reportResp) {
		File file = null;
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = generateSheet(wb);

			int rowIndex = 0;
			rowIndex = insertTxnReportHeaderData(wb, sheet, rowIndex);
			rowIndex = insertTxnReportData(wb, sheet, rowIndex,reportResp);
			String fileLocation = System.getProperty("reconFileLocation").substring(5) + "/weeklyReport.xlsx";
			file = CommonUtils.writeToOutputStream(wb, fileLocation);
		} 
		catch (Exception e) {
			LOGGER.error("Error in generateTxnReportExcel ===",e);
		}
		return file;
	}
	
	private int insertTxnReportHeaderData(Workbook wb, Sheet sheet, int index) {

		CellStyle csBold = CommonUtils.getArialHeaderCellStyle(wb);
		int rowIndex = 0;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		row = sheet.createRow(rowIndex);
		int colIdx = 1;
		for (TxnReportHeaderContent headerContent : TxnReportHeaderContent.values()) {
			if(headerContent.colSpan()>1) {
				CellRangeAddress region = new CellRangeAddress( rowIndex, rowIndex, colIdx, colIdx+headerContent.colSpan()-1);
				sheet.addMergedRegion(region);
				}
				else {
				c = row.createCell(colIdx);
				}
				String alias = headerContent.alias();
				c=CellUtil.createCell(row, colIdx, alias);
				c.setCellStyle(csBold);
				//c.setCellValue(alias);
				colIdx = colIdx+headerContent.colSpan();
				//int colSpan = headerContent.colSpanRow2();
				//j = j + colSpan;
			/*c = row.createCell(j);
			String alias = headerContent.alias();
			c.setCellStyle(csBold);
			c.setCellValue(alias);
			int colSpan = headerContent.colSpan();
			j = j + colSpan;*/
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		colIdx = 1;
		for (TxnReportHeaderContent headerContent : TxnReportHeaderContent.values()) {
			if(headerContent.colSpan()>1) {
				CellRangeAddress region = new CellRangeAddress( rowIndex, rowIndex, colIdx, colIdx+headerContent.colSpan()-1);
				sheet.addMergedRegion(region);
				}
				else {
				c = row.createCell(colIdx);
				}
				String[] headerDetails = headerContent.getHeaderDetails(headerContent);
				c=CellUtil.createCell(row, colIdx, headerDetails[0]);
				c.setCellStyle(csBold);
				colIdx = colIdx+headerContent.colSpan();

			/*c = row.createCell(colIdx);
			String[] headerDetails = headerContent.getHeaderDetails(headerContent);
			c.setCellStyle(csBold);
			c.setCellValue(headerDetails[0]);
			int colSpan = headerContent.colSpan();
			j = j + colSpan;*/
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		colIdx = 1;
		for (TxnReportHeaderContent headerContent : TxnReportHeaderContent.values()) {
			int colSpan = headerContent.colSpan();
			String[] headerDetails = headerContent.getHeaderDetails(headerContent);
			for (int k = 1; k <= colSpan; k++) {
				c = row.createCell(colIdx++);
				c.setCellStyle(csBold);
				c.setCellValue(headerDetails[k]);
			}
		}
		return rowIndex;
	}
	
	private int insertTxnReportData(Workbook wb, Sheet sheet, int index, TxnReportResp txnReportResp) {

		CellStyle cs = CommonUtils.getArialCellStyle(wb);
				
		int rowIndex = 0;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		row = sheet.createRow(rowIndex);
		int j = 1;
		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue("1");

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getBbpouName());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getNoOfAgentOutlets().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOnUsTxnCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOffUsTxnCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOnUsTxnTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOffUsTxnTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOnUsFailedTxnCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOffUsFailedTxnCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOnUsFailedTxnTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOffUsFailedTxnTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getFailureReason());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getCashPaymentCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getDCCCPaymentCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getNetBankingPaymentCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getIMPSPaymentCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getPPIsPaymentCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(txnReportResp.getOtherPaymentCount().doubleValue());
		return rowIndex;
	}
	
	public File generateComplaintReportExcel(ComplaintReportResp reportResp) {
		File file = null;
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = generateSheet(wb);
			int rowIndex = 0;
			rowIndex = insertComplaintReportHeaderData(wb, sheet, rowIndex);
			rowIndex = insertComplaintReportData(wb, sheet, rowIndex,reportResp);
			// Write the Excel file
			String fileLocation = System.getProperty("reconFileLocation").substring(5) + "/weeklyReport.xlsx";
			file = CommonUtils.writeToOutputStream(wb, fileLocation);
		} catch (Exception e) {
			LOGGER.error("Error in generateComplaintReportExcel ===",e);
		}
		return file;
	}
	
	private int insertComplaintReportData(Workbook wb, Sheet sheet, int index, ComplaintReportResp reportResp) {
		CellStyle cs = CommonUtils.getArialCellStyle(wb);
		
		int rowIndex = 0;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		row = sheet.createRow(rowIndex);
		int j = 1;
		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue("1");

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getBbpouName());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOnUsoutstandingLastWeekCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOnUsreceivedThisWeekCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOnUsTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOffUsoutstandingLastWeekCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOffUsreceivedThisWeekCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOffUsTot().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOnUsResolvedCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOffUsResolvedCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOnUsPendingCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getOffUsPendingCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getTxnBasedCount().doubleValue());

		c = row.createCell(j++);
		c.setCellStyle(cs);
		c.setCellValue(reportResp.getServiceBasedCount().doubleValue());

		return rowIndex;
	}
	
	private int insertComplaintReportHeaderData(Workbook wb, Sheet sheet, int index) {
		CellStyle csBold = CommonUtils.getArialHeaderCellStyle(wb);
		csBold.setWrapText(true);
		int rowIndex = 0;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		row = sheet.createRow(rowIndex);
		int colIdx = 1;
		for (ComplaintReportHeaderContent headerContent : ComplaintReportHeaderContent.values()) {
			if(headerContent.colSpanRow2()>1) {
			CellRangeAddress region = new CellRangeAddress( rowIndex, rowIndex, colIdx, colIdx+headerContent.colSpanRow2()-1);
			sheet.addMergedRegion(region);
			}
			else {
			c = row.createCell(colIdx);
			}
			String alias = headerContent.alias();
			c=CellUtil.createCell(row, colIdx, alias);
			c.setCellStyle(csBold);
			//c.setCellValue(alias);
			colIdx = colIdx+headerContent.colSpanRow2();
			//int colSpan = headerContent.colSpanRow2();
			//j = j + colSpan;
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		colIdx = 1;
		for (ComplaintReportHeaderContent headerContent : ComplaintReportHeaderContent.values()) {
			String[] headerDetails = headerContent.getHeaderDetails(headerContent);
			for(int columnCount = 0; columnCount<headerContent.colSpanRow1(); columnCount++) {
				if((headerContent.colSpanRow2()/headerContent.colSpanRow1())>1) {
					CellRangeAddress region = new CellRangeAddress( rowIndex, rowIndex, colIdx, colIdx+(headerContent.colSpanRow2()/headerContent.colSpanRow1())-1);
					sheet.addMergedRegion(region);
					}
					else {
					c = row.createCell(colIdx);
					}
			c=CellUtil.createCell(row, colIdx, headerDetails[columnCount]);
			c.setCellStyle(csBold);
			//c.setCellValue(headerDetails[columnCount]);
			int colSpan = headerContent.colSpanRow2()/headerContent.colSpanRow1();
			colIdx = colIdx + colSpan;
		}
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		colIdx = 1;
		for (ComplaintReportHeaderContent headerContent : ComplaintReportHeaderContent.values()) {
			int colSpan = headerContent.colSpanRow2();
			String[] headerDetails = headerContent.getHeaderDetails(headerContent);
			for (int k = headerContent.colSpanRow1(); k < colSpan+headerContent.colSpanRow1(); k++) {
				c = row.createCell(colIdx++);
				c.setCellStyle(csBold);
				c.setCellValue(headerDetails[k]);
			}
		}
		return rowIndex;
	}
		
	private Sheet generateSheet(Workbook wb) {
		Sheet sheet = wb.createSheet("Weekly_Report_" + CommonUtils.getDateFromddMMyy(new Date()));

		// Get current Date and Time
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

		// Setup the Page margins - Left, Right, Top and Bottom
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.TopMargin, 0.75);
		sheet.setMargin(Sheet.BottomMargin, 0.75);

		// Setup the Header and Footer Margins
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);

		Header header = sheet.getHeader();
	
		header.setRight(df.format(date));

		// Set Footer Information with Page Numbers
		Footer footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());
		return sheet;
	}
	public File generateSegmentReportExcel(SegmentReportRespList reportResp) {
		File file = null;
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = generateSheet(wb);

			int rowIndex = 0;
			rowIndex = insertSegmentReportHeaderData(wb, sheet, rowIndex);
			rowIndex = insertSegmentReportData(wb, sheet, rowIndex,reportResp);
			String fileLocation = System.getProperty("reconFileLocation").substring(5) + "/weeklyReport.xlsx";
			file = CommonUtils.writeToOutputStream(wb, fileLocation);
		} 
		catch (Exception e) {
			LOGGER.error("Error in generateSegmentReportExcel ===",e);
		}
		return file;
	}
	
	private int insertSegmentReportHeaderData(Workbook wb, Sheet sheet, int index) {

		CellStyle csBold = CommonUtils.getArialHeaderCellStyle(wb);
		int rowIndex = 0;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		row = sheet.createRow(rowIndex);
		int colIdx = 1;
		for (SegmentReportHeaderContent headerContent : SegmentReportHeaderContent.values()) {
			if(headerContent.colSpan()>1) {
			CellRangeAddress region = new CellRangeAddress( rowIndex, rowIndex, colIdx, colIdx+headerContent.colSpan()-1);
			sheet.addMergedRegion(region);
			}
			else {
			c = row.createCell(colIdx);
			}
			String alias = headerContent.alias();
			c=CellUtil.createCell(row, colIdx, alias);
			c.setCellStyle(csBold);
			//c.setCellValue(alias);
			colIdx = colIdx+headerContent.colSpan();
			//int colSpan = headerContent.colSpanRow2();
			//j = j + colSpan;
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		colIdx = 1;
		for (SegmentReportHeaderContent headerContent : SegmentReportHeaderContent.values()) {
			int colSpan = headerContent.colSpan();
			String[] headerDetails = headerContent.getHeaderDetails(headerContent);
			for (int k = 0; k < colSpan; k++) {
				c = row.createCell(colIdx++);
				c.setCellStyle(csBold);
				c.setCellValue(headerDetails[k]);
			}
		}
		return rowIndex;
	}
	
	private int insertSegmentReportData(Workbook wb, Sheet sheet, int index, SegmentReportRespList segmentReportRespList) {

		CellStyle cs = CommonUtils.getArialCellStyle(wb);
				
		int rowIndex = 0, k = 1;
		Row row = null;
		Cell c = null;

		rowIndex = index + 1;
		//row = sheet.createRow(rowIndex);
		List<SegmentReportResp> segmentReportResps = segmentReportRespList.getSegmentReportResps();
		int colIdx = 0;
		char ch='A';
		for(int i=0; i<segmentReportResps.size(); ch++) {
			row = sheet.createRow(rowIndex++);
			colIdx = 1;
			c = row.createCell(colIdx++);
			c.setCellStyle(cs);
			c.setCellValue((char)ch+"");
			
			c = row.createCell(colIdx++);
			c.setCellStyle(cs);
			c.setCellValue(segmentReportResps.get(i).getBlrCategory());
			k = 1;
			do {
				//rowIndex = index + 1;
				colIdx = 2;
				row = sheet.createRow(rowIndex++);
				
				c = row.createCell(colIdx++);
				c.setCellStyle(cs);
				c.setCellValue("("+(k++)+") "+segmentReportResps.get(i).getBlrName());
				
				c = row.createCell(colIdx++);
				c.setCellStyle(cs);
				c.setCellValue(segmentReportResps.get(i).getOnUsCount().doubleValue());
				
				c = row.createCell(colIdx++);
				c.setCellStyle(cs);
				c.setCellValue(segmentReportResps.get(i).getOffUsCount().doubleValue());
				
				c = row.createCell(colIdx++);
				c.setCellStyle(cs);
				c.setCellValue(segmentReportResps.get(i).getOnUsTot().doubleValue());
				
				c = row.createCell(colIdx++);
				c.setCellStyle(cs);
				c.setCellValue(segmentReportResps.get(i).getOffUsTot().doubleValue());
				i++;
				
			} while (i<segmentReportResps.size() && segmentReportResps.get(i)!=null && segmentReportResps.get(i).getBlrCategory().equals(segmentReportResps.get(i-1).getBlrCategory()));
		}
		return rowIndex;
	}
	
}
