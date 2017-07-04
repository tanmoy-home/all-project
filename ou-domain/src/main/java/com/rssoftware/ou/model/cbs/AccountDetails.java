package com.rssoftware.ou.model.cbs;

public class AccountDetails {

	private String acctNo;
	private double acctBalance;
	private double clearBalance;
	private int acctIndex;
	private String currency;
	private String currencyName;
	private String branchName;
	private String branchCity;
	private String jointAcct;
	private String customerName;
	private String customerRelation;
	private AccountType accountType;
	private String ifsc;
	private String mmid;
	private boolean aeba;
	private boolean meba;

	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public double getAcctBalance() {
		return acctBalance;
	}
	public void setAcctBalance(double acctBalance) {
		this.acctBalance = acctBalance;
	}
	public double getClearBalance() {
		return clearBalance;
	}
	public void setClearBalance(double clearBalance) {
		this.clearBalance = clearBalance;
	}
	public int getAcctIndex() {
		return acctIndex;
	}
	public void setAcctIndex(int acctIndex) {
		this.acctIndex = acctIndex;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchCity() {
		return branchCity;
	}
	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
	public String getJointAcct() {
		return jointAcct;
	}
	public void setJointAcct(String jointAcct) {
		this.jointAcct = jointAcct;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerRelation() {
		return customerRelation;
	}
	public void setCustomerRelation(String customerRelation) {
		this.customerRelation = customerRelation;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the ifsc
	 */
	public String getIfsc() {
		return ifsc;
	}
	/**
	 * @param ifsc the ifsc to set
	 */
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	/**
	 * @return the aeba
	 */
	public boolean isAeba() {
		return aeba;
	}
	/**
	 * @param aeba the aeba to set
	 */
	public void setAeba(boolean aeba) {
		this.aeba = aeba;
	}
	public boolean isMeba() {
		return meba;
	}
	public void setMeba(boolean meba) {
		this.meba = meba;
	}
	public String getMmid() {
		return mmid;
	}
	public void setMmid(String mmid) {
		this.mmid = mmid;
	}
	
}
