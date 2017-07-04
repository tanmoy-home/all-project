package com.rssoftware.ou.batch.writer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.batch.to.AgentDetails;
import com.rssoftware.ou.batch.to.Recon;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.database.entity.tenant.ReconDetails;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.tenant.dao.AgentDao;
import com.rssoftware.ou.tenant.dao.impl.TransactionDataDaoImpl;

public class AllCUApprovedAgentsWriter implements ItemWriter<AgentDetails>{
	private static Logger log = LoggerFactory.getLogger(AllCUApprovedAgentsWriter.class);

	@Autowired
	TransactionDataDaoImpl transactionDataDao;

	@Autowired
	AgentDao agentDao;
	
	private ExecutionContext executionContext;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution)
	{
	    this.executionContext = stepExecution.getJobExecution().getExecutionContext();
	}
	private AgentDetails formReconDetailsFromTD(AgentDetails item) {
		
		return item;
	}

	private TransactionData formTransactionData(Recon item) {
		TransactionDataPK id = new TransactionDataPK();
		id.setRefId(item.getRefId());
		id.setTxnType(item.getTxnType());
		TransactionData transactionData = transactionDataDao.get(id);
		transactionData.setReconTs(new Timestamp(System.currentTimeMillis()));
		transactionData.setReconCycleNo(new BigDecimal(item.getReconCycleNo().intValue()+1));
		return transactionData;
	}
	
	
	@SuppressWarnings("unchecked")
	private void process(AgentDetails item) throws Exception {
		System.out.println("in AllCUApprovedAgentsWriter");
		//ReconDetails reconDetails = new ReconDetails();
		List<AgentDetail> cuSentAgents=  agentDao.getAllAgentsByActivationStatus(EntityStatus.CU_SENT);
		
		AgentDetail approvedAgent = new AgentDetail();
		approvedAgent.setAgentId(item.getAgentId());
		approvedAgent.setAgentCUID(item.getAgentCUID());
		List<AgentDetail> approvedAgents = null;
		if(null == this.executionContext.get("approvedAgents")){
			approvedAgents = new ArrayList<AgentDetail>();
		}
		else{
			approvedAgents = (List<AgentDetail>)this.executionContext.get("approvedAgents");
		}
		approvedAgents.add(approvedAgent);
		
		this.executionContext.put("approvedAgents", approvedAgents);

		
		//this.executionContext.putLong("broughtForwardTxns", this.executionContext.getLong("broughtForwardTxns", 0 ) + 1 );
		//TransactionData transactionData = formTransactionData(item);
		//transactionData.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.PENDING.name());
		//transactionDataDao.createOrUpdate(transactionData);
	}	

	
	@Override
	public void write(List<? extends AgentDetails> items) throws Exception {
		if(items!=null){
				for(AgentDetails data: items)
				{
					process(data);

				}
		}
	}
	
}