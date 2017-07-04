package com.rssoftware.ou.tenant.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.tenant.dao.IDGeneratorDao;
import com.rssoftware.ou.tenant.service.IDGeneratorService;

@Service
public class IDGeneratorServiceImpl implements IDGeneratorService {
	@Autowired
	private IDGeneratorDao idGeneratorDao;
	
	@Override
	// TODO: think of a better approach of generating fixed length ID
	public String getUniqueID(int length, String prefix) {
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(StringUtils.leftPad(String.valueOf(idGeneratorDao.getNextSequence()), length-sb.length(), '0'));
		
		return sb.toString();
	}
	

}
