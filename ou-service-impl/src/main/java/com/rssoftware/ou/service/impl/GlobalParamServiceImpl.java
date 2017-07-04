package com.rssoftware.ou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.common.ContentType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.dao.GlobalParamDao;
import com.rssoftware.ou.dao.impl.GlobalParamDaoImpl;
import com.rssoftware.ou.database.entity.global.GlobalParam;
import com.rssoftware.ou.service.GlobalParamService;

@Service
public class GlobalParamServiceImpl implements GlobalParamService {
	@Autowired
	private GlobalParamDao globalParamDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void saveGlobalParam(String name, String charValue) {
		if (!CommonUtils.hasValue(name) || !CommonUtils.hasValue(charValue)){
			throw new IllegalArgumentException("null values");
		}

		GlobalParam gt = new GlobalParam();
		gt.setParamName(name);
		gt.setParamType(ContentType.STRING.name());
		gt.setParamValueChar(charValue);
		
		globalParamDao.createOrUpdate(gt);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void saveGlobalParam(String name, byte[] byteValue) {
		if (!CommonUtils.hasValue(name) || byteValue == null || byteValue.length==0){
			throw new IllegalArgumentException("null values");
		}

		GlobalParam gt = new GlobalParam();
		gt.setParamName(name);
		gt.setParamType(ContentType.BYTE_ARRAY.name());
		gt.setParamValueBin(byteValue);
		
		globalParamDao.createOrUpdate(gt);

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String retrieveStringParamByName(String name) {
		GlobalParam t = globalParamDao.get(name);
		if (t != null && ContentType.STRING.name().equals(t.getParamType())){
			return t.getParamValueChar();
		}
		return null;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String retrieveStringParamByName(String name, boolean isLocal) {
		GlobalParamDaoImpl globalParamDao = new GlobalParamDaoImpl(null);
		GlobalParam t = globalParamDao.get(name);
		if (t != null && ContentType.STRING.name().equals(t.getParamType())){
			return t.getParamValueChar();
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public byte[] retrieveByteArrayParamByName(String name) {
		GlobalParam t = globalParamDao.get(name);
		if (t != null && ContentType.BYTE_ARRAY.name().equals(t.getParamType())){
			return t.getParamValueBin();
		}
		return null;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public byte[] retrieveByteArrayParamByName(String name, boolean isLocal) {
		GlobalParamDaoImpl globalParamDao = new GlobalParamDaoImpl(null);
		GlobalParam t = globalParamDao.get(name);
		if (t != null && ContentType.BYTE_ARRAY.name().equals(t.getParamType())){
			return t.getParamValueBin();
		}
		return null;
	}
	
}
