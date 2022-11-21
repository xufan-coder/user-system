package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.export.util.ExcelHandlerUtils;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.service.CallControlRecordService;
import com.zerody.user.vo.CallControlRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 呼叫限制记录控制层
 * @author  DaBai
 * @date  2022/11/9 10:48
 */

@Slf4j
@RequestMapping("/call-control-record")
@RestController
public class CallControlRecordController {

    @Autowired
    private CallControlRecordService callControlRecordService;


    /**
     *   呼叫限制记录列表
     */
    @GetMapping("/page")
    public DataResult<IPage<CallControlRecord>> getPageList(CallControlRecordPageDto pageDto) {
        try {
            IPage<CallControlRecord> data = this.callControlRecordService.getPageList(pageDto);
            return R.success(data);
        } catch (Exception e) {
            log.error("查询呼叫限制次数列表出错:{}", e, e);
            return R.error("查询呼叫限制次数列表出错:"+e.getMessage());
        }
    }

    /**
     *  导出呼叫限制记录
     */
    @PostMapping("/export")
    public DataResult<Void> exportPageList(CallControlRecordPageDto pageDto, HttpServletResponse response) {
        try {
            List<CallControlRecordVo> list = this.callControlRecordService.getList(pageDto);
            ExcelHandlerUtils.exportExcel(list, "呼叫限制记录", CallControlRecordVo.class, "呼叫限制记录列表.xls", response);
            return R.success();
        } catch (Exception e) {
            log.error("导出呼叫限制记录出错:{}", e, e);
            return R.error("导出呼叫限制记录出错:"+e.getMessage());
        }
    }

    /**
     *   解除呼叫限制
     */
    @PutMapping("/relieve/{id}")
    public DataResult relieveCallControlRecord(@PathVariable String id){
        try {
            this.callControlRecordService.doRelieveCallControlRecord(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("解除呼叫限制错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("解除呼叫限制错误：{}", e, e);
            return R.error("解除呼叫限制错误" + e.getMessage());
        }
    }

    /**
     *   批量解除呼叫限制
     */
    @PostMapping("/relieve/batch")
    public DataResult relieveCallControlRecordList(@RequestBody List<String> ids){
        try {
            this.callControlRecordService.doRelieveCallControlRecordList(ids);
            return R.success();
        } catch (DefaultException e) {
            log.error("批量解除呼叫限制错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量解除呼叫限制错误：{}", e, e);
            return R.error("批量解除呼叫限制错误" + e.getMessage());
        }
    }


}
