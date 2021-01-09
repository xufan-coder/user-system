package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AdminUserInfo;

public interface AdminUserService extends IService<AdminUserInfo> {

    com.zerody.user.api.vo.AdminUserInfo checkLoginAdmin(String phone);
}
