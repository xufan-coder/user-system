package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.BlessIngParam;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserBirthdayTemplateDto;
import com.zerody.user.vo.AppUserNotPushVo;
import com.zerody.user.vo.UserBirthdayTemplateVo;

import java.util.Date;
import java.util.List;

public interface UserBirthdayTemplateService extends IService<UserBirthdayTemplate> {
    /**添加生日模板*/
    void addTemplate(UserVo user, UserBirthdayTemplateDto template);

    void modifyTemplate(UserVo user, UserBirthdayTemplateDto template);

    Page<UserBirthdayTemplateVo> getTemplate(TemplatePageDto queryDto);

    UserBirthdayTemplateVo getTemplateInfo();

    UserBirthdayTemplateVo getNoticeInfo(String userId);

    /**根据推送时间查询模板*/
    UserBirthdayTemplate getTimeTemplate(Date time,Integer type);

    /**根据推送时间查询入职周年模板*/
    UserBirthdayTemplate getEntryTimeTemplate(String year,Date time,Integer type);

    UserBirthdayTemplateVo getTemplateInfo(String templateId);

    void modifyTemplateById(String id);

    void modifyTemplate(List<String> ids);

    boolean whetherBirthday(String userId);

    void addBlessing(BlessIngParam param);
    /**
    *
    *  @description   获取今天是否入职周年
    *  @author        YeChangWei
    *  @date          2022/11/16 19:01
    *  @return        java.lang.Object
    */
    boolean whetherEntry(String userId);
    /**
    *
    *  @description   查询入职周年的信息
    *  @author        YeChangWei
    *  @date          2022/11/16 20:45
    *  @return        com.zerody.user.vo.AppUserNotPushVo
    */
    AppUserNotPushVo getEntryPullData(String userId);
}
