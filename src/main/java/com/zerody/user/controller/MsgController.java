package com.zerody.user.controller;

import java.util.Date;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.util.UserUtils;
import com.zerody.user.dto.*;
import com.zerody.user.domain.*;
import com.zerody.user.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
/**
 * 
 * 消息控制类 
 * @author 黄华盛 
 * @date 2021-01-11
 */
@RestController
@RequestMapping("/msg")
public class MsgController {
	private static final Logger log=LoggerFactory.getLogger(MsgController.class);
	@Autowired
	private MsgService service;
	private static final int MODULE_CODE=1001;
	
	/**
	 *  消息分页查询 
	 * @param grid
	 * @param session
	 * @return
	 * @author 黄华盛
	 * @date 2021-01-11
	 */
	@ApiOperation(value = "分页查询")
	@GetMapping("/page")
	public DataResult<?> page(MsgPageDto pageDto) {
		try {
			Object data = this.service.pageData(pageDto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询消息出错:{}", e, e);
			return R.error("分页查询消息出错:"+e.getMessage());
		}
	}
	
	@ApiOperation(value = "删除消息")
	@DeleteMapping("/{id}")
	public DataResult<?> del(@PathVariable String id) {
		try {
			this.service.removeById(id);
			return R.success();
		} catch (Exception e) {
			log.error("删除消息出错:{}", e, e);
			return R.error("删除消息出错:"+e.getMessage());
		}
	}
	@ApiOperation(value = "获取消息")
	@GetMapping("/{id}")
	public DataResult<Msg> get(@PathVariable String id) {
		try {
			Msg obj = this.service.getById(id);
			if (obj == null) {
				return R.error("找不到消息");
			}
			return R.success(obj);
		} catch (Exception e) {
			log.error("获取消息出错:{}", e, e);
			return R.error("获取消息出错:"+e.getMessage());
		}
	}
}
