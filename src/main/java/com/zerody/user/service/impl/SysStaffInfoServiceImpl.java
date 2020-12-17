package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.UnionRoleStaff;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.BaseStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoServiceImpl
 * @DateTime 2020/12/17_17:31
 * @Deacription TODO
 */
@Service
public class SysStaffInfoServiceImpl extends BaseService<SysStaffInfoMapper, SysStaffInfo> implements SysStaffInfoService {


    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Override
    public DataResult addStaff(SysStaffInfo staff) {
        this.saveOrUpdate(staff);
        return new DataResult();
    }

    @Override
    public DataResult deleteStaffRole(String  staffId, String roleId) {
        QueryWrapper<UnionRoleStaff> rsQW = new QueryWrapper<>();
        rsQW.lambda().eq(UnionRoleStaff::getRoleId,roleId);
        rsQW.lambda().eq(UnionRoleStaff::getStaffId,staffId);
        unionRoleStaffMapper.delete(rsQW);
        return new DataResult();
    }

    @Override
    public DataResult staffAddRole(String staffId, String roleId) {
        UnionRoleStaff roleStaff = new UnionRoleStaff();
        roleStaff.setId(UUIDutils.getUUID32());
        roleStaff.setStaffId(staffId);
        roleStaff.setRoleId(roleId);
        unionRoleStaffMapper.insert(roleStaff);
        return new DataResult();
    }

    @Override
    public DataResult selectStaffByRoleId(String roleId) {
//       List<> sysStaffInfoMapper
        return null;
    }
}
