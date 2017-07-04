package com.rssoftware.ou.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	
	private static Pattern numericPattern = Pattern.compile("^[0-9]+$");
	private static Pattern alPhanumericWithAllPattern = Pattern.compile("^[a-zA-Z0-9 \\/!#$%&(),\\.\\\\\\\\*_|?\\-{};\\-:<>^`\\~@/.=+\"\'\\[\\]*]*$");
	
	protected static boolean matches(Pattern p, CharSequence input) {
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	protected static boolean isEmpty(Object attribute) {
		return attribute == null ? true : attribute.toString().length() == 0 ? true : false;
	}
	
	public static boolean isNumeric(String s) {
		if (Validator.isEmpty(s)) {
			return false;
		}
		if (matches(numericPattern, s)) {
			return true;
		}
		return false;
	}
	
	public static boolean isAlphaNumericWithAllSpcl(String s) {
		if (Validator.isEmpty(s)) {
			return false;
		}
		if (s.trim().length() == 0) {
			return false;
		}
		if (matches(alPhanumericWithAllPattern, s)) {
			return true;
		}
		return false;
	}
}