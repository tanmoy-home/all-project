package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.database.entity.tenant.BusinessCategory;
import com.rssoftware.ou.model.tenant.CategoryView;
import com.rssoftware.ou.tenant.dao.CategoryDao;
import com.rssoftware.ou.tenant.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	private final static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryDao categoryDao;

	@Override
	public List<CategoryView> fetchAll() throws DataAccessException {
		
		List<BusinessCategory> category = categoryDao.getAll();
		
		if (category != null){
			List<CategoryView> cv = new ArrayList<CategoryView>(category.size());
			
			for (BusinessCategory c:category){
				cv.add(mapFrom(c));
			}
			
			return cv;
		}
		
		return null; 
		
	}

	@Override
	public CategoryView fetchByName(String cmsCategory) throws DataAccessException {
		 BusinessCategory category = categoryDao.fetchByName(cmsCategory);
		 return mapFrom(category);
	}
	
	private static CategoryView mapFrom(BusinessCategory category){
		if (category == null){
			return null;
		}
		
		CategoryView cv = new CategoryView();
		cv.setCategoryId(category.getCategoryId());
		cv.setCategoryName(category.getCategoryName());
		//cv.setEntityType(category.getEntityType());
			
		
		return cv;
	}
	@Override
	public List<CategoryView> retiriveCategoryList() throws DataAccessException {
		List<CategoryView> retiriveCategoryList = Collections.emptyList();
		List<BusinessCategory> categoryList = Collections.emptyList();
		
		try{
			categoryList = categoryDao.getAll();
			
			if(!categoryList.isEmpty() && categoryList.size()>0){
				retiriveCategoryList = retiriveCategoryViewList(categoryList);
				retiriveCategoryList.add(0,new CategoryView(new Long(0), "Please Select Category"));
			}
		}catch(DataAccessException dae){
			logger.error( dae.getMessage(), dae);
	        logger.info("In Excp : " + dae.getMessage());
		}
		return retiriveCategoryList;
	}
	private List<CategoryView> retiriveCategoryViewList(List<BusinessCategory> categoryList) {
		List<CategoryView> categoryView = Collections.emptyList();
		if(!categoryList.isEmpty() && categoryList.size()>0){
			categoryView = new ArrayList<CategoryView>();
			for (BusinessCategory category : categoryList) {
				System.out.println("#################### inside pojo to pojoView map... ##############");
				categoryView.add(mapFromCategory(category));
			}
		}
		return categoryView;
	}
	private static CategoryView mapFromCategory(BusinessCategory category){
		if (category == null){
			return null;
		}
	
		CategoryView categoryView = new CategoryView();
		categoryView.setCategoryId(category.getCategoryId());
		categoryView.setCategoryName(category.getCategoryName());
		
		return categoryView;
	}

}
