package com.rssoftware.ou.common;

import java.util.regex.Pattern;

import ch.qos.logback.core.boolex.Matcher;

public class PasswordAdvisor{
	public static boolean checkPasswordStrength(String password){
		if(password.length() < 8){
			return false; //password should be minimun 8 character
		}
		else if (password.length() >=8){		      
			if(Pattern.compile("\\d+/").matcher(password).matches()){
				return false; //password should be minimun 8 character//password should be minimun 8 character
			}
			else if(Pattern.compile("/[a-z]/").matcher(password).matches()){
				return false; //password should be minimun 8 character
			}
			else if(Pattern.compile("/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/").matcher(password).matches()){
				return false; //password should be minimun 8 character
			}
			else if(password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")){
				return true; //password should be minimun 8 character
			}
		}
		return false;
	}
}