package com.zerody.user.service.base;

import com.alibaba.druid.util.StringUtils;
import com.zerody.common.constant.TimeDimensionality;
import com.zerody.common.enums.util.TimeFormat;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.dto.bean.SetTimePeriod;
import com.zerody.user.dto.bean.SetTimePeriodPage;
import com.zerody.user.dto.bean.UserPositionPageParam;
import com.zerody.user.dto.bean.UserPositionParam;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName CheckUtil
 * @DateTime 2021/3/14_23:16
 * @Deacription TODO
 */
@Slf4j
@Component
public class CheckUtil {

    @Autowired
    private SysStaffInfoService staffService;

    @Autowired
    private OauthFeignService oauthFeignService;

    @Autowired
    private SysDepartmentInfoService departmentInfoService;

    public <T extends UserPositionPageParam>  void SetUserPositionInfo(T  param){
        if (UserUtils.getUser().isBackAdmin()){
            return;
        }

        UserVo user = UserUtils.getUser();
        if (!StringUtils.isEmpty(param.getDepartId())) {
            param.setDepartIds(departmentInfoService.getSubordinateIdsById(param.getDepartId()));
            if (CollectionUtils.isEmpty(param.getDepartIds())) {
                param.setDepartIds(new ArrayList<>());
            }
            param.getDepartIds().add(param.getDepartId());
        }
        param.setCompanyId(user.getCompanyId());
        if (StringUtils.isEmpty(param.getUserId()) && StringUtils.isEmpty(param.getDepartId())) {
            AdminVo admin = this.staffService.getIsAdmin(user);
            if(admin.getIsCompanyAdmin()){

            } else if (admin.getIsDepartAdmin()) {
                param.setDepartId(user.getDeptId());
                param.setDepartIds(departmentInfoService.getSubordinateIdsById(param.getDepartId()));
                if (CollectionUtils.isEmpty(param.getDepartIds())) {
                    param.setDepartIds(new ArrayList<>());
                }
                param.getDepartIds().add(param.getDepartId());
            } else {
                param.setUserId(user.getUserId());
            }
        }
    }

    public <T extends UserPositionParam>  void SetUserPositionInfo(T  param){
        if (UserUtils.getUser().isBackAdmin()){
            return;
        }
        UserVo user = UserUtils.getUser();
        if (!StringUtils.isEmpty(param.getDepartId())) {
            param.setDepartIds(departmentInfoService.getSubordinateIdsById(param.getDepartId()));
            if (CollectionUtils.isEmpty(param.getDepartIds())) {
                param.setDepartIds(new ArrayList<>());
            }
            param.getDepartIds().add(param.getDepartId());
        }
        param.setCompanyId(user.getCompanyId());
        if (StringUtils.isEmpty(param.getUserId()) && StringUtils.isEmpty(param.getDepartId())) {
            AdminVo admin = this.staffService.getIsAdmin(user);
            if(admin.getIsCompanyAdmin()){

            } else if (admin.getIsDepartAdmin()) {
                param.setDepartId(user.getDeptId());
                param.setDepartIds(departmentInfoService.getSubordinateIdsById(param.getDepartId()));
                if (CollectionUtils.isEmpty(param.getDepartIds())) {
                    param.setDepartIds(new ArrayList<>());
                }
                param.getDepartIds().add(param.getDepartId());
            } else {
                param.setUserId(user.getUserId());
            }
        }
    }


