package com.zerody.user.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.utils.IdCardUtil;
import com.zerody.common.vo.IdCardDate;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 * 初始化身份证号 到生日 月-日
 */
@Component
@Slf4j
public class UserSyncCardTask {


    @Autowired
    private SysUserInfoService userService;

    /**
     *
     * @author               kuang
     * @description          初始化身份证号 到生日 月-日
     */
    @XxlJob("user_sync_card_task")
    public ReturnT<String> execute(String param) throws ParseException {

        QueryWrapper<SysUserInfo> qw = new QueryWrapper<>();
        qw.lambda().isNull(SysUserInfo::getBirthdayTime);
        qw.lambda().isNotNull(SysUserInfo::getCertificateCard);
        qw.lambda().ne(SysUserInfo::getCertificateCard, "");
        qw.lambda().last("limit 100");
        List<SysUserInfo> userInfos = this.userService.list(qw);

        List<SysUserInfo> updateList = new ArrayList<>();
        for(SysUserInfo info : userInfos){
            if (DataUtil.isEmpty(info.getCertificateCard())) {
                continue;
            }
           SysUserInfo entity = new SysUserInfo();
           entity.setIdCardSex(IdCardUtil.getSex(info.getCertificateCard()));
           //获取身份证年月日
            IdCardDate date = DateUtil.getIdCardDate(info.getCertificateCard());
            if (DataUtil.isEmpty(date.getYear())) {
                continue;
            }
           entity.setBirthdayTime(Date.from(LocalDate.of(date.getYear(), date.getMonth(), date.getDay()).atStartOfDay().toInstant(ZoneOffset.of("+8"))));
           entity.setId(info.getId());
            updateList.add(entity);
        }
        log.info("初始化身份证生日");
        if(updateList.size() > 0) {
            this.userService.updateBatchById(updateList);
        }
        return ReturnT.SUCCESS;
    }

}
