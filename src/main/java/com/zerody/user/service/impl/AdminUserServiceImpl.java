package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.oauth.api.vo.PlatformRoleVo;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.UnionPlatformRoleStaff;
import com.zerody.user.dto.AdminUserDto;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.AdminUserMapper;
import com.zerody.user.mapper.UnionPlatformRoleStaffMapper;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.CopyStaffInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/9 13:30
 */
@Slf4j
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserInfo> implements AdminUserService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private UnionPlatformRoleStaffMapper roleStaffMapper;
    @Autowired
    private OauthFeignService oauthFeignService;

    @Override
    public com.zerody.user.api.vo.AdminUserInfo checkLoginAdmin(String phone) {
        QueryWrapper<AdminUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(AdminUserInfo::getPhoneNumber,phone);
        com.zerody.user.api.vo.AdminUserInfo userInfo=new com.zerody.user.api.vo.AdminUserInfo();
        DataUtil.getKeyAndValue(userInfo,this.getOne(qw));
        return userInfo;
    }

    @Override
    public AdminUserInfo addAdminUser(AdminUserDto data) {
        CopyStaffInfoVo vo=sysStaffInfoService.selectStaffInfo(data.getStaffId());
        if(DataUtil.isEmpty(vo)){
            throw new DefaultException("选中的员工不存在！");
        }
        AdminUserInfo userInfo=new AdminUserInfo();
        userInfo.setUserPwd(vo.getUserPwd());
        userInfo.setUserName(vo.getUserName());
        userInfo.setAvatar(vo.getAvatar());
        userInfo.setStaffId(vo.getStaffId());
        userInfo.setPhoneNumber(vo.getPhoneNumber());
        userInfo.setCreateBy(UserUtils.getUserId());
        userInfo.setCreateTime(new Date());
        this.save(userInfo);
        //保存角色关系
        UnionPlatformRoleStaff roleStaff=new UnionPlatformRoleStaff();
        roleStaff.setAdminUserId(userInfo.getId());
        roleStaff.setRoleId(data.getRoleId());
        if(DataUtil.isEmpty(data.getRoleName())) {
            DataResult<PlatformRoleVo> platformRole = oauthFeignService.getPlatformRoleById(data.getRoleId());
            if (!platformRole.isSuccess()) {
                throw new DefaultException("服务异常！");
            }
            if (DataUtil.isEmpty(platformRole.getData())) {
                throw new DefaultException("角色不存在或已被删除！");
            }
            roleStaff.setRoleName(platformRole.getData().getRoleName());
        }else {
            roleStaff.setRoleName(data.getRoleName());
        }
        roleStaff.setStaffId(vo.getStaffId());
        roleStaffMapper.insert(roleStaff);
        return userInfo;
    }

    @Override
    public void updateAdminUser(AdminUserInfo data) {
        this.updateById(data);
    }

    @Override
    public void removeAdminUser(String id) {
        AdminUserInfo byId = this.getById(id);
        byId.setDeleted(0);
        this.updateById(byId);
        QueryWrapper<UnionPlatformRoleStaff> qw =new QueryWrapper<>();
        qw.lambda().eq(UnionPlatformRoleStaff::getStaffId,byId.getStaffId());
        roleStaffMapper.delete(qw);
    }


    @Override
    public void updateRole(String id,String roleId) {
        QueryWrapper<UnionPlatformRoleStaff> qw =new QueryWrapper<>();
        qw.lambda().eq(UnionPlatformRoleStaff::getAdminUserId,id);
        UnionPlatformRoleStaff roleStaff = roleStaffMapper.selectOne(qw);
        DataResult<PlatformRoleVo> platformRole = oauthFeignService.getPlatformRoleById(roleId);
        if(!platformRole.isSuccess()){
            throw new DefaultException("服务异常！");
        }
        if(DataUtil.isEmpty(platformRole.getData())){
            throw new DefaultException("角色不存在或已被删除！");
        }
        roleStaff.setRoleId(roleId);
        roleStaff.setRoleName(platformRole.getData().getRoleName());
        roleStaffMapper.updateById(roleStaff);
    }
}
