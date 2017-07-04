package com.rssoftware.ou;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;

public class OUFilter implements Filter {
	@Value("${application.mode}")
	private String applicationMode;
	
	@Override
	public void destroy() {
		

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (applicationMode != null && !"".equals(applicationMode.trim())){
			if (req instanceof HttpServletRequest){
				HttpServletRequest request = (HttpServletRequest)req;
				String uri = request.getRequestURI();
				
				if ("UI".equals(applicationMode)){
					
				}
				else if ("API".equals(applicationMode)){
					if (uri.contains("/resource/")){
						 HttpServletResponse resp = (HttpServletResponse) res;
					    resp.reset();
					    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					    return;
					}
				}
			}
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
