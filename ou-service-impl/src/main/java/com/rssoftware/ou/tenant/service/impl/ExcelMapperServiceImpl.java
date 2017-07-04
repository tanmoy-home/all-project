package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.tenant.dao.ExcelMapperDao;
import com.rssoftware.ou.tenant.service.ExcelMapperService;

@Service
public class ExcelMapperServiceImpl implements ExcelMapperService {

	@Autowired
	ExcelMapperDao excelMapperDao;

	@Override
	public String getMappedFromName(String mappedToName) {
		String mappedFromName = excelMapperDao.getMappedFromName(mappedToName);
		return mappedFromName;
	}

	@Override
	public String getMappedToName(String mappedFromName) {
		String mappedToName = excelMapperDao.getMappedToName(mappedFromName);
		return mappedToName;
	}

}
