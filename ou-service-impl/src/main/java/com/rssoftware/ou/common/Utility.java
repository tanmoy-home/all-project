package com.rssoftware.ou.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	public static final String	CU_DATE_FFORMAT				= "yyyy-MM-dd";
	public static final String	DEFAULT_BOU_DATE_FFORMAT	= "yyyyMMdd";

	private static final Logger	logger						= LoggerFactory.getLogger(Utility.class);

	public static String convertDateStrToCuFormat(String date, String dateFormat) {
		if (dateFormat == null)
			dateFormat = DEFAULT_BOU_DATE_FFORMAT;

		DateFormat bouFormat = new SimpleDateFormat(dateFormat);
		DateFormat cuFormat = new SimpleDateFormat(CU_DATE_FFORMAT);

		try {
			return cuFormat.format(bouFormat.parse(date));
		} catch (ParseException e) {
			logger.error("Date conversion error: " + e.getMessage());
		}
		return date;
	}

	public static Date convertDateStrToCuDate(String date, String dateFormat) {
		if (dateFormat == null)
			dateFormat = DEFAULT_BOU_DATE_FFORMAT;

		try {
			return new SimpleDateFormat(dateFormat).parse(date);
		} catch (ParseException e) {
			logger.error("Date conversion error: " + e.getMessage());
		}
		return null;
	}
}