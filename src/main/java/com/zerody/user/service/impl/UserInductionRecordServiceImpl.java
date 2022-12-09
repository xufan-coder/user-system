package com.zerody.user.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.customer.api.dto.SetUserDepartDto;
import com.zerody.user.domain.*;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UnionStaffDeparService;
import com.zerody.user.service.UserInductionRecordService;
import com.zerody.user.vo.LeaveUserInfoVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author kuang
 */
@Service
public class UserInductionRecordServiceImpl extends ServiceImpl<UserInductionRecordMapper, UserInductionRecord> implements UserInductionRecordService {

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private UnionStaffDepartMapper staffDepartMapper;

    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

    @Autowired
    private UnionStaffPositionMapper unionStaffPositionMapper;

    @Override
    public Page<UserInductionRecordVo> getInductionPage(UserInductionPage queryDto) {

        Page<UserInductionRecordVo> page = new Page<>(queryDto.getCurrent(),queryDto.getPageSize());

        return this.baseMapper.getInductionPage(page,queryDto);
    }

    @Override
    public UserInductionRecordInfoVo getInductionInfo(String id) {

        UserInductionRecordInfoVo infoVo = this.baseMapper.getInductionInfo(id);
        if(infoVo == null) {
            throw  new DefaultException("未找到相关入职申请");
        }

        LeaveUserInfoVo leaveInfo = sysStaffInfoMapper.getLeaveUserInfo(infoVo.getLeaveUserId());
        infoVo.setLeaveInfo(leaveInfo);
        return infoVo;
    }

    @Override
    public UserInductionRecord addOrUpdateRecord(UserInductionRecord param) {
        if (StringUtils.isEmpty(param.getId())) {
            //判断是否已在申请中
            QueryWrapper<UserInductionRecord> qw = new QueryWrapper<>();
            qw.lambda().eq(UserInductionRecord::getUserId, param.getLeaveUserId());
            qw.lambda().eq(UserInductionRecord::getApproveState,ApproveStatusEnum.APPROVAL.name());
            qw.lambda().eq(UserInductionRecord::getDeleted,YesNo.NO);
            if(DataUtil.isNotEmpty(this.getOne(qw))){
                throw  new DefaultException("该伙伴正在申请中!");
            }
            //保存
            param.setCreateTime(new Date());
            param.setApproveState(ApproveStatusEnum.APPROVAL.name());
            param.setDeleted(YesNo.NO);
            param.setCreateBy(param.getUserId());
            param.setId(UUIDutils.getUUID32());
            this.save(param);
        }else {
            //修改
            param.setUpdateTime(new Date());
            this.updateById(param);
        }
        return param;
    }

    @Override
    public void doRenewInduction(UserInductionRecord induction) {

        induction.setApproveState(ApproveStatusEnum.SUCCESS.name());
        this.updateById(induction);

        // 更新伙伴的部门id
        String staffId = sysStaffInfoMapper.getStaffIdByUserId(induction.getLeaveUserId());
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, staffId);
        UnionStaffDepart dep = this.staffDepartMapper.selectOne(usdQW);
        if (DataUtil.isNotEmpty(dep)) {
            dep.setDepartmentId(induction.getSignDeptId());
            this.staffDepartMapper.updateById(dep);
        }else {
            dep = new UnionStaffDepart();
            dep.setId(UUIDutils.getUUID32());
            dep.setDepartmentId(induction.getSignDeptId());
            this.staffDepartMapper.insert(dep);
        }

        // 更新伙伴的角色
        QueryWrapper<UnionRoleStaff> ursQW = new QueryWrapper<>();
        ursQW.lambda().eq(UnionRoleStaff::getStaffId, staffId);
        unionRoleStaffMapper.delete(ursQW);
        UnionRoleStaff rs = new UnionRoleStaff();
        //给员工赋予角色
        rs.setStaffId(staffId);
        rs.setRoleId(induction.getSignRoleId());
        rs.setRoleName(induction.getSignRole());
        this.unionRoleStaffMapper.insert(rs);

        // 更新员工岗位
        QueryWrapper<UnionStaffPosition> spQw = new QueryWrapper<>();
        spQw.lambda().eq(UnionStaffPosition::getStaffId, staffId);
        unionStaffPositionMapper.delete(spQw);


        // 更新伙伴的状态 && 更新离职时间  离职原因  staff
        this.sysStaffInfoMapper.updateLeaveInfo(staffId,induction.getSignTime());
        this.sysUserInfoMapper.updateLeaveState(induction.getLeaveUserId());


    }

}
