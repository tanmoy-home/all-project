package com.rssoftware.ou.batch.job;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.to.BillDump;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.model.tenant.BillerView;

import reactor.bus.Event;
import reactor.bus.EventBus;


@Component
public class BillPaymentJob {

	private static Log logger = LogFactory.getLog(BillPaymentJob.class);
	//private static final String outputDirectory = "E:/BBPS/BILLS/";
	//private static final String zeroByteFile = "ZERO_BYTE_FILE";
	
	@Value("${outputDirectory}")
    private String outputDirectory;

	@Value("${zeroByteFile}")
    private String zeroByteFile;
	
	public void billPayment(String billerOU, String billerId)
	{
		TransactionContext.putTenantId(billerOU);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String[] springConfig = { "JobWriter.xml" };
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("billPaymentJob");
		
		String outputFile = outputDirectory+File.separator+
							billerOU+File.separator+
							billerId+File.separator+
							"billDetails_"+timestamp.getTime()+".csv";
		
		String zeroFile = outputDirectory+File.separator+
							billerOU+File.separator+
							billerId+File.separator+
							"billDetails_"+timestamp.getTime()+"_"+zeroByteFile+".csv";

		try {

			JobParameters param = new JobParametersBuilder().
					addString("biller_id", "\'"+billerId+"\'")
					.addString("schema_name", billerOU)
					.addString("output_file", outputFile)
					.addDate("date", new Date())
					.toJobParameters();
			
			File file = new File(outputFile);
			if(file.exists())
				logger.info(file.getName() + " is deleted! "+file.delete());
			
			if(!file.getParentFile().exists())
			{
				Path path = Paths.get(file.getParent());
				logger.info("outputDirectory "+file.getParent()+" create : "+Files.createDirectories(path));
			}
			
			JobExecution execution = jobLauncher.run(job, param);
			logger.info("Exit Status : " + execution.getStatus());
			
			execution.getStatus();
			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason : " + execution.getAllFailureExceptions());
				execution.stop();
				execution.setExitStatus(ExitStatus.FAILED);
			}
			else
			{
				File zero = new File(zeroFile);
				zero.createNewFile();
				logger.info("Bill generation succesfully completed. Please collect the bill "+outputFile);
			}

		} catch (Exception e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		context.close();
		logger.info("Done");

	  }
	
	public void reportBillPayment(String billerOU, String billerId) {
		System.out.println("method reportBillPayment.....................");
		TransactionContext.putTenantId(billerOU);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String[] springConfig = { "bill_payment_report.xml" };
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("billPaymentReportJob");

//		String outputFile = outputDirectory + billerOU + "/" + billerId + "/" + "billPaymentReport_" + timestamp.getTime()
//				+ ".csv";
//		String zeroFile = outputDirectory + billerOU + "/" + billerId + "/" + "billPaymentReport_" + timestamp.getTime() + "_"
//				+ zeroByteFile + ".csv";
		
		String outputFile = outputDirectory + File.separator + billerOU + File.separator + billerId + File.separator
				+ "billPaymentReport_" + timestamp.getTime() + ".csv";

		String zeroFile = outputDirectory + File.separator + billerOU + File.separator + billerId + File.separator
				+ "billPaymentReport_" + timestamp.getTime() + "_" + zeroByteFile + ".csv";
		
		try {

			JobParameters param = new JobParametersBuilder().addString("biller_id", "\'" + billerId + "\'")
					.addString("schema_name", billerOU).addString("output_file", outputFile).addDate("date", new Date())
					.toJobParameters();

			File file = new File(outputFile);
			if (file.exists())
				logger.info(file.getName() + " is deleted! " + file.delete());
			
			if(!file.getParentFile().exists()) {
				Path path = Paths.get(file.getParent());
				logger.info("outputDirectory "+file.getParent()+" create : "+Files.createDirectories(path));
			}

			JobExecution execution = jobLauncher.run(job, param);
			logger.info("Exit Status : " + execution.getStatus());

			execution.getStatus();
			if (execution.getStatus().equals(BatchStatus.FAILED)) {
				logger.info("Failure reason : " + execution.getAllFailureExceptions());
				execution.stop();
				execution.setExitStatus(ExitStatus.FAILED);
			} else {
				File zero = new File(zeroFile);
				zero.createNewFile();
				logger.info("Bill generation succesfully completed. Please collect the bill " + outputFile);
			}

		} catch (Exception e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		context.close();
		logger.info("Bill Payment Report Done");
	}


	public void billPayment(String tenantId, List<BillerView> activeBillers) {
		for(BillerView bill: activeBillers){
			BillDump billDump = new BillDump();
			billDump.setBillerId(bill.getBlrId());
			billDump.setBillerOU(tenantId);
			billDump.setBatchType(TypeOfBatch.BILL_PAYMENT);
			BeanLocator.getBean(EventBus.class).notify(CommonConstants.OU_BATCH_FILE_EVENT, Event.wrap(billDump));
		}
	}
	
	public void reportBillPayment(String tenantId, List<BillerView> activeBillers) {
		for(BillerView bill: activeBillers){
			BillDump billDump = new BillDump();
			billDump.setBillerId(bill.getBlrId());
			billDump.setBillerOU(tenantId);
			billDump.setBatchType(TypeOfBatch.BILL_PAYMENT_REPORT);
			BeanLocator.getBean(EventBus.class).notify(CommonConstants.OU_BATCH_FILE_EVENT, Event.wrap(billDump));
		}
	}

	
}
