package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysUserInfoServiceImpl
 * @DateTime 2020/12/16_17:12
 * @Deacription TODO
 */
@Service
public class SysUserInfoServiceImpl extends BaseService<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;


    @Override
    public DataResult addUser(SysUserInfo userInfo) {
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult updateUser(SysUserInfo userInfo) {
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserById(String userId) {
        this.removeById(userId);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserBatchByIds(List<Integer> ids) {
        this.removeByIds(ids);
        return new DataResult();
    }

    @Override
    public DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto) {
        return null;
    }
}
