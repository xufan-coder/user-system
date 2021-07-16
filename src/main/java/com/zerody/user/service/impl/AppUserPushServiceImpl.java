package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.partner.api.dto.PartRegisterDto;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.feign.PartnerFeignService;
import com.zerody.user.mapper.AppUserPushMapper;
import com.zerody.user.service.AppUserPushService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.task.AppUserPushTask;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2021/7/9 16:26
 */

@Service
public class AppUserPushServiceImpl extends ServiceImpl<AppUserPushMapper, AppUserPush> implements AppUserPushService {
	private static final Logger log = LoggerFactory.getLogger(AppUserPushServiceImpl.class);
	@Autowired
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private PartnerFeignService partnerFeignService;
	@Async
	@Override
	public void doPushAppUser(String userId,String companyId) {
		AppUserPush appUserPush= new AppUserPush();
		appUserPush.setCompId(companyId);
		appUserPush.setUserId(userId);
		appUserPush.setCreateTime(new Date());
		appUserPush.setState(YesNo.NO);
		appUserPush.setDeleted(YesNo.NO);
		this.save(appUserPush);
	}

	@Override
	public void sendAppUserInfo(AppUserPush user) {
		//查询Crm用户信息
		SysLoginUserInfoVo sysLoginUserInfoVo = sysUserInfoService.selectTransUserInfo(user.getUserId());
		if(DataUtil.isEmpty(sysLoginUserInfoVo)){
			log.error("查不到用户ID");
			return;
		}
		//推送用户信息到APP
		PartRegisterDto dto=new PartRegisterDto();
		dto.setName(sysLoginUserInfoVo.getUserName());
		dto.setMobile(sysLoginUserInfoVo.getPhoneNumber());
		dto.setPassword(sysLoginUserInfoVo.getUserPwd());
		dto.setCrmCompanyId(sysLoginUserInfoVo.getCompanyId());
		dto.setCrmUserId(sysLoginUserInfoVo.getId());
		dto.setAvatar(sysLoginUserInfoVo.getAvatar());
		dto.setCrmCompanyName(sysLoginUserInfoVo.getCompanyName());
		dto.setCrmDeptName(sysLoginUserInfoVo.getDeptName());
		DataResult<Void> voidDataResult = partnerFeignService.partRegisters(dto);
		if(!voidDataResult.isSuccess()){
			log.error("推送失败："+user.getUserId()+"------"+voidDataResult.getMessage());
		}
		//并修改推送状态
		user.setState(YesNo.YES);
		user.setSendTime(new Date());
		this.updateById(user);
	}

	@Override
	public List<AppUserPush> selectAll() {
		QueryWrapper<AppUserPush> qw =new QueryWrapper<>();
		qw.lambda().eq(AppUserPush::getState,YesNo.NO)
				.eq(AppUserPush::getDeleted,YesNo.YES);
		return this.list(qw);
	}
}
