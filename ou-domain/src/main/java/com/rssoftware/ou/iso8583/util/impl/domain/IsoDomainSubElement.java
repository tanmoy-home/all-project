package com.rssoftware.ou.iso8583.util.impl.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/*
 * This is a dummy class for Domain.A UPI related domain object needs to be created.
 */
public class IsoDomainSubElement {

	private static final Logger log = LoggerFactory.getLogger(IsoDomainSubElement.class);
	private String name;
	private int length;
	private Class type;
	private int varlength;
	private int pos;
	private String tagValue;
	

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	private Map<Integer,IsoDomainSubElement> subElementMap = new TreeMap<Integer, IsoDomainSubElement>();
		


	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	String value;
	

	public String getValue() {
		return value;
	}

	public IsoDomainSubElement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public int getVarlength() {
		return varlength;
	}

	public void setVarlength(int varlength) {
		this.varlength = varlength;
	}

	public IsoDomainSubElement(String name, int length, Class type, int varlength) {
		super();
		this.name = name;
		this.length = length;
		this.type = type;
		this.varlength = varlength;
		//this.position = position;
	}
	


}
