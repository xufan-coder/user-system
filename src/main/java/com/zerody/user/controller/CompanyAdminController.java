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
/**
 * 
 * 企业管理员控制类 
 * @author 黄华盛 
 * @date 2021-01-04
 */
@RestController
@RequestMapping("/company-admin")
public class CompanyAdminController {
	private static final Logger log=LoggerFactory.getLogger(CompanyAdminController.class);
	@Autowired
	private CompanyAdminService service;
	private static final int MODULE_CODE=1001;
	
	/**
	 *  企业管理员分页查询 
	 * @param grid
	 * @param session
	 * @return
	 * @author 黄华盛
	 * @date 2021-01-04
	 */
	@ApiOperation(value = "分页查询")
	@GetMapping("/page")
	public DataResult<?> page(CompanyAdminPageDto pageDto) {
		try {
			Object data = this.service.pageData(pageDto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询企业管理员出错:{}", e, e);
			return R.error("分页查询企业管理员出错:"+e.getMessage());
		}
	}
	
	/**
	*	添加管理员
	*/
	@PostMapping("/add")
	public DataResult<CompanyAdmin> save(@RequestBody CompanyAdmin data) {
		try {
			String userId = UserUtils.getUserId();
			if (userId != null) {
				data.setCreateBy(userId);
			}
			data.setCreateTime(new Date());
			this.service.addCompanyAdmin(data);
			return R.success(data);
		} catch (Exception e) {
			log.error("新增企业管理员出错:{}", e, e);
			return R.error("新增企业管理员出错:"+e.getMessage());
		}
	}
	@ApiOperation(value = "修改企业管理员")
	@PutMapping
	public DataResult<CompanyAdmin> update(@RequestBody CompanyAdmin data) {
		try {
			String userId = UserUtils.getUserId();
			this.service.updateCompanyAdmin(data);
			return R.success(data);
		} catch (Exception e) {
			log.error("修改企业管理员出错:{}", e, e);
			return R.error("修改企业管理员出错:"+e.getMessage());
		}
	}
	@ApiOperation(value = "删除企业管理员")
	@DeleteMapping("/{id}")
	public DataResult<?> del(@PathVariable String id) {
		try {
			this.service.removeById(id);
			return R.success();
		} catch (Exception e) {
			log.error("删除企业管理员出错:{}", e, e);
			return R.error("删除企业管理员出错:"+e.getMessage());
		}
	}
	@ApiOperation(value = "获取企业管理员")
	@GetMapping("/{id}")
	public DataResult<CompanyAdmin> get(@PathVariable String id) {
		try {
			CompanyAdmin obj = this.service.getById(id);
			if (obj == null) {
				return R.error("找不到企业管理员");
			}
			return R.success(obj);
		} catch (Exception e) {
			log.error("获取企业管理员出错:{}", e, e);
			return R.error("获取企业管理员出错:"+e.getMessage());
		}
	}
	
	@ApiOperation(value = "查询企业管理员")
	@GetMapping("/items")
	public DataResult<?> getItems() {
		try {
		
			Object obj = this.service.findData();
			return R.success(obj);
		} catch (Exception e) {
			log.error("查询企业管理员出错:{}", e, e);
			return R.error("查询企业管理员出错:"+e.getMessage());
		}
	}
}
