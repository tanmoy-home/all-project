package com.rssoftware.ou.service;

import java.util.Date;

import org.quartz.SchedulerException;

import com.rssoftware.ou.common.TypeOfBatch;

public interface SharedSchedulerService {
	
	void schedule(String name, TypeOfBatch typeOfBatch, String cronExpression) throws SchedulerException;
	void schedule(String name, TypeOfBatch typeOfBatch, String cronExpression,String refId) throws SchedulerException;
	void unSchedule(String name) throws SchedulerException;
	Date getNextExecutionForJob(String name);
	Date getPreviousExecutionForJob(String name);
	boolean isJobExist(String name);
	
}
