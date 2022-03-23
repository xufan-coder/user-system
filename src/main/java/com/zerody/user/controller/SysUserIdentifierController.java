package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.service.SysUserIdentifierService;
import com.zerody.user.vo.SysUserIdentifierVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author kuang
 * @date 2022年03月22日 15:34
 * 登录设备绑定
 */
@Slf4j
@RestController
@RequestMapping("/identifier")
public class SysUserIdentifierController {

    @Autowired
    private SysUserIdentifierService service;

   /**
    * @author kuang
    * @description 账号添加设备绑定
    * @date  2022年03月22日 18:00
    * @Param data 设备绑定参数
    **/
    @PostMapping("/add")
    public DataResult<Object> addSysUserIdentifier(@RequestBody SysUserIdentifier data) {
        try {
            String userId = UserUtils.getUserId();
            if (userId != null) {
                data.setUserId(userId);
            }
            data.setCompanyId(UserUtils.getUser().getCompanyId());
            this.service.addSysUserIdentifier(data);
            return R.success();
        } catch (Exception e) {
            log.error("账号设备绑定出错:{}", JSON.toJSONString(data), e);
            return R.error("账号设备绑定出错:"+e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description  设备解绑申请/撤销
     * @param id    设备绑定id
     * @param approveState    审批状态
     **/
    @PostMapping("/approve/{id}")
    public DataResult<Object> addApprove(@PathVariable String id, @RequestParam String approveState){

        if(StringUtils.isEmpty(approveState)){
            throw new DefaultException("审批状态不能为空");
        }

        try {
            this.service.addApprove(id, approveState,UserUtils.getUserId());
            return R.success();
        } catch (Exception e) {
            log.error("账号设备申请出错:{}", JSON.toJSONString(approveState), e);
            return R.error("账号设备申请出错:"+e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description  解除绑定
     **/
    @PostMapping("/remove")
    public DataResult<Object> addApprove(){

        try {
            this.service.addApprove(UserUtils.getUserId());
            return R.success();
        } catch (Exception e) {
            return R.error("账号设备申请出错:"+e.getMessage());
        }
    }


    /**
     * @author kuang
     * @description 设备申请解绑列表
     **/
    @GetMapping("/page/user-identifier")
    public DataResult<List<SysUserIdentifier>> getPageUserIdentifier(@RequestBody SysUserIdentifierQueryDto queryDto){
        try {
            List<SysUserIdentifier> identifierList = this.service.getPageUserIdentifier(queryDto);
            return R.success(identifierList);
        } catch (Exception e) {
            log.error("查询设备审批列表出错:{}", JSON.toJSONString(queryDto), e);
            return R.error("查询设备审批列表出错:"+e.getMessage());
        }
    }


    /**
     * @author kuang
     * @description 设备绑定详情信息
     **/
    @GetMapping("/get/identifier-info/{id}")
    public DataResult<SysUserIdentifier> getUserIdentifierInfo(@PathVariable String id){
        try {
            String userId = UserUtils.getUserId();
            SysUserIdentifier identifier = this.service.getIdentifierInfo(userId,id);
            if(Objects.isNull(identifier)) {
                identifier = new SysUserIdentifier();
            }
            return R.success(identifier);
        } catch (Exception e) {
            return R.error("设备绑定详情信息出错:"+e.getMessage());
        }
    }


    /**
     * @author kuang
     * @description 查询伙伴绑定的设备详情
     **/
    @GetMapping("/get/user-identifier-info")
    public DataResult<SysUserIdentifierVo> getUserIdentifierInfo(){
        try {
            String userId = UserUtils.getUserId();
            SysUserIdentifierVo identifierVo = this.service.getUserIdentifierInfo(userId);
            return R.success(identifierVo);
        } catch (Exception e) {
            return R.error("设备绑定详情信息出错:"+e.getMessage());
        }
    }

}
