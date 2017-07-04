package org.npci.iso.imps.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TlvFormat {

	private static final Logger log = LoggerFactory.getLogger(TlvFormat.class);

	private String tag;
	private int length;
	private String value;

	public TlvFormat(final String tag, final String value) {
		this.tag = tag;
		this.value = value;
		this.length = value.length();
	}

	@Override
	public String toString() {
		return String.format("%s %02d %s", tag, length,	value);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLength() {
		return length;
	}

	/*public void setLength(int length) {
		this.length = length;
	}*/

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
