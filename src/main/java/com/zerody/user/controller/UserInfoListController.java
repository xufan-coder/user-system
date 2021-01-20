package com.zerody.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;

import lombok.extern.slf4j.Slf4j;

/**
 * 查询用户列表
 * @author huanghuasheng
 * @create_time 2021年1月20日
 */
@Slf4j
@RestController
public class UserInfoListController {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CompanyAdminService amdinService;

    @Autowired
    private AdminUserService amdinUserService;
	/**
	 * 根据部门ID查询人员
	 * @param deptId
	 * @return
	 */
	@GetMapping(value = "/sys-user-info/dept-user")
	DataResult<List<UserVo>> getUserByDepartmentId(String deptId){
		return null;
		
	}

	/**
	 * 根据角色ID查询人员
	 * @param roleId
	 * @return
	 */
	@GetMapping(value = "/sys-user-info/role-user")
	DataResult<List<UserVo>> getUserByRoleId(String roleId){
		return null;
	}

	/**
	 * 根据岗位ID查询人员
	 * @param postId
	 * @return
	 */
	@GetMapping(value = "/sys-user-info/post-user")
	DataResult<List<UserVo>> getUserByPositionId(String postId){
		return null;
		
	}

}
