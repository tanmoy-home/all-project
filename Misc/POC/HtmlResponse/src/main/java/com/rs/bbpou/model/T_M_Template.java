package com.rs.bbpou.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_m_template")
public class T_M_Template implements Serializable{

	

	@Id
	@Column(name="templateId")
	private  Integer templateId;
	
	@Column(name="templateName")
	private  String templateName ;
	@Column(name="templateType")
	private  String templateType ;
	@Column(name="charContent")
	private String charContent;
	
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getCharContent() {
		return charContent;
	}
	public void setCharContent(String charContent) {
		this.charContent = charContent;
	}
	
	
	
}

