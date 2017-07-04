package com.rssoftware.ou.database.entity.global;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the GLOBAL_TEMPLATES database table.
 * 
 */
@Entity
@Table(name="GLOBAL_TEMPLATES")
@NamedQuery(name="GlobalTemplate.findAll", query="SELECT g FROM GlobalTemplate g")
public class GlobalTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TEMPLATE_NAME")
	private String templateName;

		
	@Column(name="CONTENT_BIN")
	private byte[] contentBin;

	
	@Column(name="CONTENT_CHAR")
	private String contentChar;

	@Column(name="TEMPLATE_TYPE")
	private String templateType;

	public GlobalTemplate() {
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public byte[] getContentBin() {
		return this.contentBin;
	}

	public void setContentBin(byte[] contentBin) {
		this.contentBin = contentBin;
	}

	public String getContentChar() {
		return this.contentChar;
	}

	public void setContentChar(String contentChar) {
		this.contentChar = contentChar;
	}

	public String getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

}