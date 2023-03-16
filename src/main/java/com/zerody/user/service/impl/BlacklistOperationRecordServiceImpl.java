package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.export.util.ExcelHandlerUtils;
import com.zerody.user.domain.*;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.dto.BlackOperationRecordDto;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.BlacklistOperationRecordMapper;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.util.DateUtils;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.vo.BlackOperationRecordVo;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;
import com.zerody.user.vo.CreateInfoVo;
import com.zerody.user.vo.MobileBlacklistOperationQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2023/3/8 18:55
 */

@Slf4j
@Service
public class BlacklistOperationRecordServiceImpl extends ServiceImpl<BlacklistOperationRecordMapper, BlacklistOperationRecord> implements BlacklistOperationRecordService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;


    @Override
    public IPage<BlacklistOperationRecordPageVo> getPageBlacklistOperationRecord(BlacklistOperationRecordPageDto param) {
        IPage<BlacklistOperationRecordPageVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageBlacklistOperationRecord(param,iPage);
        return iPage;
    }

    @Override
    public void doExportRecord(BlacklistOperationRecordPageDto param, HttpServletResponse response) {
        List<BlackOperationRecordVo> data = this.baseMapper.doExportRecord(param);
        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList<>();
        }
        String fileName = "内控伙伴操作记录";
        ExcelHandlerUtils.exportExcel(data, fileName, BlackOperationRecordVo.class,  fileName + ".xls", response);
    }

    @Override
    public void addBlacklistOperationRecord(BlacklistOperationRecordAddDto param,UserVo userVo) {
        BlacklistOperationRecord blacklistOperationRecord = new BlacklistOperationRecord();

        MobileBlacklistOperationQueryVo blacklistByMobile = this.baseMapper.getBlacklistByMobile(param);
        CreateInfoVo createInfo = this.baseMapper.getCreateInfoByCreateId(userVo.getStaffId());

        new Thread( () -> {
            if (ObjectUtils.isNotEmpty(blacklistByMobile) && blacklistByMobile.getIsBlack() == 1
                    && ObjectUtils.isNotEmpty(createInfo) && !createInfo.getMobile().equals("13800138000")) {
                BeanUtils.copyProperties(blacklistByMobile, blacklistOperationRecord);
                blacklistOperationRecord.setType(param.getType());
                blacklistOperationRecord.setRemarks(param.getRemarks());
                blacklistOperationRecord.setCreateTime(new Date());
                    blacklistOperationRecord.setCreateBy(createInfo.getOperateUserId());
                    blacklistOperationRecord.setCreateName(createInfo.getOperateUserName());
                    blacklistOperationRecord.setOperateCompanyId(createInfo.getOperateCompanyId());
                    blacklistOperationRecord.setOperateCompanyName(createInfo.getOperateCompanyName());
                    blacklistOperationRecord.setOperateDeptId(createInfo.getOperateDeptId());
                    blacklistOperationRecord.setOperateDeptName(createInfo.getOperateDeptName());
                this.save(blacklistOperationRecord);
            }
        }).start();

    }
}
