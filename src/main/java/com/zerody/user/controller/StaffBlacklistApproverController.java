package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.StaffBlacklistApproverPageDto;
import com.zerody.user.vo.StaffBlacklistApproverVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
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
    private StaffBlacklistApproverService approverService;

    @GetMapping("/page")
    public DataResult<IPage<StaffBlacklistApproverVo>> getBlacklistApproverPage(StaffBlacklistApproverPageDto param){
        try {
            IPage<StaffBlacklistApproverVo> page = approverService.getBlacklistApproverPage(param);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("分页查询内控名单申请记录错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("分页查询内控名单申请记录错误：{}", e, e);
            return R.error("分页查询内控名单申请记录错误" + e.getMessage());
        }
    }

}
