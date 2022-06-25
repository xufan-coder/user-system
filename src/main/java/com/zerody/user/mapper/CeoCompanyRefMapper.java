package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.vo.CompanyRefVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *@ClassName CeoCompanyRefMapper
 *@author    PengQiang
 *@DateTime  2022/6/18_10:50
 *@Deacription TODO
 */
public interface CeoCompanyRefMapper extends BaseMapper<CeoCompanyRef> {

    @Select({"<script> SELECT sci.id, sci.company_name AS companyName FROM ceo_company_ref ccr  JOIN sys_company_info sci ON sci.id = ccr.company_id ",
            "where ccr.ceo_id= #{ceoId}</script>"})
    List<CompanyRefVo> getCeoRef(@Param("ceoId") String ceoId);


}
