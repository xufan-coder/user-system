package com.zerody.user.mapper;

import com.zerody.user.domain.CompanyWorkTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公司上下班时间表
 * 
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Mapper
public interface CompanyWorkTimeMapper extends BaseMapper<CompanyWorkTime> {
	
}
