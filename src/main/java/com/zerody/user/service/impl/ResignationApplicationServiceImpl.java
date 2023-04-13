package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.*;
import com.zerody.user.dto.ResignationPageDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.VisitNoticeTypeEnum;
import com.zerody.user.mapper.ResignationApplicationMapper;
import com.zerody.user.service.PositionRecordService;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.PrepareExecutiveRecordVo;
import com.zerody.user.vo.SysUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2021/8/5 15:10
 */

@Slf4j
@Service
public class ResignationApplicationServiceImpl extends ServiceImpl<ResignationApplicationMapper, ResignationApplication> implements ResignationApplicationService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private PositionRecordService positionRecordService;
    @Autowired
    private PrepareExecutiveRecordServiceImpl prepareExecutiveRecordService;
    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Override
    public ResignationApplication addOrUpdateResignationApplication(ResignationApplication data) {
        SysUserInfoVo sysUserInfoVo = null;
        if (DataUtil.isNotEmpty(data.getUserId())) {
            sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(data.getUserId(),null,false);
        }
        if(DataUtil.isNotEmpty(data.getId())){
            data.setApprovalTime(new Date());
            this.updateById(data);
        }else {
            if(DataUtil.isNotEmpty(data.getUserId())){
                if(DataUtil.isNotEmpty(sysUserInfoVo)){
                    data.setStaffId(sysUserInfoVo.getStaffId());
                    data.setName(sysUserInfoVo.getUserName());
                    data.setCompanyId(sysUserInfoVo.getCompanyId());
                    data.setCompanyName(sysUserInfoVo.getCompanyName());
                    data.setDepartId(sysUserInfoVo.getDepartId());
                    data.setDepartName(sysUserInfoVo.getDepartName());
                    data.setPositionId(sysUserInfoVo.getPositionId());
                    data.setPositionName(sysUserInfoVo.getPositionName());

                    //预备高管
                    PrepareExecutiveRecordVo prepareExecutiveRecord = prepareExecutiveRecordService.getPrepareExecutiveRecordInner(data.getUserId());
                    if(DataUtil.isNotEmpty(prepareExecutiveRecord)){
                        SysUserInfo byId = this.sysUserInfoService.getById(data.getUserId());
                        if(DataUtil.isNotEmpty(byId)){
                            if(prepareExecutiveRecord.getEnterDate().after(data.getResignationTime())){
                                throw new DefaultException("当前离职时间小于预备高管入学时间，不允许离职");
                            }
                            byId.setIsPrepareExecutive(2);
                            PrepareExecutiveRecord record= new PrepareExecutiveRecord();
                            BeanUtils.copyProperties(prepareExecutiveRecord,record);
                            record.setIsPrepareExecutive(2);
                            record.setOutDate(data.getResignationTime());
                            record.setOutReason(data.getReason());
                            log.info("记录-----------"+record);
                            this.prepareExecutiveRecordService.updateById(record);
                            this.sysUserInfoService.updateById(byId);
                        }

                    }
                }
            }
            data.setCreateTime(new Date());
            this.save(data);
        }
        return data;
    }

    @Override
    public IPage<ResignationApplication> selectPage(ResignationPageDto dto) {
        Page<ResignationApplication> page = new Page<ResignationApplication>();
        page.setCurrent(dto.getCurrent());
        page.setSize(dto.getPageSize());
        QueryWrapper<ResignationApplication> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(ResignationApplication::getCreateTime);
        qw.lambda().eq(DataUtil.isNotEmpty(dto.getCompanyId()),ResignationApplication::getCompanyId,dto.getCompanyId())
                .eq(DataUtil.isNotEmpty(dto.getDepartId()),ResignationApplication::getDepartId,dto.getDepartId())
                .eq(DataUtil.isNotEmpty(dto.getUserId()),ResignationApplication::getUserId,dto.getUserId());
        //过滤ceo和后台企业关联
        qw.lambda().in(DataUtil.isNotEmpty(dto.getCompanyIds()),ResignationApplication::getCompanyId,dto.getCompanyIds());

        IPage<ResignationApplication> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public List<ResignationApplication> getLeaveUsers() {
        //当天已批准离职的
        QueryWrapper<ResignationApplication> qw = new QueryWrapper<>();
        qw.lambda().eq(ResignationApplication::getApprovalState,ApproveStatusEnum.SUCCESS);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateFormat = format.format(new Date());
        qw.lambda().between(ResignationApplication::getResignationTime,nowDateFormat+" 00:00:00",nowDateFormat+" 23:59:59");
        return this.list(qw);
    }
}
