package com.rssoftware.ou.security;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.controller.AdminController;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.iso8583.exception.ParseException;
import com.rssoftware.ou.service.UserService;

@Component
public class LimitLoginAuthenticationProviderHandler implements AuthenticationFailureHandler {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Value("${ou.tenantId}")
	private String tenantId;

	@Autowired
	UserService userService;

	private static final int MAX_ATTEMPTS = 3;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authexp) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		int attempts = 1;
		String userName = request.getParameter("username");
		UserEntity user = userService.findByUserName(userName);

		if (user != null) {
			if (session.getAttribute(userName + "Invalid_login_attempt") != null) {
				attempts = (Integer) session.getAttribute(userName + "Invalid_login_attempt");
				attempts++;
			}
			session.setAttribute(userName + "Invalid_login_attempt", attempts);

			if (user.getIsAccountLocked()) {
				redirectStrategy.sendRedirect(request, response, "/login?error=User Account is locked!");
				return;
			} else {
				if (attempts > MAX_ATTEMPTS) {
					user.setIsAccountLocked(true);
					user.setUpdatedBy(userName);
					user.setUpdatedOn(new Timestamp(new Date().getTime()));
					user.setLastLockOn(new Timestamp(new Date().getTime()));
					userService.saveUser(user);
				} 
				else if (authexp.getMessage().equals("Maximum sessions of 1 for this principal exceeded")) {
					redirectStrategy.sendRedirect(request, response, "/login?error=Invalid Session");
				} else { // Invalid credentials
					redirectStrategy.sendRedirect(request, response, "/login?error=Invalid User credentials");
					return;
				}

			}

		} else {
			redirectStrategy.sendRedirect(request, response, "/login?error=No user Exists");
		}
	}

}
