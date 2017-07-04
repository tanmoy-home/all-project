package com.rssoftware.ou.helper;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;


public class CrDrStanGenerator {
	
	//signifies the last number used in generating stan for ReqCr/ReqDr flows
	private static AtomicInteger currentCrDrStanIdx = new AtomicInteger();
	
	
	/*
	 * Boot instance must be started with --bootInstanceId=<2 digit int>
	 * e.g. --bootInstanceId=01
	 * --bootInstanceId=22
	 */
	@Value("${bootInstanceId}")
	private static int bootInstanceId;
	
	public static  int generateStanForCrDr() {
		int currentCrDrStanIdxInt;
		currentCrDrStanIdx.compareAndSet(10000, 0);
		currentCrDrStanIdxInt = currentCrDrStanIdx.incrementAndGet();
		int stan = (bootInstanceId * 10000) + (currentCrDrStanIdxInt);
		return stan;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

