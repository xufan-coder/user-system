package com.zerody.user.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.util.IdCardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public ReturnT<String> execute() throws ParseException {

        QueryWrapper<SysUserInfo> qw = new QueryWrapper<>();
        qw.lambda().isNull(SysUserInfo::getBirthdayMonth);
        qw.lambda().last("LENGTH(certificate_card) > 0");
        qw.lambda().last("limit 100");
        List<SysUserInfo> userInfos = this.userService.list(qw);

        System.out.println("______________--------------");
        List<SysUserInfo> updateList = new ArrayList<>();
        for(SysUserInfo info : userInfos){
            // 校验身份证号是否正确
            if(IdCardUtil.isValidatedAllIdcard(info.getCertificateCard())){
                SysUserInfo user = new SysUserInfo();
                user.setId(info.getId());
                String birthday;
                Date birthDate;
                // 处理身份证号为15位的数据
                if(info.getCertificateCard().length() == 15){
                    birthday = info.getCertificateCard().substring(6, 12);
                    // 校验出生日期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                    birthDate = sdf.parse(birthday);
                }else {
                    // 处理身份证号为18位数据
                    birthday = info.getCertificateCard().substring(6, 14);
                    // 校验出生日期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    birthDate = sdf.parse(birthday);
                }
                // 赋值
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthDate);
                if(info.getBirthday() == null) {
                    user.setBirthday(birthDate);
                }
                user.setBirthdayMonth((calendar.get(Calendar.MONTH)+1));
                user.setBirthdayDay(calendar.get(Calendar.DATE));
                updateList.add(user);
            }else {
                SysUserInfo user = new SysUserInfo();
                user.setId(info.getId());
                user.setBirthdayDay(0);
                user.setBirthdayMonth(0);
                updateList.add(user);
            }
        }
        log.info("初始化身份证生日");
        if(updateList.size() > 0) {
            this.userService.updateBatchById(updateList);
        }
        return ReturnT.SUCCESS;
    }

}
