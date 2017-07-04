package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class CustomerRegistrationView implements Serializable{
	
	private String custID;
    private String bbpouID;
    private String custName;
    private String custGender;
    private String custDob;
    private String custMobile;
    private String custEmail;
    private String custAddrLine1;
    private String custAddrLine2;
    private String custAddrLine3;
    private String custCity;
    private String custState;
    private String custPin;
    private String custAadhaar;
    private String custPan;
    private String custPassport;
    private String effectiveFrom;
    private String effectiveTo;

    public String getCustID() {
            return custID;
    }
    public void setCustID(String custID) {
            this.custID = custID;
    }
    public String getBbpouID() {
            return bbpouID;
    }
    public void setBbpouID(String bbpouID) {
            this.bbpouID = bbpouID;
    }
    public String getCustName() {
            return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
   }
    public String getCustGender() {
        return custGender;
   }
   public void setCustGender(String custGender) {
        this.custGender = custGender;
   }
   public String getCustDob() {
        return custDob;
   }
   public void setCustDob(String custDob) {
        this.custDob = custDob;
   }
   public String getCustMobile() {
        return custMobile;
   }
   public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
   }
   public String getCustEmail() {
        return custEmail;
   }
  public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
   }
  public String getCustAddrLine1() {
        return custAddrLine1;
   }
  public void setCustAddrLine1(String custAddrLine1) {
        this.custAddrLine1 = custAddrLine1;
  }
  public String getCustAddrLine2() {
        return custAddrLine2;
  }
  public void setCustAddrLine2(String custAddrLine2) {
        this.custAddrLine2 = custAddrLine2;
  }
  public String getCustAddrLine3() {
      return custAddrLine3;
  }
 public void setCustAddrLine3(String custAddrLine3) {
      this.custAddrLine3 = custAddrLine3;
  }
 public String getCustCity() {
      return custCity;
  }
 public void setCustCity(String custCity) {
      this.custCity = custCity;
 }
 public String getCustState() {
      return custState;
 }
 public void setCustState(String custState) {
      this.custState = custState;
 }
 public String getCustPin() {
      return custPin;
 }
 public void setCustPin(String custPin) {
      this.custPin = custPin;
 }
 public String getCustAadhaar() {
      return custAadhaar;
 }
 public void setCustAadhaar(String custAadhaar) {
      this.custAadhaar = custAadhaar;
 }
 public String getCustPan() {
      return custPan;
 }
 public void setCustPan(String custPan) {
      this.custPan = custPan;
 }
 public String getCustPassport() {
      return custPassport;
 }
 
 public void setCustPassport(String custPassport) {
     this.custPassport = custPassport;
}
public String getEffectiveFrom() {
     return effectiveFrom;
}
public void setEffectiveFrom(String effectiveFrom) {
     this.effectiveFrom = effectiveFrom;
}
public String getEffectiveTo() {
     return effectiveTo;
}
public void setEffectiveTo(String effectiveTo) {
     this.effectiveTo = effectiveTo;
}
@Override
public String toString() {
     return "CustomerRegistrationView [custID=" + custID + ", bbpouID=" + bbpouID + ", custName=" + custName
                     + ", custGender=" + custGender + ", custDob=" + custDob + ", custMobile=" + custMobile + ", custEmail="
                     + custEmail + ", custAddrLine1=" + custAddrLine1 + ", custAddrLine2=" + custAddrLine2
                     + ", custAddrLine3=" + custAddrLine3 + ", custCity=" + custCity + ", custState=" + custState
                     + ", custPin=" + custPin + ", custAadhaar=" + custAadhaar + ", custPan=" + custPan + ", custPassport="
                     + custPassport + ", effectiveFrom=" + effectiveFrom + ", effectiveTo=" + effectiveTo + "]";
}

  



}
