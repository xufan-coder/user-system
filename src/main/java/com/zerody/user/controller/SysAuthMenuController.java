package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.service.SysAuthMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuController
 * @DateTime 2020/12/16_11:46
 * @Deacription TODO
 */
@RestController
@RequestMapping("/sysAuthMenu")
public class SysAuthMenuController {

    @Autowired
    private SysAuthMenuService sysAuthMenuService;

    @RequestMapping("/getMenuCodeList")
    @ApiOperation(value = "查询当前用户(当前企业)所有菜单/按钮code")
    public DataResult getMenuCodeList(String compId){
        return sysAuthMenuService.getMenuCodeList(compId);
    }
}
