package com.rssoftware.ou;

import static reactor.bus.selector.Selectors.$;

import java.util.Properties;

import org.apache.catalina.connector.Connector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import reactor.Environment;
import reactor.bus.EventBus;

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
import com.rssoftware.ou.service.ServiceConfiguration;

@SpringBootApplication
@ComponentScan({ "com.rssoftware.ou.controller","com.rssoftware.ou.batch","com.rssoftware.ou.security"})
@Import(value = { ServiceConfiguration.class, IntegrationChannelConfig.class})
@IntegrationComponentScan({ "com.rssoftware.ou.gateway" })
public class Application implements CommandLineRunner {
	private static Log logger = LogFactory.getLog(Application.class);

	@Value("${smtp.host}")
    private String host;

	@Value("${smtp.port}")
    private String port;

	@Value("${smtp.username}")
    private String username;

	@Value("${smtp.password}")
    private String password;

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

	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	EventBus createEventBus(Environment env) {
		return EventBus.create(env, Environment.THREAD_POOL);
	}

	/* Commenting out for weblogic compatibility 
	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				logger.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}
		};
	}*/
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		OUFilter ouFilter = new OUFilter();
		registrationBean.setFilter(ouFilter);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl impl = new JavaMailSenderImpl();
		impl.setHost(host);
		if (port != null) {
			impl.setPort(Integer.parseInt(port));
		}
		impl.setUsername(username);
		impl.setPassword(password);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.starttls.enable", "true");
		impl.setJavaMailProperties(props);
		return impl;
	}	
	
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("Core started .............");
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
	
	@Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

            @Override
            public void customize(Connector connector) {
                connector.setAsyncTimeout(60000);
            }
        });
        return factory;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}