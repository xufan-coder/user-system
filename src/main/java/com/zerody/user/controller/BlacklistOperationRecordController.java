package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.dto.BlackOperationRecordDto;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.DateUtils;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/3/8 14:58
 */

@Slf4j
@RestController
@RequestMapping("/black-operation-record")
public class BlacklistOperationRecordController {


    @Autowired
    private BlacklistOperationRecordService service;

    @Autowired
    private CheckUtil checkUtil;


    /**
    * @Description:         分页查询内控名单操作记录
    * @Param:               [param]
    * @return:              com.zerody.commonai.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BlacklistOperationRecordPageVo>>
    * @Author:              xufan
    * @Date:                2023/3/10 8:44
    */
    @GetMapping("/all/page")
    public DataResult<IPage<BlacklistOperationRecordPageVo>> getBlacklistOperationRecord(BlacklistOperationRecordPageDto param){
        try {
            // 设置组织架构条件值
            param.setCompanyIds(this.checkUtil.setBackCompany(UserUtils.getUserId()));
            if (DataUtil.isNotEmpty(param.getEndTime())) {
                param.setEndTime(new Date(param.getEndTime().getTime() + DateUtils.DAYS));
            }
            IPage<BlacklistOperationRecordPageVo> result = service.getPageBlacklistOperationRecord(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("查询内控名单操作记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询内控名单操作记录错误：{}", e, e);
            return R.error("查询内控名单操作记录错误" + e.getMessage());
        }
    }


    /**
     * @Author               luolujin
     * @Description         导出内控名单操作记录
     */
    @PostMapping("/get/expert/user")
    public void doExportRecord(@RequestBody BlacklistOperationRecordPageDto param, HttpServletResponse response) {
        try {
            // 设置组织架构条件值
            param.setCompanyIds(this.checkUtil.setBackCompany(UserUtils.getUserId()));
            if (DataUtil.isNotEmpty(param.getEndTime())) {
                param.setEndTime(new Date(param.getEndTime().getTime() + DateUtils.DAYS));
            }
            this.service.doExportRecord(param, response);
        } catch (Exception e) {
            log.error("导出内控名单操作记录：{}", e, e);
        }
    }
    /**
    * @Description:         添加内控名单操作记录
    * @Param:               [blacklistOperationRecordAddDto]
    * @return:              com.zerody.common.api.bean.DataResult<java.lang.Object>
    * @Author:              xufan
    * @Date:                2023/3/10 18:40
    */
    @PostMapping("/add")
    public DataResult<Object> addBlacklistOperationRecord(@RequestBody BlacklistOperationRecordAddDto param){
        try {
            this.service.addBlacklistOperationRecord(param, UserUtils.getUser());
            return R.success();
        } catch (DefaultException e) {
            log.error("添加内控名单操作记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加内控名单操作记录错误：{}", e, e);
            return R.error("添加内控名单操作记录错误" + e.getMessage());
        }
    }






}
