package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.dto.UserOpinionAssistantRefDto;
import com.zerody.user.service.UserOpinionAssistantRefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author : xufan
 * @create 2024/2/29 9:42
 */
@Slf4j
@RestController
@RequestMapping("/user-opinion-assistant-ref")
public class UserOpinionAssistantRefController {

    @Autowired
    private UserOpinionAssistantRefService service;

    /**
    * @Description:         添加手动回复协助人
    * @Author:              xufan
    * @Date:                2024/2/29 11:44
    */
    @PostMapping(value = "/add/manual")
    public DataResult<Object> addManualAssistantRef(@RequestBody @Validated UserOpinionAssistantRefDto param) {
        try {
            param.setUserId(UserUtils.getUserId());
            param.setUserName(UserUtils.getUser().getUserName());
            param.setIsCeo(UserUtils.getUser().isCEO());
            this.service.addManualAssistantRef(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加手动回复协助人异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加手动回复协助人出错:{}", e, e);
            return R.error("添加手动回复协助人出错" + e);
        }
    }


    /**
    * @Description:         添加自动回复协助人
    * @Author:              xufan
    * @Date:                2024/3/7 10:05
    */
    @PostMapping(value = "/add/automatic")
    public DataResult<Object> addAutoAssistantRef(@RequestBody @Validated UserOpinionAssistantRefDto param) {
        try {
            param.setUserId(UserUtils.getUserId());
            param.setUserName(UserUtils.getUser().getUserName());
            param.setIsCeo(UserUtils.getUser().isCEO());
            this.service.addAutoAssistantRef(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加自动回复协助人异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加自动回复协助人出错:{}", e, e);
            return R.error("添加自动回复协助人出错" + e);
        }
    }

    /**
    * @Description:         获取意见回复协助人信息
    * @Author:              xufan
    * @Date:                2024/2/29 11:52
    */
    @GetMapping("/get")
    public DataResult<List<StaffInfoVo>> getAssistantUserInfo(){
        try {
            List<StaffInfoVo> result = this.service.getAssistantUserInfo(UserUtils.getUserId());
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取意见回复协助人出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取意见回复协助人出错：{}", e, e);
            return R.error("获取意见回复协助人出错" + e.getMessage());
        }
    }


    /**
    * @Description:         根据协助人userId删除信件回复协助人
    * @Author:              xufan
    * @Date:                2024/3/7 9:36
    */
    @DeleteMapping("/del/{id}")
    public DataResult<Void> removeAssistantById(@PathVariable("id") String assistantUserId){
        try {
            String userId = UserUtils.getUserId();
            this.service.removeByUserId(userId,assistantUserId);
            return R.success();
        } catch (Exception e) {
            log.error("删除信件回复协助人出错:{}", assistantUserId, e);
            return R.error("删除信件回复协助人出错:"+e.getMessage());
        }
    }

}
