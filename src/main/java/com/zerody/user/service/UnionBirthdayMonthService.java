package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UnionBirthdayMonth;

import java.util.List;

public interface UnionBirthdayMonthService extends IService<UnionBirthdayMonth> {

    List<String> getExistMonthList(String templateId,Integer type);

    /**查询此月份有无记录*/
    int getMonthCount(String templateId,List<Integer> monthList, Integer type);
    /**查询此年份有无记录*/
     int getYearCount(String templateId, List<String> yearList,Integer type);

    void addTemplateMonth(List<Integer> monthList, String templateId);

    void addTemplateYear(List<String> yearList, String templateId);

}
