package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuVo
 * @DateTime 2020/12/16_13:37
 * @Deacription TODO
 */
@Data
public class SysStaffInfoVo {

    private String id;

    //企业id
    private String compId;

    //用户id
    private String userId;

    //用户姓名
    private String userName;

    //工作地点
    private String workPlace;

    //工号
    private String jobNumber;

    /**
     *
     *角色名称
     */
    private String roleName;
    //转正时间
    private Date dateJoin;

    //入职时间
    private Date conversionDate;

    //离职时间
    private Date dateLeft;

    //状态：0.生效、1.离职、2.删除、3.合作
    private Integer status;
}
