package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "admin_tbl_organization")
public class OrganizationEntity implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 5480781072243110146L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@SequenceGenerator(name="organization_seq_gen",sequenceName="ADMIN_TBL_ORGANIZATION_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="organization_seq_gen")
	Long id;
	
	@Column(name = "ORG_NAME")
	private String organizationName;
	
	@OneToOne(targetEntity=OrganizationEntity.class,cascade=CascadeType.ALL)
	@JoinColumn(name = "parent_org", referencedColumnName="id")
	private OrganizationEntity parentOrganization;
	
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public OrganizationEntity getParentOrganization() {
		return parentOrganization;
	}

	public void setParentOrganization(OrganizationEntity parentOrganization) {
		this.parentOrganization = parentOrganization;
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
