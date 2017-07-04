package com.rssoftware.ou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.common.ContentType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.dao.GlobalTemplateDao;
import com.rssoftware.ou.database.entity.global.GlobalTemplate;
import com.rssoftware.ou.service.GlobalTemplateService;

@Service
public class GlobalTemplateServiceImpl implements GlobalTemplateService {
	@Autowired
	private GlobalTemplateDao globalTemplateDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void saveGlobalTemplate(String name, String charValue) {
		if (!CommonUtils.hasValue(name) || !CommonUtils.hasValue(charValue)){
			throw new IllegalArgumentException("null values");
		}

		GlobalTemplate gt = new GlobalTemplate();
		gt.setTemplateName(name);
		gt.setTemplateType(ContentType.STRING.name());
		gt.setContentChar(charValue);
		
		globalTemplateDao.createOrUpdate(gt);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void saveGlobalTemplate(String name, byte[] byteValue) {
		if (!CommonUtils.hasValue(name) || byteValue == null || byteValue.length==0){
			throw new IllegalArgumentException("null values");
		}

		GlobalTemplate gt = new GlobalTemplate();
		gt.setTemplateName(name);
		gt.setTemplateType(ContentType.BYTE_ARRAY.name());
		gt.setContentBin(byteValue);
		
		globalTemplateDao.createOrUpdate(gt);

	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String retrieveStringTemplateByName(String name) {
		GlobalTemplate gt = globalTemplateDao.get(name);
		if (gt != null && ContentType.STRING.name().equals(gt.getTemplateType())){
			return gt.getContentChar();
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public byte[] retrieveByteArrayTemplateByName(String name) {
		GlobalTemplate gt = globalTemplateDao.get(name);
		if (gt != null && ContentType.BYTE_ARRAY.name().equals(gt.getTemplateType())){
			return gt.getContentBin();
		}
		return null;
	}

}
