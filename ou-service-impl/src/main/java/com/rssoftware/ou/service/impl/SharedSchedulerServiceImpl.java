package com.rssoftware.ou.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.common.BatchRequest;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.service.SharedSchedulerService;

@Service
public class SharedSchedulerServiceImpl implements SharedSchedulerService {
	
	private final static Logger logger = LoggerFactory.getLogger(SharedSchedulerServiceImpl.class);

	@Autowired
	private SchedulerFactoryBean sharedSpringScheduler;

	@Override
	public void schedule(String name, TypeOfBatch typeOfBatch, String cronExpression) throws SchedulerException {
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new SchedulerException("tenant ID is blank");
		}
		
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		Map<String, Object> jdMap = new HashMap<>();
		jdMap.put("destination", typeOfBatch);
		jdMap.put("tenantId", tenantId);
		
		JobDetail jd = JobBuilder.newJob(MessageDispatcherJob.class).withIdentity(getJobName(tenantId, name)).setJobData(new JobDataMap(jdMap)).build();

		sharedSpringScheduler.getScheduler().scheduleJob(jd, trigger);	
		
	} 
	
	@Override
	public void schedule(String name, TypeOfBatch typeOfBatch, String cronExpression,String refId) throws SchedulerException {
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new SchedulerException("tenant ID is blank");
		}
		
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		Map<String, Object> jdMap = new HashMap<>();
		jdMap.put("destination", typeOfBatch);
		jdMap.put("tenantId", tenantId);
		jdMap.put("refId", refId);
		JobDetail jd = JobBuilder.newJob(MessageDispatcherJob.class).withIdentity(getJobName(tenantId, name)).setJobData(new JobDataMap(jdMap)).build();

		sharedSpringScheduler.getScheduler().scheduleJob(jd, trigger);	
		
	} 
	@Override
	public void unSchedule(String name) throws SchedulerException {
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new SchedulerException("tenant ID is blank");
		}

		sharedSpringScheduler.getScheduler().deleteJob(new JobKey(getJobName(tenantId, name)));
	}
	
	@Override
	public Date getNextExecutionForJob(String name){
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			try {
				throw new SchedulerException("tenant ID is blank");
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
		}

		try {
			JobDetail jd = sharedSpringScheduler.getScheduler().getJobDetail(new JobKey(getJobName(tenantId, name)));
			if (jd != null){
				List<? extends Trigger> triggers = sharedSpringScheduler.getScheduler().getTriggersOfJob(new JobKey(getJobName(tenantId, name)));
				if (triggers != null){
					for (Trigger trigger:triggers){
						if (trigger instanceof CronTrigger){
							return ((CronTrigger)trigger).getNextFireTime();
						}
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	@Override
	public Date getPreviousExecutionForJob(String name){
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			try {
				throw new SchedulerException("tenant ID is blank");
			} catch (SchedulerException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
		}

		try {
			JobDetail jd = sharedSpringScheduler.getScheduler().getJobDetail(new JobKey(getJobName(tenantId, name)));
			if (jd != null){
				List<? extends Trigger> triggers = sharedSpringScheduler.getScheduler().getTriggersOfJob(new JobKey(getJobName(tenantId, name)));
				if (triggers != null){
					for (Trigger trigger:triggers){
						if (trigger instanceof CronTrigger){
							return ((CronTrigger)trigger).getPreviousFireTime();
						}
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public boolean isJobExist(String name){
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			try {
				throw new SchedulerException("tenant ID is blank");
			} catch (SchedulerException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
		}

		try {
			JobDetail jd = sharedSpringScheduler.getScheduler().getJobDetail(new JobKey(getJobName(tenantId, name)));
			if (jd != null){
				return true;
			}
		} catch (SchedulerException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}
		return false;
	}

	private static String getJobName(String tenantId, String name){
		return tenantId + "_" + name;
	}
	
	public static class MessageDispatcherJob implements Job {
		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
			TypeOfBatch typeOfBatch = (TypeOfBatch)jobExecutionContext.getMergedJobDataMap().get("destination");
			String tenantId = (String)jobExecutionContext.getMergedJobDataMap().get("tenantId");
			BatchRequest br = new BatchRequest(tenantId, typeOfBatch);
			String refId=(String)jobExecutionContext.getMergedJobDataMap().get("refId");
			if(refId!=null){
				br.setRefId(refId);
			}
			BeanLocator.getBean(EventBus.class).notify(CommonConstants.OU_BATCH_REQ_EVENT, Event.wrap(br));
		}

	}

	
}
