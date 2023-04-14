package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.dto.PrepareExecutiveRecordDto;
import com.zerody.user.service.PrepareExecutiveRecordService;
import com.zerody.user.vo.PrepareExecutiveRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : xufan
 * @create 2023/4/3 15:21
 */

@Slf4j
@RestController
@RequestMapping("/prepare-executive-record")
public class PrepareExecutiveRecordController {

    @Autowired
    private PrepareExecutiveRecordService prepareExecutiveRecordService;

    /**
    * @Description:         添加预备高管记录
    * @Param:               [param]
    * @return:              com.zerody.common.api.bean.DataResult<java.lang.Object>
    * @Author:              xufan
    * @Date:                2023/4/3 15:34
    */
    @PostMapping("/add")
    public DataResult<Object> addPrepareExecutiveRecord(@RequestBody PrepareExecutiveRecordDto param){
        try {
            this.prepareExecutiveRecordService.addPrepareExecutiveRecord(param, UserUtils.getUser());
            return R.success();
        } catch (DefaultException e) {
            log.error("添加预备高管记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加预备高管记录错误：{}", e, e);
            return R.error("添加预备高管记录错误" + e.getMessage());
        }
    }

    /**
    * @Description:         根据用户id查询个人预备高管记录列表
    * @Param:               [userId]
    * @return:              com.zerody.common.api.bean.DataResult<list<PrepareExecutiveRecordVo>>
    * @Author:              xufan
    * @Date:                2023/4/4 10:27
    */
    @GetMapping("/getList/{id}")
    public DataResult<List<PrepareExecutiveRecordVo>> getPrepareExecutiveRecordListById(@PathVariable(name = "id") String userId){
        try {
            List<PrepareExecutiveRecordVo> page = prepareExecutiveRecordService.getPrepareExecutiveRecordList(userId);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("根据用户id查询预备高管记录列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据用户id查询预备高管记录列表错误：{}", e, e);
            return R.error("根据用户id查询预备高管记录列表错误" + e.getMessage());
        }
    }

    /**
    * @Description:         根据用户id查询预备高管记录
    * @Param:               [userId]
    * @return:              com.zerody.common.api.bean.DataResult<com.zerody.user.vo.PrepareExecutiveRecordVo>
    * @Author:              xufan
    * @Date:                2023/4/4 16:05
    */
    @GetMapping("/get/{id}")
    public DataResult<PrepareExecutiveRecordVo> getById(@PathVariable(name = "id") String userId){
        try {
            PrepareExecutiveRecordVo result = prepareExecutiveRecordService.getPrepareExecutiveRecord(userId);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据用户id查询预备高管记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据用户id查询预备高管记录错误：{}", e, e);
            return R.error("根据用户id查询预备高管记录错误" + e.getMessage());
        }
    }

}
