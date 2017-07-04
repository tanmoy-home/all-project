package com.rssoftware.ou.tenant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfig;
import com.rssoftware.ou.model.tenant.BillerCoverageView;
import com.rssoftware.ou.model.tenant.BillerOwnershipView;

public interface ApplicationConfigService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String getValueByName(String applicationConfigName);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String getValueByNameAndGroup(String applicationConfigName, String applicationConfigGroupName);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<ApplicationConfig> getValuesByGroup(String applicationConfigGroupName);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public void refresh();
	
	public Map<String, Set<String>> getPaymentModeChannelMap();
	
	public Map<String, String> getPaymentInfoMap();
	
	public Map<String, Set<String>> getAgentDevMap();
	
	public List<String> getCoverageMap();
	
	public List<String> getOwnershipMap();
}