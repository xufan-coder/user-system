package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.TimeOperate;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.statis.UserAgeStatisQueryDto;
import com.zerody.user.dto.statis.UserSexStatisQueryDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.UserStatisService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.statis.UserAgeStatisQueryVo;
import com.zerody.user.vo.statis.UserSexStatisQueryVo;
import com.zerody.user.vo.*;
import com.zerody.user.vo.statis.UserTrendQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisController
 * @DateTime 2023/4/29 10:55
 */

@Slf4j
@RestController
@RequestMapping("/user-statis")
public class UserStatisController {

    @Autowired
    private UserStatisService service;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CheckUtil checkUtil;


    /**
     *
     * 伙伴趋势统计
     * @author PengQiang
     * @description 伙伴趋势统计
     * @date 2023/4/29 11:39
     * @param param 请求参数
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.statis.UserTrendQueryVo>>
     */
    @GetMapping("/user-trends")
    public DataResult<List<UserTrendQueryVo>> getUserTrendS(UserStatisQueryDto param) {
        try {
            if (DataUtil.isEmpty(param.getTimePeriod())) {
                param.setTimePeriod(TimeOperate.DAY.name());
            }
            this.checkUtil.SetUserPositionInfo(param);
            List<UserTrendQueryVo> list = this.service.getUserTrendS(param);
            return R.success(list);
        } catch (DefaultException e) {
            log.warn("伙伴趋势统计校验:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.warn("伙伴趋势统计校验:{}", e, e);
            return R.error("获取趋势统计出错!请联系管理员");
        }
    }

    /**
     *
     *
     * @author PengQiang
     * @description 伙伴年龄分布统计
     * @date 2023/5/3 10:31
     * @param param 查询参数
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.statis.UserAgeStatisQueryVo>>
     */
    @GetMapping("/age")
    public DataResult<List<UserAgeStatisQueryVo>> getAgeStatis(UserAgeStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.service.getAgeStatis(param));
        } catch (DefaultException e) {
            log.warn("伙伴年龄分布统计校验:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("伙伴年龄分布统计出错:{}", e, e);
            return R.error("伙伴年龄分布统计出错！请联系管理员");
        }
    }


    /**
     *
     *
     * @author PengQiang
     * @description 伙伴性别分布统计
     * @date 2023/5/3 15:22
     * @param param 参数
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.statis.UserSexStatisQueryVo>>
     */
    @GetMapping("/sex")
    public DataResult<List<UserSexStatisQueryVo>> getSexStatis(UserSexStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.service.getSexStatis(param));
        } catch (DefaultException e) {
            log.warn("伙伴性别分布统计校验:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("伙伴性别分布统计出错:{}", e, e);
            return R.error("性别分布统计出错!请联系管理员");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 查询伙伴概况
     * @Date: 2023/4/28 17:17
     */
    @GetMapping("/get/user/overview")
    public DataResult<UserStatistics> getUserOverview(UserStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.sysStaffInfoService.getUserOverview(param));
        } catch (DefaultException e) {
            log.error("查询伙伴概况出错:{}", e.getMessage());
            return R.error("查询伙伴概况出错");
        } catch (Exception e) {
            log.error("查询伙伴概况出错:{}", e, e);
            return R.error("查询伙伴概况出错");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 统计伙伴签约与解约
     * @Date: 2023/4/28 19:55
     */
    @GetMapping("/statistics/partner")
    public DataResult<UserStatistics> statisticsContractAndRescind(UserStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.sysStaffInfoService.statisticsContractAndRescind(param));
        } catch (DefaultException e) {
            log.error("统计伙伴签约与解约出错:{}", e.getMessage());
            return R.error("统计伙伴签约与解约出错");
        } catch (Exception e) {
            log.error("统计伙伴签约与解约出错:{}", e, e);
            return R.error("统计伙伴签约与解约出错");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 统计伙伴签约详情
     * @Date: 2023/4/28 20:36
     */
    @GetMapping("/statistics/partner/details")
    public DataResult<List<StatisticsDataDetailsVo>> statisticsDetails(UserStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.service.statisticsDetails(param));
        } catch (DefaultException e) {
            log.error("统计伙伴签约详情出错:{}", e.getMessage());
            return R.error("统计伙伴签约详情出错");
        } catch (Exception e) {
            log.error("统计伙伴签约详情出错:{}", e, e);
            return R.error("统计伙伴签约详情出错");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取解约原因分析
     * @Date: 2023/4/29 10:59
     */
    @GetMapping("/termination/analysis")
    public DataResult<List<TerminationAnalysisVo>> getTerminationAnalysis(UserStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.sysStaffInfoService.getTerminationAnalysis(param));
        } catch (DefaultException e) {
            log.error("获取解约原因分析出错:{}", e.getMessage());
            return R.error("获取解约原因分析出错");
        } catch (Exception e) {
            log.error("获取解约原因分析出错:{}", e, e);
            return R.error("获取解约原因分析出错");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取学历分析
     * @Date: 2023/4/29 11:29
     */
    @GetMapping("/degree/analysis")
    public DataResult<DegreeAnalysisVo> getDegreeAnalysis(UserStatisQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            return R.success(this.sysStaffInfoService.getDegreeAnalysis(param));
        } catch (DefaultException e) {
            log.error("获取学历分析出错:{}", e.getMessage());
            return R.error("获取学历分析出错");
        } catch (Exception e) {
            log.error("获取学历分析出错:{}", e, e);
            return R.error("获取学历分析出错");
        }
    }

    
    /**
    * @Author: chenKeFeng
    * @param  
    * @Description: 签约数据汇总报表
    * @Date: 2023/5/3 15:42
    */
    @GetMapping("/get/sign/summary")
    public DataResult<List<SignSummaryVo>> getSignSummary(UserStatisQueryDto param) {
        try {
            return R.success(this.sysStaffInfoService.getSignSummary(param));
        } catch (DefaultException e) {
            log.error("获取签约数据汇总报表出错:{}", e.getMessage());
            return R.error("获取签约数据汇总报表出错");
        } catch (Exception e) {
            log.error("获取签约数据汇总报表出错:{}", e, e);
            return R.error("获取签约数据汇总报表出错");
        }
    }

}
