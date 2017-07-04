package com.rssoftware.ou.portal.web;

import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class PortalConfig  extends WebMvcConfigurerAdapter {

//	private static final Logger log = LoggerFactory.getLogger(PortalConfig.class);
    
	@Value("${session.timeout}")
	private Integer timeoutMunites;
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/sessionout").setViewName("sessionout");
        registry.addViewController("/welcome").setViewName("welcome");
        registry.addViewController("/error404").setViewName("error404");
        registry.addViewController("/error401").setViewName("error401");
    }    
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webContentInterceptor());
        registry.addInterceptor(new PortalInterceptor());
    }
    
    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0);
        interceptor.setUseExpiresHeader(true);;
        interceptor.setUseCacheControlHeader(true);
        interceptor.setUseCacheControlNoStore(true);
        return interceptor;
    }
    
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
     
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
     
                ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error404");
                ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error401");
                ErrorPage errorEx = new ErrorPage(Throwable.class, "/errorEx");
                container.setSessionTimeout(timeoutMunites, TimeUnit.MINUTES);
     
                container.addErrorPages(error404, error401, errorEx);
            }
        };
    }
    
    /*@Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new SessionTimeoutCustomizer(timeoutMunites);
    }*/
    
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
      TomcatEmbeddedServletContainerFactory tomcat = new      TomcatEmbeddedServletContainerFactory() {
          @Override
          protected void postProcessContext(Context context) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
          }
        };

      tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
      return tomcat;
    }

    private Connector initiateHttpConnector() {
      Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
      connector.setScheme("http");
      connector.setPort(8080);
      connector.setSecure(false);
      connector.setRedirectPort(8443);

      return connector;
    }
    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

}
