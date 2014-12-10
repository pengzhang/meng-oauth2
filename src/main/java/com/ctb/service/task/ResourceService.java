/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.service.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ctb.entity.Resource;
import com.ctb.repository.ResourceDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ResourceService {

	 @Autowired
	    private ResourceDao resourceDao;

	    public Resource createResource(Resource resource) {
	        return resourceDao.save(resource);
	    }

	    public Resource updateResource(Resource resource) {
	        return resourceDao.save(resource);
	    }

	    public void deleteResource(Long resourceId) {
	        resourceDao.delete(resourceId);
	    }

	    public Resource findOne(Long resourceId) {
	        return resourceDao.findOne(resourceId);
	    }

	    public Iterable<Resource> findAll() {
	        return resourceDao.findAll();
	    }

	    public Set<String> findPermissions(Set<Long> resourceIds) {
	        Set<String> permissions = new HashSet<String>();
	        for(Long resourceId : resourceIds) {
	            Resource resource = findOne(resourceId);
	            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
	                permissions.add(resource.getPermission());
	            }
	        }
	        return permissions;
	    }

	    public List<Resource> findMenus(Set<String> permissions) {
	    	Iterable<Resource> allResources = findAll();
	        List<Resource> menus = new ArrayList<Resource>();
	        for(Resource resource : allResources) {
	            if(resource.isRootNode()) {
	                continue;
	            }
	            if(resource.getType() != Resource.ResourceType.menu) {
	                continue;
	            }
	            if(!hasPermission(permissions, resource)) {
	                continue;
	            }
	            menus.add(resource);
	        }
	        return menus;
	    }

	    private boolean hasPermission(Set<String> permissions, Resource resource) {
	        if(StringUtils.isEmpty(resource.getPermission())) {
	            return true;
	        }
	        for(String permission : permissions) {
	            WildcardPermission p1 = new WildcardPermission(permission);
	            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
	            if(p1.implies(p2) || p2.implies(p1)) {
	                return true;
	            }
	        }
	        return false;
	    }
}
