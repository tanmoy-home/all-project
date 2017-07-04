package com.rssoftware.ou.model.tenant;

public class PostalCodeView {
	
	private String pinCode;
	private String pinLocation;
	private Long cityId;
	private Long stateId;
	
	private Boolean isSelected;
	
	public PostalCodeView() {
		super();
	}
	
	public PostalCodeView(String pinCode) {
		super();
		this.pinCode = pinCode;
			
	}
	
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getPinLocation() {
		return pinLocation;
	}
	public void setPinLocation(String pinLocation) {
		this.pinLocation = pinLocation;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
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

}
