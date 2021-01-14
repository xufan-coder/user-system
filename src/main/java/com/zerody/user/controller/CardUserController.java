package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.dto.AdminUserDto;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CardUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 名片账户管理
 * @author  DaBai
 * @date  2021/1/14 19:11
 */

@Slf4j
@RestController
@RequestMapping("/card-user")
public class CardUserController {
	@Autowired
	private CardUserService service;

	/**
	 * 【绑定手机号】
	*   修改名片用户信息
	*/
	@PutMapping
	public DataResult<CardUserInfo> update(@RequestBody CardUserInfo data) {
		try {
			this.service.bindPhoneNumber(data);
			return R.success(data);
		} catch (Exception e) {
			log.error("修改名片用户出错:{}", e, e);
			return R.error("修改名片用户出错:"+e.getMessage());
		}
	}




}
