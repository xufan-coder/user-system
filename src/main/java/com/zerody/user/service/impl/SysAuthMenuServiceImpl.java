package com.zerody.user.service.impl;

import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.ResultEnum;
import com.zerody.common.util.UserUtils;
import com.zerody.user.mapper.SysAuthMenuMapper;
import com.zerody.user.mapper.SysAuthRoleInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.pojo.SysAuthMenu;
import com.zerody.user.service.SysAuthMenuService;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import com.zerody.user.vo.SysStaffInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuServiceImpl
 * @DateTime 2020/12/16_11:35
 * @Deacription TODO
 */
@Service
public class SysAuthMenuServiceImpl implements SysAuthMenuService  {

    @Autowired
    private SysAuthMenuMapper sysAuthMenuMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysAuthRoleInfoMapper sysAuthRoleInfoMapper;

    @Override
    public DataResult getMenuCodeList(String compId) {
        String userId = UserUtils.getUserId();  //获取当前登录用户
        SysStaffInfoVo staffVo = sysStaffInfoMapper.selectByUserIdAndCompId(userId,compId);
        if(Objects.isNull(staffVo)){
            return  new DataResult(ResultCodeEnum.RESULT_ERROR, false, "没有该员工", null);
        }
        List<SysAuthRoleInfoVo> roles = sysAuthRoleInfoMapper.selectRolesByStaffId(staffVo.getId());
        if(roles.size()<1){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "没有该员工", null);
        }
        // admin当前用户是否是超级管理员
        boolean admin = false;
        for (SysAuthRoleInfoVo role: roles
             ) {
            //查看有没有超级管理员权限
            if ("0".equals(role.getRoleType())){
                admin = !admin;
            }
        }
        //超级管理员拥有全部的菜单权限
        if(admin){
            return new DataResult(sysAuthMenuMapper.selectCodeAdmin());
        }
        return new DataResult(sysAuthMenuMapper.selectCodeByRoleId(roles));
    }
}
