/**
 * 
 */
package com.rssoftware.ou.common;

import java.io.Serializable;

import com.rssoftware.upiint.schema.DeviceDetails;
import com.rssoftware.upiint.schema.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rsdpp
 *
 */
public class ServiceRequestContext implements Serializable {
	
	
	/**
	 * Serial version UID for ServiceRequestContext
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static ThreadLocal<ServiceRequestContext> requestContext;
	
	private static final Logger log = LoggerFactory.getLogger(ServiceRequestContext.class);
	
	static {
		requestContext = new ThreadLocal<ServiceRequestContext>();
	}
	
	/**
	 * 
	 * @param request
	 */
	public static void initialize(ServiceRequest request) {
		/*if (request == null) {
			throw new IllegalArgumentException("Insufficient values supplied");
		}*/
		
		if (requestContext == null) {
			throw new IllegalStateException("Illegal state - Service Request Context");
		}
		
		if (requestContext.get() != null) {
			if (log.isDebugEnabled()) {
				//log.debug("Service Request Context - reinitializing");
			}
		} 
		
		requestContext.set(new ServiceRequestContext(request));
		if (log.isDebugEnabled()) {
			//log.debug("Service Request Context - Initialized");
		}
	}
	
	public static void initialize() {
		/*if (request == null) {
			throw new IllegalArgumentException("Insufficient values supplied");
		}*/
		
		if (requestContext == null) {
			throw new IllegalStateException("Illegal state - Service Request Context");
		}
		
		if (requestContext.get() != null) {
			if (log.isDebugEnabled()) {
				//log.debug("Service Request Context - reinitializing");
			}
		} 
		
		requestContext.set(new ServiceRequestContext(null));
		if (log.isDebugEnabled()) {
			//log.debug("Service Request Context - Initialized");
		}
	}
	
	public static void initialize(ServiceRequestContext serviceRequestContext) {
		
		if (requestContext == null) {
			throw new IllegalStateException("Illegal state - Service Request Context");
		}
		
		if (requestContext.get() != null) {
			if (log.isDebugEnabled()) {
				//log.debug("Service Request Context - reinitializing");
			}
		} 
		
		requestContext.set(serviceRequestContext);
		if (log.isDebugEnabled()) {
			//log.debug("Service Request Context - Initialized");
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static ServiceRequestContext getCurrent() {
		if (requestContext == null)
			throw new IllegalArgumentException("Service Request Context not initialized");
		if (requestContext.get() == null)
			throw new IllegalArgumentException("Service Request Context not initialized");
		return requestContext.get();
	}
	
	/**
	 * Request specific objects
	 */
	private DeviceDetails deviceDetails = null;
	
	/**
	 * Constructor
	 * @param request
	 */
	private ServiceRequestContext(ServiceRequest request) {
		if (request != null)
			deviceDetails = request.getDeviceDetails();
	}
	
	public DeviceDetails getDeviceDetails() {
		return deviceDetails;
	}
}
