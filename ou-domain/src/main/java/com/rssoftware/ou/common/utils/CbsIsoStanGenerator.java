package com.rssoftware.ou.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;

public class CbsIsoStanGenerator {
	//signifies the last number used in generating stan for IMPS
	private static AtomicInteger currentIMPSStanIdx = new AtomicInteger();
	private static int bootInstanceId = Integer.parseInt(System.getProperty("bootInstanceId", "0"));
	
	public static /*synchronized*/ String generateStanForCBS() /*throws MaxStanReachedForInstanceException*/{
		/*int currentIMPSStanIdxInt = currentIMPSStanIdx.incrementAndGet();
		//TODO: need to check whether 'unique within business day' requirement is valid, if yes also need to implement database saving  
		//As per NPCI STAN doesn't have to be unique in a business day
		if((Math.log10(currentIMPSStanIdxInt)+1)>4){
			//throw new MaxStanReachedForInstanceException("Max STAN id reached for this boot instance");
			currentIMPSStanIdx.set(0);
			currentIMPSStanIdxInt = currentIMPSStanIdx.incrementAndGet();
		}*/
		int currentIMPSStanIdxInt;
		currentIMPSStanIdx.compareAndSet(10000, 0);
		currentIMPSStanIdxInt = currentIMPSStanIdx.incrementAndGet();
		int stan = (bootInstanceId * 10000) + (currentIMPSStanIdxInt);
		//System.out.println("Stan : "+stan);
		

	/*	if((Math.log10(stan)+1)>6){
			throw new MaxStanReachedForInstanceException("Max STAN id reached for this boot instance");
		}*/
		DateFormat df = new SimpleDateFormat("yyMMdd");
		String stanStr = df.format(new Date()) + stan;
		int l = 12 - stanStr.length();
		for(int i=0;i < l;i++){
			stanStr+="0";
		}
		return stanStr;
	}
	
	public static void main(String[] args) {
		System.out.println(generateStanForCBS());
	}
}