    /**
     *
     *  设置日期筛选条件
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/23 15:47
     * @param                [param]
     * @return               void
     */
    public <T extends SetTimePeriod> void  setFiltrateTime(T param) throws ParseException {
        // TODO: 2021/4/27 没有日期类型 就不设置条件
        if (StringUtils.isEmpty(param.getTimePeriod())) {
            return;
        }
        param.setMonth(null);
        // TODO: 2021/4/23 自定义条件设置
        if (TimeDimensionality.DEFINITION.equals(param.getTimePeriod())) {
            if (!com.alibaba.druid.util.StringUtils.isEmpty(param.getBeginTime())) {
                param.setBegin(DateUtil.getDate(param.getBeginTime(), TimeFormat.YYYY_MM_DD));
            }
            if (!com.alibaba.druid.util.StringUtils.isEmpty(param.getEndTime())) {
                param.setEnd(new Date(DateUtil.getDate(param.getEndTime(), TimeFormat.YYYY_MM_DD).getTime() + DateUtils.DAYS));
            }
        } else {
            if (!TimeDimensionality.FORMER_YEAR.equals(param.getTimePeriod())) {
                param.setBegin(DateUtils.obtainTodayOrWeekOrMonthDate(param.getTimePeriod()));
            }
            // TODO: 2021/4/23 如果筛选条件为昨天 设置日期结束条件
            if (TimeDimensionality.YESTER.equals(param.getTimePeriod())) {
                param.setEnd(DateUtils.obtainTodayOrWeekOrMonthDate(TimeDimensionality.DAY));
            }
            if (TimeDimensionality.FORMER_YEAR.equals(param.getTimePeriod())) {
                param.setBegin(DateUtils.obtainTodayOrWeekOrMonthDate(TimeDimensionality.FORMER_YEAR));
            }
        }
    }


    /**
     *
     *  设置日期筛选条件
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/23 15:47
     * @param                [param]
     * @return               void
     */
    public <T extends SetTimePeriodPage> void  setFiltrateTime(T param) throws ParseException {
        // TODO: 2021/4/27 没有日期类型 就不设置条件
        if (StringUtils.isEmpty(param.getTimePeriod())) {
            return;
        }
        param.setMonth(null);
        // TODO: 2021/4/23 自定义条件设置
        if (TimeDimensionality.DEFINITION.equals(param.getTimePeriod())) {
            if (!com.alibaba.druid.util.StringUtils.isEmpty(param.getBeginTime())) {
                param.setBegin(DateUtil.getDate(param.getBeginTime(), TimeFormat.YYYY_MM_DD));
            }
            if (!com.alibaba.druid.util.StringUtils.isEmpty(param.getEndTime())) {
                param.setEnd(new Date(DateUtil.getDate(param.getEndTime(), TimeFormat.YYYY_MM_DD).getTime() + DateUtils.DAYS));
            }
        } else {
            if (!TimeDimensionality.FORMER_YEAR.equals(param.getTimePeriod())) {
                param.setBegin(DateUtils.obtainTodayOrWeekOrMonthDate(param.getTimePeriod()));
            }
            // TODO: 2021/4/23 如果筛选条件为昨天 设置日期结束条件
            if (TimeDimensionality.YESTER.equals(param.getTimePeriod())) {
                param.setEnd(DateUtils.obtainTodayOrWeekOrMonthDate(TimeDimensionality.DAY));
            }
            if (TimeDimensionality.FORMER_YEAR.equals(param.getTimePeriod())) {
                param.setBegin(DateUtils.obtainTodayOrWeekOrMonthDate(TimeDimensionality.FORMER_YEAR));
            }
        }
    }

    public void removeUserToken(String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        log.info("删除了id：{},的token ", userId);
        oauthFeignService.removeToken(userIds);
    }

    public void getCheckAddBlacListParam(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        // 添加校验
        if (StringUtils.isEmpty(blac.getId())) {
            if (StringUtils.isEmpty(blac.getReason())) {
                throw new DefaultException("原因不能为空");
            }
            if (blac.getReason().length() > 200) {
                throw new DefaultException("原因最多200个字符");
            }
        }
        List<String> images =  param.getImages();
        if (CollectionUtils.isEmpty(images)) {
            return;
        }
        if (images.size() > 9) {
            throw new DefaultException("相关图片最多9张");
        }
    }
}
