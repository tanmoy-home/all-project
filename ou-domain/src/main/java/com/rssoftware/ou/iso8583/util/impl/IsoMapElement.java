package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class IsoMapElement {

	private static final Logger log = LoggerFactory.getLogger(IsoMapElement.class);
	private String name;
	private int length;
	private Class type;
	private int varlength;
	private int pos;
	private boolean isTLV = false;	
	private String value;
	
	public boolean isTLV() {
		return isTLV;
	}

	public void setTLV(boolean isTLV) {
		this.isTLV = isTLV;
	}

	private Map<String,IsoMapSubElement> subElementMap = new TreeMap<String, IsoMapSubElement>();


	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	
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

	public IsoMapElement() {
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

	public IsoMapElement(String name, int length, Class type, int varlength) {
		super();
		this.name = name;
		this.length = length;
		this.type = type;
		this.varlength = varlength;
		//this.position = position;
	}
	
	
	public IsoMapElement(String name, int length, int varlength,String value, int pos) {
		super();
		this.name = name;
		this.length = length;
		this.varlength = varlength;
		this.value = value;
		this.pos = pos;
		//this.position = position;
	}
	
	public Map<String, IsoMapSubElement> getSubElementMap() {
		return subElementMap;
	}

	public void setSubElementMap(Map<String, IsoMapSubElement> subElementMap) {
		this.subElementMap = subElementMap;
	}

}
