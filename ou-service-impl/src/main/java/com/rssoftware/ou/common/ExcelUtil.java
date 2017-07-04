package com.rssoftware.ou.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.tenant.service.ExcelMapperService;

public class ExcelUtil {
	
	private final static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	
	private static ExcelMapperService excelMapperService = BeanLocator
			.getBean(ExcelMapperService.class);

	private static Map<String, String> fieldLabelMap = new HashMap<String, String>();
	private static List<String> orderLabels = new ArrayList<String>();

	public static <T> byte[] writeReportToExcelAnnotation(List<T> data,
			String sheetName) throws  IllegalAccessException, IllegalArgumentException,
    InvocationTargetException,NoSuchMethodException, SecurityException {

		processAnnotations(data.get(0));
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = null;
		Cell cel = null;
		Row row = null;
		sheet = workbook.createSheet(sheetName);

		int rowCount = 0;
		int columnCount = 0;

		row = sheet.createRow(rowCount++);
		ExcelUtil excelUtil = new ExcelUtil();
		List<String> newOrderList = excelUtil.getNewList(sheetName);

		for (String labelName : newOrderList) {
			cel = row.createCell(columnCount++);
			cel.setCellValue(labelName);
			cel.setCellStyle(getColumnHeaderCellStyle(workbook));
		}
		Class<? extends Object> classz = data.get(0).getClass();
		for (T t : data) {
			row = sheet.createRow(rowCount++);

			columnCount = 0;

			for (String label : newOrderList) {
				String methodName = getFieldLabelMap().get(
						excelMapperService.getMappedToName(label));
				cel = row.createCell(columnCount);
				Method method = classz.getMethod(methodName);
				Object value = method.invoke(t, (Object[]) null);
				if (value != null) {
					if (value instanceof String) {
						cel.setCellValue((String) value);
					} else if (value instanceof Long) {
						cel.setCellValue((Long) value);
					} else if (value instanceof Integer) {
						cel.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cel.setCellValue((Double) value);
					} else if (value instanceof Timestamp) {
						cel.setCellValue((Timestamp) value);
					}
				}
				columnCount++;
			}
		}

		fieldLabelMap.clear();
		orderLabels.clear();
		sheet = null;
		ByteArrayOutputStream bos = null;
		bos = new ByteArrayOutputStream();
		try {
			if (workbook != null) {
				workbook.write(bos);

				byte[] bytes = bos.toByteArray();

				return bytes;
			}

		} catch (IOException e) {

			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());
		}

