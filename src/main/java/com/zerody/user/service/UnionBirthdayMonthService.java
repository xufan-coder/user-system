package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UnionBirthdayMonth;
import com.zerody.user.vo.UserBirthdayTemplateVo;

import java.util.List;

public interface UnionBirthdayMonthService extends IService<UnionBirthdayMonth> {

    List<String> getExistMonthList(String templateId);

    /**查询此月份有无记录*/
    int getMonthCount(String templateId,List<Integer> monthList);

    void addTemplateMonth(List<Integer> monthList, String templateId);

}
