package com.zerody.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.domain.UserInductionSplitRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.dto.UserInductionSplitDto;
import com.zerody.user.dto.UserInductionVerificationDto;
import com.zerody.user.service.UserInductionRecordService;
import com.zerody.user.service.UserInductionSplitRecordService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author kuang
 * 入职申请记录
 */
@RestController
@RequestMapping("/induction/group")
@Slf4j
public class UserInductionSplitRecordController {

    @Autowired
    private UserInductionSplitRecordService inductionSplitRecordService;


    /**************************************************************************************************
     **
     *  原子服务添加或修改申请
     *
     * @param param
     * @return {@link UserInductionRecord }
     * @author DaBai
     * @date 2022/12/1  11:41
     */
    @PostMapping("/add")
    public DataResult<Object> addOrUpdate(@RequestBody UserInductionSplitDto param){
        try {
            log.info("跨企业二次入职申请:{}",param);
            this.inductionSplitRecordService.addOrUpdateRecord(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加申请记录错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加申请记录错误：{}", e, e);
            return R.error("添加申请记录错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 二次入职账号校验
     **/
    @PostMapping("/verification")
    public DataResult<Object> verification(@RequestBody UserInductionVerificationDto param){
        try {
            if(StringUtils.isEmpty(param.getCertificateCard())) {
                return R.error("身份证号不能为空");
            }
            if(StringUtils.isEmpty(param.getMobile())) {
                return R.error("手机号不能为空");
            }
            param.setCompanyId(UserUtils.getUser().getCompanyId());
            param.setUserId(UserUtils.getUser().getUserId());
            JSONObject data= this.inductionSplitRecordService.verification(param);
            return R.success(data);
        } catch (DefaultException e) {
            log.error("添加申请记录错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加申请记录错误：{}", e, e);
            return R.error("添加申请记录错误" + e.getMessage());
        }
    }
}
