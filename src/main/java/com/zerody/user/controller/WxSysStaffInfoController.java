package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PengQiang
 * @ClassName WxSysStaffInfoController
 * @DateTime 2020/12/24_9:11
 * @Deacription 微信小程序员工管理
 */
@RestController
@RequestMapping("/wx/staffInfo")
public class WxSysStaffInfoController {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;


    @GetMapping("/getStaffInfoByOpenId")
    public DataResult getStaffInfoByOpenId(String openId){
        return sysStaffInfoService.getStaffInfoByOpenId(openId);
    }

}
