package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.MonthActivityUser;

/**
 * @author PengQiang
 * @ClassName MonthActivityUserService
 * @DateTime 2022/1/15_14:37
 * @Deacription TODO
 */
public interface MonthActivityUserService extends IService<MonthActivityUser> {
    void doAddMonthActivityUser();
}
