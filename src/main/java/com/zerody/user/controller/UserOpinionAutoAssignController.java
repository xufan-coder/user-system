package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserOpinionAutoAssign;
import com.zerody.user.dto.UserOpinionAssistantRefDto;
import com.zerody.user.dto.UserOpinionAutoAssignDto;
import com.zerody.user.service.UserOpinionAutoAssignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author : xufan
 * @create 2024/2/29 15:59
 */
@Slf4j
@RestController
@RequestMapping("/user-opinion-auto-assign")
public class UserOpinionAutoAssignController {

    @Autowired
    private UserOpinionAutoAssignService service;


    /**
    * @Description:         新增或修改用户自动分配状态
    * @Param:               [param]
    * @return:              com.zerody.common.api.bean.DataResult<java.lang.Object>
    * @Author:              xufan
    * @Date:                2024/3/1 10:50
    */
    @PostMapping(value = "/add")
    public DataResult<Object> addUserAutoAssign(@RequestBody UserOpinionAutoAssignDto param) {
        try {
            if (DataUtil.isEmpty(param.getAutoAssign())){
                throw new DefaultException("是否开启自动分配状态不能为空");
            }
            param.setUserId(UserUtils.getUserId());
            this.service.addUserAutoAssign(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改用户自动分配状态异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改用户自动分配状态出错:{}", e, e);
            return R.error("修改用户自动分配状态出错" + e);
        }
    }

    /**
    * @Description:         // 获取用户自动分配状态
    * @Author:              xufan
    * @Date:                2024/3/1 11:52
    */
    @GetMapping("/get")
    public DataResult<Boolean> getAutoAssignState(){
        try {
            Boolean result = this.service.isAutoAssign(UserUtils.getUserId());
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取用户自动分配状态出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取用户自动分配状态出错：{}", e, e);
            return R.error("获取用户自动分配状态出错" + e.getMessage());
        }
    }


}
