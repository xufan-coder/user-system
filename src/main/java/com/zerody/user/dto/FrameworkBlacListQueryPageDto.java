package com.zerody.user.dto;

import com.zerody.user.dto.bean.UserPositionPageParam;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName FrameworkBlacListQueryPageDto
 * @DateTime 2021/8/4_11:22
 * @Deacription TODO
 */
@Data
public class FrameworkBlacListQueryPageDto extends UserPositionPageParam {

    /**
    *   关键词筛选/app查询名字或手机号
    */
    private String keyword;

    private String userName;

    private String mobile;

    private String state;

    private String queryDimensionality;

    private Integer type;
}
