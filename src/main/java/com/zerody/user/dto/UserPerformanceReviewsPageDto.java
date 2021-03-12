package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.utils.DataUtil;
import lombok.Data;

import java.util.Date;

/**
 * 业务总结报表分页dto
 * @author PengQiang
 * @ClassName UserPerformanceReviewsPageDto
 * @DateTime 2021/3/10_16:31
 * @Deacription TODO
 */
@Data
public class UserPerformanceReviewsPageDto extends PageQueryDto {

    /** 企业id */
    private String companyId;

    private String departmentId;

    /** 客户名称 */
    private String customerName;

    private String userId;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;

}
