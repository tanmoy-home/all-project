package com.rssoftware.ou.helper;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;


public class IMPSStanGenerator {
	
	//signifies the last number used in generating stan for IMPS
	private static AtomicInteger currentIMPSStanIdx = new AtomicInteger();
	
	
	/*
	 * Boot instance must be started with --bootInstanceId=<2 digit int>
	 * e.g. --bootInstanceId=01
	 * --bootInstanceId=22
	 */
	@Value("${bootInstanceId}")
	private static int bootInstanceId;
	
	public static /*synchronized*/ int generateStanForIMPS() /*throws MaxStanReachedForInstanceException*/{
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
		return stan;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

