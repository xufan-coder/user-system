package com.zerody.user.dto.bean;

import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserPositionPageParam
 * @DateTime 2021/3/14_23:22
 * @Deacription TODO
 */
@Data
public class UserPositionParam {

    /** 企业id */
    private String companyId;

    /** 部门id */
    private String departId;

    /** 用户id */
    private String userId;

    /** 部门id */
    private List<String> departIds;

    /** CEO关联企业IDS */
    private List<String> companyIds;
}
