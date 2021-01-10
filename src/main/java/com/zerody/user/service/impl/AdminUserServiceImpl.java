package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.mapper.AdminUserMapper;
import com.zerody.user.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author  DaBai
 * @date  2021/1/9 13:30
 */
@Slf4j
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserInfo> implements AdminUserService {


    @Override
    public com.zerody.user.api.vo.AdminUserInfo checkLoginAdmin(String phone) {
        QueryWrapper<AdminUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(AdminUserInfo::getPhoneNumber,phone);
        com.zerody.user.api.vo.AdminUserInfo userInfo=new com.zerody.user.api.vo.AdminUserInfo();
        DataUtil.getKeyAndValue(userInfo,this.getOne(qw));
        return userInfo;
    }
}
