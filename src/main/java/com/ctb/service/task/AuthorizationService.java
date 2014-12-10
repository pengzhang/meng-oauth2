/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.service.task;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.entity.Authorization;
import com.ctb.entity.User;
import com.ctb.repository.AuthorizationDao;
import com.ctb.service.account.AccountService;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class AuthorizationService {

	@Autowired
	private AuthorizationDao authorizationDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AppService appService;
	@Autowired
	private RoleService roleService;

	public Authorization createAuthorization(Authorization authorization) {
		return merge(authorization);
	}

	public Authorization updateAuthorization(Authorization authorization) {
		return merge(authorization);
	}

	public Authorization merge(Authorization authorization) {
		Authorization dbAuthorization = authorizationDao.findByAppIdAndUserId(authorization.getAppId(),
				authorization.getUserId());
		if (dbAuthorization == null) {// 如果数据库中不存在相应记录 直接新增
			return authorizationDao.save(authorization);
		}

		if (dbAuthorization.equals(authorization)) {// 如果是同一条记录直接更新即可
			return authorizationDao.save(authorization);
		}

		for (Long roleId : authorization.getRoleIds()) {// 否则合并
			if (!dbAuthorization.getRoleIds().contains(roleId)) {
				dbAuthorization.getRoleIds().add(roleId);
			}
		}

		if (dbAuthorization.getRoleIds().isEmpty()) {// 如果没有角色 直接删除记录即可
			authorizationDao.delete(dbAuthorization.getId());
			return dbAuthorization;
		}
		// 否则更新
		return authorizationDao.save(dbAuthorization);
	}

	public void deleteAuthorization(Long authorizationId) {
		authorizationDao.delete(authorizationId);
	}

	public Authorization findOne(Long authorizationId) {
		return authorizationDao.findOne(authorizationId);
	}

	public Iterable<Authorization> findAll() {
		return authorizationDao.findAll();
	}

	/**
	 * 根据用户名查找其角色
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> findRoles(String appKey, String username) {
		User user = accountService.findUserByLoginName(username);
		if (user == null) {
			return Collections.EMPTY_SET;
		}
		Long appId = appService.findAppIdByAppKey(appKey);
		if (appId == null) {
			return Collections.EMPTY_SET;
		}
		Authorization authorization = authorizationDao.findByAppIdAndUserId(appId, user.getId());
		if (authorization == null) {
			return Collections.EMPTY_SET;
		}
		return roleService.findRoles(authorization.getRoleIds().toArray(new Long[0]));
	}

	/**
	 * 根据用户名查找其权限
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> findPermissions(String appKey, String username) {
		User user = accountService.findUserByLoginName(username);
		if (user == null) {
			return Collections.EMPTY_SET;
		}
		Long appId = appService.findAppIdByAppKey(appKey);
		if (appId == null) {
			return Collections.EMPTY_SET;
		}
		Authorization authorization = authorizationDao.findByAppIdAndUserId(appId, user.getId());
		if (authorization == null) {
			return Collections.EMPTY_SET;
		}
		return roleService.findPermissions(authorization.getRoleIds().toArray(new Long[0]));
	}
}
