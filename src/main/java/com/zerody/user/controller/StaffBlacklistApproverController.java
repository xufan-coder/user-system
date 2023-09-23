package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.StaffBlacklistApproverPageDto;
import com.zerody.user.vo.StaffBlacklistApproverDetailVo;
import com.zerody.user.vo.StaffBlacklistApproverVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
    * @Description:         分页查询内控名单申请记录
    * @Param:               [param]
    * @return:              com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.StaffBlacklistApproverVo>>
    * @Author:              xufan
    * @Date:                2023/9/21 14:56
    */
    @GetMapping("/page")
    public DataResult<IPage<StaffBlacklistApproverVo>> getBlacklistApproverPage(StaffBlacklistApproverPageDto param){
        try {
            IPage<StaffBlacklistApproverVo> page = service.getBlacklistApproverPage(param);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("分页查询内控名单申请记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("分页查询内控名单申请记录错误：{}", e, e);
            return R.error("分页查询内控名单申请记录错误" + e.getMessage());
        }
    }


    /**
    * @Description:         根据id查询内控名单申请记录详情
    * @Param:               [id]
    * @return:              com.zerody.common.api.bean.DataResult<com.zerody.user.vo.StaffBlacklistApproverDetailVo>
    * @Author:              xufan
    * @Date:                2023/9/21 15:21
    */
    @GetMapping("/by/{id}")
    public DataResult<StaffBlacklistApproverDetailVo> getDetailById(@PathVariable("id") String id){
        try {
            StaffBlacklistApproverDetailVo result = this.service.getDetailById(id);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据id查询内控名单申请记录详情错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据id查询内控名单申请记录详情错误：{}", e, e);
            return R.error("根据id查询内控名单申请记录详情错误" + e.getMessage());
        }
    }


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
