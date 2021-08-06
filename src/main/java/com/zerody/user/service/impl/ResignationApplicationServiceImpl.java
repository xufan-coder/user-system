package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.dto.ResignationPageDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.mapper.ResignationApplicationMapper;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/8/5 15:10
 */

@Slf4j
@Service
public class ResignationApplicationServiceImpl extends ServiceImpl<ResignationApplicationMapper, ResignationApplication> implements ResignationApplicationService {
    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Override
    public void addOrUpdateResignationApplication(ResignationApplication data) {
        if(DataUtil.isNotEmpty(data.getId())){
            if(ApproveStatusEnum.SUCCESS.name().equals(data.getApprovalState())){
                sysStaffInfoService.updateStaffStatus(data.getUserId(), StatusEnum.stop.getValue());
            }
            this.updateById(data);
        }else {
            data.setCreateTime(new Date());
            this.save(data);
        }
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
        IPage<ResignationApplication> pageResult = this.page(page,qw);
        return pageResult;
    }
}
