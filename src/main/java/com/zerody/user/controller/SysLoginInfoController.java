package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.api.service.UserLoginInfoRemoteService;
import com.zerody.user.service.SysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author  DaBai
 * @date  2020/12/31 9:48
 */

@RequestMapping("/sysLoginInfo")
@RestController
public class SysLoginInfoController implements UserLoginInfoRemoteService {


    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    /**
    *   修改登录表信息
    */
    @Override
    @RequestMapping(value = {"/update/inner"},method = {RequestMethod.PUT},produces = {"application/json"})
    public DataResult updateById(com.zerody.user.api.vo.SysLoginInfo sysLoginInfo) {
        sysLoginInfoService.updateLoginInfoByUserId(sysLoginInfo);
        return R.success();
    }
}
