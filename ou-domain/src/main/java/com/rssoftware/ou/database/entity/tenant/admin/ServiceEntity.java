package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "admin_tbl_Services")
public class ServiceEntity implements Serializable{

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1369279224740734441L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@SequenceGenerator(name="services_seq_gen",sequenceName="ADMIN_TBL_SERVICES_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="services_seq_gen")
	private Long id;
	
	@Column(name = "SERVICE_NAME")
	private String serviceName;
	
	@Column(name = "SERVICE_DESCRIPTION")
	private String serviceDescription;
	
	@Column(name = "SERVICE_URL")
	private String serviceURL;
	
	
	//@Column(name = "IS_ACTIVE",columnDefinition = "TINYINT")
	@Column(name = "IS_ACTIVE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isActive;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}