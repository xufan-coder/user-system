package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.UnionRoleStaff;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.BaseStringService;
import com.zerody.user.vo.SysStaffInfoVo;
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
    public DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto) {
        //设置当前页与当前页所显示条数
        Integer pageNum = sysStaffInfoPageDto.getPageNum() == 0 ? 1 : sysStaffInfoPageDto.getPageNum();
        Integer pageSize = sysStaffInfoPageDto.getPageSize() == 0 ? 1 : sysStaffInfoPageDto.getPageSize();
        IPage<SysStaffInfoVo> infoVoIPage = new Page<>(pageNum,pageSize);
        infoVoIPage = sysStaffInfoMapper.selectPageStaffByRoleId(sysStaffInfoPageDto,infoVoIPage);
        return new DataResult(infoVoIPage);
    }

    @Override
    public DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        //设置当前页与当前页所显示条数
        Integer pageNum = sysStaffInfoPageDto.getPageNum() == 0 ? 1 : sysStaffInfoPageDto.getPageNum();
        Integer pageSize = sysStaffInfoPageDto.getPageSize() == 0 ? 1 : sysStaffInfoPageDto.getPageSize();
        IPage<SysStaffInfoVo> infoVoIPage = new Page<>(pageNum,pageSize);
        infoVoIPage = sysStaffInfoMapper.getPageAllStaff(sysStaffInfoPageDto,infoVoIPage);
        return null;
    }
}
