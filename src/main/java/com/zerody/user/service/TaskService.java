package com.zerody.user.service;

import java.util.Map;

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
	void buildVisitNoticeInfo(Map<String, String> user);

	/**
	 *
	 * 异步删除用户
	 * @author               PengQiang
	 * @description          DELL
	 * @date                 2021/4/1 20:48
	 * @param
	 * @return               int
	 */
    int removeUser();
}
