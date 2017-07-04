package com.rssoftware.ou.batch.processor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobBuilder;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.job.BillFetchJob;
import com.rssoftware.ou.batch.job.BillPaymentJob;
import com.rssoftware.ou.batch.to.BillDump;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.ParamService;

import reactor.bus.Event;
import reactor.fn.Consumer;


@Service
public class BOUBlrReconProcessor implements Consumer<Event<BillDump>>{
	private static Log logger = LogFactory.getLog(BOUBlrReconProcessor.class);
	
	@Autowired 
	private BillFetchJob billFetchJob;

	@Autowired 
	private BillPaymentJob billPaymentJob;

	@Autowired
	private IDGeneratorService idGeneratorService; 

	@Autowired
	private ParamService paramService; 
	
	@Value("${bouBlrReconSQL}")
	private String bouBlrsql;

	@Override
	public void accept(Event<BillDump> event) 
	{
		BillDump billDump = event.getData();
		TransactionContext.putTenantId(billDump.getBillerOU());
		String[] springConfig = { "BOUBlrReconJob.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("bouBlrReconJob");
		String outputFile = null;
		
		try {
			outputFile = retrieveFileNameBOU(billDump.getBillerId());
			JobParametersBuilder jobBuilder = new JobParametersBuilder()
					.addString("output_file", outputFile)
					.addString("sql", bouBlrsql+paramService.retrieveStringParamByName("RECON_TIMEOUT")+"' minute AND t.blr_id = '"+billDump.getBillerId()+"'");
					
			JobParameters param = jobBuilder.toJobParameters();
			
			File file = new File(outputFile);
			if(file.exists())
				logger.info(file.getName() + " is deleted! "+file.delete());

			JobExecution execution = jobLauncher.run(job, param);
			
			logger.info("Exit Status : " + execution.getStatus());
			
			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason : " + execution.getAllFailureExceptions());
				execution.stop();
				execution.setExitStatus(ExitStatus.FAILED);
			}
		} 
		catch (Exception e) {
			logger.error("Fetching BOU-Blr reconciliation data call failed", e); 
		}
		finally {
			logger.info("BOU-Blr Reconciliation Done!");
		}
	}
	
	private String retrieveFileNameBOU(String billerId) {
		String path = System.getProperty("reconFileLocation")+"/biller";
		String extension = "_"+billerId+"_"+idGeneratorService.getUniqueID(3, "")+".csv";
		String fileName = paramService.retrieveStringParamByName("BOU_BLR_RECON_FILE_NAME");
		String dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+"/"+fileName+extension;
	}
}
