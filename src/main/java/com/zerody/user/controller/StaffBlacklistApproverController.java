package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.zerody.user.service.StaffBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : xufan
 * @create 2023/9/21 10:38
 */
@Slf4j
@RestController
@RequestMapping("/staff-blacklist-approver")
public class StaffBlacklistApproverController {

    @Autowired
    private StaffBlacklistApproverService service;

    /**************************************************************************************************
     **
     * 原子服务调用
     *  添加伙伴内控申请记录
     *
     * @param param
     * @return {@link StaffBlacklistApprover }
     * @author DaBai
     * @date 2023/9/21  10:48
     */
    @PostMapping("/add")
    public DataResult<StaffBlacklistApprover> addStaffBlaklistRecord(@RequestBody StaffBlacklistApprover param){
        try {
            StaffBlacklistApprover result = this.service.addStaffBlaklistRecord(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("添加内控申请记录错误：{}", e.getMessage(), e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加内控申请记录错误：{}", e, e);
            return R.error("添加内控申请记录错误" + e.getMessage());
        }
    }


}
