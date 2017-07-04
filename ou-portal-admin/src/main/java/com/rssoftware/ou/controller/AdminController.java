package com.rssoftware.ou.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rssoftware.ou.common.APIURL;
import com.rssoftware.ou.common.CommonAPIRequest;
import com.rssoftware.ou.common.PasswordAdvisor;
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.database.entity.tenant.SMSDetails;
import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;
import com.rssoftware.ou.database.entity.tenant.admin.OrganizationEntity;
import com.rssoftware.ou.database.entity.tenant.admin.PersistentLoginEntity;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.RolePrivilegeEntity;
import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.model.tenant.SMSDetailsView.SMSType;
import com.rssoftware.ou.service.ConfigurationMenu;
import com.rssoftware.ou.service.ConfigurationOrganization;
import com.rssoftware.ou.service.ConfigurationRole;
import com.rssoftware.ou.service.ConfigurationService;
import com.rssoftware.ou.service.PasswordService;
import com.rssoftware.ou.service.PersistentLoginService;
import com.rssoftware.ou.service.RolePrivilegeService;
import com.rssoftware.ou.service.UserService;
import com.rssoftware.ou.vo.signerView;

import in.co.rssoftware.bbps.schema.AgentDetail;
import in.co.rssoftware.bbps.schema.AgentInst;
import in.co.rssoftware.bbps.schema.AgentInstList;
import in.co.rssoftware.bbps.schema.CityList;
import in.co.rssoftware.bbps.schema.PostalCodeList;
import in.co.rssoftware.bbps.schema.PostalDetail;
import in.co.rssoftware.bbps.schema.SmsType;
import in.co.rssoftware.bbps.schema.StateDetail;
import in.co.rssoftware.bbps.schema.StateList;

@Controller
@RequestMapping("/")
@SessionAttributes({ "roles", "organizations" })
public class AdminController {

	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordService passwordService;

	@Autowired
	ConfigurationService configurationService;
	@Autowired
	ConfigurationOrganization configurationOrganization;
	@Autowired
	ConfigurationRole configurationRole;
	@Autowired
	ConfigurationMenu configurationMenu;
	@Autowired
	MessageSource messageSource;
	@Autowired
	PersistentLoginService persistentLoginService;
	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	RolePrivilegeService rolePrivilegeService;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext()
				.getAuthentication().getDetails();

		String ip = details.getRemoteAddress();
		model.addAttribute("loggedinuserrole", authentication.getAuthorities());
		model.addAttribute("logintime", new Date().toString());
		model.addAttribute("loggedinuser", getPrincipal());

		PersistentLoginEntity persistentEntity = persistentLoginService.findByUserId(authentication.getName());
		model.addAttribute("lastlogintime", persistentEntity.getLogged_in());
		model.addAttribute("ip", ip);

