package com.zerody.user.mapper;

import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CompanyAdmin;
import java.io.Serializable;
import java.util.List;

import com.zerody.user.vo.SubordinateUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * @author 黄华盛 
 * @date 2021-01-04
 */
public interface CompanyAdminMapper extends  BaseMapper<CompanyAdmin>{

    List<SubordinateUserQueryVo> getAdminList(@Param("companyId") String companyId);

    StaffInfoVo getAdminInfoByCompanyId(@Param("companyId") String companyId);
}