		return null;

	}

	private static CellStyle getColumnHeaderCellStyle(Workbook workbook) {
		return createColumnHeaderCellStyle(workbook);
	}

	private static CellStyle createColumnHeaderCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();

		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		Font headerFont = workbook.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT
				.getIndex());
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setFont(headerFont);

		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	private static <T> void processAnnotations(T object) {
		fieldLabelMap.clear();
		orderLabels.clear();

		Class<?> clazz = object.getClass();
		ExcelReport reportAnnotation = (ExcelReport) clazz
				.getAnnotation(ExcelReport.class);
		String reportName = null;
		if (reportAnnotation != null)
			reportName = reportAnnotation.reportName();
		else
			reportName = "Biller";
		if ((reportName == null) || (reportName.trim().length() < 1)) {
			// should never get here.

		}

		for (Method method : clazz.getMethods()) {
			ExcelColumn excelColumn = method.getAnnotation(ExcelColumn.class);
			if ((excelColumn != null) && !excelColumn.ignore()) {
				getFieldLabelMap().put(excelColumn.label(), method.getName());
				getOrderLabels().add(excelColumn.label());
			}
		}
	}

	private List<String> getNewList(String sheetName) {
		List<String> orderList = new ArrayList<String>();
		List<String> preList = getOrderLabels();
		if (("switching fee discount Details".equals(sheetName))
				|| ("switching fee Details".equals(sheetName))) {
			if (preList.contains("MTI")) {
				orderList.add("MTI");
			}
			if (preList.contains("Biller Category")) {
				orderList.add("Biller Category");
			}
			if (preList.contains("Response Code")) {
				orderList.add("Response Code");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Interchange fee config".equals(sheetName)) {
			if (preList.contains("Biller OU Id")) {
				orderList.add("Biller OU Id");
			}
			if (preList.contains("Biller Id")) {
				orderList.add("Biller Id");
			}
			if (preList.contains("MTI")) {
				orderList.add("MTI");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Interchange fee".equals(sheetName)) {
			if (preList.contains("Biller OU Id")) {
				orderList.add("Biller OU Id");
			}
			if (preList.contains("Biller Id")) {
				orderList.add("Biller Id");
			}
			if (preList.contains("Fee Code")) {
				orderList.add("Fee Code");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("velocity config Details".equals(sheetName)) {
			if (preList.contains("OU")) {
				orderList.add("OU");
			}
			if (preList.contains("Agent Institute")) {
				orderList.add("Agent Institute");
			}
			if (preList.contains("Agent")) {
				orderList.add("Agent");
			}
			if (preList.contains("Biller")) {
				orderList.add("Biller");
			}
			if (preList.contains("Effective From")) {
				orderList.add("Effective From");
			}
			if (preList.contains("Effective To")) {
				orderList.add("Effective To");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			if (preList.contains("MTI")) {
				orderList.add("MTI");
			}
			if (preList.contains("Payment Mode")) {
				orderList.add("Payment Mode");
			}
			return orderList;
		} else if ("OU Details".equals(sheetName)) {
			if (preList.contains("OU ID")) {
				orderList.add("OU ID");
			}
			if (preList.contains("OU Name")) {
				orderList.add("OU Name");
			}
			if (preList.contains("Business Type")) {
				orderList.add("Business Type");
			}
			if (preList.contains("Effective To")) {
				orderList.add("Effective To");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Agent Details".equals(sheetName)) {
			if (preList.contains("Agent ID")) {
				orderList.add("Agent ID");
			}
			if (preList.contains("Agent Name")) {
				orderList.add("Agent Name");
			}
			if (preList.contains("Business Type")) {
				orderList.add("Business Type");
			}
			if (preList.contains("Agent Institution ID")) {
				orderList.add("Agent Institution ID");
			}
			if (preList.contains("Mobile")) {
				orderList.add("Mobile");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Holiday Details".equals(sheetName)) {
			if (preList.contains("Date")) {
				orderList.add("Date");
			}
			if (preList.contains("Year")) {
				orderList.add("Year");
			}
			if (preList.contains("Reason")) {
				orderList.add("Reason");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Biller".equals(sheetName)) {
			if (preList.contains("Biller ID")) {
				orderList
						.add(excelMapperService.getMappedFromName("Biller ID"));
			}
			if (preList.contains("Biller Name")) {
				orderList.add(excelMapperService
						.getMappedFromName("Biller Name"));
			}
			if (preList.contains("Biller Category")) {
				orderList.add(excelMapperService
						.getMappedFromName("Biller Category"));
			}
			if (preList.contains("Is Parent")) {
				orderList
						.add(excelMapperService.getMappedFromName("Is Parent"));
			}
			if (preList.contains("Parent Biller ID")) {
				orderList.add(excelMapperService
						.getMappedFromName("Parent Biller ID"));
			}
			if (preList.contains("Status")) {
				orderList.add(excelMapperService.getMappedFromName("Status"));
			}
			return orderList;
		} else if ("AI Details".equals(sheetName)) {
			if (preList.contains("AgentInst ID")) {
				orderList.add(excelMapperService.getMappedFromName("AgentInst ID"));
			}
			if (preList.contains("AgentInst Name")) {
				orderList.add(excelMapperService.getMappedFromName("AgentInst Name"));
			}
			if (preList.contains("Inst. Type")) {
				orderList.add(excelMapperService.getMappedFromName("Inst. Type"));
			}
			if (preList.contains("Business Type")) {
				orderList.add(excelMapperService.getMappedFromName("Business Type"));
			}
			if (preList.contains("Effective From")) {
				orderList.add(excelMapperService.getMappedFromName("Effective From"));
			}
			if (preList.contains("Effective To")) {
				orderList.add(excelMapperService.getMappedFromName("Effective To"));
			}
			if (preList.contains("Status")) {
				orderList.add(excelMapperService.getMappedFromName("Status"));
			}
			return orderList;
		} else if ("Cert Details".equals(sheetName)) {
			if (preList.contains("OU Id")) {
				orderList.add("OU Id");
			}
			if (preList.contains("Certificate Type")) {
				orderList.add("Certificate Type");
			}
			if (preList.contains("Key Label")) {
				orderList.add("Key Label");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Key Details".equals(sheetName)) {
			if (preList.contains("Key Name")) {
				orderList.add("Key Name");
			}
			if (preList.contains("Key Owner")) {
				orderList.add("Key Owner");
			}
			if (preList.contains("Key Algorithm")) {
				orderList.add("Key Algorithm");
			}
			if (preList.contains("Effective From")) {
				orderList.add("Effective From");
			}
			if (preList.contains("Effective To")) {
				orderList.add("Effective To");
			}
			if (preList.contains("Status")) {
				orderList.add("Status");
			}
			return orderList;
		} else if ("Interchange CustOU fee".equals(sheetName)) {
			if (preList.contains("Biller Id")) {
				orderList.add("Biller Id");
			}
			if (preList.contains("Biller Name")) {
				orderList.add("Biller Name");
			}
			if (preList.contains("Fee Code")) {
				orderList.add("Fee Code");
			}
			return orderList;
		}
		return preList;
	}

	public static Map<String, String> getFieldLabelMap() {
		return fieldLabelMap;
	}

	public static void setFieldLabelMap(Map<String, String> fieldLabelMap) {
		ExcelUtil.fieldLabelMap = fieldLabelMap;
	}

	public static List<String> getOrderLabels() {
		return orderLabels;
	}

	public static void setOrderLabels(List<String> orderLabels) {
		ExcelUtil.orderLabels = orderLabels;
	}
}
