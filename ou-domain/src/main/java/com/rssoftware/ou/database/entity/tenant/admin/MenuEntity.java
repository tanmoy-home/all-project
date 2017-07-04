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

@Entity
@Table(name = "admin_tbl_Menu")
public class MenuEntity implements Serializable{

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1120655541395535149L;

	@Id
	@SequenceGenerator(name="menu_seq_gen",sequenceName="ADMIN_TBL_MENU_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="menu_seq_gen")
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Column(name = "MENU_NAME")
	private String menuName;
	
	@Column(name = "MENU_TYPE")
	private String menuType;
	
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

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
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
