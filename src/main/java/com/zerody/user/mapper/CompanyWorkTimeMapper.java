package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.CompanyWorkTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.vo.CompanyWorkTimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 公司上下班时间表
 * 
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Mapper
public interface CompanyWorkTimeMapper extends BaseMapper<CompanyWorkTime> {

    /**
    * @Author: chenKeFeng
    * @Description: 分页查询企业上下班时间
    * @Date: 2022/8/31 14:58
    */
    CompanyWorkTimeVo getPageCompanyWorkTime(@Param("param") CompanyWorkTimeDto param);

}
