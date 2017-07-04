package org.npci.iso.imps.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.nulleins.formats.iso8583.formatters.AlphaFormatter;
import org.nulleins.formats.iso8583.formatters.TypeFormatter;
import org.nulleins.formats.iso8583.types.CharEncoder;
import org.nulleins.formats.iso8583.types.Dimension;
import org.nulleins.formats.iso8583.types.FieldType;

public class TlvFomatter extends TypeFormatter<TlvFormat[]> {

	private static final Logger log = LoggerFactory.getLogger(TlvFomatter.class);
	private final TypeFormatter<String> formatter;
	private static final int Segmentlength = 20; // size of the recurring
													// fieldlist segment
	private static final int tagLength = 3;
	private static final int lengthOfLength = 3;

	public TlvFomatter() {
		setCharset(CharEncoder.ASCII);
		formatter = new AlphaFormatter(CharEncoder.ASCII);
	}

	@Override
	public TlvFormat[] parse(String type, Dimension dimension, int position,
			byte[] data) throws ParseException {
		// TODO Auto-generated method stub
		 final String field = new String(data);
		 final List<TlvFormat> result = new ArrayList<TlvFormat>();
		 for (int p = 0; p < field.length();) {
			 final String tag = formatter.parse(FieldType.ALPHANUM, Dimension.parse("fixed(3)"), p, field.substring(p, p + tagLength).getBytes()) ;
			 p = p + tagLength;
			 final String length = field.substring(p, p + lengthOfLength);
			 p = p + lengthOfLength;
			 final String value = formatter.parse(FieldType.ALPHANUM,Dimension.parse("fixed(" + length.trim() + ")"), p, field.substring(p, p + Integer.parseInt(length.trim())).getBytes()) ;
			 p = p + Integer.parseInt(length.trim());
			 result.add(new TlvFormat(tag, value));
		 }
		return result.toArray(new TlvFormat[result.size()]);
	}

	@Override
	public byte[] format(String type, Object data, Dimension dimension) {
		if (!(data instanceof TlvFormat[])) {
			throw new IllegalArgumentException(
					"data must be a TlvFormat instance");
		}
		final StringBuilder result = new StringBuilder();
		final TlvFormat[] items = (TlvFormat[]) data;
		if (items.length < 1) {
			throw new IllegalArgumentException(
					"Array of TlvFormat must contain atleast 1 value");
		}
		for (final TlvFormat item : items) {
			if (item == null) {
				throw new IllegalArgumentException(
						"TlvFormat null in data array");
			}
			result.append(new String(formatter.format(FieldType.ALPHANUM,
					item.getTag(), Dimension.parse("fixed(3)"))));
			result.append(new String(formatter.format(FieldType.ALPHANUMPAD,
					Integer.toString(item.getLength()),
					Dimension.parse("fixed(3)"))));
			result.append(new String(formatter.format(
					FieldType.ALPHANUM,
					item.getValue(),
					Dimension.parse("fixed("
							+ Integer.toString(item.getLength()) + ")"))));
		}

		return result.toString().getBytes();
	}

	@Override
	public boolean isValid(Object value, String type, Dimension dimension) {
		if (!(value instanceof TlvFormat[])) {
			return false;
		}
		final TlvFormat[] tlvData = (TlvFormat[]) value;
		if (tlvData.length < 1 || tlvData.length > 6) {
			return false;
		}
		for (final TlvFormat item : tlvData) {
			if (item == null) {
				return false;
			}
			if (item.getTag() == null) {
				return false;
			}
			if (item.getLength() < 1) {
				return false;
			}
			if (item.getValue() == null) {
				return false;
			}
		}
		return true;
	}

}
