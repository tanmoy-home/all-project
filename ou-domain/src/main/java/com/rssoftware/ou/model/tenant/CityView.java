package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CityView")
public class CityView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6200747175297235026L;
	private Long cityId;
	private String cityName;
	private Long stateId;
	
	private Boolean isSelected = false;
	
	public CityView() {
		super();
	}
	
	public CityView(Long cityId, String cityName) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "CityView [cityId=" + cityId + ", cityName=" + cityName
				+ ", stateId=" + stateId + "]";
	}
	
}
