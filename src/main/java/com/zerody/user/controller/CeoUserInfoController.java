package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.service.CeoUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author  DaBai
 * @date  2021/4/21 15:36
 */

@Slf4j
@RequestMapping("/ceo-user-info")
@RestController
public class CeoUserInfoController {


    @Autowired
    private CeoUserInfoService ceoUserInfoService;


    /**
     *    分页查询ceo用户
     */
    @RequestMapping("/select-page")
    public DataResult selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto){
        IPage<CeoUserInfo> ceoUserInfoIPage = ceoUserInfoService.selectCeoUserPage(ceoUserInfoPageDto);
        return R.success(ceoUserInfoIPage);
    }




    /**
     *    添加
     */
    @PostMapping("/add")
    public DataResult addCeoUser(@RequestBody CeoUserInfo ceoUserInfo){
         ceoUserInfoService.addCeoUser(ceoUserInfo);
        return R.success();
    }

    /**
     *    修改
     */
    @PutMapping("/update")
    public DataResult updateCeoUser(@RequestBody CeoUserInfo ceoUserInfo){
        ceoUserInfoService.updateCeoUser(ceoUserInfo);
        return R.success();
    }

    /**
     *    id删除
     */
    @DeleteMapping("/delete/{id}")
    public DataResult deleteCeoUserById(@PathVariable(value = "id") String id){
        ceoUserInfoService.deleteCeoUserById(id);
        return R.success();
    }
}
