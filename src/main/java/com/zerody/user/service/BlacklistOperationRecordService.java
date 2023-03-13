package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.BlacklistOperationRecord;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.dto.BlackOperationRecordDto;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : xufan
 * @create 2023/3/8 18:41
 */


public interface BlacklistOperationRecordService extends IService<BlacklistOperationRecord> {

    /**
    * @Description:         分页查询内控名单操作记录
    * @Param:               [param]
    * @return:              com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BlacklistOperationRecordPageVo>
    * @Author:              xufan
    * @Date:                2023/3/10 8:52
    */
    IPage<BlacklistOperationRecordPageVo> getPageBlacklistOperationRecord(BlacklistOperationRecordPageDto param);

    /**
    * @Description:         添加内控名单操作记录
    * @Param:               [param]
    * @return:              void
    * @Author:              xufan
    * @Date:                2023/3/11 9:37
    */
    void addBlacklistOperationRecord(BlacklistOperationRecordAddDto param);

    void doExportRecord(BlacklistOperationRecordPageDto param, HttpServletResponse response);
}
