package com.zerody.user.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息跟进提醒生成
 * @author  DaBai
 * @date  2021/1/11 17:38
 */
@Component
@Slf4j
public class BuildVisitNoticeTask {

    @Autowired
    private TaskService taskService;
	@Autowired
	SysUserInfoService sysUserInfoService;

    @XxlJob("follow_up_message")
    public ReturnT<String> execute(String param){
		// 获取所有用户ID
		List<Map<String, String>> list = sysUserInfoService.selectAllUserId();
		// 统计客户跟进提醒三种类型的，客户数
		for (Map<String, String> user : list) {
			taskService.buildVisitNoticeInfo(user);
		}
        return ReturnT.SUCCESS;
    }
}
