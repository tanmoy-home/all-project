package com.rssoftware.ou.portal.web;

import static reactor.bus.selector.Selectors.$;

import java.io.IOException;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.rssoftware.ou.IntegrationChannelConfig;
import com.rssoftware.ou.batch.processor.BOUBlrReconProcessor;
import com.rssoftware.ou.batch.processor.BatchFileProcessor;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.consumer.BatchRequestProcessor;
import com.rssoftware.ou.consumer.BillFetchProcessor;
import com.rssoftware.ou.consumer.BillerSimulatorProcessor;
import com.rssoftware.ou.consumer.CUResponseProcessor;
import com.rssoftware.ou.consumer.OURequestProcessor;
import com.rssoftware.ou.consumer.ReconciliationProcessor;
import com.rssoftware.ou.consumer.SettlementFilePersisterProcessor;
import com.rssoftware.ou.portal.config.CustomThymleafResolver;
import com.rssoftware.ou.service.ServiceConfiguration;

import reactor.Environment;
import reactor.bus.EventBus;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rssoftware.ou.portal.web","com.rssoftware.ou.portal.service","com.rssoftware.ou.controller",
		"com.rssoftware.ou.cbs","com.rssoftware.ou.cbsiso", "com.rssoftware.ou.aop", "com.rssoftware.ou.batch", "com.rssoftware.ou.security" })
@Import(value = { ServiceConfiguration.class, IntegrationChannelConfig.class })
@EnableAutoConfiguration(exclude = { JmxAutoConfiguration.class, TemplateResolver.class})
@IntegrationComponentScan({ "com.rssoftware.ou.gateway","com.rssoftware.ou.cbsiso.gateway" })
public class ApplicationContext implements CommandLineRunner {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private CUResponseProcessor cuResponseProcessor;

	@Autowired
	private BatchRequestProcessor batchRequest;

	@Autowired
	private BatchFileProcessor batchFile;
	
	@Autowired
	private SettlementFilePersisterProcessor settlementFilePersisterProcessor;

	@Autowired
	private BillFetchProcessor billFetchProcessor;
	
	@Autowired
	private BillerSimulatorProcessor billerSimulatorProcessor;
	
	@Autowired
	private OURequestProcessor ouRequestProcessor;

	@Autowired
	private ReconciliationProcessor reconciliationProcessor;
	
	@Autowired
	private BOUBlrReconProcessor bouBlrRecon;
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationContext.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		eventBus.on($(CommonConstants.CU_RESPONSE_EVENT), cuResponseProcessor);
		eventBus.on($(CommonConstants.OU_BATCH_REQ_EVENT), batchRequest);
		eventBus.on($(CommonConstants.OU_BATCH_FILE_EVENT), batchFile);
		eventBus.on($(CommonConstants.SETTLEMENT_FILE_EVENT), settlementFilePersisterProcessor);
		eventBus.on($(CommonConstants.OU_BILL_FETCH_EVENT), billFetchProcessor);
		eventBus.on($(CommonConstants.OU_BILLER_SIMULATOR_EVENT), billerSimulatorProcessor);
		eventBus.on($(CommonConstants.OU_REQUEST_EVENT), ouRequestProcessor);
		eventBus.on($(CommonConstants.RECONCILIATION_FILE_EVENT), reconciliationProcessor);				
		eventBus.on($(CommonConstants.BOU_BLR_RECON_EVENT), bouBlrRecon);				

	}

	@Autowired
	SpringTemplateEngine templateEngine;

	@Bean
	public TemplateResolver templateResolver() {
		TemplateResolver resolver = new TemplateResolver();
		resolver.setResourceResolver(new CustomThymleafResolver());
		resolver.setPrefix("");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("LEGACYHTML5");
		resolver.setCharacterEncoding(CharEncoding.UTF_8);
		resolver.setOrder(1);
		resolver.setCacheable(false);

		templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		return resolver;
	}
	
	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	EventBus createEventBus(Environment env) {
		return EventBus.create(env, Environment.THREAD_POOL);
	}
	/*@Bean(name="multipartResolver") 
    public CommonsMultipartResolver getResolver() throws IOException{
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
                resolver.setMaxUploadSize(78643200);//5MB
     return resolver;
    }*/
}
