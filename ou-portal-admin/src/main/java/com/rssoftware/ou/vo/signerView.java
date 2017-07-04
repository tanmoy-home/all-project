package com.rssoftware.ou.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class signerView {

       private String userName;
       private String oldPassword;
       private String newPassword;
       private String confirmPassword;
       
       public String getUserName() {
              return userName;
       }
       public void setUserName(String userName) {
              this.userName = userName;
       }
       public String getOldPassword() {
              return oldPassword;
       }
       public void setOldPassword(String oldPassword) {
              this.oldPassword = oldPassword;
       }
       public String getNewPassword() {
              return newPassword;
       }
       public void setNewPassword(String newPassword) {
              this.newPassword = newPassword;
       }
       
       
       public String getConfirmPassword() {
              return confirmPassword;
       }
       public void setConfirmPassword(String confirmPassword) {
              this.confirmPassword = confirmPassword;
       }
       public String toString() {
              return ToStringBuilder.reflectionToString(this);
       }
       
}

