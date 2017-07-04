package com.rssoftware.ou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.database.entity.global.TenantDatasource;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfig;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;

@Configuration
public class TenantMailConfigurationManager
		implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {
	private final Logger log = LoggerFactory.getLogger(getClass());

	protected ConfigurableEnvironment environment;
	protected static BeanFactory beanFactory;
	protected static BeanDefinitionRegistry registry;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		TenantMailConfigurationManager.beanFactory = beanFactory;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		TenantMailConfigurationManager.registry = registry;
	}

	public void prepareMailConfigurationForTenant(String tenantId, String host, String username, String password,
			String port, String protocol, String authRequired, String isSslEnabled) {
		if (host != null && !CommonConstants.EMPTY_STRING.equalsIgnoreCase(host) && username != null
				&& !CommonConstants.EMPTY_STRING.equalsIgnoreCase(username) && password != null
				&& !CommonConstants.EMPTY_STRING.equalsIgnoreCase(password) && port != null
				&& !CommonConstants.EMPTY_STRING.equalsIgnoreCase(port)) {
			Map<String, Object> datasourceDetails = new HashMap<>();

			datasourceDetails.put("host", host);
			datasourceDetails.put("username", username);
			datasourceDetails.put("password", password);
			datasourceDetails.put("port", port);
			datasourceDetails.put("protocol", protocol);

			Properties props = new Properties();
			props.put("mail.smtp.auth", authRequired);
			props.put("mail.smtp.starttls.enable", isSslEnabled);
			props.put("mail.transport.protocol", protocol);
			datasourceDetails.put("javaMailProperties", props);

			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(JavaMailSenderImpl.class);
			beanDefinition.getPropertyValues().addPropertyValues(datasourceDetails);
			/*
			 * ConstructorArgumentValues c = new ConstructorArgumentValues();
			 * c.addIndexedArgumentValue(0, beanDefinition);
			 * beanDefinition.getConstructorArgumentValues().addArgumentValues(c
			 * );
			 */
			beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
			registry.registerBeanDefinition(CommonConstants.BEAN_MAIL_SENDER_PREFIX + tenantId, beanDefinition);
			log.debug("JavaMailSender initialized for " + tenantId);
		}
	}

	public void prepareMailConfigurationForTenant(String tenantId) {
		TransactionContext.putTenantId(tenantId);
		ApplicationConfigService configService = BeanLocator.getBean(ApplicationConfigService.class);
		List<ApplicationConfig> applicationConfigList = configService
				.getValuesByGroup(CommonConstants.APPLICATION_CONFIG_GROUP_SMTP);
		if (applicationConfigList != null) {
			String smtpHost = CommonConstants.EMPTY_STRING, smtpUserName = CommonConstants.EMPTY_STRING,
					smtpPassword = CommonConstants.EMPTY_STRING, smtpPort = CommonConstants.EMPTY_STRING,
					protocol = CommonConstants.EMPTY_STRING, authRequired = CommonConstants.EMPTY_STRING,
					isSslEnabled = CommonConstants.EMPTY_STRING;
			for (ApplicationConfig appConfig : applicationConfigList) {
				if ("smtp.host".equalsIgnoreCase(appConfig.getConfigParam())) {
					smtpHost = appConfig.getConfigParamValue();
				} else if ("smtp.port".equalsIgnoreCase(appConfig.getConfigParam())) {
					smtpPort = appConfig.getConfigParamValue();
				} else if ("smtp.username".equalsIgnoreCase(appConfig.getConfigParam())) {
					smtpUserName = appConfig.getConfigParamValue();
				} else if ("smtp.password".equalsIgnoreCase(appConfig.getConfigParam())) {
					smtpPassword = appConfig.getConfigParamValue();
				} else if ("smtp.protocol".equalsIgnoreCase(appConfig.getConfigParam())) {
					protocol = appConfig.getConfigParamValue();
				} else if ("smtp.auth".equalsIgnoreCase(appConfig.getConfigParam())) {
					authRequired = appConfig.getConfigParamValue();
				} else if ("smtp.tls".equalsIgnoreCase(appConfig.getConfigParam())) {
					isSslEnabled = appConfig.getConfigParamValue();
				}

			}
			prepareMailConfigurationForTenant(tenantId, smtpHost, smtpUserName, smtpPassword, smtpPort, protocol,
					authRequired, isSslEnabled);
		} else {
			throw new IllegalArgumentException("no entries found for tenant Id:" + tenantId);
		}
	}

	public void initializeMailConfiguration() {
		for (TenantDatasource tenantDS : BeanLocator.getBean(TenantDatasourceService.class).fetchAll()) {
			prepareMailConfigurationForTenant(tenantDS.getTenantId());
		}
	}

}
