package com.rssoftware.ou.database.entity.tenant.admin;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name="admin_tbl_USER")
public class UserEntity implements Serializable{

	private static final long serialVersionUID = 8187995455668977894L;

	@Id 
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@SequenceGenerator(name="user_seq_gen",sequenceName="ADMIN_TBL_USER_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq_gen")
	@Column(name="id", unique=true, nullable=false)
	private Long id;

	//@NotEmpty
	@Column(name="USER_NAME", nullable=false)
	private String username;
	
	//@NotEmpty
	@Column(name="PASSWORD", nullable=false)
	private String password;
		
	//@NotEmpty
	@Column(name="FIRST_NAME", nullable=false)
	private String firstName;

	//@NotEmpty
	@Column(name="LAST_NAME")
	private String lastName;

	//@NotEmpty
	@Column(name="EMAIL", nullable=false)
	private String email;	
	
	@Column(name = "CONTACT_NUMBER", nullable=false)
	private String contactNumber;
	
	@Column(name = "RESET_PASSWORD_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean resetPasswordFlag;	
		
	//@NotEmpty
	@Column(name = "user_ref_id")
	private String userRefId;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "pincode")
	private Long pincode;
	
	//@NotEmpty
	@Column(name = "mobile", nullable=false)
	private Long mobile;	

	@Column(name = "pan")
	private String pan;
	
	@Column(name = "aadhar")
	private String aadhar;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_ON")
	private Timestamp createdOn;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "UPDATED_ON")
	private Timestamp updatedOn;
	
	//@Column(name = "IS_ACTIVE",columnDefinition = "TINYINT")
	@Column(name = "IS_ACTIVE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isActive;
	
	//@Column(name = "IS_ACCOUNT_LOCKED",columnDefinition = "TINYINT")
	@Column(name = "IS_ACCOUNT_LOCKED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isAccountLocked;
	
	@Column(name = "IS_PWD_EXPIRED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isPasswordExpired;

	@Column(name = "LAST_LOCK_ON")
	private Timestamp lastLockOn;

	//@NotEmpty
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "admin_tbl_user_role_mapping", 
             joinColumns = { @JoinColumn(name = "user_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<RoleEntity> roles = new HashSet<RoleEntity>();

	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Boolean getResetPasswordFlag() {
		return resetPasswordFlag;
	}

	public void setResetPasswordFlag(Boolean resetPasswordFlag) {
		this.resetPasswordFlag = resetPasswordFlag;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
	
	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	
	public Boolean getIsAccountLocked() {
		return isAccountLocked;
	}

	public void setIsAccountLocked(Boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}
	
	
	public String getUserRefId() {
		return userRefId;
	}

	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Boolean getIsPasswordExpired() {
		return isPasswordExpired;
	}

	public void setIsPasswordExpired(Boolean isPasswordExpired) {
		this.isPasswordExpired = isPasswordExpired;
	}
	
	public Timestamp getLastLockOn() {
		return lastLockOn;
	}

	public void setLastLockOn(Timestamp lastLockOn) {
		this.lastLockOn = lastLockOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserEntity))
			return false;
		UserEntity other = (UserEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username
				+ ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", contactNumber=" + contactNumber + ", resetPasswordFlag="
				+ resetPasswordFlag + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy
				+ ", updatedOn=" + updatedOn + ", isActive=" + isActive
				+ ", isAccountLocked=" + isAccountLocked + ", roles=" + roles
				+ "]";
	}

}
