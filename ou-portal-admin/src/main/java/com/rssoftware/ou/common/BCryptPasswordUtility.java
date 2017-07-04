package com.rssoftware.ou.common;

import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordUtility {
	
	public static void main(String args[]){
		String plainTextPass = "password";
		String userName = "agent1";
		
		if (args.length > 1){
			userName = args[0];
			plainTextPass = args[1];
		}
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String bcryptPass = bCryptPasswordEncoder.encode(plainTextPass);
		System.out.println("brcypted password is "+bcryptPass);
		byte[] encodedBytes = Base64.getEncoder().encode((userName+":"+bcryptPass).getBytes());
		System.out.println("Base 64 'user:password' encodedBytes " + new String(encodedBytes));
		
		System.out.println(bCryptPasswordEncoder.matches("password", "$2a$10$IgVZ5w5nmVudoJ9OtD0i7uCMsSYZtHjldnON0piOvpelUbJSclSEe"));
		
	}
}
