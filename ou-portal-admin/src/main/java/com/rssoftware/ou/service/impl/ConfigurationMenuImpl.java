package com.rssoftware.ou.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;
import com.rssoftware.ou.repository.MenuRepository;
import com.rssoftware.ou.service.ConfigurationMenu;

@Service
public class ConfigurationMenuImpl implements ConfigurationMenu{
	
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public void saveMenu(MenuEntity menuEntity) {
		menuRepository.save(menuEntity);
	}
	


	@Override
	public List<MenuEntity> findAllMenus() {
		Iterable<MenuEntity> menus = menuRepository.findAll();
		return (List<MenuEntity>) menus;
	}
	
	
	
	
	public MenuEntity findById(Long id) {
		return menuRepository.findOne(id);
	}



	@Override
	public void deleteMenuById(Long id) {
		menuRepository.delete(id);
	}

}
