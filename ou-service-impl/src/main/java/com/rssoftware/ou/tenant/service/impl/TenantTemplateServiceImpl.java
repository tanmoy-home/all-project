package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.ContentType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.TenantTemplate;
import com.rssoftware.ou.tenant.dao.TenantTemplateDao;
import com.rssoftware.ou.tenant.service.TenantTemplateService;

@Service
public class TenantTemplateServiceImpl implements TenantTemplateService {
	@Autowired
	private TenantTemplateDao tenantTemplateDao;
	
	@Override
	public void saveTenantTemplate(String name, String charValue) {
		if (!CommonUtils.hasValue(name) || !CommonUtils.hasValue(charValue)){
			throw new IllegalArgumentException("null values");
		}

		TenantTemplate gt = new TenantTemplate();
		gt.setTemplateName(name);
		gt.setTemplateType(ContentType.STRING.name());
		gt.setContentChar(charValue);
		
		tenantTemplateDao.createOrUpdate(gt);
	}

	@Override
	public void saveTenantTemplate(String name, byte[] byteValue) {
		if (!CommonUtils.hasValue(name) || byteValue == null || byteValue.length==0){
			throw new IllegalArgumentException("null values");
		}

		TenantTemplate gt = new TenantTemplate();
		gt.setTemplateName(name);
		gt.setTemplateType(ContentType.BYTE_ARRAY.name());
		gt.setContentBin(byteValue);
		
		tenantTemplateDao.createOrUpdate(gt);

	}

	@Override
	public String retrieveStringTemplateByName(String name) {
		TenantTemplate t = tenantTemplateDao.get(name);
		if (t != null && ContentType.STRING.name().equals(t.getTemplateType())){
			return t.getContentChar();
		}
		return null;
	}

	@Override
	public byte[] retrieveByteArrayTemplateByName(String name) {
		TenantTemplate t = tenantTemplateDao.get(name);
		if (t != null && ContentType.BYTE_ARRAY.name().equals(t.getTemplateType())){
			return t.getContentBin();
		}
		return null;
	}

}
