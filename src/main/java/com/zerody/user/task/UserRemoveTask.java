package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName UserRemoveTask
 * @DateTime 2021/4/1_20:06
 * @Deacription TODO
 */
@Component
@Slf4j
public class UserRemoveTask {

    @Autowired
    private TaskService taskService;

    @XxlJob("remove_user")
    public ReturnT<String> execute(String param){
        log.info("异步删除用户开始");
        ReturnT r=ReturnT.SUCCESS;
        try {
            int updateCount = taskService.removeUser();
            r.setMsg("删除数:"+updateCount);
        } catch (Exception e) {
            log.error("删除失败:{}",e.getMessage(),e);
            r.setMsg(e.getMessage());
        }
        log.info("结束删除用户");
        return r;
    }
}