		return "home";
	}

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/user/user-list" }, method = RequestMethod.GET)
	public String listUsers(Model model) {

		//Iterable<UserEntity> users = userService.findByUpdatedBy(getPrincipal());
		Iterable<UserEntity> users = userService.findUsersByRolePriviledge(getPrincipal());
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "User List");

		return "user-list";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/user/add-user" }, method = RequestMethod.GET)
	public String newUser(HttpServletRequest request, @RequestParam(required = false) Long id, ModelMap model) {
		UserEntity user = new UserEntity();
		user.setIsActive(true);
		user.setResetPasswordFlag(true);
		user.setCountry("India");
		List<StateDetail> stateLists = Collections.EMPTY_LIST;
		List<RoleEntity> roleList = new ArrayList<>();

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		Iterable<RoleEntity> role = configurationRole.findAllRoles();

		ResponseEntity<?> responseEntityRoleSet = ouInternalRestTemplate.exchange(URI.create(APIURL.TENENT_PARAM_URL + "/" + "DEFAULT_ROLE_SET"), HttpMethod.GET,
				PortalUtils.getHttpEntity(userService), String.class);
		String defaultRole = (String) responseEntityRoleSet.getBody();
		List<String> defaultRoleSet = new ArrayList<String>(Arrays.asList(defaultRole.split(",")));

		for (GrantedAuthority sRole : authorities) {
			if (defaultRoleSet.contains(sRole.getAuthority().toUpperCase())) {
				// get data from tanent Param
				ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.TENENT_PARAM_URL + "/" + sRole.getAuthority()), HttpMethod.GET,
						PortalUtils.getHttpEntity(userService), String.class);
				String chkCount = (String) responseEntity.getBody();
				// end
				Iterable<RolePrivilegeEntity> rolePrivilege = rolePrivilegeService
						.findPrivilegeByRoleid(Long.parseLong(chkCount));

				for (RoleEntity obj : role) {
					for (RolePrivilegeEntity rolepvg : rolePrivilege) {
						if (obj.getId().equals(rolepvg.getRolePrvlgId())) {
							roleList.add(obj);
						}
					}
				}
				break;
			}
		}

		ResponseEntity<?> responseEntityAI = ouInternalRestTemplate.exchange(URI.create(APIURL.AI_LIST_URL),
				HttpMethod.POST, PortalUtils.getHttpEntity(userService), AgentInstList.class);
		AgentInstList AI = (AgentInstList) responseEntityAI.getBody();
		List<AgentInst> agentInstList = AI.getAgentInsts();

		model.addAttribute("agentInstList", agentInstList);

		// Lists.newArrayList(role).retainAll(Lists.newArrayList(rolePrivilege));
		model.addAttribute("roleList", roleList);

		StateList statelst = new StateList();
		statelst.setStateId(Long.parseLong("0"));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.STATE_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader(statelst, userService), StateList.class);
		StateList stateList = (StateList) responseEntity.getBody();
		stateLists = stateList.getStateLists();
		model.addAttribute("stateList", stateLists);

		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			user = userService.findById(id);
			
			CityList cityLst = new CityList();
			cityLst.setStateId(Long.parseLong(user.getState()));
			ResponseEntity<?> responseEntity2 = ouInternalRestTemplate.exchange(URI.create(APIURL.POSTALCODE_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(cityLst, userService), PostalCodeList.class);
			PostalCodeList postalList = (PostalCodeList) responseEntity2.getBody();
			List<PostalDetail> postalLists = postalList.getPostals();
			model.addAttribute("pinList", postalLists);
			model.addAttribute("edit", true);
		}
		model.addAttribute("user", user);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "User Details");
		return "new-user";
	}

	@RequestMapping(value = "/postalList/{stateId}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse fetchPostalList(@PathVariable("stateId") String stateId) {
		String METHOD_NAME = "postalList";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		CityList cityLst = new CityList();
		cityLst.setStateId(Long.parseLong(stateId));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.POSTALCODE_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(cityLst, userService), PostalCodeList.class);
		PostalCodeList postalLists = (PostalCodeList) responseEntity.getBody();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api portalList Length: " + postalLists.getPostals().size());
		}
		json.setResult(postalLists);
		return json;
	}

	/*
	 * @RequestMapping(value = "/user/add-user/{agentId}", method =
	 * RequestMethod.POST) public @ResponseBody JsonResponse
	 * getAgentDetails(@PathVariable("agentId") String agentId, ModelMap model)
	 * { String METHOD_NAME = "agentDetails"; if (LOG.isDebugEnabled()) {
	 * LOG.debug("Entering " + METHOD_NAME); } JsonResponse json = new
	 * JsonResponse();
	 * 
	 * uri = URI.create(APIURL.View_AGENT_DTL_URL); CommonAPIRequest
	 * commonAPIRequest = new CommonAPIRequest();
	 * commonAPIRequest.setAgentId(agentId); ResponseEntity<?> responseEntity =
	 * ouInternalRestTemplate.exchange(uri, HttpMethod.POST,
	 * PortalUtils.getHttpHeader(commonAPIRequest, userService),
	 * AgentDetail.class); AgentDetail agentList = (AgentDetail)
	 * responseEntity.getBody(); UserEntity user = new UserEntity(); if
	 * (agentList != null) { user.setFirstName(agentList.getAgentName());
	 * user.setLastName(""); user.setAddress(agentList.getAgentAddr());
	 * 
	 * uri = URI.create(APIURL.STATE_URL); StateList statelst = new StateList();
	 * statelst.setStateId(Long.parseLong(agentList.getAgentState()));
	 * ResponseEntity<?> responseEntity1 = ouInternalRestTemplate.exchange(uri,
	 * HttpMethod.POST, PortalUtils.getHttpHeader(statelst, userService),
	 * StateList.class); StateList stateList = (StateList)
	 * responseEntity1.getBody();
	 * 
	 * user.setState(stateList.getStateName().getStateName());
	 * user.setCity(agentList.getAgentCity());
	 * user.setCountry(agentList.getAgentCountry());
	 * user.setPincode(Long.parseLong(agentList.getAgentPin()));
	 * user.setMobile(Long.parseLong(agentList.getAgentMobile())); } //
	 * model.addAttribute("user", user); // model.addAttribute("edit", false);
	 * // model.addAttribute("loggedinuser", getPrincipal()); //
	 * model.addAttribute("pagetitle", "User Details"); json.setResult(user);
	 * return json; }
	 */

	@RequestMapping(value = "/user/add-user/checkDuplicateUser/{userName}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse checkDuplicateUser(@PathVariable("userName") String userName) {
		String METHOD_NAME = "checkDuplicateUser";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		if (!userService.isUserNameUnique(null, userName)) {
			json.setResult("1");
			return json;
		}
		json.setResult("0");
		return json;
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/user/add-user/save-user" }, method = RequestMethod.POST)
	public String saveUser(HttpServletRequest request, @Valid UserEntity user, BindingResult bindingResult,
			ModelMap model, RedirectAttributes redirectAttributes) {
		if (LOG.isDebugEnabled()) {
			LOG.info("User inside saveUser" + user);
		}

		HttpSession httpSession = request.getSession(false);

		if (user.getUserRefId().isEmpty()) {
			if (user.getRoles().contains("OU_ADMIN")) {
				user.setUserRefId(System.getProperty("OUName", "OU04"));
			} else {
				user.setUserRefId(httpSession.getAttribute("userRefID").toString());
			}
		}

		user.setLastName(" ");
		user.setResetPasswordFlag(true);
		// user.setResetPasswordFlag(user.getResetPasswordFlag() == null ? false
		// : true);
		user.setIsActive(user.getIsActive() == null ? false : true);
		user.setIsAccountLocked(user.getIsAccountLocked() == null ? false : true);
		user.setIsPasswordExpired(user.getIsPasswordExpired() == null ? false : true);

		if (user.getId() == null) {
			user.setCreatedBy(getPrincipal());
			user.setCreatedOn(new Timestamp(new Date().getTime()));
			user.setUpdatedBy(getPrincipal());
			user.setUpdatedOn(new Timestamp(new Date().getTime()));
		} else {
			user.setUpdatedBy(getPrincipal());
			user.setUpdatedOn(new Timestamp(new Date().getTime()));
		}

		/*
		 * if (bindingResult.hasErrors()) { return "new-user"; }
		 */
		/*
		 * Preferred way to achieve uniqueness of field [sso] should be
		 * implementing custom @Unique annotation and applying it on field [sso]
		 * of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you
		 * can fill custom errors outside the validation framework as well while
		 * still using internationalized messages.
		 */

		if (user.getId() == null && !userService.isUserNameUnique(user.getId(), user.getUsername())) {
			FieldError userNameError = new FieldError("user", "userName", messageSource
					.getMessage("non.unique.userNameError", new String[] { user.getUsername() }, Locale.getDefault()));
			bindingResult.addError(userNameError);

			return "redirect:/user/newuser";
		}

		saveUser(user);

		redirectAttributes.addFlashAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " saved successfully");
		return "redirect:/user/user-list";
	}

	private void saveUser(UserEntity user) {
		if (LOG.isDebugEnabled()) {
			LOG.info("User inside saveUser" + user);
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be
		 * implementing custom @Unique annotation and applying it on field [sso]
		 * of Model class [User].
		 * 
		 */
		try {
			String pwd = "";
			if (user.getId() != null) {
				UserEntity userEntity = userService.findById(user.getId());
				user.setPassword(userEntity.getPassword());
			} else {/*
					 * if (user.getPassword() == null ||
					 * user.getPassword().isEmpty()) {
					 */
				pwd = PortalUtils.generateRandomId();
				System.out.println(pwd);
				LOG.info("User password" + pwd);
				user.setPassword(passwordEncoder.encode(pwd));
			}

			userService.saveUser(user);

			// SMS implement start
			SmsType sms = new SmsType();
			sms.setMessage("Dear " + user.getUsername() +", Your password has been set. Your password is " + pwd);
			sms.setMobileNo(user.getMobile().toString());
			sms.setStatus("D");
			sms.setType(SMSType.RESET_PASSWORD.name());
			// call sms
			
			ResponseEntity<?> responseEntitySms = ouInternalRestTemplate.exchange(URI.create(APIURL.SEND_SMS_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(sms, userService), String.class);
			//SmsType smsType = (SmsType) responseEntitySms.getBody();
			String smsDtl = (String) responseEntitySms.getBody();
			// SMS implement end

		} catch (Exception ex) {
			LOG.error("In class AdminController >> saveUser: ", ex);
		}
	}

	/**
	 * This method will block/Unblock user
	 */
	@RequestMapping(value = { "/user/block-unblock-user" }, method = RequestMethod.GET)
	public String blockUnblockUser(@RequestParam(required = false) Long id, ModelMap model,
			RedirectAttributes redirectAttributes) {
		UserEntity userEntity = userService.findById(id);
		userEntity.setUpdatedBy(getPrincipal());
		userEntity.setUpdatedOn(new Timestamp(new Date().getTime()));
		userEntity.setIsActive(!userEntity.getIsActive());
		saveUser(userEntity);

		redirectAttributes.addFlashAttribute("success", "User " + userEntity.getFirstName() + " "
				+ (userEntity.getIsActive() ? "unblock" : "block") + " successfully");

		return "redirect:/user/user-list";
	}

	/**
	 * This method will provide the medium to change password
	 */
	@RequestMapping(value = { "/users/change-password" }, method = RequestMethod.GET)
	public String changePassword(ModelMap model, signerView passwordVo, BindingResult result,
			HttpServletRequest request) {
		passwordVo.setUserName(getPrincipal());
		model.addAttribute("passwordvo", passwordVo);
		model.addAttribute("firsttimelogin", request.getAttribute("firsttimelogin"));
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Change Password");
		return "change-password";
	}

	/**
	 * This method will provide the medium to change password
	 */
	@RequestMapping(value = { "/users/save-password" }, method = RequestMethod.POST)
	public String savePassword(signerView passwordVo, BindingResult result, ModelMap model) {
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.TENENT_PARAM_URL + "/PASSWORD_COMP_LIMIT"), HttpMethod.GET,
				PortalUtils.getHttpEntity(userService), String.class);
		String chkCount = (String) responseEntity.getBody();

		// password Validation
		if (passwordService.findAllPasswordByUser(passwordVo.getUserName(), passwordVo.getNewPassword(),
				Integer.parseInt(chkCount))) {
			if (PasswordAdvisor.checkPasswordStrength(passwordVo.getNewPassword())) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				UserEntity userEntity = userService.findByUserName(passwordVo.getUserName());
				if (passwordEncoder.matches(passwordVo.getOldPassword(), userEntity.getPassword())) {
					userEntity.setPassword(passwordVo.getNewPassword());
					userEntity.setUpdatedBy(getPrincipal());
					userEntity.setUpdatedOn(new Timestamp(new Date().getTime()));
					userEntity.setIsActive(userEntity.getIsActive());
					userEntity.setResetPasswordFlag(false);
					userEntity.setIsPasswordExpired(false);
					userService.changePassword(userEntity);
					model.addAttribute("loggedinuser", getPrincipal());

					// SMS implement start
					SmsType sms = new SmsType();
					sms.setMessage("Your password has been reset. Your new password is " + passwordVo.getNewPassword());
					sms.setMobileNo(userEntity.getMobile().toString());
					sms.setStatus("D");
					sms.setType("");

					// call sms

					// SMS implement end
				} else {
					model.addAttribute("passwordvo", passwordVo);
					model.addAttribute("loggedinuser", getPrincipal());
					model.addAttribute("pagetitle", "Change Password");
					model.addAttribute("message", "Please enter correct old password.");
					return "change-password";
				}
			} else {
				model.addAttribute("passwordvo", passwordVo);
				model.addAttribute("loggedinuser", getPrincipal());
				model.addAttribute("pagetitle", "Change Password");
				model.addAttribute("message", "Please enter correct new password.");
				return "change-password";
			}
		} else {
			model.addAttribute("passwordvo", passwordVo);
			model.addAttribute("loggedinuser", getPrincipal());
			model.addAttribute("pagetitle", "Change Password");
			model.addAttribute("message", "Please don't use previous old password");
			return "change-password";
		}
		return "redirect:/home";
	}

	/**
	 * This method will list all existing organizations.
	 */
	@RequestMapping(value = { "/organization/organization-list" }, method = RequestMethod.GET)
	public String listOrganizations(Model model) {

		Iterable<OrganizationEntity> organizationEntity = configurationOrganization.findAllOrganizations();
		model.addAttribute("organizations", organizationEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "List of Organizations");
		return "organization-list";
	}

	/**
	 * This method will provide the medium to update an existing organization.
	 */
	@RequestMapping(value = { "/organization/add-organization" }, method = RequestMethod.GET)
	public String addEditOrganization(@RequestParam(required = false) Long id, ModelMap model) {
		OrganizationEntity organizationEntity = new OrganizationEntity();
		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			organizationEntity = configurationOrganization.findById(id);
			model.addAttribute("edit", true);
		}
		model.addAttribute("organization", organizationEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Organization Details");
		return "new-organization";
	}

	/**
	 * This method will provide the medium to add/update a organization.
	 */
	@RequestMapping(value = { "/organization/add-organization/save-organization" }, method = RequestMethod.POST)
	public String saveOrganization(@Valid OrganizationEntity organizationEntity, ModelMap model,
			RedirectAttributes redirectAttributes) {
		organizationEntity.setIsActive(organizationEntity.getIsActive() == null ? false : true);

		configurationOrganization.saveOrganization(organizationEntity);
		// model.addAttribute("organization", organizationEntity);
		// model.addAttribute("loggedinuser", getPrincipal());
		redirectAttributes.addFlashAttribute("success",
				"Organization " + organizationEntity.getOrganizationName() + " saved successfully");
		return "redirect:/organization/organization-list";
	}

	/**
	 * This method will provide the medium to delete an existing organization.
	 */
	@RequestMapping(value = { "/organization/delete-organization" }, method = RequestMethod.GET)
	public String deleteOrganization(@RequestParam(required = false) Long id, ModelMap model) {
		OrganizationEntity organizationEntity = new OrganizationEntity();
		if (id != null && id != 0) {
			organizationEntity = configurationOrganization.findById(id);
			organizationEntity.setIsActive(!organizationEntity.getIsActive());
			System.out.println(organizationEntity.getIsActive());
			configurationOrganization.saveOrganization(organizationEntity);
		}
		model.addAttribute("organizations", organizationEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Organization Details");
		return "redirect:/organization/organization-list";
	}

	/**
	 * This method will provide the medium to add/edit role.
	 */

	@RequestMapping(value = { "/role/add-role" }, method = RequestMethod.GET)

	public String addEditRole(@RequestParam(required = false) Long id, ModelMap model) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setIsActive(true);
		roleEntity.setIsApiAccess(true);
		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			roleEntity = configurationRole.findById(id);
			model.addAttribute("edit", true);
		}

		model.addAttribute("organizations", configurationOrganization.findAllOrganizations());
		model.addAttribute("services", configurationService.findAllServices());
		model.addAttribute("menus", configurationMenu.findAllMenus());
		model.addAttribute("role", roleEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Role Details");
		return "new-role";
	}

	/**
	 * This method will provide the medium to save/update a role.
	 */
	@RequestMapping(value = { "/role/add-role/save-role" }, method = RequestMethod.POST)
	public String saveRole(@Valid RoleEntity role, BindingResult bindingResult, ModelMap model) {
		List<ServiceEntity> services = configurationService.findAllServices();
		List<MenuEntity> menus = configurationMenu.findAllMenus();
		role.setServiceMapFlag(role.getServiceMapFlag() == null ? false : true);
		role.setIsActive(role.getIsActive() == null ? false : true);
		role.setIsApiAccess(role.getIsApiAccess() == null ? false : true);

		if (role.getServiceMapFlag()) {
			role.setServices(new HashSet<ServiceEntity>(services));
			role.setMenus(new HashSet<MenuEntity>(menus));
		}
		Iterable<RoleEntity> roles = configurationRole.findAllRoles();
		boolean flg = false;
		for (RoleEntity r : roles) {
			if (r.getRoleName().equalsIgnoreCase(role.getRoleName()) && role.getId() == null) {
				flg = true;
				break;
			}
		}
		if (!flg) {
			String SaveStatus;
			if (role.getId() == null) {
				SaveStatus = " created";
			} else {
				SaveStatus = " updated";
			}
			configurationRole.saveRole(role);
			if (LOG.isDebugEnabled()) {
				LOG.info("Role Id after save " + role.getId());
			}
			model.addAttribute("role", role);
			model.addAttribute("loggedinuser", getPrincipal());
			model.addAttribute("message", "Role " + role.getRoleName() + SaveStatus + " successfully");
			model.addAttribute("loggedinuser", getPrincipal());
			model.addAttribute("services", configurationService.findAllServices());
			model.addAttribute("menus", configurationMenu.findAllMenus());
			model.addAttribute("roleManage", "roleManage");
		} else {
			model.addAttribute("role", role);
			model.addAttribute("loggedinuser", getPrincipal());
			model.addAttribute("message", "Role " + role.getRoleName() + " Duplicate.");
			model.addAttribute("services", configurationService.findAllServices());
			model.addAttribute("menus", configurationMenu.findAllMenus());
		}
		return "new-role";
	}

	@RequestMapping(value = { "/role/delete-role" }, method = RequestMethod.GET)
	public String deleteRole(@RequestParam(required = false) Long id, ModelMap model) {
		RoleEntity roleEntity = new RoleEntity();
		if (id != null && id != 0) {
			roleEntity = configurationRole.findById(id);
			configurationRole.deleteRoleByOrganizationId(roleEntity.getOrganization().getId());
		}
		model.addAttribute("role", roleEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Role Details");
		return "redirect:/role/role-list";
	}

	/**
	 * This method will provide the medium to save/update a role.
	 */
	@RequestMapping(value = { "/role/save-role-manage" }, method = RequestMethod.POST)
	public String saveRoleManage(@Valid RoleEntity role, BindingResult bindingResult, ModelMap model,
			RedirectAttributes redirectAttributes) {
		RoleEntity entity = configurationRole.findById(role.getId());
		entity.setServices(role.getServices());
		entity.setMenus(role.getMenus());
		configurationRole.saveRole(entity);
		if (LOG.isDebugEnabled()) {
			LOG.info("Role Id after save " + entity.getId());
		}
		// model.addAttribute("role", entity);
		// model.addAttribute("loggedinuser", getPrincipal());
		redirectAttributes.addFlashAttribute("success", "Role " + entity.getRoleName() + " saved successfully");
		// model.addAttribute("loggedinuser", getPrincipal());
		return "redirect:/role/role-list";
	}

	/**
	 * This method will list all existing roles.
	 */
	@RequestMapping(value = { "/role/role-list" }, method = RequestMethod.GET)
	public String listRoles(Model model) {

		Iterable<RoleEntity> roles = configurationRole.findAllRoles();
		model.addAttribute("roles", roles);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "List of Roles");
		return "role-list";
	}

	/**
	 * This method will provide the medium to update an existing service.
	 */
	@RequestMapping(value = { "/service/add-service" }, method = RequestMethod.GET)
	public String addEditService(@RequestParam(required = false) Long id, ModelMap model) {
		ServiceEntity serviceEntity = new ServiceEntity();
		serviceEntity.setIsActive(true);
		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			serviceEntity = configurationService.findById(id);
			model.addAttribute("edit", true);
		}
		model.addAttribute("service", serviceEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Service Details");
		return "new-service";
	}

	/**
	 * This method will provide the medium to save/update a service.
	 */
	@RequestMapping(value = { "/service/add-service/save-service" }, method = RequestMethod.POST)
	public String saveService(@Valid ServiceEntity serviceEntity, ModelMap model,
			RedirectAttributes redirectAttributes) {
		configurationService.saveService(serviceEntity);
		if (LOG.isDebugEnabled()) {
			LOG.info("Service Id after save " + serviceEntity.getId());
		}
		// model.addAttribute("service", serviceEntity);
		redirectAttributes.addFlashAttribute("success",
				"Service " + serviceEntity.getServiceName() + " saved successfully");
		return "redirect:/service/service-list";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/service/service-list" }, method = RequestMethod.GET)
	public String listServices(Model model) {

		Iterable<ServiceEntity> serviceEntity = configurationService.findAllServices();
		model.addAttribute("services", serviceEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "List of Services");
		return "service-list";
	}

	/**
	 * This method will provide the medium to delete an existing service.
	 */
	@RequestMapping(value = { "/service/delete-service" }, method = RequestMethod.GET)
	public String deleteService(@RequestParam(required = false) Long id, ModelMap model) {
		ServiceEntity serviceEntity = new ServiceEntity();
		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			configurationService.deleteServiceById(id);
		}
		model.addAttribute("service", serviceEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "Service Details");
		return "redirect:/service/service-list";
	}

	/**
	 * This method will provide the medium to update an existing Webpage.
	 */
	@RequestMapping(value = { "/webpage/add-webpage" }, method = RequestMethod.GET)
	public String addEditMenu(@RequestParam(required = false) Long id, ModelMap model) {
		MenuEntity menuEntity = new MenuEntity();
		menuEntity.setIsActive(true);
		model.addAttribute("edit", false);
		if (id != null && id != 0) {
			menuEntity = configurationMenu.findById(id);
			model.addAttribute("edit", true);
		}
		model.addAttribute("menu", menuEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "UI Element");
		return "new-webpage";
	}

	/**
	 * This method will provide the medium to add/update a web page menu.
	 */
	@RequestMapping(value = { "/webpage/add-webpage/save-webpage" }, method = RequestMethod.POST)
	public String saveWebPage(@Valid MenuEntity menuEntity, ModelMap model, RedirectAttributes redirectAttributes) {
		menuEntity.setIsActive(menuEntity.getIsActive() == null ? false : true);

		configurationMenu.saveMenu(menuEntity);
		if (LOG.isDebugEnabled()) {
			LOG.info("Menu after save.." + menuEntity.getId());
		}

		// model.addAttribute("menu",menuEntity);
		// model.addAttribute("loggedinuser", getPrincipal());
		redirectAttributes.addFlashAttribute("success",
				"UI Element " + menuEntity.getMenuName() + " saved successfully");
		return "redirect:/webpage/webpage-list";
	}

	/**
	 * This method will list all existing webpage.
	 */
	@RequestMapping(value = { "/webpage/webpage-list" }, method = RequestMethod.GET)
	public String listWebPages(Model model) {

		Iterable<MenuEntity> menuEntity = configurationMenu.findAllMenus();
		model.addAttribute("menus", menuEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "List of UI Elements");
		return "webpage-list";
	}

	/**
	 * This method will provide the medium to delete an existing Webpage.
	 */
	@RequestMapping(value = { "/webpage/delete-webpage" }, method = RequestMethod.GET)
	public String deleteMenu(@RequestParam(required = false) Long id, ModelMap model) {
		MenuEntity menuEntity = new MenuEntity();
		if (id != null && id != 0) {
			configurationMenu.deleteMenuById(id);
		}
		model.addAttribute("menu", menuEntity);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("pagetitle", "UI Element");
		return "redirect:/webpage/webpage-list";
	}

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public Iterable<RoleEntity> initializeProfiles() {
		return configurationRole.findAllRoles();
	}

	/**
	 * This method will provide organization list to views
	 */
	@ModelAttribute("organizations")
	public Iterable<OrganizationEntity> initializeOrganizations() {
		return configurationOrganization.findAllOrganizations();
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests. If users is already logged-in and
	 * tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		} else {
			return "redirect:/logout";
		}
	}

	/**
	 * This method handles logout requests. Toggle the handlers if you are
	 * RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	/**
	 * This method returns true if users is already authenticated [logged-in],
	 * else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);

	}

}