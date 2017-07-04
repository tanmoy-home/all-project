package com.rssoftware.ou.model.tenant;

public class TenantTemplateView {

	private String templateName;
	private byte[] contentBin;
	private String contentChar;
	private String templateType;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public byte[] getContentBin() {
		return contentBin;
	}

	public void setContentBin(byte[] contentBin) {
		this.contentBin = contentBin;
	}

	public String getContentChar() {
		return contentChar;
	}

	public void setContentChar(String contentChar) {
		this.contentChar = contentChar;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

}
