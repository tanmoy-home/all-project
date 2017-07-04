package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TENANT_TEMPLATES database table.
 * 
 */
@Entity
@Table(name="TENANT_TEMPLATES")
@NamedQuery(name="TenantTemplate.findAll", query="SELECT t FROM TenantTemplate t")
public class TenantTemplate implements Serializable {
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

	public TenantTemplate() {
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