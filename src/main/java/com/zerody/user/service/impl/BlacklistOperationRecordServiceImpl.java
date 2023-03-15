package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.export.util.ExcelHandlerUtils;
import com.zerody.user.domain.BlacklistOperationRecord;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.dto.BlackOperationRecordDto;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.mapper.BlacklistOperationRecordMapper;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.util.DateUtils;
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
import java.util.List;

/**
 * @Author : xufan
 * @create 2023/3/8 18:55
 */

@Slf4j
@Service
public class BlacklistOperationRecordServiceImpl extends ServiceImpl<BlacklistOperationRecordMapper, BlacklistOperationRecord> implements BlacklistOperationRecordService {


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
    public void addBlacklistOperationRecord(BlacklistOperationRecordAddDto param) {


        BlacklistOperationRecord blacklistOperationRecord = new BlacklistOperationRecord();

        MobileBlacklistOperationQueryVo blacklistByMobile = this.baseMapper.getBlacklistByMobile(param);
        if(ObjectUtils.isNotEmpty(blacklistByMobile)){
            blacklistOperationRecord.setBlackUserId(blacklistByMobile.getBlackUserId());
            blacklistOperationRecord.setBlackName(blacklistByMobile.getBlackName());
            blacklistOperationRecord.setMobile(blacklistByMobile.getMobile());
            blacklistOperationRecord.setIdentityCard(blacklistByMobile.getIdentityCard());
            blacklistOperationRecord.setBlackTime(blacklistByMobile.getBlackTime());
            blacklistOperationRecord.setBlackCompanyId(blacklistByMobile.getBlackCompanyId());
            blacklistOperationRecord.setBlackCompanyName(blacklistByMobile.getBlackCompanyName());
            blacklistOperationRecord.setBlackDeptId(blacklistByMobile.getBlackDeptId());
            blacklistOperationRecord.setBlackDeptName(blacklistByMobile.getBlackDeptName());
            blacklistOperationRecord.setBlackReason(blacklistByMobile.getBlackReason());
        }
        Integer type = param.getType();
        String remarks = param.getRemarks();

        //操作人id
        String createBy = UserUtils.getUserId();
        String createName = UserUtils.getUserName();

        CreateInfoVo createInfo = this.baseMapper.getCreateInfoByCreateId(createBy);

        if (ObjectUtils.isNotEmpty(createInfo)){
            blacklistOperationRecord.setType(type);
            blacklistOperationRecord.setRemarks(remarks);
            blacklistOperationRecord.setCreateTime(DateUtil.getDate());
            blacklistOperationRecord.setCreateBy(createBy);
            blacklistOperationRecord.setCreateName(createName);
            blacklistOperationRecord.setOperateCompanyId(createInfo.getOperateCompanyId());
            blacklistOperationRecord.setOperateCompanyName(createInfo.getOperateCompanyName());
            blacklistOperationRecord.setOperateDeptId(createInfo.getOperateDeptId());
            blacklistOperationRecord.setOperateDeptName(createInfo.getOperateDeptName());
        }

        this.save(blacklistOperationRecord);

    }
}
