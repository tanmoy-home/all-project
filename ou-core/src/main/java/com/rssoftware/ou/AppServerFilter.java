package com.rssoftware.ou;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AppServerFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());

		if (path.startsWith("/js") || path.startsWith("/css") || path.startsWith("/images")) {
		    chain.doFilter(request, response);
		} 
		else if (path.startsWith("/pingTest")) {
			request.getRequestDispatcher(path).forward(request, response);
		} 
		else {
		    request.getRequestDispatcher("/api" + path).forward(request, response);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
