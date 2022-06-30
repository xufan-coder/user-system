package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.IUser;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.dto.SysUserIdentifierDto;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.service.SysUserIdentifierService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.SysUserIdentifierVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CheckUtil checkUtil;
   /**
    * @author kuang
    * @description 账号添加设备绑定
    * @date  2022年03月22日 18:00
    * @Param data 设备绑定参数
    **/
    @PostMapping("/add")
    public DataResult<Object> addSysUserIdentifier(@Validated @RequestBody SysUserIdentifier data) {
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
     * 申请解绑设备-发起流程
     * 设备解绑申请 1申请/0撤销
     * @author  DaBai
     * @date  2022/4/19 15:25
     */
    @PostMapping("/apply")
    public DataResult<Object> addApplyV2(@RequestBody SysUserIdentifierDto data){
        try {
            data.setUserId(UserUtils.getUserId());
            data.setUser(UserUtils.getUser());
            this.service.addApplyV2(data);
            return R.success();
        } catch (Exception e) {
            log.error("解绑设备申请出错:{}", JSON.toJSONString(data), e);
            return R.error("解绑设备申请出错:"+e.getMessage());
        }
    }


    /**
     * 4-19作废  替换为流程申请 -invalid
     * @author kuang
     * @description  设备解绑申请 1申请/0撤销
     **/
    @PostMapping("/apply-invalid")
    public DataResult<Object> addApply(@RequestBody SysUserIdentifierDto data){

        if(Objects.isNull(data.getState())){
            return R.error("审批状态不能为空");
        }
        if(data.getState() > YesNo.YES || data.getState() < YesNo.NO) {
            return R.error("审批状态错误");
        }
        try {
            this.service.addApply(data.getId(), data.getState(),UserUtils.getUserId());
            return R.success();
        } catch (Exception e) {
            log.error("账号设备申请出错:{}", JSON.toJSONString(data), e);
            return R.error("账号设备申请出错:"+e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description  设备解绑审批 1同意 / 0 拒绝
     **/
    @PostMapping("/approve")
    public DataResult<Object> addApprove(@RequestBody SysUserIdentifierDto data){

        if(Objects.isNull(data.getState())){
            return R.error("审批状态不能为空");
        }
        if(data.getState() > YesNo.YES || data.getState() < YesNo.NO) {
            return R.error("审批状态错误");
        }
        try {
            this.service.addApprove(data.getId(), data.getState(),UserUtils.getUser());
            return R.success();
        } catch (Exception e) {
            log.error("解绑审批出错:{}", JSON.toJSONString(data), e);
            return R.error("解绑审批出错:"+e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description  解除绑定
     **/
    @GetMapping("/remove/{id}")
    public DataResult<Object> unbound(@PathVariable String id){

        try {
            this.service.addUnbound(id,UserUtils.getUserId());
            return R.success();
        } catch (Exception e) {
            return R.error("解除绑定出错:"+e.getMessage());
        }
    }


    /**
     * @author kuang
     * @description 设备申请解绑列表
     **/
    @GetMapping("/page/user-identifier")
    public DataResult<Page<SysUserIdentifier>> getPageUserIdentifier(SysUserIdentifierQueryDto queryDto){
        log.info("查询设备审批列表入参:{}", JSON.toJSONString(queryDto));
        try {
            if(UserUtils.getUser().isBack()){
                queryDto.setCompanyIds(this.checkUtil.setBackCompany(UserUtils.getUserId()));
            }
            Page<SysUserIdentifier> identifierList = this.service.getPageUserIdentifier(queryDto);
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
    @GetMapping("/get/identifier-info")
    public DataResult<SysUserIdentifier> getIdentifierInfo(){
        try {
            String userId = UserUtils.getUserId();
            SysUserIdentifier identifier = this.service.getIdentifierInfo(userId);
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
    @GetMapping("/get/user-identifier-info/{id}")
    public DataResult<SysUserIdentifierVo> getUserIdentifierInfo(@PathVariable String id){
        try {
            SysUserIdentifierVo identifierVo = this.service.getUserIdentifierInfo(id);
            return R.success(identifierVo);
        } catch (Exception e) {
            return R.error("设备绑定详情信息出错:"+e.getMessage());
        }
    }

}
