package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.ContentType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.TenantParam;
import com.rssoftware.ou.tenant.dao.TenantParamDao;
import com.rssoftware.ou.tenant.dao.impl.TenantParamDaoImpl;
import com.rssoftware.ou.tenant.service.TenantParamService;

@Service
public class TenantParamServiceImpl implements TenantParamService {
	@Autowired
	private TenantParamDao tenantParamDao;
	
	@Override
	public void saveTenantParam(String name, String charValue) {
		if (!CommonUtils.hasValue(name) || !CommonUtils.hasValue(charValue)){
			throw new IllegalArgumentException("null values");
		}

		TenantParam gt = new TenantParam();
		gt.setParamName(name);
		gt.setParamType(ContentType.STRING.name());
		gt.setParamValueChar(charValue);
		
		tenantParamDao.createOrUpdate(gt);
	}

	@Override
	public void saveTenantParam(String name, byte[] byteValue) {
		if (!CommonUtils.hasValue(name) || byteValue == null || byteValue.length==0){
			throw new IllegalArgumentException("null values");
		}

		TenantParam gt = new TenantParam();
		gt.setParamName(name);
		gt.setParamType(ContentType.BYTE_ARRAY.name());
		gt.setParamValueBin(byteValue);
		
		tenantParamDao.createOrUpdate(gt);

	}

	@Override
	public String retrieveStringParamByName(String name) {
		TenantParam t = tenantParamDao.get(name);
		if (t != null && ContentType.STRING.name().equals(t.getParamType())){
			return t.getParamValueChar();
		}
		return null;
	}
	
	public String retrieveStringParamByName(String name, boolean isLocal) {
		TenantParamDaoImpl tenantParamDao = new TenantParamDaoImpl();
		TenantParam t = tenantParamDao.get(name);
		if (t != null && ContentType.STRING.name().equals(t.getParamType())){
			return t.getParamValueChar();
		}
		return null;
	}

	@Override
	public byte[] retrieveByteArrayParamByName(String name) {
		TenantParam t = tenantParamDao.get(name);
		if (t != null && ContentType.BYTE_ARRAY.name().equals(t.getParamType())){
			return t.getParamValueBin();
		}
		return null;
	}

	public byte[] retrieveByteArrayParamByName(String name, boolean isLocal) {
		TenantParamDaoImpl tenantParamDao = new TenantParamDaoImpl();
		TenantParam t = tenantParamDao.get(name);
		if (t != null && ContentType.BYTE_ARRAY.name().equals(t.getParamType())){
			return t.getParamValueBin();
		}
		return null;
	}
	
}
