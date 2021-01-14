package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysUserClewCollectVo
 * @DateTime 2021/1/9_11:48
 * @Deacription TODO
 */
@Data
public class SysUserClewCollectVo {

    /**
     * 线索负责人id
     */
    private String userId;
    /**
     * 线索负责人
     */
    private String name;

    /**
     *  所属部门
     */
    private String departName;

    /**
     *  所属岗位
     */
    private String jobName;


    /**
     *线索总条数
     */
    private Integer totalClew;

    /**
     * 已呼叫
     */
    private Integer haveCalled;

    /**
     *  今日呼叫
     */
    private Integer todayCalled;

    /**
     * 最后呼叫时间
     */
    private Date lastCallTime;

    /**
     * 最后入库时间
     */
    private Date lastStorageTime;

    /**
     * 最后登录时间
     */
    private Date loginTime;

}