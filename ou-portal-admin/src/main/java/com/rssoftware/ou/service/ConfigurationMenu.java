package com.rssoftware.ou.service;

import java.util.List;

import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;

public interface ConfigurationMenu {
	
	public List<MenuEntity> findAllMenus();
	
	public void saveMenu(MenuEntity menuEntity);
	
	public MenuEntity findById(Long id);
	
	public void deleteMenuById(Long id);
}
