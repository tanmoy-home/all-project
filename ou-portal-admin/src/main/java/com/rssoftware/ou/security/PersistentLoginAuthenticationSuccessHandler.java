package com.rssoftware.ou.security;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.controller.AdminController;
import com.rssoftware.ou.database.entity.tenant.admin.ChangePasswordEntity;
import com.rssoftware.ou.database.entity.tenant.admin.PersistentLoginEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.service.PasswordService;
import com.rssoftware.ou.service.UserService;
import com.rssoftware.ou.service.impl.PersistentLoginEntityImpl;

@Component
public class PersistentLoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Value("${ou.tenantId}")
	private String tenantId;

	@Autowired
	UserService userService;

	@Autowired
	PersistentLoginEntityImpl persistentLoginImp;

	@Autowired
	Environment env;

	@Autowired
	PasswordService passwordService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		// int login_count=0;
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();

		String logged_in_ip = details.getRemoteAddress();
		UserEntity user = userService.findByUserName(userName);

		// check Lock-Unlock Account
		if (user.getIsAccountLocked()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			long lockTimeInMin = Long.parseLong(env.getProperty("lockTimeInMin"));
			long min5 = lockTimeInMin * 60 * 1000;
			try {
				Date lockTime = format.parse(user.getLastLockOn().toString());
				Date currDate = new Date();
				if (currDate.getTime() - min5 >= lockTime.getTime()) {
					user.setIsAccountLocked(false);
					user.setUpdatedBy(userName);
					user.setUpdatedOn(new Timestamp(new Date().getTime()));
					user.setLastLockOn(null);
					userService.saveUser(user);
				} else {
					redirectStrategy.sendRedirect(request, response, "/login?error=User Account is locked!");
					return;
				}
			} catch (java.text.ParseException e) {
				LOGGER.info("Error for Lock- Unlock user.");
			}
		}
		// check Lock-Unlock Account

		// Check Account Expiry
		/*Iterable<ChangePasswordEntity> passChangeRec = passwordService.findByUsernameOrderByResetOnDesc(userName);
		if (passChangeRec != null) {
			int expiryPasswordInDays = Integer.parseInt(env.getProperty("expiryPasswordInDays"));
			Calendar c = Calendar.getInstance();
			c.setTime(passChangeRec.iterator().next().getResetOn());// last password setup Date
			c.add(Calendar.DATE, expiryPasswordInDays);
			Date expDate = c.getTime();
			System.out.println(expDate);
			System.out.println(expDate.after(new Date()));
			if (expDate.after(new Date())) {
				user.setResetPasswordFlag(true);
			}
		}*/
		// Check Account Expiry

		PersistentLoginEntity persistentLoginEntity = new PersistentLoginEntity();
		persistentLoginEntity.setLogged_in(new Date().toString());
		persistentLoginEntity.setUserid(user.getUsername());
		persistentLoginEntity.setLogged_in_ip(logged_in_ip);
		// persistentLoginEntity.setLogin_count(++login_count);

		LOGGER.info("persistentLoginEntity Name" + persistentLoginEntity.getUserid());

		persistentLoginImp.saveLoginEntity(persistentLoginEntity);
		HttpSession httpSession = request.getSession(false);
		if (!httpSession.getId().isEmpty()) {
			httpSession.setAttribute("userRefID", user.getUserRefId());
		}
		LOGGER.info("persistentLoginEntity Name after save " + persistentLoginEntity.getUserid());
		if (user.getResetPasswordFlag()) {
			// request.getSession().setAttribute("firsttimelogin", true);
			request.setAttribute("firsttimelogin", true);
			redirectStrategy.sendRedirect(request, response, "/users/change-password");
			return;
		}

		redirectStrategy.sendRedirect(request, response, "/home");
		// response.sendRedirect("/AccessManagement/home");

	}
}
