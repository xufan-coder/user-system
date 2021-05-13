package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.service.UserLoginInfoRemoteService;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CardUserService;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysLoginInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author  DaBai
 * @date  2020/12/31 9:48
 */

@SuppressWarnings("ALL")
@RequestMapping("/sys-login-info")
@RestController
public class SysLoginInfoController implements UserLoginInfoRemoteService {


    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private CardUserService cardUserService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    /**
     *   修改总裁登录表信息
     */
    @Override
    @RequestMapping(value = {"/update-ceo/inner"},method = {RequestMethod.PUT},produces = {"application/json"})
    public DataResult updateById(com.zerody.user.api.vo.CeoUserInfoVo ceoUserInfoVo) {
        ceoUserInfoService.updateCeoById(ceoUserInfoVo);
        return R.success();
    }

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

    /**
     *   名片登录时UnionID查用户
     */
    @Override
    @RequestMapping(value = "/card-get/inner",method = GET, produces = "application/json")
    public DataResult<CardUserDto> getCardUserByOpenId(String openId){
        try {
            return R.success(cardUserService.getCardUserByOpenId(openId));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }


    /**
     *   修改平台管理员信息
     */
    @Override
    @RequestMapping(value = {"/admin-update/inner"},method = {RequestMethod.PUT},produces = {"application/json"})
    public DataResult updateAdminInfo(com.zerody.user.api.vo.AdminUserInfo adminUserInfo) {
        try {
            if (DataUtil.isNotEmpty(adminUserInfo)) {
                AdminUserInfo info = new AdminUserInfo();
                BeanUtils.copyProperties(adminUserInfo, info);
                info.setUpdateTime(new Date());
                adminUserService.updateById(info);
            }
            return R.success();
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }
}
