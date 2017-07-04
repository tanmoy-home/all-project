package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "admin_tbl_Role")
public class RoleEntity implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 5480781072243110146L;

	@Id
    @SequenceGenerator(name="role_seq_gen",sequenceName="ADMIN_TBL_ROLE_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="role_seq_gen") 
	private Long id;
	
	@Column(name = "ROLE_NAME")
	private String roleName;
	
	@Column(name = "ROLE_DESC")
	private String roleDesc;
	
	//@Column(name = "SERVICE_MAP_FLAG",columnDefinition = "TINYINT")
	@Column(name = "SERVICE_MAP_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean serviceMapFlag;
	
	@OneToOne(targetEntity=OrganizationEntity.class,cascade=CascadeType.ALL)
	@JoinColumn(name = "org_id", referencedColumnName="id")
	private OrganizationEntity organization;
	

	//@NotEmpty
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "admin_tbl_role_access_mapping", 
             joinColumns = { @JoinColumn(name = "role_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "service_id") })
	private Set<ServiceEntity> services = new HashSet<ServiceEntity>();
	
	//@NotEmpty
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "admin_tbl_role_access_mapping", 
             joinColumns = { @JoinColumn(name = "role_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "menu_id") })
	private Set<MenuEntity> menus = new HashSet<MenuEntity>();

	//@Column(name = "IS_ACTIVE",columnDefinition = "TINYINT")
	@Column(name = "IS_ACTIVE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isActive;

	@Column(name = "IS_API_ACCESS")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isApiAccess;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getRoleDesc() {
		return roleDesc;
	}


	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}


	public OrganizationEntity getOrganization() {
		return organization;
	}
 
	public void setOrganization(OrganizationEntity organization) {
		this.organization = organization;
	}	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsApiAccess() {
		return isApiAccess;
	}


	public void setIsApiAccess(Boolean isApiAccess) {
		this.isApiAccess = isApiAccess;
	}


	public Set<ServiceEntity> getServices() {
		return services;
	}

	public void setServices(Set<ServiceEntity> services) {
		this.services = services;
	}
	
	public Set<MenuEntity> getMenus() {
		return menus;
	}

	public void setMenus(Set<MenuEntity> menus) {
		this.menus = menus;
	}
	
	public Boolean getServiceMapFlag() {
		return serviceMapFlag;
	}

	public void setServiceMapFlag(Boolean serviceMapFlag) {
		this.serviceMapFlag = serviceMapFlag;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

