package com.rssoftware.ou.model.tenant;

public class PasswordWrapper {
	private String oldpass;
	private String newpass;
	private String confNewpass;
	public String getOldpass() {
		return oldpass;
	}
	public void setOldpass(String oldpass) {
		this.oldpass = oldpass;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	public String getConfNewpass() {
		return confNewpass;
	}
	public void setConfNewpass(String confNewpass) {
		this.confNewpass = confNewpass;
	}
	
}
