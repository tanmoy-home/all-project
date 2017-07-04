package com.rssoftware.ou;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;

@Configuration
public class IntegrationChannelConfig {

	@Bean
	public DirectChannel ouBillFetchRequestChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel ouBillFetchResponseChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel ouBillPaymentRequestChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel ouBillPaymentResponseChannel() {
		return new DirectChannel();
	}
	@Bean
	public DirectChannel ouBillPaymentQuickPayRequestChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public DirectChannel settlementFilePersisterChannel() {
		return new DirectChannel();
	}
	
}
