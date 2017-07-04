package com.rssoftware.ou.batch.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.common.FileSearch;
import com.rssoftware.ou.batch.to.BillDump;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.TypeOfBatch;

import reactor.bus.Event;
import reactor.bus.EventBus;


@Component
public class BillFetchJob {
	
	
	@Autowired
	private FileSearch fileSearch;

	private static Log logger = LogFactory.getLog(BillFetchJob.class);
	//private static final String inputDirectory = "E:/BBPS/WatchDirectory_";
	//private static final String zeroByteFile = "ZERO_BYTE_FILE";
	
	@Value("${inputDirectory}")
    private String inputDirectory;

	@Value("${zeroByteFile}")
    private String zeroByteFile;

	
	
	public void watchDirectory()
	{
		if(!new File(inputDirectory).exists())
			try {
				Path path = Paths.get(inputDirectory);
				logger.info("inputDirectory "+inputDirectory+" create : "+Files.createDirectories(path));
			} catch (IOException e) {
				logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
				return;
			}
		
		Map<String,String> billerDetails = fileSearch.billerSearch(inputDirectory, zeroByteFile);
		if(billerDetails!=null){
			
			billerDetails.forEach((k,v)->logger.info("BILLER ID: " + v + " :::::: BILLER FILE: " + k));
			
			for (Map.Entry<String, String> entry : billerDetails.entrySet()) {
				BillDump billDump = new BillDump();
				billDump.setBillerId(entry.getValue());
				billDump.setBillerOU(TransactionContext.getTenantId());
				billDump.setInputFile(entry.getKey());
				billDump.setBatchType(TypeOfBatch.BILL_FETCH);
				BeanLocator.getBean(EventBus.class).notify(CommonConstants.OU_BATCH_FILE_EVENT, Event.wrap(billDump));
			}
			
			
		}
	}
    
    
	public void billFetch(String billerOU, String billerId, String inputFile) {
		TransactionContext.putTenantId(billerOU);

		String[] springConfig = { "Job.xml" };
		ClassPathXmlApplicationContext  context = new ClassPathXmlApplicationContext(springConfig);
		
		File file = new File(inputFile);
		String absolutePath = inputFile.substring(0,inputFile.lastIndexOf(File.separator));
		String originalFileName = file.getName();
		File validationReport = new File(absolutePath+File.separator+originalFileName+"_validationReport.txt");
		File processingFile = new File(absolutePath+File.separator+originalFileName+".Processing");
		File processedFile = new File(absolutePath+File.separator+originalFileName+".Processed");

		try{
			if(file.exists())
			{
				file.renameTo(processingFile);
				
				//delete the old file
				if (validationReport.exists() && validationReport.delete()) {
				  //boolean deleted = validationReport.delete();
					logger.info("Validation Report " + validationReport.getName() + " successfully deleted.");
				}
				//create a new validation report file
				validationReport.createNewFile();
			}
			else
			{
				logger.info(inputFile+" doesn't exist !!!!!!!!!!!!!!!!!!!!!!!");
				context.close();
				return;
			}
		}catch(Exception e){
				logger.info(e.getMessage());
				context.close();
				return;
		}
		
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("billFetchJob");
		
		try {

			JobParameters param = new JobParametersBuilder().
					addString("biller_id", "\'"+billerId+"\'")
					.addString("schema_name", billerOU)
					.addString("input_file", processingFile.getAbsolutePath())
					.addString("validation_Report", validationReport.getAbsolutePath())
					.addDate("date", new Date())
					.toJobParameters();

			
			JobExecution execution = jobLauncher.run(job, param);
			logger.info("Exit Status : " + execution.getStatus());
			execution.getStatus();

			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason : " + execution.getAllFailureExceptions());
				execution.stop();
				execution.setExitStatus(ExitStatus.FAILED);
			}

		} catch (Exception e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		context.close();
		
		if(processingFile.exists())
			processingFile.renameTo(processedFile);
		
		if(validationReport.length() == 0 && validationReport.delete()){
			logger.info("Old file " + validationReport.getName() + " successfully deleted.");
		}
		logger.info("Done >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
	}
	
}
