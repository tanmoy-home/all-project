package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "admin_tbl_user_password_hist")
public class ChangePasswordEntity implements Serializable{

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 1369279224740734441L;

	@Id
	@SequenceGenerator(name="password_seq_gen",sequenceName="ADMIN_TBL_USER_PWD_HIST_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="password_seq_gen")
	private Long id;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "RESET_ON")
	private Timestamp resetOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getResetOn() {
		return resetOn;
	}

	public void setResetOn(Timestamp resetOn) {
		this.resetOn = resetOn;
	}
	
}