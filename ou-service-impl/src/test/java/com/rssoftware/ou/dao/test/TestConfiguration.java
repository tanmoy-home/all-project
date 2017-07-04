/**
 * 
 */
package com.rssoftware.ou.dao.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.rssoftware.ou.service.ServiceConfiguration;

import reactor.Environment;
import reactor.bus.EventBus;


/**
 * @author rsdpp
 *
 */
@Configuration
@PropertySource("classpath:app.properties")
@Import(value=ServiceConfiguration.class)
public class TestConfiguration {

	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	EventBus createEventBus(Environment env) {
		return EventBus.create(env, Environment.THREAD_POOL);
	}
}
