/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.entity.Organization;
import com.ctb.repository.OrganizationDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class OrganizationService {

	@Autowired
	private OrganizationDao organizationDao;

	
    public Organization createOrganization(Organization organization) {
        return organizationDao.save(organization);
    }

    public Organization updateOrganization(Organization organization) {
        return organizationDao.save(organization);
    }

    public void deleteOrganization(Long organizationId) {
        organizationDao.delete(organizationId);
    }

    public Organization findOne(Long organizationId) {
        return organizationDao.findOne(organizationId);
    }

    public Iterable<Organization> findAll() {
        return organizationDao.findAll();
    }

    public List<Organization> findAllWithExclude(Organization excludeOraganization) {
    	return organizationDao.findIdNotAndParentIdsNotLike(excludeOraganization.getId(), excludeOraganization.getParentIds());
    }

    
    //TODO 需要测试
    public void move(Organization source, Organization target) {
        moveSource(source.getId(),target); 
        moveSourceDescendants(source,target);
    }

    
    public void moveSource(Long id, Organization target){
    	Organization tmp = organizationDao.findOne(id);
    	tmp.setParentId(target.getId());
    	tmp.setParentIds(target.getParentIds());
    	organizationDao.save(tmp);
    }
    
    public void moveSourceDescendants(Organization source, Organization target){
    	Organization tmp = organizationDao.findParentIdsLike(source.makeSelfAsParentIds() + "%");
    	tmp.setParentIds(target.makeSelfAsParentIds().concat(tmp.getParentIds().substring(source.makeSelfAsParentIds().length())));
    	organizationDao.save(tmp);
    }
	
}
