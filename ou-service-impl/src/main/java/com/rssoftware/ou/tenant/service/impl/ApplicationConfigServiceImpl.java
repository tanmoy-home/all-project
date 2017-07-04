package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfig;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfigPK;
import com.rssoftware.ou.domain.CoverageMode;
import com.rssoftware.ou.domain.OwnershipMode;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.tenant.dao.ApplicationConfigDao;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;

@Service
public class ApplicationConfigServiceImpl implements ApplicationConfigService {
	
	@Autowired
	private ApplicationConfigDao applicationConfigDao;
	
	private Map<String, Map<String, Set<String>>> tenantPaymentModeChannelMap = new HashMap<String, Map<String, Set<String>>>();
	private Map<String, Map<String, String>> tenantPaymentInfoMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, Set<String>>> tenantAgentDevMap = new HashMap<String, Map<String, Set<String>>>();
	private Map<String, List<String>> tenantCoverageMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> tenantOwnershipMap = new HashMap<String, List<String>>();

	/*private Map<String, BillerCoverage> coverageMap = new HashMap<>();
	private Map<String, BillerOwnership> ownershipMap = new HashMap<>();*/

	@Override
	public String getValueByName(String applicationConfigName) {
		ApplicationConfigPK appConfigPk = new ApplicationConfigPK();
		appConfigPk.setConfigParam(applicationConfigName);
		appConfigPk.setConfigParamGroup(applicationConfigName);
		ApplicationConfig ac = applicationConfigDao.get(appConfigPk);
		if (ac != null){
			return ac.getConfigParamValue();
		}
		
		return null;
	}
	
	@Override
	public String getValueByNameAndGroup(String applicationConfigName, String applicationConfigGroupName) {
		ApplicationConfigPK appConfigPk = new ApplicationConfigPK();
		appConfigPk.setConfigParam(applicationConfigName);
		appConfigPk.setConfigParamGroup(applicationConfigGroupName);
		ApplicationConfig ac = applicationConfigDao.get(appConfigPk);
		if (ac != null){
			return ac.getConfigParamValue();
		}
		
		return null;
	}
	
	@Override
	public List<ApplicationConfig> getValuesByGroup(String applicationConfigGroupName) {
		List<ApplicationConfig> appConfigList = applicationConfigDao.getAll();
		List<ApplicationConfig> filteredAppConfigList = new ArrayList<>();
		if (applicationConfigGroupName != null){
			for(ApplicationConfig appConfig : appConfigList) {
				if(applicationConfigGroupName.equalsIgnoreCase(appConfig.getConfigParamGroup())) {
					filteredAppConfigList.add(appConfig);
				}
			}
			return filteredAppConfigList;
		}		
		return filteredAppConfigList;
	}
	
	@Override
	public void refresh() {
		String tenantId = TransactionContext.getTenantId();
		if(null == tenantPaymentModeChannelMap.get(tenantId)) {
			tenantPaymentModeChannelMap.put(tenantId, new HashMap<String, Set<String>>());
		}
		if(null == tenantPaymentInfoMap.get(tenantId)) {
			tenantPaymentInfoMap.put(tenantId, new HashMap<String, String>());
		}
		if(null == tenantAgentDevMap.get(tenantId)) {
			tenantAgentDevMap.put(tenantId, new HashMap<String, Set<String>>());
		}
		if(null == tenantCoverageMap.get(tenantId)) {
			tenantCoverageMap.put(tenantId, new ArrayList<String>());
		}
		if(null == tenantOwnershipMap.get(tenantId)) {
			tenantOwnershipMap.put(tenantId, new ArrayList<String>());
		}
		
		List<ApplicationConfig> acList = applicationConfigDao.getAll();
		if (acList != null) {
			for (ApplicationConfig appConfig : acList) {
				if (appConfig.getConfigParamGroup().equals(
						CommonConstants.PAYMENT_MODE_Vs_CHANNEL)) {
					if (PaymentMode.getFromExpandedForm(appConfig
							.getConfigParam()) != null) {
						Set<String> set = splitParamValueSet(appConfig);
						tenantPaymentModeChannelMap.get(tenantId).put(
								appConfig.getConfigParam(), set);
					}
				} else if (appConfig.getConfigParamGroup().equals(
						CommonConstants.PAYMENT_MODE_Vs_INFORMATION)) {
					if (PaymentMode.getFromExpandedForm(appConfig
							.getConfigParam()) != null) {
						tenantPaymentInfoMap.get(tenantId).put(
								appConfig.getConfigParam(),
								appConfig.getConfigParamValue());
					}
				} else if (appConfig.getConfigParamGroup().equals(
						CommonConstants.CHANNEL_Vs_TAG)) {
					if (PaymentChannel.getFromExpandedForm(appConfig
							.getConfigParam()) != null) {

						Set<String> set = splitParamValueSet(appConfig);
						tenantAgentDevMap.get(tenantId).put(
								appConfig.getConfigParam(), set);
					}

				} else if (appConfig.getConfigParamGroup().equals(
						CommonConstants.APPLICATION_CONFIG_COVERAGE)) {
					if (CoverageMode.getExpCoverageMode(appConfig
							.getConfigParam()) != null) {
						List<String> coverageList = splitParamValueList(appConfig);
						tenantCoverageMap.get(tenantId).addAll(coverageList);

					}
				} else if (appConfig.getConfigParamGroup().equals(
						CommonConstants.APPLICATION_CONFIG_OWNERSHIP)) {
					if (OwnershipMode.getExpOwnershipMode(appConfig
							.getConfigParam()) != null) {
						List<String> ownerpshipList = splitParamValueList(appConfig);
						tenantOwnershipMap.get(tenantId).addAll(ownerpshipList);

					}
				}
			}
		}
	}
	
	private Set<String> splitParamValueSet(ApplicationConfig appConfig) {
		Set<String> set = new HashSet<>();
		set.addAll(splitParamValueList(appConfig));
		return set;
	}
	
	private List<String> splitParamValueList(ApplicationConfig appConfig) {
		List<String> list = new ArrayList<>(1);
		if(appConfig.getConfigParamValue() != null && !"".equalsIgnoreCase(appConfig.getConfigParamValue())) {
			if(appConfig.getConfigParamValue().indexOf("~") > 1) {
				list = Arrays.asList(appConfig.getConfigParamValue().split("~"));		
			} else {
				list.add(appConfig.getConfigParamValue());
			}
		}
		return list;
	}
	
	@Override
	public Map<String, Set<String>> getPaymentModeChannelMap() {
		return tenantPaymentModeChannelMap.get(TransactionContext.getTenantId());
	}

	@Override
	public Map<String, String> getPaymentInfoMap() {
		return tenantPaymentInfoMap.get(TransactionContext.getTenantId());
	}

	@Override
	public Map<String, Set<String>> getAgentDevMap() {
		return tenantAgentDevMap.get(TransactionContext.getTenantId());
	}
	@Override
	public List<String> getCoverageMap() {
		return tenantCoverageMap.get(TransactionContext.getTenantId());
	}
	
	@Override
	public List<String> getOwnershipMap() {
		return tenantOwnershipMap.get(TransactionContext.getTenantId());
	}
}