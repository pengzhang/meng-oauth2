/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ctb.entity.Organization;

public interface OrganizationDao extends PagingAndSortingRepository<Organization, Long> {

	//"select id, name, parent_id, parent_ids, available from sys_organization where id!=? and parent_ids not like ?";
	 List<Organization> findIdNotAndParentIdsNotLike(Long id,String parentIds);
	 
	 //"update sys_organization set parent_id=?,parent_ids=? where id=?"
	 
	 //"update sys_organization set parent_ids=concat(?, substring(parent_ids, length(?))) where parent_ids like ?"
	 Organization findParentIdsLike(String parentIds);
}
