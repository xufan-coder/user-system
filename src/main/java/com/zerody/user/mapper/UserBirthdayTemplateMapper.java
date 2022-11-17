package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author  kuang
 */

public interface UserBirthdayTemplateMapper extends BaseMapper<UserBirthdayTemplate> {

    Page<UserBirthdayTemplateVo> getTemplatePage(@Param("queryDto") TemplatePageDto queryDto, Page<UserBirthdayTemplateVo> page );

    UserBirthdayTemplateVo getTemplateInfo(@Param("month") String month);

    UserBirthdayTemplate getTemplateByTime(@Param("month") String month,@Param("time") Date time,@Param("type")Integer type );

    UserBirthdayTemplate getTemplateByYear(@Param("year") String year,@Param("time") Date time,@Param("type")Integer type );

    UserBirthdayTemplate getTemplateInfoByYear(@Param("year") String year, @Param("type")Integer type );

    UserBirthdayTemplateVo getTemplateInfoById(@Param("templateId") String templateId);
}
