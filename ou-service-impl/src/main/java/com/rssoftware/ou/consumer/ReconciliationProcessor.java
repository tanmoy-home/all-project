package com.rssoftware.ou.consumer;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import reactor.bus.Event;
import reactor.fn.Consumer;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.ReconSummaryView;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.ReconSummaryService;
import com.rssoftware.ou.tenant.service.TenantParamService;

@Service
public class ReconciliationProcessor implements Consumer<Event<RawDataView>> {
	private static Log logger = LogFactory.getLog(ReconciliationProcessor.class);

	@Autowired
	private IDGeneratorService idGeneratorService; 
	
	@Autowired
	private ReconSummaryService reconSummaryService;
	
	@Autowired
	private TenantParamService paramService;
	
	@Override
	public void accept(Event<RawDataView> arg0) {
		String METHOD_NAME = "ReconciliationProcessor starts..";
		if (logger.isDebugEnabled()) {
			logger.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(arg0.getData().getTenantId());
		
		String[] springConfigCOU = { "ReconciliationJobCOU.xml"};
		String[] springConfigBOU = { "ReconciliationJobBOU.xml"};

		ApplicationContext contextCOU = new ClassPathXmlApplicationContext(springConfigCOU);
		ApplicationContext contextBOU = new ClassPathXmlApplicationContext(springConfigBOU);

		JobLauncher jobLauncherCOU = (JobLauncher) contextCOU.getBean("jobLauncher");
		JobLauncher jobLauncherBOU = (JobLauncher) contextCOU.getBean("jobLauncher");

		Job jobCOU = (Job) contextCOU.getBean("reconciliationJobCOU");
		Job jobBOU = (Job) contextBOU.getBean("reconciliationJobBOU");

		BigDecimal reconMaxTry = new BigDecimal(paramService.retrieveStringParamByName("RECON_MAX_TRY"));

		try {

			JobParameters param = new JobParametersBuilder()
			.addString("id", idGeneratorService.getUniqueID(5, arg0.getData().getTenantId()))
			.addString("settlement_id", arg0.getData().getSettlementCycleId())
			.addDouble("recon_max_try", reconMaxTry.doubleValue())
			.toJobParameters();

			JobExecution execution = jobLauncherCOU.run(jobCOU, param);
			JobExecution executionBOU = jobLauncherBOU.run(jobBOU, param);
			logger.info("Exit Status COU: " + execution.getStatus()+", BOU: "+executionBOU.getStatus());
			if(execution.getStatus().equals(BatchStatus.FAILED) || executionBOU.getStatus().equals(BatchStatus.FAILED)) {
				logger.info("Failure reason CU: " + execution.getAllFailureExceptions()+", BOU: "+ executionBOU.getAllFailureExceptions());
				execution.stop();
				executionBOU.stop();
				execution.setExitStatus(ExitStatus.FAILED);
				executionBOU.setExitStatus(ExitStatus.FAILED);
			}
				long matchedTxnsCOU = execution.getExecutionContext().get("matchedTxns")!=null?(long)execution.getExecutionContext().getLong("matchedTxns"):0;
				long matchedTxnsBOU = executionBOU.getExecutionContext().get("matchedTxns")!=null?(long)executionBOU.getExecutionContext().getLong("matchedTxns"):0;

				long notInOuTxnsCOU = execution.getExecutionContext().get("notInOuTxns")!=null?(long)execution.getExecutionContext().getLong("notInOuTxns"):0;
				long notInOuTxnsBOU = executionBOU.getExecutionContext().get("notInOuTxns")!=null?(long)executionBOU.getExecutionContext().getLong("notInOuTxns"):0;

				logger.debug("JobCountProcessorCOU: "+notInOuTxnsCOU);

				long notInCuTxnsCOU = execution.getExecutionContext().get("notInCuTxns")!=null?(long)execution.getExecutionContext().getLong("notInCuTxns"):0;
				long notInCuTxnsBOU = executionBOU.getExecutionContext().get("notInCuTxns")!=null?(long)executionBOU.getExecutionContext().getLong("notInCuTxns"):0;

				long mismatchedTxnsCOU = execution.getExecutionContext().get("mismatchedTxns")!=null?(long)execution.getExecutionContext().getLong("mismatchedTxns"):0;
				long mismatchedTxnsBOU = executionBOU.getExecutionContext().get("mismatchedTxns")!=null?(long)executionBOU.getExecutionContext().getLong("mismatchedTxns"):0;

				long pendingTxnsCOU = execution.getExecutionContext().get("pendingTxns")!=null?(long)execution.getExecutionContext().getLong("pendingTxns"):0;
				long pendingTxnsBOU = executionBOU.getExecutionContext().get("pendingTxns")!=null?(long)executionBOU.getExecutionContext().getLong("pendingTxns"):0;

				long broughtForwardTxnsCOU = execution.getExecutionContext().get("broughtForwardTxns")!=null?(long)execution.getExecutionContext().getLong("broughtForwardTxns"):0;
				long broughtForwardTxnsBOU = executionBOU.getExecutionContext().get("broughtForwardTxns")!=null?(long)executionBOU.getExecutionContext().getLong("broughtForwardTxns"):0;

			ReconSummaryView reconSummaryView = new ReconSummaryView();
			reconSummaryView.setReconId(arg0.getData().getSettlementCycleId());
			reconSummaryView.setMatchedCount(new BigDecimal(matchedTxnsCOU+matchedTxnsBOU));
			reconSummaryView.setNotInCUCount(new BigDecimal(notInCuTxnsCOU+notInCuTxnsBOU));
			reconSummaryView.setNotInOUCount(new BigDecimal(notInOuTxnsCOU+notInOuTxnsBOU));
			reconSummaryView.setMismatchedCount(new BigDecimal(mismatchedTxnsCOU+mismatchedTxnsBOU));
			reconSummaryView.setPendingCount(new BigDecimal(pendingTxnsCOU+pendingTxnsBOU));
			reconSummaryView.setBroughtForwardCount(new BigDecimal(broughtForwardTxnsCOU+broughtForwardTxnsBOU));
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			reconSummaryView.setReconTs(timestamp);
			reconSummaryService.insert(reconSummaryView);
			
		}
		catch (Exception e) {
			logger.error("Reconciliation failed", e); 
		}
		finally {
			logger.info("Reconciliation Done!");
		}
	}
}