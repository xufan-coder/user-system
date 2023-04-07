package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.Data;
import com.zerody.user.domain.PrepareExecutiveRecord;
import com.zerody.user.dto.PrepareExecutiveRecordDto;
import com.zerody.user.mapper.PrepareExecutiveRecordMapper;
import com.zerody.user.service.*;
import com.zerody.user.vo.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private StaffBlacklistService staffBlacklistService;

    @Override
    public void addPrepareExecutiveRecord(PrepareExecutiveRecordDto param, UserVo userVo) {

        if (param.getIsPrepareExecutive() != 0 && param.getIsPrepareExecutive() != null){

            PrepareExecutiveRecord prepareExecutiveRecord = new PrepareExecutiveRecord();
            SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(param.getUserId(), userVo, true);
            BeanUtils.copyProperties(sysUserInfoVo,prepareExecutiveRecord);
            prepareExecutiveRecord.setUserId(param.getUserId());
            prepareExecutiveRecord.setIsPrepareExecutive(param.getIsPrepareExecutive());


            if (param.getIsPrepareExecutive() == 1) {
                QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
                qw.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,1);
                qw.lambda().eq(PrepareExecutiveRecord::getUserId,param.getUserId());
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
                            record.setOutDate(new Date());
                        } else if (param.getOutDate().after(record.getEnterDate()) || param.getOutDate().equals(record.getEnterDate())) {
                            record.setOutDate(param.getOutDate());
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
                final CreateInfoVo infoVo = createInfoVo;

                prepareExecutiveRecord.setCreateId(infoVo.getOperateUserId());
                prepareExecutiveRecord.setCreateName(infoVo.getOperateUserName());
                prepareExecutiveRecord.setCreateTime(new Date());
            }

            this.save(prepareExecutiveRecord);
        }

    }

    @Override
    public List<PrepareExecutiveRecordVo> getPrepareExecutiveRecordList(String userId) {
        QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(PrepareExecutiveRecord::getIsPrepareExecutive,2);
        qw.lambda().eq(PrepareExecutiveRecord::getUserId,userId);
        qw.lambda().orderByDesc(PrepareExecutiveRecord::getCreateTime);
        List<PrepareExecutiveRecord> list = this.list(qw);
        List<PrepareExecutiveRecordVo> voList = new ArrayList<>();
        for (PrepareExecutiveRecord prepareExecutiveRecord:list) {
            PrepareExecutiveRecordVo vo = new PrepareExecutiveRecordVo();
            BeanUtils.copyProperties(prepareExecutiveRecord,vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public PrepareExecutiveRecordVo getPrepareExecutiveRecord(String userId) {
        QueryWrapper<PrepareExecutiveRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(PrepareExecutiveRecord::getUserId,userId);
        qw.lambda().orderByDesc(PrepareExecutiveRecord::getCreateTime);
        qw.lambda().last("limit 0,1");
        PrepareExecutiveRecord one = this.getOne(qw);
        PrepareExecutiveRecordVo recordVo = new PrepareExecutiveRecordVo();
        BeanUtils.copyProperties(one,recordVo);
        return recordVo;
    }

}
