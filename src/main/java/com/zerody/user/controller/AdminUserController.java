package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.AdminUserDto;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CompanyAdminService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 平台管理员
 * @author  DaBai
 * @date  2021/1/11 9:27
 */
@Slf4j
@RestController
@RequestMapping("/admin-user")
public class AdminUserController {
	@Autowired
	private AdminUserService service;

	/**
	*	添加平台管理员
	*/
	@PostMapping("/add")
	public DataResult<AdminUserInfo> save(@RequestBody AdminUserDto data) {
		try {
			this.service.addAdminUser(data);
			return R.success();
		} catch (Exception e) {
			log.error("新增管理员出错:{}", e, e);
			return R.error("新增管理员出错:"+e.getMessage());
		}
	}
	/**
	*   编辑管理员
	*/
	@PutMapping
	public DataResult<AdminUserInfo> update(@RequestBody AdminUserInfo data) {
		try {
			this.service.updateAdminUser(data);
			return R.success(data);
		} catch (Exception e) {
			log.error("修改管理员出错:{}", e, e);
			return R.error("修改管理员出错:"+e.getMessage());
		}
	}
	/**
	*    移除平台管理员
	*/
	@DeleteMapping("/del/{id}")
	public DataResult del(@PathVariable String id) {
		try {
			this.service.removeAdminUser(id);
			return R.success();
		} catch (Exception e) {
			log.error("删除管理员出错:{}", e, e);
			return R.error("删除管理员出错:"+e.getMessage());
		}
	}

	/**
	 *   编辑管理员权限
	 */
	@PutMapping("/update-role")
	public DataResult updateRole(@RequestBody AdminUserDto dto) {
		try {
			this.service.updateRole(dto.getId(),dto.getRoleId());
			return R.success();
		} catch (Exception e) {
			log.error("修改管理员权限出错:{}", e, e);
			return R.error("修改管理员权限出错:"+e.getMessage());
		}
	}




}
