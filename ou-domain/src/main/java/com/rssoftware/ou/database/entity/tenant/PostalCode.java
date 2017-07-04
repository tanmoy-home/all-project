package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="postalcode")
@NamedQuery(name="PostalCode.findAll", query="SELECT t FROM PostalCode t")
public class PostalCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="postal_code")
	private String pinCode;
	
	@Column(name="postal_location")
	private String pinLocation;
	
	@Column(name="city_id")
	private Long cityId;
	
	@Column(name="state_id")
	private Long stateId;

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
}
