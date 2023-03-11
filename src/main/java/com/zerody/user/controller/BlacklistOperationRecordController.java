package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    * @Description:         添加内控名单操作记录
    * @Param:               [blacklistOperationRecordAddDto]
    * @return:              com.zerody.common.api.bean.DataResult<java.lang.Object>
    * @Author:              xufan
    * @Date:                2023/3/10 18:40
    */
    @PostMapping("/add")
    public DataResult<Object> addBlacklistOperationRecord(@RequestBody BlacklistOperationRecordAddDto param){
        try {
            this.service.addBlacklistOperationRecord(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加内控名单操作记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加内控名单操作记录错误：{}", e, e);
            return R.error("查询内控名单操作记录错误" + e.getMessage());
        }
    }
}
