package com.rssoftware.ou.portal.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PortalInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(PortalInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		/*String loggedUser=CommonUtils.getLoggedInUser();		
		log.info("Request URL::" + request.getRequestURL().toString() + ":: Logged User=" + loggedUser + ":: Start Time=" + startTime);
		if(request.getServletPath().compareTo("/login") == 0 &&request.getSession(false)!=null && !"SYSTEM".equals(loggedUser) && !"anonymousUser".equals(loggedUser))
			response.sendRedirect(request.getContextPath() +"/home");		
		
		else if(handler instanceof HandlerMethod){
			Method method = ((HandlerMethod)handler).getMethod();		
			if(method.isAnnotationPresent(CheckAccess.class)){
				CheckAccess checkAccess=method.getAnnotation(CheckAccess.class);
				if(!PortalUtils.getLoggedInUserAccess().contains(checkAccess.value().name())){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access tis page");
					return false;
					//throw new AccessDeniedException("Hi"+ loggedUser +", You don't have access to this page");
				}
			}
			if (method.isAnnotationPresent(CheckRole.class)) {
				CheckRole checkRole = method.getAnnotation(CheckRole.class);
				for (Role role : checkRole.value())
					if (Role.USER != role && PortalUtils.getLoggedInUserRole().equals(role.name())) {
						return true;
					}
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access tis page");
				return false;
			}
		}*/
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		/*if(handler instanceof HandlerMethod){
			Method method = ((HandlerMethod)handler).getMethod();
			if(method.isAnnotationPresent(AddAccessToModel.class)){
				List<String> acccess=PortalUtils.getLoggedInUserAccess();
				AddAccessToModel addAccessToModel=method.getAnnotation(AddAccessToModel.class);
				if(addAccessToModel.canSubmit()!=UserAccess.NA)
					modelAndView.getModelMap().addAttribute("canSubmit", acccess.contains(addAccessToModel.canSubmit().name()));
									
				if(addAccessToModel.canApprove()!=UserAccess.NA)
					modelAndView.getModelMap().addAttribute("canApprove", acccess.contains(addAccessToModel.canApprove().name()));
				
				if(addAccessToModel.canView()!=UserAccess.NA)
					modelAndView.getModelMap().addAttribute("canView", acccess.contains(addAccessToModel.canView().name()));
			}
			if(method.isAnnotationPresent(AddRoleToModel.class)){	
				String role=PortalUtils.getLoggedInUserRole();
				AddRoleToModel addRoleToModel=method.getAnnotation(AddRoleToModel.class);
				boolean isSuperAdmin=role.equals(addRoleToModel.isSuperAdmin().name());
				boolean isAdmin=role.equals(addRoleToModel.isAdmin().name());
				modelAndView.getModelMap().addAttribute("isSuperAdmin", isSuperAdmin);
				modelAndView.getModelMap().addAttribute("isAdmin", isAdmin);
				modelAndView.getModelMap().addAttribute("isUser", !(isSuperAdmin || isAdmin));
			}
		}*/
	}

	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		long startTime = (Long) request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		log.info("Request URL::" + request.getRequestURL().toString() + ":: End Time=" + endTime + ":: Time Taken=" + (endTime - startTime));
		//logger.error("Request URL::" + request.getRequestURL().toString() + ":: Error",ex);
	}
}
