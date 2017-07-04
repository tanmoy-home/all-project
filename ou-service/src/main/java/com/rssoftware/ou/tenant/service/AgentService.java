package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.tenant.AgentView;

public interface AgentService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public AgentView getAgentById(String agentId);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public AgentView getDefaultAgent(String paymentChannel);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public AgentView save(AgentView agentView) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(String agentId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	byte[] exportReportToExcel(String loggedInUsersOuId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<AgentView> getAllAgentList() throws DataAccessException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	AgentView getAgentByIdAndStatus(String agentId, String entytystatus);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<AgentView> getAllAgentsByInstituteId(String instId);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<AgentView> getAllPendingAgentsByInstituteId(String instId);

	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public AgentList getAgentJaxb(List<AgentView> agentInstViews);*/

	public double getAvailableBalance(CBSRequest req);


	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)	
	Agent updateStatus(AgentView agentView, EntityStatus entityStatus) throws ValidationException;*/

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void saveBulk (List<AgentDetail> agentList) throws IOException;
}
