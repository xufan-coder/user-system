package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.enums.common.ResultsSummaryQueryType;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.user.domain.MonthActivityUser;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.MonthActivityUserMapper;
import com.zerody.user.service.MonthActivityUserService;
import com.zerody.user.service.SysCompanyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * @author PengQiang
 * @ClassName MonthActivityUserServiceImpl
 * @DateTime 2022/1/15_14:37
 * @Deacription TODO
 */
@Slf4j
@Service
public class MonthActivityUserServiceImpl extends ServiceImpl<MonthActivityUserMapper, MonthActivityUser> implements MonthActivityUserService {

    @Autowired
    private OauthFeignService oauthFeignService;


    @Override
    public void doAddMonthActivityUser() {
        DataResult<List<String>> salesmanRolesResult = this.oauthFeignService.getSalesmanRole(null);
        if (!salesmanRolesResult.isSuccess()) {
            log.error("获取业务角色异常：{}", salesmanRolesResult.getMessage());
            return;
        }
        if (CollectionUtils.isEmpty(salesmanRolesResult.getData())) {
            log.info("找不到业务人员角色");
            return;
        }
        List<String> roleIds = salesmanRolesResult.getData();
        //获取当前年月
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        // 查本年 本月 该企业是否已有记录
        QueryWrapper<MonthActivityUser> compActivityQw = new QueryWrapper<>();
        compActivityQw.lambda().eq(MonthActivityUser::getYear, year);
        compActivityQw.lambda().eq(MonthActivityUser::getMonth, month);
        compActivityQw.lambda().eq(MonthActivityUser::getType, ResultsSummaryQueryType.COMPANY.name());
        List<MonthActivityUser> compExists = this.list(compActivityQw);
        List<MonthActivityUser> compActivity = this.baseMapper.getComapnyMonthAcitvityUser(roleIds);
        ListIterator<MonthActivityUser> it = compActivity.listIterator();
        while (it.hasNext()) {
            MonthActivityUser monthCompanyUser = it.next();
            monthCompanyUser.setCreateTime(new Date());
            monthCompanyUser.setYear(year);
            monthCompanyUser.setMonth(month);
            monthCompanyUser.setType(ResultsSummaryQueryType.COMPANY.name());
            int index = compExists.indexOf(monthCompanyUser);
            if (index > -1) {
                compExists.get(index).setNumber(monthCompanyUser.getNumber());
                it.remove();
            }
        }
        if (CollectionUtils.isNotEmpty(compExists)) {
            this.updateBatchById(compExists);
        }
        if (CollectionUtils.isNotEmpty(compActivity)) {
            this.saveBatch(compActivity);
        }
        // 查本年 本月 该部门是否已有记录
        QueryWrapper<MonthActivityUser> departActivityQw = new QueryWrapper<>();
        compActivityQw.lambda().eq(MonthActivityUser::getYear, year);
        compActivityQw.lambda().eq(MonthActivityUser::getMonth, month);
        compActivityQw.lambda().and(type ->
                type.eq(MonthActivityUser::getType, ResultsSummaryQueryType.DEPART.name()).or()
                        .eq(MonthActivityUser::getType, ResultsSummaryQueryType.TEAM.name())
        );
        List<MonthActivityUser> departExists = this.list(departActivityQw);
        List<MonthActivityUser> departActivity = this.baseMapper.getDepartMonthAcitvityUser(roleIds);
        it = departActivity.listIterator();
        while (it.hasNext()) {
            MonthActivityUser monthDeaprtUser = it.next();
            monthDeaprtUser.setCreateTime(new Date());
            monthDeaprtUser.setYear(year);
            monthDeaprtUser.setMonth(month);
            monthDeaprtUser.setType(ResultsSummaryQueryType.DEPART.name());
            if (monthDeaprtUser.getDepartId().indexOf("_") > -1) {
                monthDeaprtUser.setType(ResultsSummaryQueryType.TEAM.name());
            }
            int index = compExists.indexOf(monthDeaprtUser);
            if (index > -1) {
                departExists.get(index).setNumber(monthDeaprtUser.getNumber());
                it.remove();
            }
        }
        if (CollectionUtils.isNotEmpty(departExists)) {
            this.updateBatchById(departExists);
        }
        if (CollectionUtils.isNotEmpty(departActivity)) {
            this.saveBatch(departActivity);
        }
    }
}
