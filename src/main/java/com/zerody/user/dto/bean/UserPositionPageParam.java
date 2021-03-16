package com.zerody.user.dto.bean;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserPositionPageParam
 * @DateTime 2021/3/14_23:22
 * @Deacription TODO
 */
@Data
public class UserPositionPageParam extends PageQueryDto {

    /** 企业id */
    private String companyId;

    /** 部门id */
    private String departId;

    /** 用户id */
    private String userId;
}