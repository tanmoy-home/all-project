package com.rs.bbpou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rs.bbpou.dao.HtmlResponseDao;


@Service("htmSer")
public class HtmlResponseServiceImpl implements HtmlResponseService{

	@Autowired
	private HtmlResponseDao htmRes;
	@Override
	public String getHtmlResponse(Integer id) {
		String htm =htmRes.getHtmlResponse(id);
		return htm;
	}

}
