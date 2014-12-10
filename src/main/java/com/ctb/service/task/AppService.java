/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.entity.App;
import com.ctb.repository.AppDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class AppService {

	@Autowired
	private AppDao appDao;

	public App createApp(App app) {
		return appDao.save(app);
	}

	public App updateApp(App app) {
		return appDao.save(app);
	}

	public void deleteApp(Long appId) {
		appDao.delete(appId);
	}

	public App findOne(Long appId) {
		return appDao.findOne(appId);
	}
	
	public Iterable<App> findAll(){
		return appDao.findAll();
	}

	public Page<App> findAll(int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return appDao.findAll(pageRequest);
	}

	public Long findAppIdByAppKey(String appKey) {
		return appDao.findByAppKey(appKey).getId();
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
