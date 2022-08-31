package com.zerody.user.controller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CompanyWorkTime;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.service.CompanyWorkTimeService;
import com.zerody.user.vo.CompanyWorkTimeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公司上下班时间表
 *
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Slf4j
@RestController
@RequestMapping("/work-time")
public class CompanyWorkTimeController {

    @Autowired
    private CompanyWorkTimeService companyWorkTimeService;

    /**
    * @Author: chenKeFeng
    * @Description: 分页查询企业上下班时间
    * @Date: 2022/8/31 9:05
    */
    @GetMapping("/page")
    public DataResult<IPage<CompanyWorkTimeVo>> page(CompanyWorkTimeDto companyWorkTimeDto) {
        try {
            IPage<CompanyWorkTimeVo> companyWorkTimeList = companyWorkTimeService.getPageCompanyWorkTime(companyWorkTimeDto);
            return R.success(companyWorkTimeList);
        } catch (Exception e) {
            log.error("分页查询上下班时间列表出错:{}", e.getMessage());
            return R.error("分页查询上下班时间列表出错" + e.getMessage());
        }
    }


    /**
     * @Author: chenKeFeng
     * @Description: 获取公司上下班时间
     * @Date: 2022/8/29 14:49
     */
    @GetMapping("/get-company-work-time")
    public DataResult<List<CompanyWorkTimeVo>> getCompanyWorkTimeList(CompanyWorkTimeDto companyWorkTimeDto) {
        try {
            return R.success(companyWorkTimeService.getCompanyWorkTimeList(companyWorkTimeDto));
        } catch (Exception e) {
            log.error("获取公司上下班时间出错:{}", e.getMessage());
            return R.error("获取公司上下班时间出错" + e.getMessage());
        }
    }


    /**
     * 查询公司上下班时间详情
     */
    @GetMapping("/info/{id}")
    public DataResult<CompanyWorkTime> info(@PathVariable("id") String id) {
        try {
            CompanyWorkTimeDto companyWorkTimeDto = new CompanyWorkTimeDto();
            companyWorkTimeDto.setCompanyId(id);
            CompanyWorkTime companyWorkTime = companyWorkTimeService.getCompanyWorkTimeById(companyWorkTimeDto);
            return R.success(companyWorkTime);
        } catch (Exception e) {
            log.error("查询公司上下班时间详情出错:{}", e.getMessage());
            return R.error(e.getMessage() + e.getMessage());
        }
    }

    /**
     * 新增上下班时间
     */
    @PostMapping("/add")
    public DataResult<Object> add(@RequestBody CompanyWorkTimeDto companyWorkTimeDto) {
        try {
            companyWorkTimeService.addCompanyWorkTime(companyWorkTimeDto);
            return R.success();
        } catch (Exception e) {
            log.error("新增企业上下班时间出错:{}", e.getMessage());
            return R.error("新增上下班时间出错", e.getMessage());
        }
    }

    /**
     * 修改上下班时间
     */
    @PutMapping("/edit")
    public DataResult<Object> edit(@RequestBody CompanyWorkTimeDto companyWorkTimeDto) {
        try {
            companyWorkTimeService.editCompanyWorkTime(companyWorkTimeDto);
            return R.success();
        } catch (Exception e) {
            log.error("修改上下班时间出错:{}", e.getMessage());
            return R.error("修改上下班时间出错", e.getMessage());
        }
    }


    /**
     * @Author: chenKeFeng
     * @Description: 设置企业上下班时间
     * @Date: 2022/8/31 8:41
     */
    @PutMapping("/set-time")
    public DataResult<Object> setCommuteTime(@RequestBody CompanyWorkTimeDto companyWorkTimeDto) {
        if (DataUtil.isEmpty(companyWorkTimeDto.getCompanyId())) {
            return R.error("企业id不能为空");
        }
        try {
            return R.success(companyWorkTimeService.setCommuteTime(companyWorkTimeDto));
        } catch (Exception e) {
            log.error("设置企业上下班时间出错:{}", e.getMessage());
            return R.error("设置企业上下班时间出错" + e.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public DataResult<Object> delete(@RequestBody String[] ids) {
        try {
            companyWorkTimeService.removeByIds(Arrays.asList(ids));
            return R.success();
        } catch (Exception e) {
            log.error("删除描述出错:{}", e.getMessage());
            return R.error("删除描述出错", e.getMessage());
        }
    }

}
