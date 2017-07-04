package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admin_tbl_PERSISTENT_LOGINS")
public class PersistentLoginEntity implements Serializable{
	
	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 5480781072243119146L;


	@Id
	@Column(name="USERID", unique=true, nullable=false)
	private String userid;
	
	@Column(name = "LOGGED_IN")
	private String logged_in;
	
	@Column(name = "LOGGED_OUT")
	private Timestamp logged_out;
	
	@Column(name="LOGIN_COUNT")
	private int login_count;
	
	@Column(name = "LOGGED_IN_IP")
	private String logged_in_ip;
	

	

	public int getLogin_count() {
		return login_count;
	}

	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}

	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLogged_in() {
		return logged_in;
	}

	public void setLogged_in(String logged_in) {
		this.logged_in = logged_in;
	}


	public Timestamp getLogged_out() {
		return logged_out;
	}

	public void setLogged_out(Timestamp logged_out) {
		this.logged_out = logged_out;
	}

	public String getLogged_in_ip() {
		return logged_in_ip;
	}

	public void setLogged_in_ip(String logged_in_ip) {
		this.logged_in_ip = logged_in_ip;
	}

	
}
