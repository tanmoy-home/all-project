package com.rssoftware.ou.batch.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.AgentDetails;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.tenant.dao.AgentDao;

public class CUAgentApprovalReqProcessor implements ItemProcessor<AgentDetails, AgentDetails> {

	 @Autowired
	 AgentDao agentDao;
	
	private ExecutionContext executionContext;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		executionContext = stepExecution.getJobExecution().getExecutionContext();
	}

	@Override
	public AgentDetails process(AgentDetails agentDetail) throws Exception {

		// List<AgentDetail> agentDetails =
		// agentDao.getAllAgentsByActivationStatus(EntityStatus.PENDING_ACTIVATION);
		// generateCUAgentApprovalBatchFile(agentDetails,outDir);
		//
		List<String> forCUApprovalAgents = null;
		if(null == this.executionContext.get("forCUApprovalAgents")){
			forCUApprovalAgents = new ArrayList<String>();
		}
		else{
			forCUApprovalAgents = (List<String>)this.executionContext.get("forCUApprovalAgents");
		}
		forCUApprovalAgents.add(agentDetail.getAgentId());
		
		this.executionContext.put("forCUApprovalAgents", forCUApprovalAgents);

		return agentDetail;
	}

}