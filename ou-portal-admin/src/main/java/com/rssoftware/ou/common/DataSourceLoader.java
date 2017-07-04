package com.rssoftware.ou.common;

import java.util.Map;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.validation.BindException;

public class DataSourceLoader implements ImportBeanDefinitionRegistrar, EnvironmentAware {
//	@Value("${datasource.mode}")
//	private String datasourceMode;
	
	private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment)environment;
    }
    
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    	System.out.println("Registering base Data Source");
    	String datasourceMode = environment.getProperty("datasource.mode");
    	if (datasourceMode != null && datasourceMode.equals("BOOT")){
        	DataSourceSettings settings = resolveSettings();
            if (settings == null || settings.clientDataSources() == null || settings.clientDataSources().isEmpty()){
            	System.err.println("Failed to initialize dataSources");
            	System.exit(1);
            }
            else {
            	createDsBean(registry, settings.clientDataSources().get(0));
            }
    	}
    	else {
    		createDsBean(registry, null);
    	}
    }

    private void createDsBean(BeanDefinitionRegistry registry, Map<String, String> datasourceDetails) {
    	String datasourceMode = environment.getProperty("datasource.mode");
    	if (datasourceMode != null && datasourceMode.equals("BOOT")){
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
           	beanDefinition.setPrimary(true);
    		beanDefinition.setBeanClass(BBPSDataSource.class);
            beanDefinition.getPropertyValues().addPropertyValues(datasourceDetails);
            beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        	registry.registerBeanDefinition("ds", beanDefinition);
    	}
//    	else {
//    		Map<String, String> jndiDSProps = new HashMap<String, String>();
//    		jndiDSProps.put("jndiName", CommonConstants.PLATFORM_DATASOURCE_JNDI);
//    		beanDefinition.setBeanClass(JndiObjectFactoryBean.class);
//    		beanDefinition.getPropertyValues().addPropertyValues(jndiDSProps);
//    	}

    }
    
    private DataSourceSettings resolveSettings() {
        DataSourceSettings settings = new DataSourceSettings();
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<Object>(settings);
        factory.setTargetName("ds");
        factory.setPropertySources(environment.getPropertySources());
        factory.setConversionService(environment.getConversionService());
        try {
            factory.bindPropertiesToTarget();
        }
        catch (BindException ex) {
            throw new FatalBeanException("Could not bind DataSourceSettings properties", ex);
        }
        return settings;
    }
}
