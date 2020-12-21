package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysAuthMenuPageDto;
import com.zerody.user.service.SysAuthMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    //查询当前用户(当前企业)所有菜单/按钮code
    @RequestMapping("/getMenuCodeList")
    @ApiOperation(value = "查询当前用户(当前企业)所有菜单/按钮code")
    public DataResult getMenuCodeList(String compId){
        return sysAuthMenuService.getMenuCodeList(compId);
    }


    //查询所有菜单信息(分页)
    @RequestMapping("/getMenuPage")
    @ApiOperation(value = "查询所有菜单信息(分页)")
    public DataResult getMenuPage(@RequestBody SysAuthMenuPageDto sysAuthMenuPageDto){

        return sysAuthMenuService.getMenuPage(sysAuthMenuPageDto);
    }

}
