package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class IsoMapSubElement {

	private static final Logger log = LoggerFactory.getLogger(IsoMapSubElement.class);
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

	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	String value;
	//private int position;
	
	/*public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}*/

	public String getValue() {
		return value;
	}

	public IsoMapSubElement() {
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

	public IsoMapSubElement(String name, int length, Class type, int varlength) {
		super();
		this.name = name;
		this.length = length;
		this.type = type;
		this.varlength = varlength;
		//this.position = position;
	}



}
