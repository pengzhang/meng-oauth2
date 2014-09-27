/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ctb.rest;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springside.modules.beanvalidator.BeanValidators;

import com.ctb.entity.Task;
import com.ctb.service.task.TaskService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author zp
 */
@Path("/task")
@Api(value="/task" , description = "Task 管理")
@Component
public class RestController {

	private static Logger logger = LoggerFactory.getLogger(RestController.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private Validator validator;


	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	 @ApiOperation(value = "根据id查找Task", notes = "id大于0",response = Task.class)
	 @ApiResponses(value = { @ApiResponse(code = 400, message = "无效的id"),
	 @ApiResponse(code = 404, message = "Task没有找到") })
	public Task get(@PathParam("id")  Long id) {
		Task task = taskService.getTask(id);
		if (task == null) {
			logger.warn("任务不存在(id:" + id + ")");
			throw new JerseyException("t001");
		}
		return task;
	}
	
	@GET
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	 @ApiOperation(value = "创建Task", notes = "创建Task",response = String.class)
	 @ApiResponses(value = { @ApiResponse(code = 400, message = "创建Task失败"),
	 @ApiResponse(code = 404, message = "创建Task地址无效") })
	public MessageBean create(Task task) {
			// 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
			BeanValidators.validateWithException(validator, task);
			// 保存任务
			taskService.saveTask(task);
		return RestMessage.getMessageBean("t001");
	}

}
