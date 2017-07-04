package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ExcelMapper;
import com.rssoftware.ou.tenant.dao.ExcelMapperDao;

@Repository
public class ExcelMapperDaoImpl extends
		GenericDynamicDaoImpl<ExcelMapper, String> implements ExcelMapperDao {
	private static final String GET_MAPPED_FROM_NAME = "select a from ExcelMapper a where a.mapToName = :mappedToName";
	private static final String GET_MAPPED_TO_NAME = "select a from ExcelMapper a where a.mappedFromName = :mappedFromName";

	public ExcelMapperDaoImpl() {
		super(ExcelMapper.class);
	}

	@Override
	public String getMappedFromName(String mappedToName) {
		ExcelMapper excelMapper = (ExcelMapper) getSessionFactory()
				.getCurrentSession().createQuery(GET_MAPPED_FROM_NAME)
				.setString("mappedToName", mappedToName).uniqueResult();
		if (excelMapper != null)
			return excelMapper.getMappedFromName();
		else
			return null;

	}

	@Override
	public String getMappedToName(String mappedFromName) {
		ExcelMapper excelMapper = (ExcelMapper) getSessionFactory()
				.getCurrentSession().createQuery(GET_MAPPED_TO_NAME)
				.setString("mappedFromName", mappedFromName).uniqueResult();
		if (excelMapper != null)
			return excelMapper.getMapToName();
		else
			return null;

	}

}
