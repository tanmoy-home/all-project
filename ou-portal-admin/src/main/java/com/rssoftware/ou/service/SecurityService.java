/**
 * 
 */
package com.rssoftware.ou.service;

import org.springframework.security.core.Authentication;

/**
 * @author MalobikaM
 *
 */
public interface SecurityService {

	public boolean userHasAccess(final Authentication auth, String objectName) ;
}
