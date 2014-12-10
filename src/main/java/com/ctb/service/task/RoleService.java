/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.service.task;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.entity.Role;
import com.ctb.repository.RoleDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ResourceService resourceService;

	public Role createApp(Role role) {
		return roleDao.save(role);
	}

	public Role updateApp(Role role) {
		return roleDao.save(role);
	}

	public void deleteApp(Long appId) {
		roleDao.delete(appId);
	}

	public Role findOne(Long appId) {
		return roleDao.findOne(appId);
	}

	public Set<String> findRoles(Long... roleIds) {
		Set<String> roles = new HashSet<String>();
		for (Long roleId : roleIds) {
			Role role = findOne(roleId);
			if (role != null) {
				roles.add(role.getRole());
			}
		}
		return roles;
	}
	
	public Set<String> findPermissions(Long[] roleIds) {
        Set<Long> resourceIds = new HashSet<Long>();
        for(Long roleId : roleIds) {
            Role role = findOne(roleId);
            if(role != null) {
                resourceIds.addAll(role.getResourceIds());
            }
        }
        return resourceService.findPermissions(resourceIds);
    }

	public Iterable<Role> findAll() {
		return roleDao.findAll();
	}

	public Page<Role> findAll(int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return roleDao.findAll(pageRequest);
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
