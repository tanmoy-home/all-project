package com.rssoftware.ou.security.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.security.BasicAuthenticationProvider;
import com.rssoftware.ou.security.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfig  extends WebSecurityConfigurerAdapter{

	private static String REALM="SECURE_REALM";

	@Autowired
	private BasicAuthenticationProvider basicAuthenticationProvider;
	
	
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/APIService/**")
		.hasAnyRole("AccessApi")
				.and()
				.httpBasic()
				.realmName(REALM).authenticationEntryPoint(entryPoint()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.headers()
		        .addHeaderWriter(new StaticHeadersWriter("Server",""))
		        .and()
		        .headers()
		        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.DENY));
		}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws IsoMsgException {
		auth.authenticationProvider(basicAuthenticationProvider);
	}

	@Bean
	public RestAuthenticationEntryPoint entryPoint() {
		RestAuthenticationEntryPoint entrypoint = new RestAuthenticationEntryPoint();
		entrypoint.setRealmName(REALM);
		return entrypoint;
	}

}