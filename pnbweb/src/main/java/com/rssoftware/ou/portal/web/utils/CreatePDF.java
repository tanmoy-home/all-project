package com.rssoftware.ou.portal.web.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class CreatePDF {

	private static Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);

	private static Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12,
			Font.BOLD);

	/**
	 * 
	 * @param args
	 */

	public static Document createPDF(String file,String[] receipt_lebel,String[] receipt_data,
			List<Image> images , String title,String header) throws IOException {

		Document document = null;

		try {

			document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();

			addMetaData(document ,title);

			// addTitlePage(document);

			addReceiptImages(document, images);
			createTable(document, receipt_lebel,receipt_data,header);

			document.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (DocumentException e) {

			e.printStackTrace();

		}

		return document;

	}

	private static void addMetaData(Document document ,String title ) {

		document.addTitle(title);

		document.addSubject(title);

		document.addAuthor("SIB Bank");

		document.addCreator("SIB Bank");

	}

	private static void addTitlePage(Document document)

	throws DocumentException {

		Paragraph preface = new Paragraph();

		creteEmptyLine(preface, 1);

		preface.add(new Paragraph("PDF Report", TIME_ROMAN));

		creteEmptyLine(preface, 1);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

		preface.add(new Paragraph("Report created on "

		+ simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));

		document.add(preface);

	}

	private static void creteEmptyLine(Paragraph paragraph, int number) {

		for (int i = 0; i < number; i++) {

			paragraph.add(new Paragraph(" "));

		}

	}

	private static void createTable(Document document, String[] receipt_lebel , String[] receipt_data,String header)
			throws DocumentException, IOException {

		Paragraph paragraph = new Paragraph();
		creteEmptyLine(paragraph, 2);
		document.add(paragraph);
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setWidthPercentage(70);

		headerTable.getDefaultCell().setHorizontalAlignment(
				Element.ALIGN_CENTER);

		headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		headerTable.addCell(header);
		document.add(headerTable);

		PdfPTable table = new PdfPTable(2);

		// PdfPCell c1 = new PdfPCell(new Phrase("Txn Name : "));
		// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		// table.addCell(c1);

		// PdfPCell c2 = new PdfPCell(new Phrase("Txn Type : "));
		// c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		// table.addCell(c2);

		//StringTokenizer st = new StringTokenizer(receipt_data, "~");
		int length = receipt_lebel.length;

//		System.out.println(receipt_data);
//		System.out.println(length);

//		List<String> arr = new ArrayList<>();
//		int i = 0;
//
//		while (st.hasMoreTokens()) {
//
//			arr.add(i, st.nextToken());
//			i++;
//		}

		Map<String, String> receipt_data_map = new LinkedHashMap<String, String>();
		for(int i=0;i<length;i++)
		{
			table.setWidthPercentage(70);

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			table.addCell(receipt_lebel[i]);

			table.addCell(receipt_data[i]);
		}
//		receipt_data_map.put("Customer Name", arr.get(0));
//		receipt_data_map.put("Phone", arr.get(1));
//		receipt_data_map.put("Account", arr.get(2));
//		receipt_data_map.put("Transaction Reference Id", arr.get(3));
//		receipt_data_map.put("Payment Channel", arr.get(4));
//		receipt_data_map.put("Payment Mode", arr.get(5));
//		receipt_data_map.put("Bill Date", arr.get(6));
//		receipt_data_map.put("Bill Amount", arr.get(7));
//		receipt_data_map.put("Customer Convenience Fee", arr.get(8));
//		receipt_data_map.put("Total Amount", arr.get(9));
//		receipt_data_map.put("Trasnaction Date & Time", arr.get(10));
//		receipt_data_map.put("Authorization Code", arr.get(11));

//		Iterator it = receipt_data_map.entrySet().iterator();
//		while (it.hasNext()) {
//
//			Map.Entry pair = (Map.Entry) it.next();
//
//			System.out.println(pair.getKey() + " = " + pair.getValue());
//
//			table.setWidthPercentage(70);
//
//			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//
//			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//			table.addCell(pair.getKey().toString());
//
//			table.addCell(pair.getValue().toString());
//		}

		document.add(table);

	}

	private static void addReceiptImages(Document document, List<Image> images)
			throws DocumentException {
		Image bbpouLogo = images.get(0);
		Image bbpsLogo = images.get(1);
		
		bbpsLogo.setAbsolutePosition(320f, 775f);
		bbpsLogo.scalePercent(80f);
		
		bbpouLogo.setAbsolutePosition(120f, 778f);
		bbpouLogo.scaleAbsolute(bbpsLogo.getScaledWidth(), bbpsLogo.getScaledHeight());
		
		document.add(bbpouLogo);	
				
		document.add(bbpsLogo);
	}
}