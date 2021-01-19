package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.service.UserLoginInfoRemoteService;
import com.zerody.user.service.CardUserService;
import com.zerody.user.service.SysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author  DaBai
 * @date  2020/12/31 9:48
 */

@RequestMapping("/sys-login-info")
@RestController
public class SysLoginInfoController implements UserLoginInfoRemoteService {


    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private CardUserService cardUserService;

    /**
    *   修改登录表信息
    */
    @Override
    @RequestMapping(value = {"/update/inner"},method = {RequestMethod.PUT},produces = {"application/json"})
    public DataResult updateById(com.zerody.user.api.vo.SysLoginInfo sysLoginInfo) {
        sysLoginInfoService.updateLoginInfoByUserId(sysLoginInfo);
        return R.success();
    }

    /**
     *   新增名片用户访客登陆账户
     */
    @Override
    @RequestMapping(value = "/card-add/inner",method = POST, produces = "application/json")
    public DataResult<CardUserDto> addCardUser(@RequestBody CardUserDto cardUser){
        return R.success(cardUserService.addCardUser(cardUser));
    }

    /**
    *   CRM跳转登录名片时，查询关联
    */
    @Override
    @RequestMapping(value = "/card-check/inner",method = GET, produces = "application/json")
    public DataResult<CardUserDto> checkCardUser(@RequestParam(value = "userId") String userId){
        try {
            return R.success(cardUserService.checkCardUser(userId));
        }catch (Exception e){
            return R.error(e.getMessage());
        }

    }
}
