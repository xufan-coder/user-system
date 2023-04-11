package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.*;
import com.zerody.user.dto.PrepareExecutiveRecordDto;
import com.zerody.user.mapper.PrepareExecutiveRecordMapper;
import com.zerody.user.service.*;
import com.zerody.user.vo.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author : xufan
 * @create 2023/4/3 13:51
 */
@Service
public class PrepareExecutiveRecordServiceImpl extends ServiceImpl<PrepareExecutiveRecordMapper, PrepareExecutiveRecord> implements PrepareExecutiveRecordService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Override
    public void addPrepareExecutiveRecord(PrepareExecutiveRecordDto param, UserVo userVo) {

        if (param.getIsPrepareExecutive() != 0 && param.getIsPrepareExecutive() != null){

            PrepareExecutiveRecord prepareExecutiveRecord = new PrepareExecutiveRecord();
            SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(param.getUserId(), userVo, true);

            //同步预备高管状态字段到用户表
            SysUserInfo sysUserInfo = sysUserInfoService.getUserById(param.getUserId());
            sysUserInfo.setIsPrepareExecutive(param.getIsPrepareExecutive());
            sysUserInfoService.updateById(sysUserInfo);

            prepareExecutiveRecord.setCompanyId(sysUserInfoVo.getCompanyId());
            prepareExecutiveRecord.setCompanyName(sysUserInfoVo.getCompanyName());
            prepareExecutiveRecord.setRoleId(sysUserInfoVo.getRoleId());
            prepareExecutiveRecord.setRoleName(sysUserInfoVo.getRoleName());
            prepareExecutiveRecord.setUserId(sysUserInfoVo.getId());
            prepareExecutiveRecord.setUserName(sysUserInfoVo.getUserName());
            prepareExecutiveRecord.setIsPrepareExecutive(param.getIsPrepareExecutive());

            if (param.getIsPrepareExecutive() == 1) {
                // 查询表中是否存在记录
                QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
                qw.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,1);
                qw.lambda().eq(PrepareExecutiveRecord::getUserId,param.getUserId());
                /*qw.lambda().orderByDesc(PrepareExecutiveRecord::getEnterDate);
                qw.lambda().last("limit 0,1");*/
                PrepareExecutiveRecord record = this.getOne(qw);

                //获取签约日期
                Date registerTime = sysUserInfoVo.getRegisterTime();

                if(DataUtil.isNotEmpty(record)){
                    //修改入学日期
                    if (param.getEnterDate().after(registerTime) || param.getEnterDate().equals(registerTime)) {
                        record.setEnterDate(param.getEnterDate());
                        record.setUserId(param.getUserId());
                        this.updateById(record);
                        return;
                    }
                }else {
                    //添加入学日期
                    if (DataUtil.isNotEmpty(param.getEnterDate())){

                        //获取上一次退学日期
                        QueryWrapper<PrepareExecutiveRecord> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,2);
                        queryWrapper.lambda().eq(PrepareExecutiveRecord::getUserId,param.getUserId());
                        queryWrapper.lambda().orderByDesc(PrepareExecutiveRecord::getCreateTime);
                        queryWrapper.lambda().last("limit 0,1");
                        PrepareExecutiveRecord one = this.getOne(queryWrapper);

                        if (DataUtil.isNotEmpty(one)){
                            //判断入学日期是否大于上次退学日期,入学日期必须大于上次退学日期
                            if (param.getEnterDate().after(one.getOutDate())){
                                prepareExecutiveRecord.setEnterDate(param.getEnterDate());
                            }else {
                                throw new DefaultException("入学日期输入错误,入学日期必须大于上次退学日期");
                            }
                        }

                        prepareExecutiveRecord.setEnterDate(param.getEnterDate());
                    }
                }
            } else if (param.getIsPrepareExecutive() == 2) {

                QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
                qw.lambda().eq(PrepareExecutiveRecord::getUserId, param.getUserId());
                qw.lambda().orderByDesc(PrepareExecutiveRecord::getCreateTime);
                qw.lambda().last("limit 0,1");
                PrepareExecutiveRecord record = this.getOne(qw);
                if (DataUtil.isNotEmpty(record)) {

                    // 编辑退学日期和退学原因
                    if (DataUtil.isNotEmpty(record.getIsPrepareExecutive())) {
                        record.setOutReason(param.getOutReason());
                        /*FrameworkBlacListQueryPageVo infoById = staffBlacklistService.getInfoById(param.getUserId());
                        if (DataUtil.isNotEmpty(infoById)) {
                            record.setOutReason(infoById.getReason());
                        }
                        LeaveUserInfoVo quitUserInfo = sysStaffInfoService.getQuitUserInfo(param.getUserId());
                        if (DataUtil.isNotEmpty(quitUserInfo)){
                            record.setOutReason(quitUserInfo.getLeaveRemark());
                        }*/

                        if (DataUtil.isEmpty(param.getOutDate())) {
                            if (record.getEnterDate().before(new Date())) {
                                record.setOutDate(new Date());
                            }else {
                                throw new DefaultException("当前默认退学日期必须大于入学日期");
                            }
                        } else if (param.getOutDate().after(record.getEnterDate()) || param.getOutDate().equals(record.getEnterDate())) {
                            record.setOutDate(param.getOutDate());
                        } else {
                            throw new DefaultException("退学日期输入错误,退学日期必须大于入学日期");
                        }
                        record.setIsPrepareExecutive(param.getIsPrepareExecutive());
                        record.setUserId(param.getUserId());
                        this.updateById(record);
                        return;
                    }
                }
            }
            //创建人信息
            CreateInfoVo createInfoVo = new CreateInfoVo();
            if (ObjectUtils.isNotEmpty(userVo)) {
                if (userVo.isBack()) {
                    AdminUserInfo byId = this.adminUserService.getById(userVo.getUserId());
                    if (DataUtil.isNotEmpty(byId)) {
                        createInfoVo.setOperateUserId(byId.getId());
                        createInfoVo.setOperateUserName(byId.getUserName());
                    }
                }
                if (userVo.isCEO()) {
                    CeoUserInfo byId = this.ceoUserInfoService.getById(userVo.getUserId());
                    if (DataUtil.isNotEmpty(byId)) {
                        createInfoVo.setOperateUserId(userVo.getUserId());
                        createInfoVo.setOperateUserName(byId.getUserName());
                    }
                }
                if (!userVo.isCEO() && !userVo.isBack()) {
                    createInfoVo = this.baseMapper.getCreateInfoByCreateId(userVo);
                }
                CreateInfoVo infoVo = createInfoVo;

                prepareExecutiveRecord.setCreateId(infoVo.getOperateUserId());
                prepareExecutiveRecord.setCreateName(infoVo.getOperateUserName());
                prepareExecutiveRecord.setCreateTime(new Date());
            }

            this.save(prepareExecutiveRecord);
        } else if (param.getIsPrepareExecutive() == 0){
            //伙伴是预备高管时，之后预备高管操作不能选择：否，只能选退学
            QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
            qw.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,1);
            qw.lambda().eq(PrepareExecutiveRecord::getUserId,param.getUserId());
            PrepareExecutiveRecord record = this.getOne(qw);
            if (DataUtil.isNotEmpty(record)){
                throw new DefaultException("操作失败，伙伴已经是预备高管，预备高管操作不能选择否，只能选退学");
            }
        }

    }

    @Override
    public List<PrepareExecutiveRecordVo> getPrepareExecutiveRecordList(String userId) {
        QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,2);
        qw.lambda().eq(PrepareExecutiveRecord::getUserId,userId);
        qw.lambda().orderByDesc(PrepareExecutiveRecord::getEnterDate);
        List<PrepareExecutiveRecord> list = this.list(qw);
        List<PrepareExecutiveRecordVo> voList = new ArrayList<>();
        if(DataUtil.isNotEmpty(list) && list.size() > 0){
            for (PrepareExecutiveRecord prepareExecutiveRecord:list) {
                PrepareExecutiveRecordVo vo = new PrepareExecutiveRecordVo();
                BeanUtils.copyProperties(prepareExecutiveRecord,vo);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public PrepareExecutiveRecordVo getPrepareExecutiveRecord(String userId) {
        QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(PrepareExecutiveRecord::getUserId,userId);
        qw.lambda().orderByDesc(PrepareExecutiveRecord::getEnterDate);
        qw.lambda().last("limit 0,1");
        PrepareExecutiveRecord one = this.getOne(qw);
        if(DataUtil.isEmpty(one)){
            return null;
        }
        PrepareExecutiveRecordVo recordVo = new PrepareExecutiveRecordVo();
        BeanUtils.copyProperties(one,recordVo);
        return recordVo;
    }

    @Override
    public PrepareExecutiveRecordVo getPrepareExecutiveRecordInner(String userId) {
        QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(PrepareExecutiveRecord::getUserId,userId);
        qw.lambda().orderByDesc(PrepareExecutiveRecord::getEnterDate);
        qw.lambda().last("limit 0,1");
        PrepareExecutiveRecord one = this.getOne(qw);
        if(DataUtil.isEmpty(one)){
            return null;
        }
        if(one.getIsPrepareExecutive()!=1){
            return null;
        }
        PrepareExecutiveRecordVo recordVo = new PrepareExecutiveRecordVo();
        BeanUtils.copyProperties(one,recordVo);
        return recordVo;
    }
}
