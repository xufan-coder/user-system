package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.vo.UserVo;
import com.zerody.expression.Expression;
import com.zerody.user.domain.UnionBirthdayMonth;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserBirthdayTemplateDto;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.mapper.UserBirthdayTemplateMapper;
import com.zerody.user.service.UnionBirthdayMonthService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.vo.AppUserNotPushVo;
import com.zerody.user.vo.SysStaffInfoDetailsVo;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuang
 */
@Service
public class UserBirthdayTemplateServiceImpl extends ServiceImpl<UserBirthdayTemplateMapper, UserBirthdayTemplate> implements UserBirthdayTemplateService {

    @Autowired
    private UnionBirthdayMonthService unionBirthdayMonthService;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;



    @Override
    public void addTemplate(UserVo user, UserBirthdayTemplateDto dto) {
        UserBirthdayTemplate template = new UserBirthdayTemplate();
        BeanUtils.copyProperties(dto,template);
        template.setCreateBy(user.getUserId());
        template.setCreateTime(new Date());
        template.setCreateUsername(user.getUserName());
        template.setId(UUIDutils.getUUID32());
        int cnt = unionBirthdayMonthService.getMonthCount(null,dto.getMonthList());
        if(cnt > 0) {
            throw new DefaultException("该月份已有模板记录");
        }
        this.save(template);
        unionBirthdayMonthService.addTemplateMonth(dto.getMonthList(),template.getId());
    }

    @Override
    public void modifyTemplate(UserVo user, UserBirthdayTemplateDto dto) {
        UserBirthdayTemplate template = new UserBirthdayTemplate();
        BeanUtils.copyProperties(dto,template);
        template.setUpdateBy(user.getUserId());
        template.setUpdateTime(new Date());

        int cnt = unionBirthdayMonthService.getMonthCount(dto.getId(),dto.getMonthList());
        if(cnt > 0) {
            throw new DefaultException("该月份已有模板记录");
        }
        this.updateById(template);
        unionBirthdayMonthService.addTemplateMonth(dto.getMonthList(),template.getId());

    }

    @Override
    public Page<UserBirthdayTemplateVo> getTemplate(TemplatePageDto queryDto) {
        Page<UserBirthdayTemplateVo> page = new Page<>(queryDto.getCurrent(),queryDto.getPageSize());

        return this.baseMapper.getTemplatePage(queryDto,page);
    }

    @Override
    public UserBirthdayTemplateVo getTemplateInfo() {

        return this.baseMapper.getTemplateInfo( DateUtil.getMonth());
    }

    @Override
    public UserBirthdayTemplateVo getNoticeInfo(String userId) {
        UserBirthdayTemplateVo templateVo = this.baseMapper.getTemplateInfo( DateUtil.getMonth());
        if(templateVo != null) {
            // 设置同事生日模板
            SysStaffInfoDetailsVo detailsVo = sysStaffInfoMapper.getStaffinfoDetails(userId);
            if(detailsVo != null) {
                Map params = new HashMap();
                params.put("name", detailsVo.getUserName());
                params.put("dep", detailsVo.getDepartName());
                String noticeText = Expression.parse(templateVo.getNoticeText(), params);
                templateVo.setNoticeText(noticeText);
            }
        }
        return templateVo;
    }

    @Override
    public UserBirthdayTemplate getTimeTemplate(Date time) {
        return this.baseMapper.getTemplateByTime( DateUtil.getMonth(), time);
    }

    @Override
    public UserBirthdayTemplateVo getTemplateInfo(String templateId) {

        return this.baseMapper.getTemplateInfoById(templateId);
    }

    @Override
    public void modifyTemplateById(String id) {
        UpdateWrapper<UserBirthdayTemplate> uw = new UpdateWrapper<>();
        uw.lambda().set(UserBirthdayTemplate::getDeleted, YesNo.NO);
        uw.lambda().eq(UserBirthdayTemplate::getId,id);
        this.update(uw);

        QueryWrapper<UnionBirthdayMonth> birthdayMonth = new QueryWrapper<>();
        birthdayMonth.lambda().eq(UnionBirthdayMonth::getTemplateId,id);
        this.unionBirthdayMonthService.remove(birthdayMonth);
    }

    @Override
    public void modifyTemplate(List<String> ids) {

        UpdateWrapper<UserBirthdayTemplate> uw = new UpdateWrapper<>();
        uw.lambda().set(UserBirthdayTemplate::getDeleted, YesNo.NO);
        uw.lambda().in(UserBirthdayTemplate::getId,ids);
        this.update(uw);

        QueryWrapper<UnionBirthdayMonth> birthdayMonth = new QueryWrapper<>();
        birthdayMonth.lambda().in(UnionBirthdayMonth::getTemplateId,ids);
        this.unionBirthdayMonthService.remove(birthdayMonth);
    }

    @Override
    public boolean whetherBirthday(String userId) {
        String month = com.zerody.common.utils.DateUtil.getMonth();
        String day = com.zerody.common.utils.DateUtil.getDay();
        List<AppUserNotPushVo> userList =  this.sysUserInfoMapper.getBirthdayUserIds(month,day,userId);
        return userList.size() > 0;
    }
}
