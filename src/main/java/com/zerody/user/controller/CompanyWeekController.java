package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.dto.CompanyWorkTimeAddDto;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.service.CompanyWorkTimeService;
import com.zerody.user.vo.CompanyWorkTimeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author : chenKeFeng
 * @date : 2022/8/31 14:46
 */
@Slf4j
@RequestMapping("/work-time")
@RestController
public class CompanyWeekController {

    @Autowired
    private CompanyWorkTimeService companyWorkTimeService;


    /**
     * @Author: chenKeFeng
     * @Description: 设置企业上下班时间
     * @Date: 2022/8/31 15:56
     */
    @PostMapping("/add")
    public DataResult<Object> add(@RequestBody CompanyWorkTimeAddDto companyWorkTimeAddDto) {
        if (DataUtil.isEmpty(companyWorkTimeAddDto.getType())) {
            return R.error("类型不能为空");
        }
        try {
            if (companyWorkTimeAddDto.getType().equals(YesNo.YES)) {
                if (DataUtil.isEmpty(companyWorkTimeAddDto.getCompanyId())) {
                    return R.error("企业id不能为空");
                }
            } else {
                companyWorkTimeAddDto.setCompanyId(null);
                if (DataUtil.isEmpty(companyWorkTimeAddDto.getType())) {
                    return R.error("ceoId不能为空");
                }
            }
            return R.success(companyWorkTimeService.setCommuteTime(companyWorkTimeAddDto));
        } catch (Exception e) {
            log.error("设置企业上下班时间出错:{}", e.getMessage());
            return R.error("设置企业上下班时间出错" + e.getMessage());
        }
    }


    /**
     * @Author: chenKeFeng
     * @Description: 获取企业上下班时间详情 后台
     * @Date: 2022/8/31 15:00
     */
    @GetMapping("/get-commute-time-backstage")
    public DataResult<CompanyWorkTimeVo> getCompanyCommuteTimePc(CompanyWorkTimeDto companyWorkTimeDto) {
        if (DataUtil.isEmpty(companyWorkTimeDto.getType())) {
            return R.error("类型不能为空");
        }

        try {
            return R.success(companyWorkTimeService.getPageCompanyWorkTime(companyWorkTimeDto));
        } catch (Exception e) {
            log.error("获取企业上下班时间PC出错:{}", e.getMessage());
            return R.error(e.getMessage());
        }
    }


    /**
     * @Author: chenKeFeng
     * @Description: 获取企业上下班时间详情（客户端）
     * @Date: 2022/8/31 16:11
     */
    @GetMapping("/get-commute-time-app")
    public DataResult<CompanyWorkTimeVo> getCompanyCommuteTimeApp(CompanyWorkTimeDto companyWorkTimeDto) {
        UserVo user = UserUtils.getUser();
        log.info("用户信息： {}", user);
        log.info("是否为boss账号： {}", user.isCEO());
        if (user.isCEO()) {
            companyWorkTimeDto.setType(YesNo.NO);
        } else {
            if (DataUtil.isEmpty(companyWorkTimeDto.getCompanyId())) {
                return R.error("企业id不能为空");
            }
            companyWorkTimeDto.setType(YesNo.YES);
        }
        try {
            return R.success(companyWorkTimeService.getPageCompanyWorkTime(companyWorkTimeDto));
        } catch (Exception e) {
            log.error("获取企业上下班时间APP出错:{}", e.getMessage());
            return R.error(e.getMessage());
        }
    }

}
