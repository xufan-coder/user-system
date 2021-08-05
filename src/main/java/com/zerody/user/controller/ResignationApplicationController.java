package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.api.service.AdminUserRemoteService;
import com.zerody.user.api.vo.AdminUserJurisdictionInfo;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.dto.AdminUserDto;
import com.zerody.user.dto.ResignationPageDto;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.base.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 离职申请管理
 * @author  DaBai
 * @date  2021/8/5 15:16
 */

@Slf4j
@RestController
@RequestMapping("/resignation")
public class ResignationApplicationController {
	@Autowired
	private CheckUtil checkUtil;
	@Autowired
	private ResignationApplicationService service;
	/**
	 *    分页查询列表
	 */
	@GetMapping("/page")
	public DataResult<IPage<ResignationApplication>> page(ResignationPageDto dto) {
		try {
			this.checkUtil.SetUserPositionInfo(dto);
			IPage<ResignationApplication> data = service.selectPage(dto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询离职申请出错:{}", e, e);
			return R.error("分页离职申请出错:" + e.getMessage());
		}
	}


	/**
	*	添加或修改离职申请
	*/
	@PostMapping("/add")
	public DataResult<ResignationApplication> saveOrUpdate(@RequestBody ResignationApplication data) {
		try {
			this.service.addOrUpdateResignationApplication(data);
			return R.success();
		} catch (Exception e) {
			log.error("新增离职申请出错:{}", JSON.toJSONString(data), e);
			return R.error("新增离职申请出错:"+e.getMessage());
		}
	}

}
