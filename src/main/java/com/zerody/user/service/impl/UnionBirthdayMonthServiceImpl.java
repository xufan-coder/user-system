package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.UnionBirthdayMonth;
import com.zerody.user.mapper.UnionBirthdayMonthMapper;
import com.zerody.user.service.UnionBirthdayMonthService;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuang
 */
@Service
public class UnionBirthdayMonthServiceImpl extends ServiceImpl<UnionBirthdayMonthMapper, UnionBirthdayMonth> implements UnionBirthdayMonthService {

    @Override
    public List<String> getExistMonthList(String templateId) {

        return this.baseMapper.getMonthList(templateId);
    }

    @Override
    public int getMonthCount(String templateId,List<Integer> monthList) {
        if(monthList == null || monthList.size() ==0) {
            return 0;
        }
        return this.baseMapper.getMonthCount(templateId, monthList);
    }

    @Override
    public void addTemplateMonth(List<Integer> monthList, String templateId) {

        QueryWrapper<UnionBirthdayMonth> qw = new QueryWrapper<>();
        qw.lambda().eq(UnionBirthdayMonth::getTemplateId,templateId);
        this.remove(qw);
        List<UnionBirthdayMonth> templateMonths = new ArrayList<>();
        for(Integer month : monthList) {
            UnionBirthdayMonth template = new UnionBirthdayMonth();
            template.setId(UUIDutils.getUUID32());
            template.setTemplateId(templateId);
            template.setMonth(month);
            template.setCreateTime(new Date());
            templateMonths.add(template);
        }
        if(templateMonths.size() > 0) {
            this.saveBatch(templateMonths);
        }
    }

}
