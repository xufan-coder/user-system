package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.dto.AdminUserDto;

public interface AdminUserService extends IService<AdminUserInfo> {

    com.zerody.user.api.vo.AdminUserInfo checkLoginAdmin(String phone);

    AdminUserInfo addAdminUser(AdminUserDto data);

    void updateAdminUser(AdminUserInfo data);

    void removeAdminUser(String id);
}
