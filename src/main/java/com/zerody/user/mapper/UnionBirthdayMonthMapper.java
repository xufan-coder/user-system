package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.UnionBirthdayMonth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  kuang
 */

public interface UnionBirthdayMonthMapper extends BaseMapper<UnionBirthdayMonth> {

    int getMonthCount(@Param("templateId") String templateId, @Param("months") List<Integer> months,@Param("type")Integer type);

    int getYearCount(@Param("templateId") String templateId, @Param("years") List<String> years,@Param("type")Integer type);

    List<String> getMonthList(@Param("templateId") String templateId,@Param("type")Integer type);

    List<String> getYearList(@Param("templateId") String templateId,@Param("type")Integer type);
}
