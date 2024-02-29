package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.dto.UserOpinionAssistantRefDto;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.enums.MenuSetTypeEnum;
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
    * @Description:         添加或修改回复协助人
    * @Author:              xufan
    * @Date:                2024/2/29 11:44
    */
    @PostMapping(value = "/add")
    public DataResult<Object> addCeoAssistantRef(@RequestBody @Validated UserOpinionAssistantRefDto param) {
        try {
            UserVo userVo = UserUtils.getUser();
            if (Objects.nonNull(userVo)) {
                param.setCeoUserId(userVo.getUserId());
                param.setCeoUserName(userVo.getUserName());
            }
            this.service.addCeoAssistantRef(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加回复协助人异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加回复协助人出错:{}", e, e);
            return R.error("添加回复协助人出错" + e);
        }
    }

    /**
    * @Description:         获取ceo回复协助人
    * @Author:              xufan
    * @Date:                2024/2/29 11:52
    */
    @GetMapping("/get")
    public DataResult<List<String>> getAssistantUserIds(){
        try {
            List<String> result = this.service.getAssistantUserIds(UserUtils.getUserId());
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取ceo回复协助人出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取ceo回复协助人出错：{}", e, e);
            return R.error("获取ceo回复协助人出错" + e.getMessage());
        }
    }


}
