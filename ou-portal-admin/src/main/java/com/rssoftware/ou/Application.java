package com.rssoftware.ou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.rssoftware.ou.common.DataSourceLoader;

@SpringBootApplication
@Configuration
@Import(value = { DataSourceLoader.class })
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.rssoftware.ou.*")
@EntityScan("com.rssoftware.ou.database.entity.tenant.admin")
@EnableJpaRepositories("com.rssoftware.ou.repository")
public class Application extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected final SpringApplicationBuilder configure(
			final SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}
