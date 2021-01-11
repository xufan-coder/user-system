package com.zerody.user.service;

/**
 * 定时任务业务实现类
 * @author  DaBai
 * @date  2021/1/11 17:42
 */

public interface TaskService {
    /**************************************************************************************************
     **
     *  构建 客户跟进提醒消息
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2021/1/11  17:46
     */
    void buildVisitNoticeInfo();
}
