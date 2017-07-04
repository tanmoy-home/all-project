package com.rssoftware.ou.tenant.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ResponseCode;
import com.rssoftware.ou.database.entity.tenant.ResponseCodePK;
import com.rssoftware.ou.tenant.dao.ResponseCodeDao;
import com.rssoftware.ou.tenant.service.ResponseCodeService;

@Service
public class ResponseCodeServiceImpl implements ResponseCodeService {
	@Autowired
	private ResponseCodeDao responseCodeDao; 
	
	private Map<RequestType, Map<String, Set<String>>> responseCodesMap = null;
	private Map<RequestType, Map<String, Set<String>>> reversalResponseCodesMap = null;
	
	@Override
	public boolean isValid(String responseCode, RequestType requestType,
			String responseReason, boolean reversal) {
		if (!CommonUtils.hasValue(responseCode) || requestType == null || !CommonUtils.hasValue(responseReason)){
			return false;
		}
		
		ResponseCodePK responseCodePK = new ResponseCodePK();
		responseCodePK.setResponseCode(responseCode);
		responseCodePK.setRequestType(requestType.name());
		responseCodePK.setResponseReason(responseReason);
		responseCodePK.setIsReversal(reversal);
		
		if (responseCodeDao.get(responseCodePK) != null){
			return true;
		}
		
		return false;
	}

	@Override
	public String getDescription(String responseCode, RequestType requestType,
			String responseReason, boolean reversal) {
		if (!CommonUtils.hasValue(responseCode) || requestType == null || !CommonUtils.hasValue(responseReason)){
			return null;
		}
		
		ResponseCodePK responseCodePK = new ResponseCodePK();
		responseCodePK.setResponseCode(responseCode);
		responseCodePK.setRequestType(requestType.name());
		responseCodePK.setResponseReason(responseReason);
		responseCodePK.setIsReversal(reversal);
		
		ResponseCode respCode = responseCodeDao.get(responseCodePK);
		
		if (respCode != null){
			return respCode.getCodeDesc();
		}
		
		return null;
	}

	@Override
	public void save(String responseCode, RequestType requestType,
			String responseReason, boolean reversal, String codeDescription) {
		if (!CommonUtils.hasValue(responseCode) || requestType == null || !CommonUtils.hasValue(responseReason)){
			throw new IllegalArgumentException("null values");
		}
		
		ResponseCodePK responseCodePK = new ResponseCodePK();
		responseCodePK.setResponseCode(responseCode);
		responseCodePK.setRequestType(requestType.name());
		responseCodePK.setResponseReason(responseReason);
		responseCodePK.setIsReversal(reversal);

		ResponseCode rc = new ResponseCode();
		rc.setId(responseCodePK);
		rc.setCodeDesc(codeDescription);
		
		responseCodeDao.createOrUpdate(rc);
	}
	
	@Override
	public void refresh() {
		List<ResponseCode> responseCodes = responseCodeDao.getAll();
		
		Map<RequestType, Map<String, Set<String>>> localResponseCodesMap = new HashMap<RequestType, Map<String,Set<String>>>();
		Map<RequestType, Map<String, Set<String>>> localReversalResponseCodesMap = new HashMap<RequestType, Map<String,Set<String>>>();
		
		if (responseCodes != null){
			for (ResponseCode rc:responseCodes){
				if (rc != null){
					if (rc.getId().getIsReversal()){
						if (localReversalResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())) == null){
							localReversalResponseCodesMap.put(RequestType.valueOf(rc.getId().getRequestType()), new HashMap<String, Set<String>>());
						}
						
						if (localReversalResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).get(rc.getId().getResponseReason()) == null){
							localReversalResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).put(rc.getId().getResponseReason(), new HashSet<String>());
						}
						
						localReversalResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).get(rc.getId().getResponseReason()).add(rc.getId().getResponseCode());
					}
					else {
						if (localResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())) == null){
							localResponseCodesMap.put(RequestType.valueOf(rc.getId().getRequestType()), new HashMap<String, Set<String>>());
						}
							
						if (localResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).get(rc.getId().getResponseReason()) == null){
							localResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).put(rc.getId().getResponseReason(), new HashSet<String>());
						}
						
						localResponseCodesMap.get(RequestType.valueOf(rc.getId().getRequestType())).get(rc.getId().getResponseReason()).add(rc.getId().getResponseCode());
					}
				}
			}
		}
		
		responseCodesMap = localResponseCodesMap;
		reversalResponseCodesMap = localReversalResponseCodesMap;
	}
	
	@Override
	public String getSuccessfulResponseCode(RequestType requestType, boolean reversal) {
		if (reversal){
			if (reversalResponseCodesMap != null && 
					reversalResponseCodesMap.get(requestType) != null && reversalResponseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG) != null 
					&& !reversalResponseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG).isEmpty()){
				return reversalResponseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG).iterator().next();
			}
			
		}
		else {
			if (responseCodesMap!= null &&
					responseCodesMap.get(requestType) != null && responseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG) != null 
					&& !responseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG).isEmpty()){
				return responseCodesMap.get(requestType).get(CommonConstants.RESP_SUCCESS_MSG).iterator().next();
			}
		}
		return null;
	}
}