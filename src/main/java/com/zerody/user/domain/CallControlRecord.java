package com.zerody.user.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 呼叫限制记录
 *
 * @author  DaBai
 * @date  2022/11/9 10:22
 */

@Data
public class CallControlRecord {
    /**
    * id
    */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 伙伴id
    */
    private String userId;

    /**
    * 伙伴名称
    */
    private String userName;

    /**
    * 企业id
    */
    private String companyId;

    /**
    * 企业名称
    */
    private String companyName;

    /**
    * 所属部门id
    */
    private String deptId;

    /**
    * 所属部门
    */
    private String deptName;

    /**
    * 手机号码
    */
    private String mobile;

    /**
    * 角色名称
    */
    private String role;

    /**
     * 累计限制次数
     */
    private Integer num;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 移除时间
    */
    private Date removeTime;

    /**
     * 状态是否已移除 1是 0否
     */
    private Integer state;

    /**
    * 是否删除(1删除 0正常)
    */
    private Integer deleted;
}
