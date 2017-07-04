package com.rssoftware.ou.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;



@Configuration 
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Value("${ou.tenantId}")
	private String tenantId;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;
	
	/*@Autowired
	private UserAccessService userAccessService;
	*/
	@Autowired
	@Qualifier("customFilterSecurityMetadata")
	FilterInvocationSecurityMetadataSource newSource;
	
	
	@Autowired
    PersistentLoginAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	LimitLoginAuthenticationProviderHandler limitLoginAuthenticationProviderHandler;

	//@Autowired
	//PersistentTokenRepository tokenRepository;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		TransactionContext.putTenantId(tenantId);
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.authorizeRequests().antMatchers("/*").authenticated();
		 http.csrf().disable()
		    .formLogin()
		        .loginPage("/login")
		        .loginProcessingUrl("/login")
		        .usernameParameter("username")
		        .passwordParameter("password")
		        .successHandler(authenticationSuccessHandler)
		        .failureHandler(limitLoginAuthenticationProviderHandler)
		        //.defaultSuccessUrl("/home")
		    .and()
		    .logout()
		        .logoutSuccessUrl("/login?logout")
		        .deleteCookies("JSESSIONID", "SESSION")
		    .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .invalidSessionUrl("/login?logout")
            //.sessionFixation()
            //.changeSessionId()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            .expiredUrl("/login?logout")
            .and()
		    .and().authorizeRequests()
		    .antMatchers(
		        "/home",
		        "/static/**",
		        "/static/**/**",
		        "/error"
		    ).permitAll()
		    //.and()
		    //.authorizeRequests()
		    .anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
//                        /FilterInvocationSecurityMetadataSource newSource = new CustomFilterSecurityMetadataSource();
                        AccessDecisionManager accessDecisionManager= new CustomAccessDecisionManager();
                        fsi.setSecurityMetadataSource(newSource);
                        fsi.setAccessDecisionManager(accessDecisionManager);
                        return fsi;
                    }
                }).anyRequest().denyAll()
             .and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		//.antMatchers("/").permitAll()
		.antMatchers("/service/**","/user/**","/role/**","/organization/**").access("hasRole('USER') or hasRole('BANK_SUPERADMIN') or hasRole('DBA')")
		.antMatchers("/newuser/**", "/delete-user-*").access("hasRole('BANK_SUPERADMIN')")
		.antMatchers("/edit-user-*").access("hasRole('BANK_SUPERADMIN') or hasRole('DBA')")
		.and().formLogin().loginPage("/login")
		.loginProcessingUrl("/login").usernameParameter("userName").passwordParameter("password")
		.successHandler(authenticationSuccessHandler)
		.failureHandler(limitLoginAuthenticationProviderHandler)
		.and()
				//.rememberMe().key("remember-me").tokenRepository(tokenRepository).tokenValiditySeconds(86400).and()
		.exceptionHandling().accessDeniedPage("/Access_Denied");
<<<<<<< HEAD
	}*/

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	/*@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
				"remember-me", userDetailsService, tokenRepository);
		return tokenBasedservice;
	}*/

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}
	


/*	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.authorizeRequests()
	  	.antMatchers("/", "/home").permitAll()
	  	.antMatchers("/admin/**").access("hasRole('ADMIN')")
	  	.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
	  	.and().formLogin().loginPage("/login")
	  	.usernameParameter("ssoId").passwordParameter("password")
	  	.and().csrf()
	  	.and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}*/
}
