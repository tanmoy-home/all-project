package com.rssoftware.ou.tenant.service;

import in.co.rssoftware.bbps.schema.AgentInstList;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.AgentInstView;

public interface AgentInstService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public AgentInstView getAgentInstById(String agentInstId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void submit(AgentInstView agentInstView) throws ValidationException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void save(AgentInstView agentInstView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(String agentInstId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<AgentInstView> getAgentInstListByOU();

	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public AgentInstList getAgentInstJaxb(List<AgentInstView> agentInstViews);*/

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public byte[] exportReportToExcel();

	/*
	 * @TenantTransactional(propagation = Propagation.REQUIRED, isolation =
	 * Isolation.READ_COMMITTED) public List<AgentInstView> searchByCriteria();
	 */
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<AgentInstView> fetchFunctionallyActiveListByOU(int pageNo,
			int pageSize);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void update(AgentInstView agentInstView) throws ValidationException;

}