package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "StateView")
public class StateView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9165212710885827784L;
	private Long stateId;
	private String stateName;
	
	private Boolean isSelected;
	
	public StateView() {
		super();
	}
	
	public StateView(Long stateId, String stateName) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
	}
	
	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

}
