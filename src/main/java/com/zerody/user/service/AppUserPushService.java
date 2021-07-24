package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.task.AppUserPushTask;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/7/9 16:24
 */

public interface AppUserPushService extends IService<AppUserPush>{


	void doPushAppUser(String userId,String companyId);

	void doSendAppUserInfo(AppUserPush user);

	List<AppUserPush> selectAll();

	AppUserPush getByUserId(String id);

	List<AppUserPush> getLeaveUsers();
}
