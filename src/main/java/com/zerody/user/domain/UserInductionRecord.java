package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 伙伴入职申请记录
 *
 * @author kuang
 * @date 2022/11/30 9:56
 */
@Data
public class UserInductionRecord {


    private String id;

    /**离职伙伴id*/
    private String leaveUserId;

    /**签约时间*/
    private Date signTime;

    /**签约原因*/
    private String signReason;

    /**企业id*/
    private String companyId;

    /**部门id*/
    private String departId;

    /**角色id*/
    private String roleId;

    /**角色名称*/
    private String roleName;

    /**申请状态*/
    private String approveState;

    /**入职伙伴id*/
    private String userId;

    /**流程id*/
    private String processId;

    /**流程key*/
    private String processKey;

    /**  `leave_user_id` varchar(50) DEFAULT NULL COMMENT '离职伙伴id',
     `sign_time` datetime DEFAULT NULL COMMENT '签约时间',
     `sign_reason` varchar(1000) DEFAULT NULL COMMENT '签约原因',
     `company_id` varchar(50) DEFAULT NULL COMMENT '企业id',
     `depart_id` varchar(50) DEFAULT NULL COMMENT '部门id',
     `role_id` varchar(50) DEFAULT NULL COMMENT '角色id',
     `role_name` varchar(50) DEFAULT NULL COMMENT '角色名称',
     `approve_state` varchar(50) DEFAULT NULL COMMENT '申请状态',
     `user_id` varchar(50) DEFAULT NULL COMMENT '当前伙伴id',
     `process_id` varchar(50) DEFAULT NULL COMMENT '流程id',
     `process_key` varchar(50) DEFAULT NULL COMMENT '流程key',*/

    /**
     * 是否删除(0存在 1删除)
     */
    private Integer deleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
}